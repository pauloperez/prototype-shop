package la.payu.prototypeshop.service;

import la.payu.prototypeshop.exception.NotEnoughProductsInStockException;
import la.payu.prototypeshop.model.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    void checkout() throws NotEnoughProductsInStockException;

    BigDecimal getTotal();
}
