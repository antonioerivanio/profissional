package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CompetenciaOrganizacionalDAO;
import br.gov.ce.tce.srh.domain.CompetenciaOrganizacional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("competenciaOrganizacionalService")
public class CompetenciaOrganizacionalServiceImpl implements CompetenciaOrganizacionalService {

	@Autowired
	private CompetenciaOrganizacionalDAO dao;


	@Override
	@Transactional
	public void salvar(CompetenciaOrganizacional entidade) throws SRHRuntimeException {
		/*
		 * Regra:
		 * Validar quando o periodo for 1.
		 */
		validarPeriodo(entidade);

		// validando dados obrigatorios
		if( entidade.getCompetencia() == null )
			throw new SRHRuntimeException("A compet�ncia � obrigat�ria.");

		// persistindo
		dao.salvar(entidade);
	}


	private void validarPeriodo(CompetenciaOrganizacional entidade) {
		// validar data inicio
		if ( entidade.getInicio() == null )
			throw new SRHRuntimeException("A Data Inicial � obrigat�rio.");

		/*// validando data final
		if ( entidade.getFim() == null )
			throw new SRHRuntimeException("A Data Final � obrigat�rio.");*/

		// validando se o per�odo inicial � menor que o final
		if ( entidade.getFim() != null && entidade.getInicio().after(entidade.getFim() ) )
			throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");		
	}


	@Override
	@Transactional
	public void excluir(CompetenciaOrganizacional entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String tipo) {
		return dao.count(tipo);
	}


	@Override
	public List<CompetenciaOrganizacional> search(String tipo, int first, int rows) {
		return dao.search(tipo, first, rows);
	}


	public void setDAO(CompetenciaOrganizacionalDAO competenciaCompetenciaOrganizacionalDAO) {this.dao = competenciaCompetenciaOrganizacionalDAO;}

}
