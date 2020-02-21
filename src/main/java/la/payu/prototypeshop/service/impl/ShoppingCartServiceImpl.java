package la.payu.prototypeshop.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.model.OrderStatus;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.model.TransactionState;
import la.payu.prototypeshop.adapter.PayUBoundary;
import la.payu.prototypeshop.exception.NotEnoughProductsInStockException;
import la.payu.prototypeshop.model.Article;
import la.payu.prototypeshop.model.Order;
import la.payu.prototypeshop.model.Product;
import la.payu.prototypeshop.model.User;
import la.payu.prototypeshop.repository.OrderRepository;
import la.payu.prototypeshop.repository.ProductRepository;
import la.payu.prototypeshop.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Shopping Cart is implemented with a Map, and as a session bean
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired private ProductRepository productRepository;
	@Autowired private OrderRepository orderRepository;

	@Autowired
	private PayUBoundary payUBoundary;

	private Map<Product, Integer> products = new HashMap<>();
	private Order order;

	/**
	 * If product is in the map just increment quantity by 1.
	 * If product is not in the map with, add it with quantity 1
	 *
	 * @param product
	 */
	@Override
	public void addProduct(Product product) {

		if (products.containsKey(product)) {
			products.replace(product, products.get(product) + 1);
		} else {
			products.put(product, 1);
		}
	}

	/**
	 * If product is in the map with quantity > 1, just decrement quantity by 1.
	 * If product is in the map with quantity 1, remove it from map
	 *
	 * @param product
	 */
	@Override
	public void removeProduct(Product product) {

		if (products.containsKey(product)) {
			if (products.get(product) > 1) {
				products.replace(product, products.get(product) - 1);
			} else if (products.get(product) == 1) {
				products.remove(product);
			}
		}
	}

	/**
	 * @return unmodifiable copy of the map
	 */
	@Override
	public Map<Product, Integer> getProductsInCart() {

		return Collections.unmodifiableMap(products);
	}

	@Override
	public BigDecimal getTotal() {

		return products.entrySet().stream()
					   .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
					   .reduce(BigDecimal::add)
					   .orElse(BigDecimal.ZERO);
	}

	@Override
	public Order getPurchasingOrder() {

		return order;
	}

	/**
	 * Purchase will rollback if there is not enough of some product in stock
	 *
	 * @throws NotEnoughProductsInStockException
	 */
	@Override public void purchase() throws NotEnoughProductsInStockException {

		order = Order.builder()
					 .status(OrderStatus.NEW)
					 .description(buildOrderDescription(products.size()))
					 .value(getTotal())
					 .build();
		List<Article> articles = new ArrayList<>();

		Optional<Product> product;
		for (Map.Entry<Product, Integer> entry : products.entrySet()) {
			// Refresh quantity for every product before checking
			product = productRepository.findById(entry.getKey().getId());
			if (product.isPresent()) {
				Product productPresent = product.get();
				if (productPresent.getQuantity() < entry.getValue()) {
					throw new NotEnoughProductsInStockException(productPresent);
				}
				articles.add(new Article(entry.getKey(), entry.getValue(), order));
			}
		}
		order.setArticles(articles);
		orderRepository.saveAndFlush(order);
	}

	private String buildOrderDescription(Integer quantity) {

		return LocalDateTime.now().toString() + " | Buying " + quantity + " products.";
	}

	/**
	 * Checkout will rollback if there is not enough of some product in stock
	 *
	 * @param user
	 * @param orderWithCardInfo
	 * @throws NotEnoughProductsInStockException
	 */
	@Override
	public void checkout(User user, Order orderWithCardInfo)
			throws NotEnoughProductsInStockException, ConnectionException, InvalidParametersException, PayUException,
				   JsonProcessingException {

		TransactionResponse response = payUBoundary.doAuthorizationAndCapture(user, orderWithCardInfo);
		orderWithCardInfo.setStatus(mapStatus(response.getState()));
		orderWithCardInfo.setResponse(new ObjectMapper().writeValueAsString(response));
		orderRepository.save(orderWithCardInfo);
		order = null;

		//Updating stock
		Optional<Product> product;
		for (Map.Entry<Product, Integer> entry : products.entrySet()) {

			// Refresh quantity for every product before checking
			product = productRepository.findById(entry.getKey().getId());
			if (product.isPresent()) {
				Product productPresent = product.get();
				if (productPresent.getQuantity() < entry.getValue()) {
					throw new NotEnoughProductsInStockException(productPresent);
				}
				entry.getKey().setQuantity(productPresent.getQuantity() - entry.getValue());
			}
		}
		productRepository.saveAll(products.keySet());
		productRepository.flush();
		products.clear();
	}

	private OrderStatus mapStatus(TransactionState state) {

		OrderStatus status;
		switch (state) {
		case APPROVED:
			status = OrderStatus.AUTHORIZED;
			break;
		case DECLINED:
		case EXPIRED:
			status = OrderStatus.DECLINED;
			break;
		case PENDING:
		case SUBMITTED:
			status = OrderStatus.IN_PROGRESS;
			break;
		default:
			status = OrderStatus.CANCELLED;
		}
		return status;
	}

	@Override public Order getPurchasedOrder(Long orderId) {

		return orderRepository.findByIdAndStatusNot(orderId, OrderStatus.NEW);
	}
}
