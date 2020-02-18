package la.payu.prototypeshop.service;

import la.payu.prototypeshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Optional<Product> findById(Long id);

    Page<Product> findAllProductsOnSale(Pageable pageable);

}
