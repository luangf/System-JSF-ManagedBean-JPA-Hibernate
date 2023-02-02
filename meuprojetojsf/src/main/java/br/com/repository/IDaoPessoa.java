package br.com.repository;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.entidades.Pessoa;

//para consultas... mais especificas, q n da pra por no generic
public interface IDaoPessoa {
	Pessoa consultarUsuario(String login, String senha);
	
	//para comboBox ou selectOneMenu... se usa SelectItem
	List<SelectItem> listaEstados(); //retorna uma lista de select items

	List<Pessoa> relatorioPessoa(String nome, Date dataIni, Date dataFin);
}
