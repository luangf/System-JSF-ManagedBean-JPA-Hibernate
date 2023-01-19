package br.com.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;
import br.com.jpautil.JPAUtil;

public class IDaoLancamentoImpl implements IDaoLancamento {

	@Override
	public List<Lancamento> consultar(Long codUser) {
		List<Lancamento> lista=null;
		
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		lista=entityManager.createQuery("from Lancamento where usuario.id="+codUser).getResultList();
		
		entityTransaction.commit();
		entityManager.close();
		
		return lista;
	}

}
