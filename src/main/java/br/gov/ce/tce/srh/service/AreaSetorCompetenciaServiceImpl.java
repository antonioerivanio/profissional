package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAO;
import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("areaSetorCompetenciaService")
public class AreaSetorCompetenciaServiceImpl implements AreaSetorCompetenciaService {

	@Autowired
	private AreaSetorCompetenciaDAO dao;


	@Override
	@Transactional
	public void salvar(AreaSetorCompetencia entidade) throws SRHRuntimeException {

		// validando dados obrigatorios
		validarCampos(entidade);

		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
		verificandoSeEntidadeExiste(entidade);
		
		/*
		 * Regra:
		 * 
		 * Validando data fim, nao podendo ser menor que a data de inicio
		 * 
		 */
		if (entidade.getDataFim() != null){
			if (entidade.getDataInicio().after(entidade.getDataFim())) {
				throw new SRHRuntimeException("A data inicio deve ser menor que a data fim.");
			}	
		}

		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(AreaSetorCompetencia entidade) {
		dao.excluir(entidade);
	}

	
	@Override
	public int count(Long area) {
		return dao.count(area) ;
	}


	@Override
	public int count(Long area, Long competencia) {
		return dao.count(area, competencia);
	}


	@Override
	public List<AreaSetorCompetencia> search(Long area, int first, int rows) {
		return dao.search(area, first, rows);
	}


	@Override
	public List<AreaSetorCompetencia> search(Long area, Long competencia, int first, int rows) {
		return dao.search(area, competencia, first, rows);
	}



	@Override
	public List<AreaSetorCompetencia> findByArea(Long area) {
		return dao.findByArea(area);
	}

	
	@Override
	public List<AreaSetorCompetencia> findBySetor(Long setor) {
		return dao.findBySetor(setor);
	}



	/**
	 * Validando os campos obrigatorios da entidade
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *
	 */
	private void validarCampos(AreaSetorCompetencia entidade) throws SRHRuntimeException {

		if ( entidade.getAreaSetor() == null )
			throw new SRHRuntimeException("A área é obrigatória.");

		if ( entidade.getCompetencia() == null )
			throw new SRHRuntimeException("A competência é obrigatório.");

		if ( entidade.getDataInicio() == null )
			throw new SRHRuntimeException("A data de inicio é obrigatória.");
		
		if ( entidade.getMotivoInicio() == null || entidade.getMotivoInicio().equalsIgnoreCase("") )
			throw new SRHRuntimeException("O motivo entrada é obrigatório.");

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe entidade cadastrada com o mesmo SETOR, AREA E COMPETENCIA.
	 * 
	 * Validar datas:
	 *  Nao pode existir entidade repetida com data fim nulas.
	 *  Nao pode existir entidade entre o intervalo de data inicio e fim. 
	 *  
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException  
	 *  
	 */
	private void verificandoSeEntidadeExiste(AreaSetorCompetencia entidade) throws SRHRuntimeException {

		List<AreaSetorCompetencia> lista = dao.findByAreaCompetencia(entidade.getAreaSetor().getId(), entidade.getCompetencia().getId());
		for (AreaSetorCompetencia entidadeJaExiste : lista) {

			// verificando em caso de alteracao
			if (!entidade.getId().equals(entidadeJaExiste.getId())) {

				// se a data fim nao esta preenchida
				if (entidadeJaExiste.getDataFim() == null)
					throw new SRHRuntimeException("Competência do Setor já cadastrada. Operação cancelada.");

				// se a data inicio esta em algum intervalo cadastrado
				if (entidade.getDataInicio().after(entidadeJaExiste.getDataInicio()) 
						&& entidade.getDataInicio().before(entidadeJaExiste.getDataFim())) {
					throw new SRHRuntimeException("Competência do Setor já cadastrada para o intervalo de datas. Operação cancelada.");
				}

				// se a data fim esta em algum intervalo cadastrado
				if (entidade.getDataFim().after(entidadeJaExiste.getDataInicio()) 
						&& entidade.getDataInicio().before(entidadeJaExiste.getDataFim())) {
					throw new SRHRuntimeException("Competência do Setor já cadastrada para o intervalo de datas. Operação cancelada.");
				}

			}
		}
	}


	public void setDAO(AreaSetorCompetenciaDAO dao) {this.dao = dao;}

}
