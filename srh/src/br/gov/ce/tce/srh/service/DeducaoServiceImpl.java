/**
 * 
 */
package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DeducaoDAO;
import br.gov.ce.tce.srh.domain.Deducao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
*
* @author robstown
* 
*/
@Service("deducaoService")
public class DeducaoServiceImpl implements DeducaoService {

	@Autowired
	private DeducaoDAO dao;

	
	@Override
	@Transactional
	public void salvar(Deducao entidade) throws SRHRuntimeException {
		
		//validando dados obrigatorios
		validaDados(entidade);


		// calculado quantidade de dias
		if ( entidade.getInicio() != null )
			entidade.setQtdeDias((long) SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim()));

		
		/*
		 * Regra:
		 * 
		 * Validando Deducoes existentes.
		 * 
		 */
		verificandoDeducoesExistentes(entidade);
		

		/*
		 * Regra: 
		 * 
		 * Criando o numero do processo
		 * 
		 */	
		if (entidade.getNrProcesso() != null && !entidade.getNrProcesso().equals(""))
			entidade.setNrProcesso(entidade.getNrProcesso());


	    // persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Deducao entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long funcional) {
		return dao.count(funcional);
	}


	@Override
	public List<Deducao> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal, first, rows);
	}


	@Override
	public List<Deducao> findByPessoal(Long idPessoal) {
		return dao.findByPessoal(idPessoal);
	}



	/**
	 * Validar:
	 * 
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setado o ano de referencia.
	 *  Deve ser setado o mes de referencia.
	 *  Deve ser setado a qtde dias.
	 *  Deve ser setada a data inicial.
	 *  Deve ser setada a data final.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(Deducao entidade) throws SRHRuntimeException {
		
		//validando se as datas início e fim estão preenchidas. Caso sim, não salvar o campo mês.
		if(entidade.getInicio() != null && entidade.getFim() != null){
			entidade.setMesReferencia(0L);
		}
		
		// validando o servidor
		if (entidade.getPessoal() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");
		
		if(entidade.isFalta() == true){
			
			//Código Original
			
			/*// validando ano referencia
			if (entidade.getAnoReferencia() == null || entidade.getAnoReferencia().toString().length() < 4)
				throw new SRHRuntimeException("O ano de referência é obrigatório.");
			//validando datas de inicio e fim
			if(entidade.getInicio() == null || entidade.getFim() == null){
				// validando mes referencia
				if (entidade.getMesReferencia() == null || entidade.getMesReferencia() == 0)
					throw new SRHRuntimeException("O mês de referência é obrigatório.");
			}*/
			
			//Alteração Permitir colocar ano, mês, dias ou Data inicial e data final. Zacarias 08/08/2014
			
			if(!((entidade.getAnoReferencia()!=null && entidade.getMesReferencia()!=null && entidade.getQtdeDias()!=0)||(entidade.getInicio() != null &&  entidade.getFim() != null)))
			{	
				throw new SRHRuntimeException("Você deve informar a Data Inicial e Data Final ou o Ano Referencia, o Mês referência e a quantidade de dias.");
			}
			
		}


	/*	// validando qtde dias
		if (entidade.getQtdeDias() == null)
			throw new SRHRuntimeException("A quantidadde de dias é obrigatória.");*/

		// validando data inicial e final caso for falta
		if ( !entidade.isFalta() ) {
			
			//Código Original
			
		/*	// validar data inicio
			if ( entidade.getInicio() == null )
				throw new SRHRuntimeException("A Data Inicial é obrigatória.");

			// validando data final
			if ( entidade.getFim() == null )
				throw new SRHRuntimeException("A Data Final é obrigatória.");

			// validando se o período inicial é menor que o final
			if ( entidade.getInicio().after(entidade.getFim() ) )
				throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");*/
			if ((!(entidade.getInicio() != null &&  entidade.getFim() != null)) && entidade.getQtdeDias().equals(new Long(0)))
				throw new SRHRuntimeException("Você deve informar a Data Inicial e Data Final ou a quantidade de dias.");
			
			
			// validando se o período inicial é menor que o final
			if ((entidade.getInicio() != null &&  entidade.getFim() != null) && entidade.getInicio().after(entidade.getFim() ) )
				throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");
		}

		// validando se o período inicial é menor que o final
		if ( entidade.getInicio() != null && entidade.getInicio().after(new Date()))
			throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data atual.");
	}


	/**
	 * Regra de Negocio:
	 *  
	 * O Período Inicial e Final devem ser checados para saber se já foi lançada nenhuma 
	 * dedução para o servidor neste mesmo período.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoDeducoesExistentes(Deducao entidade) throws SRHRuntimeException {

		if ( entidade.getInicio() == null || entidade.getFim() == null ) 
			return;

		List<Deducao> deducoes = dao.findByPessoal( entidade.getPessoal().getId() );
			
		// percorrendo deducoes
		for (Deducao deducaoExistente : deducoes) {

			// verificando se nao eh alteracao ou a mesma ferias
			if(entidade.getId() != null){
				if (entidade.getId() != null && entidade.getId().equals(deducaoExistente.getId()) ) {
					continue;
				}
			}

			// validando periodo de deducao inicial
			if(deducaoExistente.getInicio() != null){
				if( deducaoExistente.getInicio().after( entidade.getInicio() )
						&& deducaoExistente.getInicio().before( entidade.getFim() ) ) {
					throw new SRHRuntimeException("Já existe uma Dedução cadastrada nesse período de tempo.");
				}
			}

			// validando periodo de deducao final
			if(deducaoExistente.getFim() != null){
				if( deducaoExistente.getFim().after( entidade.getInicio() )
						&& deducaoExistente.getFim().before( entidade.getFim() ) ) {
					throw new SRHRuntimeException("Já existe uma Dedução cadastrada nesse período de tempo.");
				}
			}

			// validando periodo de deducao no meio
			if(deducaoExistente.getInicio() != null && deducaoExistente.getFim() != null){
				if( deducaoExistente.getInicio().before( entidade.getInicio() )
						&& deducaoExistente.getFim().after( entidade.getFim() ) ) {
					throw new SRHRuntimeException("Já existe uma Dedução cadastrada nesse período de tempo.");
				}
			}

		}
	}


	public void setDAO(DeducaoDAO dao){this.dao = dao;}

}
