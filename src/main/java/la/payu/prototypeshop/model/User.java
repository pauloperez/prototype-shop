package la.payu.prototypeshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	@NonNull
	private String email;

	@Column(name = "password", nullable = false)
	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	@NonNull
	@JsonIgnore
	private String password;

	@Column(name = "username", nullable = false, unique = true)
	@Length(min = 5, message = "*Your username must have at least 5 characters")
	@NotEmpty(message = "*Please provide your name")
	@NonNull
	private String username;

	@Column(name = "name")
	@NotEmpty(message = "*Please provide your name")
	@NonNull
	private String name;

	@Column(name = "last_name")
	@NotEmpty(message = "*Please provide your last name")
	@NonNull
	private String lastName;

	@Length(min = 3, max = 20, message = "*Your DNI must have between 3 and 20 characters")
	@NotEmpty(message = "*Please provide your DNI")
	@NonNull
	private String phone;

	@Column(name = "dni_type", nullable = false)
	@NonNull
	@Enumerated(EnumType.STRING)
	private DocumentType dniType;

	@Length(min = 3, max = 20, message = "*Your DNI must have between 3 and 20 characters")
	@NotEmpty(message = "*Please provide your DNI")
	@NonNull
	private String dni;

	@NotEmpty(message = "*Please provide your address")
	@NonNull
	private String address;

	@Length(min = 3, max = 50, message = "*Your City must have between 3 and 20 characters")
	@NotEmpty(message = "*Please provide your city")
	@NonNull
	private String city;

	@Column(name = "active", nullable = false)
	@NonNull
	private Boolean active = true;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;

	public String getFullName() {

		return StringUtils.trimToEmpty(name + " " + lastName);
	}
}
