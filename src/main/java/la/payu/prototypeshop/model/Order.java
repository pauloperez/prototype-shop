package la.payu.prototypeshop.model;

import com.payu.sdk.model.OrderStatus;
import com.payu.sdk.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder(toBuilder = true) //need it to clone
@NoArgsConstructor //required for jpa
@AllArgsConstructor //required by mixing builder and jpa
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(name = "description", nullable = false)
	@Length(max = 255, message = "*The order's description must have at most 255 characters")
	private String description;

	@Digits(integer = 64, fraction = 2)
	@NonNull
	private BigDecimal value;

	@Transient
	@Length(min = 13, max = 20, message = "*The credit card must have between 13 and 20 characters")
	private String creditCardNumber;

	@Transient
	@Pattern(regexp = "\\d{4}(\\/)(((0)[0-9])|((1)[0-2]))")
	private String creditCardExpirationDate;

	@Transient
	@Length(min = 1, max = 4, message = "*The credit card security code must have between 1 and 4 characters")
	private String creditCardSecurityCode;

	@Transient
	private PaymentMethod paymentMethod;

	@Transient
	@PositiveOrZero
	@Digits(integer = 2, fraction = 0)
	private Integer installments;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<Article> articles;

	@Column(name = "response", columnDefinition = "json")
	private String response;

	public boolean isOk() {

		return OrderStatus.AUTHORIZED.equals(status);
	}
}
