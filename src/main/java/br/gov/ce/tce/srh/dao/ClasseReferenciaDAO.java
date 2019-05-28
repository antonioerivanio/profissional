package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.ClasseReferencia;

public interface ClasseReferenciaDAO {
	
	public int count(Long id);
	public List<ClasseReferencia> search(Long id, int first, int rows);

	public ClasseReferencia salvar(ClasseReferencia entidade);
	public void excluir(ClasseReferencia entidade);

	public ClasseReferencia getBySimboloReferencia(Long simbolo, Long referencia);

	public List<ClasseReferencia> findByCargo(Long cargo);

}
