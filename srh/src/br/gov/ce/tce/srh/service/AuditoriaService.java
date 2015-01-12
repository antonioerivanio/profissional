package br.gov.ce.tce.srh.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import br.gov.ce.tce.srh.domain.Revisao;
import br.gov.ce.tce.srh.domain.Revisao.Restricao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface AuditoriaService {

	public int count(Revisao revisao);
	public List<Revisao> search(Revisao revisao, int first, int rows, String nomeColuna);

	public Set<Class<?>> getEntidadesAuditadas();

	public Set<Field> getAtributosEntidade(Class<?> value);

	public void validarAdicionarRestricao(Restricao atributoSelecionado, String entidade, String valorRestricao) throws SRHRuntimeException;

}
