package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaOrganizacional;

public interface CompetenciaOrganizacionalDAO {

	public int count(String tipo);
	public List<CompetenciaOrganizacional> search(String tipo, int first, int rows);

	public CompetenciaOrganizacional salvar(CompetenciaOrganizacional entidade);
	public void excluir(CompetenciaOrganizacional entidade);

	
}