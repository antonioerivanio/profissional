package br.gov.ce.tce.srh.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.AuditoriaDAO;
import br.gov.ce.tce.srh.domain.Revisao;
import br.gov.ce.tce.srh.domain.Revisao.Restricao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robson.castro
 *
 */
@Service("auditoriaService")
public class AuditoriaServiceImpl implements AuditoriaService {

	@Autowired
	private AuditoriaDAO dao;

	@Override
	public Set<Class<?>> getEntidadesAuditadas() {
		return dao.getEntidadesAuditadas();
	}

	@Override
	public Set<Field> getAtributosEntidade(Class<?> value) {
		return dao.getAtributosEntidade(value);
	}
	
	@Override
	public void validarAdicionarRestricao(Restricao restricao, String entidade, String valorRestricao) throws SRHRuntimeException {
		

		if(entidade == null || entidade.equals("")){
			throw new SRHRuntimeException("Selecione uma Tabela.");
		}
		
		if (restricao == null) {
			throw new SRHRuntimeException("Selecione uma coluna.");
		}
		
		if (valorRestricao == null || valorRestricao.equals("")) {
			throw new SRHRuntimeException("Valor da coluna não informado.");
		}
		
	}

	@Override
	public int count(Revisao revisao) {
		return dao.count(revisao);
	}
	
	@Override
	public List<Revisao> search(Revisao revisao, int first, int rows, String nomeColuna) {
		return dao.search(revisao, first, rows, nomeColuna);
	}

	public void setDAO(AuditoriaDAO dao) { this.dao = dao; }

}
