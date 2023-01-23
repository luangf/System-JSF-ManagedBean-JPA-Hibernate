package br.com.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//parte do Banco de Dados

@Entity
public class Pet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date dataAdocao=new Date(); //data atual

	private String nome;
	private Integer idade;
	private String sexo; // no banco: MASCULINO, FEMININO, INDEFINIDO(permitido)
	private Boolean equipado;
	private String[] poderes;
	private Integer[] equipamentos; // numero q referencia um equipamento especifico, ex.: 1->Botas velozes
	private String comportamento;
	
	public Pet() {

	}
	
	public String getComportamento() {
		return comportamento;
	}
	
	public void setComportamento(String comportamento) {
		this.comportamento = comportamento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataAdocao() {
		return dataAdocao;
	}

	public void setDataAdocao(Date dataAdocao) {
		this.dataAdocao = dataAdocao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Boolean getEquipado() {
		return equipado;
	}

	public void setEquipado(Boolean equipado) {
		this.equipado = equipado;
	}

	public String[] getPoderes() {
		return poderes;
	}

	public void setPoderes(String[] poderes) {
		this.poderes = poderes;
	}

	public Integer[] getEquipamentos() {
		return equipamentos;
	}

	public void setEquipamentos(Integer[] equipamentos) {
		this.equipamentos = equipamentos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pet other = (Pet) obj;
		return Objects.equals(id, other.id);
	}

}
