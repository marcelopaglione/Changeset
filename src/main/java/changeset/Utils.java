package changeset;

import java.lang.reflect.Field;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

public class Utils {

	public final static int MAX = 30;

	static void checkMaxStringSize(String value) {

		if (value.length() > MAX) {
			CreateChangeLog.warnings.add(String.format("Constraint %s excede o numero maximo de caracteres"
					+ "\nTotal caracteres: %s" + "\nMaximo permitido %s", value, value.length(), MAX));
		}

	}

	static String toLower(String value) {
		return value.toLowerCase();
	}

	static boolean isPk(Field field) {
		return field.getAnnotation(Id.class) != null;
	}

	static boolean isNotNull(Field field) {
		return field.getAnnotation(NotNull.class) != null;
	}

	static boolean isColumn(Field field) {
		return field.getAnnotation(Column.class) != null;
	}

	static void isJoinColumn(Field field) {
		if (field.getAnnotation(JoinColumn.class) == null) {
			throw new RuntimeException("isJoinColumn for field " + field.getName());
		}
	}

	static String getJoinColumnName(Field field) {
		isJoinColumn(field);
		String name = field.getAnnotation(JoinColumn.class).name();
		checkMaxStringSize(name);
		return name;
	}

	static String getColumnName(Field field) {
		String name;
		if (isColumn(field)) {
			name = addUnderscore(field.getAnnotation(Column.class).name());
		} else {
			name = addUnderscore(field.getName());
		}
		checkMaxStringSize(name);
		return name;
	}

	static int getColumnSize(Field field) {
		if (isColumn(field)) {
			return field.getAnnotation(Column.class).length();
		}
		// TODO ask the user what size
		return 255;
	}

	static void isTable(Class<?> clazz) {
		if (clazz.getAnnotation(Table.class) == null) {
			CreateChangeLog.warnings.add("Missing @Table(name = \"\") " + clazz.getSimpleName());
		}
	}

	static String getTableName(Class<?> clazz) {
		isTable(clazz);
		String tableName = Optional.ofNullable(clazz.getAnnotation(Table.class)).map(t -> t.name()).orElse("UNDEFINED");
		checkMaxStringSize(tableName);
		return tableName;
	}

	static String addUnderscore(String currentString) {
		String separator = "_";
		if (currentString.contains(separator) || currentString.isEmpty()) {
			return currentString;
		}
		StringBuilder newString = new StringBuilder();
		char[] currentStringArray = currentString.toCharArray();
		char currentChar;
		newString.append(currentString.charAt(0));

		for (int i = 1; i < currentStringArray.length; i++) {
			currentChar = currentStringArray[i];
			if (Character.isUpperCase(currentChar)) {
				newString.append(separator).append(currentChar);
			} else {
				newString.append(currentChar);
			}

		}
		return Utils.toLower(newString.toString());
	}

}
