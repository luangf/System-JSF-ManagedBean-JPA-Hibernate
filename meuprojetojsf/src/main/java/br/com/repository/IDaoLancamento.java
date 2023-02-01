package br.com.repository;

import java.util.Date;
import java.util.List;

import br.com.entidades.Lancamento;

public interface IDaoLancamento {
	List<Lancamento> consultar(Long codUser);

	List<Lancamento> consultarLimit10(Long codUser);
	
	List<Lancamento> relatorioLancamento(String numNota, Date dataIni, Date dataFim);
}
