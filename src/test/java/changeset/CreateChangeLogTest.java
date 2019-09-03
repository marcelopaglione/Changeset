package changeset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import changeset.entity.ExampleEntity;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.CreateTableChange;

public class CreateChangeLogTest {

	public CreateChangeLog changeLog;

	@Before
	public void setup() {
		changeLog = new CreateChangeLog("test.name");
	}

	@Test
	public void addConstraintsPk() throws NoSuchFieldException, SecurityException {

		Field field = ExampleEntity.class.getDeclaredField("isId");
		assertThat(field, notNullValue());

		ConstraintsConfig constraints = changeLog.getConstraints(field);

		assertThat(constraints.isNullable(), is(false));
		assertThat(constraints.isPrimaryKey(), is(true));

	}

	@Test
	public void addConstraintsNotNull() throws NoSuchFieldException, SecurityException {

		Field field = ExampleEntity.class.getDeclaredField("isNotNull");
		assertThat(field, notNullValue());

		ConstraintsConfig constraints = changeLog.getConstraints(field);

		assertThat(constraints.isNullable(), is(false));

	}

	@Test
	public void getColumnName() throws NoSuchFieldException, SecurityException {
		Field field = ExampleEntity.class.getDeclaredField("isColumn");
		assertThat(field, notNullValue());

		String columnName = changeLog.getColumnName(field);
		assertThat(columnName, equalTo("column_name"));

		field = ExampleEntity.class.getDeclaredField("isNotColumn");
		assertThat(field, notNullValue());

		columnName = changeLog.getColumnName(field);
		assertThat(columnName, equalTo("is_not_column"));

	}

	@Test
	public void getColumnValue() {
		CreateTableChange createTable = changeLog.createTable(ExampleEntity.class);
		assertThat(createTable, notNullValue());
		createTable.getColumns().stream().forEach(c -> System.out.println(c.getName()));
		assertThat(createTable.getColumns().size(), is(12));
		AtomicInteger total = new AtomicInteger(0);
		createTable.getColumns().forEach(column -> {
			System.out.println(column.getName());
			if (Arrays.asList("is_id", "is_not_id", "id_is_join_column", "is_not_null", "is_not_not_null")
					.contains(column.getName())) {
				assertThat(column.getType(), equalTo("${numerico}"));
				total.incrementAndGet();
				System.out.println(column.getName());
			}

			else if (Arrays.asList("column_name", "is_not_column", "is_not_join_column", "no_size_name")
					.contains(column.getName())) {
				assertThat(column.getType(), equalTo("VARCHAR(255)"));
				total.incrementAndGet();
				System.out.println(column.getName());
			}

			else if (Arrays.asList("predetermined_size").contains(column.getName())) {
				assertThat(column.getType(), equalTo("VARCHAR(60)"));
				total.incrementAndGet();
				System.out.println(column.getName());
			}

			else if (Arrays.asList("big_decimal").contains(column.getName())) {
				assertThat(column.getType(), equalTo("${valor}"));
				total.incrementAndGet();
				System.out.println(column.getName());
			}

			else if (Arrays.asList("value_true").contains(column.getName())) {
				assertThat(column.getType(), equalTo("VARCHAR(1)"));
				total.incrementAndGet();
				System.out.println(column.getName());
			}
		});
		assertThat(total.get(), is(12));

	}

	@Test
	public void addfk() throws NoSuchFieldException, SecurityException {
		Field field = ExampleEntity.class.getDeclaredField("isJoinColumn");
		assertThat(field, notNullValue());
		changeLog.addFk(field);
	}
}
