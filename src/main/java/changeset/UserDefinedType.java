package changeset;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserDefinedType {

	private static Set<Class<?>> userDefinedTypes;

	static {
		userDefinedTypes = new HashSet<>();
		userDefinedTypes.add(String.class);
		userDefinedTypes.add(LocalDate.class);
		userDefinedTypes.add(BigDecimal.class);
		userDefinedTypes.add(Long.class);
		userDefinedTypes.add(boolean.class);
		userDefinedTypes.add(Boolean.class);
	}

	public static String getPersonalizedType(Field field) {

		if (field.getType() == String.class)
			return String.format("VARCHAR(%s)", Utils.getColumnSize(field));

		if (field.getType() == LocalDate.class)
			return "${data}";

		if (field.getType() == Long.class)
			return "${numerico}";

		if (field.getType() == BigDecimal.class)
			return "${valor}";

		if (field.getType() == Boolean.class || field.getType() == boolean.class)
			return "VARCHAR(1)";

		return "${numerico}";
	}

	public static boolean isPersonalizedType(Class<?> clazz) {
		return userDefinedTypes.contains(clazz);
	}

}
