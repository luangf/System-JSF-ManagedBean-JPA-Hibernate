package br.com.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

public class DaoGeneric<E> {

	public void salvar(E entidade) {
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.persist(entidade);
		
		entityTransaction.commit();
		entityManager.close();
	}
	
	public E merge(E entidade) {
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		E retorno=entityManager.merge(entidade);
		
		entityTransaction.commit();
		entityManager.close();
		
		return retorno;
	}
	
	public void deletar(E entidade) {
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.remove(entidade);
		
		entityTransaction.commit();
		entityManager.close();
	} 
	
	public void deletarPorId(E entidade) {
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		Object id=JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery("delete from "+entidade.getClass().getCanonicalName()+" where id="+id).executeUpdate();
		
		entityTransaction.commit();
		entityManager.close();
	} 
	
	public List<E> getListEntity(Class<E> entidade){
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> retorno=entityManager.createQuery("from "+entidade.getName()).getResultList();
		
		entityTransaction.commit();
		entityManager.close();
		
		return retorno;
	}
	
	public E consultar(Class<E> entidade, String pk) {
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		E objeto=(E) entityManager.find(entidade, Long.parseLong(pk));
		
		entityTransaction.commit();
		
		return objeto;
	}
	
}
