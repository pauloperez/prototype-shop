package la.payu.prototypeshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payu.sdk.model.TransactionResponse;

import javax.persistence.AttributeConverter;
import java.io.IOException;

//@Converter(autoApply = true)
public class JpaConverterJson implements AttributeConverter<TransactionResponse, String> {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(TransactionResponse meta) {

		try {
			return objectMapper.writeValueAsString(meta);
		} catch (JsonProcessingException ex) {
			return null;
			// or throw an error
		}
	}

	@Override
	public TransactionResponse convertToEntityAttribute(String dbData) {

		try {
			return objectMapper.readValue(dbData, TransactionResponse.class);
		} catch (IOException ex) {
			// logger.error("Unexpected IOEx decoding json from database: " + dbData);
			return null;
		}
	}

}
