package la.payu.prototypeshop.adapter;

import com.payu.sdk.PayU;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.Language;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.utils.LoggerUtil;
import la.payu.prototypeshop.model.Order;
import la.payu.prototypeshop.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Component
public class PayUBoundary {

	@Value("${payu.api-key}")
	private String apiKey;
	@Value("${payu.api-login}")
	private String apiLogin;
	@Value("${payu.url}")
	private String url;
	@Value("${payu.account-id}")
	private String accountId;

	private Map<String, String> parameters;

	@PostConstruct
	public void init() {

		PayU.apiKey = apiKey;
		PayU.apiLogin = apiLogin;
		PayU.language = Language.es;
		PayU.isTest = false;
		LoggerUtil.setLogLevel(Level.ALL);
		PayU.paymentsUrl = url + "/payments-api/";
		PayU.reportsUrl = url + "/reports-api/";

		parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());
		parameters.put(PayU.PARAMETERS.BUYER_COUNTRY, PaymentCountry.CO.name());
		parameters.put(PayU.PARAMETERS.PAYER_COUNTRY, PaymentCountry.CO.name());
		parameters.put(PayU.PARAMETERS.CURRENCY, "" + Currency.COP.name());
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId);
	}

	public TransactionResponse doAuthorizationAndCapture(User user, Order order) throws ConnectionException,
																						InvalidParametersException,
																						PayUException {

		fillBuyerParams(user);
		fillPayerParams(user);
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, "" + order.getId());
		parameters.put(PayU.PARAMETERS.DESCRIPTION, order.getDescription());
		parameters.put(PayU.PARAMETERS.VALUE, "" + order.getValue());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, order.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE, order.getCreditCardExpirationDate());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, order.getCreditCardSecurityCode());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, order.getPaymentMethod().name());
		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "" + order.getInstallments());

		return PayUPayments.doAuthorizationAndCapture(parameters);
	}

	private void fillBuyerParams(User user) {

		parameters.put(PayU.PARAMETERS.BUYER_ID, "" + user.getId());
		parameters.put(PayU.PARAMETERS.BUYER_NAME, user.getFullName());
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, user.getEmail());
		parameters.put(PayU.PARAMETERS.BUYER_CONTACT_PHONE, user.getPhone());
		parameters.put(PayU.PARAMETERS.BUYER_DNI_TYPE, user.getDniType().name());
		parameters.put(PayU.PARAMETERS.BUYER_DNI, user.getDni());
		parameters.put(PayU.PARAMETERS.BUYER_STREET, user.getAddress());
		parameters.put(PayU.PARAMETERS.BUYER_CITY, user.getCity());
	}

	private void fillPayerParams(User user) {

		parameters.put(PayU.PARAMETERS.PAYER_ID, "" + user.getId());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, user.getFullName());
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, user.getEmail());
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, user.getPhone());
		parameters.put(PayU.PARAMETERS.PAYER_DNI_TYPE, user.getDniType().name());
		parameters.put(PayU.PARAMETERS.PAYER_DNI, user.getDni());
		parameters.put(PayU.PARAMETERS.PAYER_STREET, user.getAddress());
		parameters.put(PayU.PARAMETERS.PAYER_CITY, user.getCity());
	}

}
