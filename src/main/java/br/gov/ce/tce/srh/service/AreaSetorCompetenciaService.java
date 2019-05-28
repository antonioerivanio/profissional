package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface AreaSetorCompetenciaService {

	public int count(Long area);
	public int count(Long area, Long competencia);

	public List<AreaSetorCompetencia> search(Long area, int first, int rows);
	public List<AreaSetorCompetencia> search(Long area, Long competencia, int first, int rows);

	public void salvar(AreaSetorCompetencia entidade) throws SRHRuntimeException;
	public void excluir(AreaSetorCompetencia entidade);

	public List<AreaSetorCompetencia> findByArea(Long area);
	public List<AreaSetorCompetencia> findBySetor(Long setor);

}
