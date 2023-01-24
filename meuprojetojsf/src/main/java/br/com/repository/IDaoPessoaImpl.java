package br.com.repository;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
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

	@Override
	public List<SelectItem> listaEstados() {
		List<SelectItem> selectItems=new ArrayList<SelectItem>();
		
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction(); 
		entityTransaction.begin();
		
		List<Estados> estados=entityManager.createQuery("from Estados").getResultList(); //HQL(tabelas->objetos)
		
		for (Estados estado : estados) {
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}
		
		return selectItems;
	}

}
