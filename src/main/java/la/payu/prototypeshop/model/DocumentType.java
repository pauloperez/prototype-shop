package la.payu.prototypeshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DocumentType {
	CC("Cédula de ciudadanía"),
	CE("Cédula de extranjería"),
	DE("Identificador único de cliente"),
	IDC("Identificador único de cliente"),
	NIT("En caso de ser una empresa"),
	PP("Pasaporte"),
	RC("Registro civil de nacimiento"),
	TI("Tarjeta de Identidad");

	@Getter private final String displayName;

}
