package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AtestoPessoaDAO;
import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author joel.barbosa
 *
 */
@Service("atestoPessoaService")
public class AtestoPessoaServiceImpl implements AtestoPessoaService {

	@Autowired
	private AtestoPessoaDAO dao;


	@Override
	@Transactional
	public void salvar(AtestoPessoa entidade) throws SRHRuntimeException {
		
		//validando dados obrigatorios
		validaDados(entidade);

		//valida competencia existente.
		validaCompetenciaParaMesmoServidor(entidade);

		// persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(AtestoPessoa entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long pessoa) {
		return dao.count(pessoa);
	}


	@Override
	public List<AtestoPessoa> search(Long pessoa, int first, int rows) {
		return dao.search(pessoa, first, rows);
	}


	@Override
	public AtestoPessoa getByPessoalCompetencia(Long pessoal, Long competencia) {
		return dao.getByPessoalCompetencia(pessoal, competencia);
	}



	/**
	 * Validar:
	 * 
	 * � Deve ser setado a pessoa (servidor).
	 * � Deve ser setada a data inicio.
	 * � Deve ser checado se a data inical menor data final.
	 * � Deve ser setado a competencia.
	 * � Deve ser setado o responsavel.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(AtestoPessoa entidade)throws SRHRuntimeException {

		//valida a pessoa
		if ( entidade.getPessoal() == null || entidade.getPessoal().getId().equals(0l) )
			throw new SRHRuntimeException("O servidor � obrigat�rio.");

		//valida competencia
		if ( entidade.getCompetencia() == null )
			throw new SRHRuntimeException("A compet�ncia � obrigat�ria.");

		//valida data de inicio
		if ( entidade.getDataInicio() == null )
			throw new SRHRuntimeException("O inicio � obrigat�rio.");
	
		//valida data fim
		if ( entidade.getDataFim() != null ) {

		    if ( entidade.getDataInicio().after( entidade.getDataFim() ) ) {
		    	throw new SRHRuntimeException("A data de inicio deve ser menor que a data fim.");
		    }

		}

		//valida responsavel
		if ( entidade.getResponsavel() == null || entidade.getResponsavel().getId().equals(0l) ) {
			throw new SRHRuntimeException("O respons�vel � obrigat�rio.");
		}
		
	}


	/**
	 * Regra de Negocio:
	 *  
	 * O Per�odo Inicial e Final devem ser checados para saber se j� foi lan�ada uma 
	 * competencia para o servidor neste mesmo per�odo.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validaCompetenciaParaMesmoServidor(AtestoPessoa entidade)throws SRHRuntimeException{

		List<AtestoPessoa> atestoPessoas = dao.findByPessoaCompetencia( entidade.getPessoal().getId(), entidade.getCompetencia().getId() );

		//percorrer os atestos da pessoa
		for (AtestoPessoa competenciaExiste : atestoPessoas) {

			if ( entidade.getId() == null || !entidade.getId().equals(competenciaExiste.getId()) ) {

				// validando periodo de atesto 
				if( competenciaExiste.getDataInicio().after( entidade.getDataInicio() )
						&& competenciaExiste.getDataInicio().before( entidade.getDataFim() ) ) {
					throw new SRHRuntimeException("J� existe uma Compet�ncia para essa Pessoa nesta mesma data.");
				}

				if( competenciaExiste.getDataFim().after( entidade.getDataInicio() )
						&& competenciaExiste.getDataFim().before( entidade.getDataFim() ) ) {
					throw new SRHRuntimeException("J� existe uma Compet�ncia para essa Pessoa nesta mesma data.");
				}
				
			}

		}
	}


	public void setDAO(AtestoPessoaDAO dao){this.dao = dao;}

}
