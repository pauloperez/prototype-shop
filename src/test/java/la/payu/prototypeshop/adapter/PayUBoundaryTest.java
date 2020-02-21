package la.payu.prototypeshop.adapter;

import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.model.OrderStatus;
import com.payu.sdk.model.PaymentMethod;
import com.payu.sdk.model.TransactionResponse;
import la.payu.prototypeshop.model.DocumentType;
import la.payu.prototypeshop.model.Order;
import la.payu.prototypeshop.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class PayUBoundaryTest {

	@Autowired
	private PayUBoundary payUBoundary;

	private User user;
	private Order order;

	@BeforeEach
	void setUp() {

		user = new User("buyer_test@test.com", "$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm",
						"user", "Name", "Lastname", "7563126", DocumentType.CC,
						"123456789", "calle 93", "Bogota");
		order = Order.builder()
					 .status(OrderStatus.IN_PROGRESS)
					 .description("payment test")
					 .value(new BigDecimal(20000))
					 .creditCardNumber("4097440000000004")
					 .creditCardExpirationDate("2024/12")
					 .creditCardSecurityCode("321")
					 .paymentMethod(PaymentMethod.VISA)
					 .installments(1)
					 .build();
	}

	@Test
	void doAuthorizationAndCapture() throws ConnectionException, InvalidParametersException, PayUException {

		TransactionResponse response = payUBoundary.doAuthorizationAndCapture(this.user, this.order);
		assertNotNull(response);
	}
}
