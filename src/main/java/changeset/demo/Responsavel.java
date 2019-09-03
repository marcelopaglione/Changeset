package changeset.demo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "responsa")
public class Responsavel {
	@Id
	@NotNull
	@JoinColumn(name = "id")
	private Long id;

	@Column(length = 25)
	private String descricao;
}
