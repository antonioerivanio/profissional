package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AcrescimoDAO;
import br.gov.ce.tce.srh.domain.Acrescimo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
*
* @author robstown
* 
*/
@Service("acrescimoService")
public class AcrescimoServiceImpl implements AcrescimoService {

	@Autowired
	private AcrescimoDAO dao;

	
	@Override
	@Transactional
	public void salvar(Acrescimo entidade) throws SRHRuntimeException {
		
		//validando dados obrigatorios
		validaDados(entidade);


		// calculado quantidade de dias
		entidade.setQtdeDias((long) SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim()));

		
		/*
		 * Regra:
		 * 
		 * Validando Acrescimos existentes.
		 * 
		 */
		verificandoAcrescimosExistentes(entidade);


	    // persistindo
		dao.salvar(entidade);
	}

	@Override
	@Transactional
	public void excluir(Acrescimo entidade) {
		dao.excluir(entidade);
	}
	

	@Override
	public int count(Long funcional) {
		return dao.count(funcional);
	}


	@Override
	public List<Acrescimo> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal, first, rows);
	}


	@Override
	public List<Acrescimo> findByPessoal(Long idPessoal) {
		return dao.findByPessoal(idPessoal);
	}



	/**
	 * Validar:
	 * 
	 * � Deve ser setado a pessoa (servidor).
	 * � Deve ser setada a data inicial.
	 * � Deve ser setada a data final.
	 * � Deve ser setado a qtde dias.
	 * � Deve ser setada a descricao.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(Acrescimo entidade) throws SRHRuntimeException {
		
		// validando o servidor
		if (entidade.getPessoal() == null)
			 throw new SRHRuntimeException("O Funcion�rio � obrigat�rio. Digite o nome e efetue a pesquisa.");

		// validar data inicio
		if ( entidade.getInicio() == null )
			throw new SRHRuntimeException("A Data Inicial � obrigat�rio.");

		// validando data final
		if ( entidade.getFim() == null )
			throw new SRHRuntimeException("A Data Final � obrigat�rio.");

		// validando se o per�odo inicial � menor que o final
		if ( entidade.getInicio().after(entidade.getFim() ) )
			throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");

		// validando qtde dias
		if (entidade.getQtdeDias() == null || entidade.getQtdeDias() == 0l )
			throw new SRHRuntimeException("A quantidadde de dias � obrigat�rio.");

		// validando descricao
		if ( entidade.getDescricao() == null || entidade.getDescricao().equals("") ) 
			throw new SRHRuntimeException("A descri��o � obrigat�rio.");

	}


	/**
	 * Regra de Negocio:
	 *  
	 * O Per�odo Inicial e Final devem ser checados para saber se j� foi lan�ado nenhum 
	 * acrescimo para o servidor neste mesmo per�odo.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoAcrescimosExistentes(Acrescimo entidade) throws SRHRuntimeException {

		List<Acrescimo> acrescimos = dao.findByPessoal( entidade.getPessoal().getId() );
			
		// percorrendo acrescimos
		for (Acrescimo acrescimoExistente : acrescimos) {

			// verificando se nao eh alteracao ou a mesma ferias
			if (entidade.getId() != null && entidade.getId().equals(acrescimoExistente.getId()) ) {
				continue;
			}

			// validando periodo de acrescimo inicial 
			if( acrescimoExistente.getInicio().after( entidade.getInicio() )
					&& acrescimoExistente.getInicio().before( entidade.getFim() ) ) {
				throw new SRHRuntimeException("J� existe um Acrescimo cadastrado nesse per�odo de tempo.");
			}

			// validando periodo de acrescimo final
			if( acrescimoExistente.getFim().after( entidade.getInicio() )
					&& acrescimoExistente.getFim().before( entidade.getFim() ) ) {
				throw new SRHRuntimeException("J� existe um Acrescimo cadastrado nesse per�odo de tempo.");
			}

			// validando periodo de acrescimo no meio
			if( acrescimoExistente.getInicio().before( entidade.getInicio() )
					&& acrescimoExistente.getFim().after( entidade.getFim() ) ) {
				throw new SRHRuntimeException("J� existe um Acrescimo cadastrado nesse per�odo de tempo.");
			}

		}
	}


	public void setDAO(AcrescimoDAO dao){this.dao = dao;}

}
