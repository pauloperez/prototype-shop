package la.payu.prototypeshop.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Embedded
	private ProductInfo productInfo;

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getName() {

		return productInfo.getName();
	}

	public void setName(String name) {

		this.productInfo.setName(name);
	}

	public String getDescription() {

		return productInfo.getDescription();
	}

	public void setDescription(String description) {

		this.productInfo.setDescription(description);
	}

	public Integer getQuantity() {

		return productInfo.getQuantity();
	}

	public void setQuantity(Integer quantity) {

		this.productInfo.setQuantity(quantity);
	}

	public BigDecimal getPrice() {

		return productInfo.getPrice();
	}

	public void setPrice(BigDecimal unitPrice) {

		this.productInfo.setPrice(unitPrice);
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Product product = (Product) o;

		return id.equals(product.id);
	}

	@Override
	public int hashCode() {

		return id.hashCode();
	}
}
