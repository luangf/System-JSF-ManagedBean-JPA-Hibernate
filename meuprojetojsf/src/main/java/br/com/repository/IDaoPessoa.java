package br.com.repository;

import br.com.entidades.Pessoa;

public interface IDaoPessoa {
	Pessoa consultarUsuario(String login, String senha);
}
