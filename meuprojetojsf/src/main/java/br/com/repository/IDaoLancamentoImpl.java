package br.com.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;

@Named
public class IDaoLancamentoImpl implements IDaoLancamento, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> consultar(Long codUser) {
		List<Lancamento> lista=null;
		
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		lista=entityManager
				.createQuery("from Lancamento where usuario.id="+codUser)
				.getResultList();
		
		entityTransaction.commit();
		
		return lista;
	}

}
