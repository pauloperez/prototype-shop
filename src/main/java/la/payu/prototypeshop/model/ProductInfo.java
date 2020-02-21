package la.payu.prototypeshop.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Embeddable
@Data
public class ProductInfo {

	@Column(name = "name", nullable = false, unique = true)
	@Length(min = 3, message = "*Name must have at least 5 characters")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "quantity", nullable = false)
	@Min(value = 0, message = "*Quantity has to be non negative number")
	private Integer quantity;

	@Column(name = "price", nullable = false)
	@DecimalMin(value = "0.00", message = "*Price has to be non negative number")
	private BigDecimal price;

}
