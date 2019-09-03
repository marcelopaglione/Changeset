package changeset.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import changeset.demo.BooleanToStringConverter;

@Table(name = "table_name")
public class ExampleEntity {

	@Id
	public Long isId;
	public Long isNotId;

	@NotNull
	public Long isNotNull;
	public Long isNotNotNull;

	@Column(name = "column_name")
	public String isColumn;
	public String isNotColumn;

	@JoinColumn(name = "codigo")
	public ExampleEntity2 isJoinColumn;
	public String isNotJoinColumn;

	@Column(length = 60, name = "predetermined_size")
	public String predeterminedSize;
	@Column(name = "no_size_name")
	public String noSize;

	@NotNull
	@JoinColumn(name = "big_decimal")
	private BigDecimal bigDecimal;

	@NotNull
	@Convert(converter = BooleanToStringConverter.class)
	@JoinColumn(name = "value_true")
	private Boolean valueTrue = Boolean.FALSE;

}
