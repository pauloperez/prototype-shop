package la.payu.prototypeshop.service.impl;

import la.payu.prototypeshop.model.Product;
import la.payu.prototypeshop.repository.ProductRepository;
import la.payu.prototypeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAllProductsOnSale(Pageable pageable) {

        return productRepository.findByQuantityGreaterThan(0, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {

        return productRepository.findById(id);
    }
}
