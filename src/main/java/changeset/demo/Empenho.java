package changeset.demo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "empenhao")
public class Empenho {
	@Id
	@NotNull
	@JoinColumn(name = "id")
	private Long idEmp;

	@Column(name = "descricao", length = 250)
	private String descricao;
}
