package changeset.demo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Motor {

	@Id
	private Long id;

	@Column(name = "ADDRESS_ID")
	private String descricao;

	@NotNull
	private String codigo;

	private List<String> pecas;

	private boolean funcionando;

	private int peso;

}
