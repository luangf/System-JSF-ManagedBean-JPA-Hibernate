package br.com.repository;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	@Override
	public List<Lancamento> consultarLimit10(Long codUser) {
		List<Lancamento> lista = null;

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		lista = entityManager.createQuery("from Lancamento where usuario.id=" + codUser + " order by id desc")
				.setMaxResults(10).getResultList();

		entityTransaction.commit();

		return lista;
	}

	@Override
	public List<Lancamento> consultar(Long codUser) {
		List<Lancamento> lista = null;

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		lista = entityManager.createQuery("from Lancamento where usuario.id=" + codUser).getResultList();

		entityTransaction.commit();

		return lista;
	}

	@Override
	public List<Lancamento> relatorioLancamento(String numNota, Date dataIni, Date dataFin) {
		List<Lancamento> lancamentos=new ArrayList<Lancamento>();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select l from Lancamento l ");
		
		if (dataIni == null && dataFin == null && numNota != null && !numNota.isEmpty()) {
			sql.append(" where l.numeroNotaFiscal= '").append(numNota.trim()).append("'");
			
		}else if(numNota == null || (numNota != null && numNota.isEmpty())
				&& dataIni != null && dataFin == null) {
			
			String dataInicioString=new SimpleDateFormat("yyyy-MM-dd").format(dataIni);
			sql.append(" where l.dataIni >= '").append(dataInicioString).append("'");
			
		}else if(numNota == null || (numNota != null && numNota.isEmpty())
				&& dataIni == null && dataFin != null) {
			
			String dataFinalString=new SimpleDateFormat("yyyy-MM-dd").format(dataFin);
			sql.append(" where l.dataFin <= '").append(dataFinalString).append("'");
			
		}else if(numNota==null || (numNota!=null && numNota.isEmpty())
				&& dataIni != null && dataFin != null) {
			
			String dataInicioString=new SimpleDateFormat("yyyy-MM-dd").format(dataIni);
			String dataFinalString=new SimpleDateFormat("yyyy-MM-dd").format(dataFin);
			sql.append(" where l.dataIni >= '").append(dataInicioString).append("' ");
			sql.append("and l.dataFin <= '").append(dataFinalString).append("'");
			
		}else if(numNota!=null && !numNota.isEmpty()
				&& dataIni != null && dataFin != null) {
			
			String dataInicioString=new SimpleDateFormat("yyyy-MM-dd").format(dataIni);
			String dataFinalString=new SimpleDateFormat("yyyy-MM-dd").format(dataFin);
			sql.append(" where l.dataIni >= '").append(dataInicioString).append("' ");
			sql.append("and l.dataFin <= '").append(dataFinalString).append("' ");
			sql.append("and l.numeroNotaFiscal = '").append(numNota.trim()).append("'");
		}
		
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		lancamentos=entityManager.createQuery(sql.toString()).getResultList();
		
		entityTransaction.commit();
		
		return lancamentos;
	}

}
