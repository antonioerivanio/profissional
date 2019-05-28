package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaOrganizacional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CompetenciaOrganizacionalService {

	public void salvar(CompetenciaOrganizacional entidade) throws SRHRuntimeException;
	public void excluir(CompetenciaOrganizacional entidade) throws SRHRuntimeException;

	public int count(String tipo);
	public List<CompetenciaOrganizacional> search(String tipo, int first, int rows);

}
