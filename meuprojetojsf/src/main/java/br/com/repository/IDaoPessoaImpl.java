package br.com.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

public class IDaoPessoaImpl implements IDaoPessoa {

	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		Pessoa pessoa=null;
		
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		pessoa=(Pessoa) entityManager.createQuery("select p from Pessoa p where p.login='"+login+"' and p.senha='"+senha+"'").getSingleResult();
		
		entityTransaction.commit();
		entityManager.close();
	
		return pessoa;
	}

}
