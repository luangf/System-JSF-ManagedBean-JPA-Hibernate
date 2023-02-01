package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

@Named(value = "daoGeneric")
public class DaoGeneric<E> implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	@Inject
	private JPAUtil jpaUtil;
	
	public void salvar(E entidade) {
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.persist(entidade);
		
		entityTransaction.commit();
	}
	
	public E merge(E entidade) {
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		E retorno=entityManager.merge(entidade);
		
		entityTransaction.commit();
		
		return retorno;
	}
	
	public void deletar(E entidade) {
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.remove(entidade);
		
		entityTransaction.commit();
	} 
	
	public void deletarPorId(E entidade) {
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		Object id=jpaUtil.getPrimaryKey(entidade);
		entityManager.createQuery("delete from "+entidade.getClass().getCanonicalName()+" where id="+id).executeUpdate();
		
		entityTransaction.commit();
	} 
	
	public List<E> getListEntity(Class<E> entidade){
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> retorno=entityManager.createQuery("from "+entidade.getName()).getResultList();
		
		entityTransaction.commit();
		
		return retorno;
	}
	
	public List<E> getListEntityLimit10(Class<E> entidade){
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> retorno=entityManager.createQuery("from "+entidade.getName()+" order by id desc")
				.setMaxResults(10)
				.getResultList();
		
		entityTransaction.commit();
		
		return retorno;
	}
	
	public E consultar(Class<E> entidade, String pk) {
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		E objeto=(E) entityManager.find(entidade, Long.parseLong(pk));
		
		entityTransaction.commit();
		
		return objeto;
	}
	
}
