package changeset;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;

public class UserDefinedTypeTest {

	@Test
	public void userDefinedTypes() {
		assertTrue(UserDefinedType.isPersonalizedType(String.class));
		assertTrue(UserDefinedType.isPersonalizedType(LocalDate.class));
		assertTrue(UserDefinedType.isPersonalizedType(BigDecimal.class));
		assertTrue(UserDefinedType.isPersonalizedType(Long.class));
		assertTrue(UserDefinedType.isPersonalizedType(boolean.class));
		assertTrue(UserDefinedType.isPersonalizedType(Boolean.class));
	}
}
