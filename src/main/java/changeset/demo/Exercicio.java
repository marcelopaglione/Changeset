package changeset.demo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "exercicio_com_nome_muito_gramde_mesmo")
public class Exercicio {
	@Id
	@NotNull
	private Long id;

	@Column(length = 25)
	private String descricao;
}
