package br.gov.ce.tce.srh.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import br.gov.ce.tce.srh.domain.Revisao;

public interface AuditoriaDAO {

	public int count(Revisao revisao);
	public List<Revisao> search(Revisao revisao, int first, int rows, String nomeColuna);

	public Set<Class<?>> getEntidadesAuditadas();
	public Set<Field> getAtributosEntidade(Class<?> value);

}
