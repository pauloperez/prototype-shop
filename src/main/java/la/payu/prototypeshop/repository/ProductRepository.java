package la.payu.prototypeshop.repository;

import la.payu.prototypeshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findById(Long id);

	Page<Product> findByProductInfoQuantityGreaterThan(int minimumQuantity, Pageable pageable);
}
