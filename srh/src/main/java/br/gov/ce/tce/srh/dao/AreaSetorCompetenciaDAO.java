package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;

public interface AreaSetorCompetenciaDAO {

	public int count(Long area);
	public int count(Long area, Long competencia);

	public List<AreaSetorCompetencia> search(Long area, int first, int rows);
	public List<AreaSetorCompetencia> search(Long area, Long competencia, int first, int rows);

	public AreaSetorCompetencia salvar(AreaSetorCompetencia entidade);
	public void excluir(AreaSetorCompetencia entidade);

	public List<AreaSetorCompetencia> findBySetor(Long setor);
	public List<AreaSetorCompetencia> findByArea(Long area);
	public List<AreaSetorCompetencia> findByAreaCompetencia(Long idAreaSetor, Long idCompetencia);

}
