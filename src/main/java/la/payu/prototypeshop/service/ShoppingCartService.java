package la.payu.prototypeshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import la.payu.prototypeshop.exception.NotEnoughProductsInStockException;
import la.payu.prototypeshop.model.Order;
import la.payu.prototypeshop.model.Product;
import la.payu.prototypeshop.model.User;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    BigDecimal getTotal();

    Order getPurchasingOrder();

    void purchase() throws NotEnoughProductsInStockException;

    void checkout(User user, Order purchasingOrder)
            throws NotEnoughProductsInStockException, ConnectionException, InvalidParametersException, PayUException,
                   JsonProcessingException;

    Order getPurchasedOrder(Long orderId);
}
