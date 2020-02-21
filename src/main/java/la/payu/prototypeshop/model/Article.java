package la.payu.prototypeshop.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "article")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private Long id;

	@Embedded
	private ProductInfo productInfo;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	public Article(Product product, Integer quantity, Order order) {

		productInfo = new ProductInfo();
		setName(product.getName());
		setDescription(product.getDescription());
		setPrice(product.getPrice());
		setQuantity(quantity);
		this.order = order;
	}

	public Article() {
		//required to jpa
	}

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

	public Order getOrder() {

		return order;
	}

	public void setOrder(Order order) {

		this.order = order;
	}

	@Override public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Article that = (Article) o;
		return Objects.equals(id, that.id);
	}

	@Override public int hashCode() {

		return Objects.hash(id);
	}
}
