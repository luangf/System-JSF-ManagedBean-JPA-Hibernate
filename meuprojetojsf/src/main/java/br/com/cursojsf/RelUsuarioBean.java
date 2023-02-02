package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;

@ViewScoped
@Named(value = "relUsuarioBean")
public class RelUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataIni;
	private Date dataFin;
	private String nome;

	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	@Inject
	private IDaoPessoa daoPessoa;

	@Inject
	private DaoGeneric<Pessoa> daoGeneric;

	@PostConstruct
	public void relPessoa() {
		if (dataIni == null && dataFin == null && nome == null) {
			pessoas = daoGeneric.getListEntity(Pessoa.class);
		} else {
			pessoas = daoPessoa.relatorioPessoa(nome, dataIni, dataFin);
		}
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public Date getDataIni() {
		return dataIni;
	}

	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}

	public Date getDataFin() {
		return dataFin;
	}

	public void setDataFin(Date dataFin) {
		this.dataFin = dataFin;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
