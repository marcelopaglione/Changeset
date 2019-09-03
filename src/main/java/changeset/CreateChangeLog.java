package changeset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import changeset.demo.Liquidacao;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;

public class CreateChangeLog {

	public static List<String> warnings = new ArrayList<>();
	public List<ChangeSet> fkList = new ArrayList<>();

	private DatabaseChangeLog databaseChangeLog;

	private String author;

	public CreateChangeLog(String author) {
		databaseChangeLog = new DatabaseChangeLog();
		this.author = author;
	}

	public static void main(String[] args) {
		new CreateChangeLog("marcelo.junior").createChangeSetForClass(Liquidacao.class);
	}

	public void createChangeSetForClass(Class<?> clazz) {
		ChangeSet createChangeSet = createChangeSet(String.format("id_%s", clazz.getSimpleName()), author);
		createChangeSet.addChange(createTable(clazz));
		databaseChangeLog.addChangeSet(createChangeSet);
		createFks();
		printXml();
	}

	private void createFks() {
		fkList.stream().forEach(cs -> databaseChangeLog.addChangeSet(cs));
	}

	private ChangeSet createChangeSet(String id, String author) {
		return new ChangeSet(id, author, false, false, "", null, null, true, null, databaseChangeLog);
	}

	public CreateTableChange createTable(Class<?> clazz) {

		CreateTableChange createTable = new CreateTableChange();

		createTable.setTableName(Utils.getTableName(clazz));
		createColumns(clazz).forEach(column -> {
			createTable.addColumn(column);
		});

		return createTable;
	}

	public void addFk(Field field) {
		AddForeignKeyConstraintChange fkChange = new AddForeignKeyConstraintChange();
		fkChange.setBaseColumnNames(Utils.getColumnName(field));
		fkChange.setBaseTableName(Utils.getTableName(field.getDeclaringClass()));
		fkChange.setReferencedTableName(Utils.getTableName(field.getType()));
		Optional<Field> fkPk = Arrays.asList(field.getType().getDeclaredFields()).stream().filter(f -> Utils.isPk(f))
				.findFirst();
		String name = fkPk.map(f -> Utils.getColumnName(f)).orElse("PK_UNDEFINED_FOR_" + field.getType());
		fkChange.setReferencedColumnNames(name);

		String fkName = String.format("%s_%s_%s", "FK", Utils.getColumnName(field),
				Utils.getTableName(field.getType()));
		Utils.checkMaxStringSize(fkName);
		fkChange.setConstraintName(fkName);

		ChangeSet changeSet = createChangeSet(String.format("fk_%s", field.getType().getSimpleName()), author);
		changeSet.addChange(fkChange);

		fkList.add(changeSet);
	}

	private List<ColumnConfig> createColumns(Class<?> clazz) {
		List<ColumnConfig> columns = new ArrayList<>();

		Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {

			ColumnConfig columnConfig = new ColumnConfig();

			String columnName;
			if (!UserDefinedType.isPersonalizedType(field.getType())) {
				columnName = String.format("id_%s", getColumnName(field));
			} else {
				columnName = getColumnName(field);
			}

			columnConfig.setName(columnName);
			columnConfig.setType(getColumnValue(field));

			ConstraintsConfig constraints = getConstraints(field);

			if (constraints.isPrimaryKey() != null || constraints.isNullable() != null) {
				columnConfig.setConstraints(constraints);
			}

			columns.add(columnConfig);

		});
		return columns;
	}

	public String getColumnValue(Field field) {
		return UserDefinedType.getPersonalizedType(field);
	}

	public String getColumnName(Field field) {
		return Utils.getColumnName(field);
	}

	public String getJoinColumnName(Field field) {
		return Utils.getJoinColumnName(field);
	}

	public ConstraintsConfig getConstraints(Field field) {

		ConstraintsConfig constraintsConfig = new ConstraintsConfig();

		if (Utils.isPk(field)) {
			constraintsConfig.setPrimaryKey(true);
			constraintsConfig.setNullable(false);
		}

		if (Utils.isNotNull(field)) {
			constraintsConfig.setNullable(false);
		}

		if (!UserDefinedType.isPersonalizedType(field.getType())) {
			addFk(field);
		}

		return constraintsConfig;

	}

	private void printXml() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			new XMLChangeLogSerializer().write(databaseChangeLog.getChangeSets(), baos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(baos.toString());
		warnings.forEach(f -> System.err.println(f));
	}

}
