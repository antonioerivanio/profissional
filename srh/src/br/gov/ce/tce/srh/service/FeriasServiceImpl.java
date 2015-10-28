/**
 * 
 */
package br.gov.ce.tce.srh.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.util.SRHUtils;
import br.gov.ce.tce.srh.dao.FeriasDAO;
import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author joel.barbosa
 *
 */
@Service("feriasService")
public class FeriasServiceImpl implements FeriasService {

	@Autowired
	private FeriasDAO dao;

	@Autowired
	private FuncionalService funcionalService;

	
	@Override
	@Transactional
	public void salvar(Ferias entidade) throws SRHRuntimeException {
		
		//validando dados obrigatorios
		validaDados(entidade);


		/*
		 * Regra
		 * 
		 * Se for contagem em dobro, a referencia deve ser <- 1998
		 * 
		 */
		if ( entidade.getTipoFerias().getId().equals(5l) && entidade.getAnoReferencia() > 1998 )
			throw new SRHRuntimeException("A referência deve ser menor ou igual a 1998.");


		//verificaQtdeDeDias
		verificaQtdeDeDias(entidade.getAnoReferencia(), entidade.getPeriodo());


		//valida QtdeDias 
	    calculaQtdeDias(entidade);


		/*
		 * Regra
		 * Setar o IDFUNCIONAL conforme a data
		 */
		setandoFuncionalConformeData(entidade);


		/*
		 * Regra:
		 * Validar quando o periodo for 2.
		 */
		validarPeriodo(entidade);


	    //validando Ferias existentes.
	    verificandoFeriasExistentes(entidade);


	    /*
	     * Regra:
	     * Validar quantidade de dias conforme o periodo e o ano referencia <= 30 dias.
	     */
	    validarQuantDiasAnoReferencia(entidade);


	    // persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Ferias entidade) {
		dao.excluir(entidade);
		
	}
	

	@Override
	public int count(Long idPessoal) {
		return dao.count(idPessoal);
	}


	@Override
	public List<Ferias> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal, first, rows);
	}


	@Override
	public List<Ferias> findByPessoal(Long idPessoal) {
		return dao.findByPessoal(idPessoal);
	}
	
	@Override
	public Ferias findMaisRecenteByPessoal(Long idPessoal) {
		return dao.findMaisRecenteByPessoal(idPessoal);
	}


	@Override
	public List<Ferias> findByPessoalTipo(Long idPessoal, Long tipo) {
		return dao.findByPessoalTipo(idPessoal, tipo);
	}


	
	/**
	 * Regra de Negocio:
	 * 
	 *  Não pode ser menor que 1935.
	 *  O campo Referência não pode ser superior a 2 anos do ano vigente. 
	 *  O Período de ser 1 ou 2!
	 * 
	 * @param anoReferencia
	 * @param periodo
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificaQtdeDeDias(Long anoReferencia, Long periodo) throws SRHRuntimeException {
		
		// valida ano Referência < 1935
		if (anoReferencia < 1935) {
			throw new SRHRuntimeException("O ano de Referência não pode ser menor que 1935.");
		}
		
		// O campo Referência não pode ser superior a 2 ano do ano vigente
		Calendar cal = GregorianCalendar.getInstance();
	    int ano = cal.get(Calendar.YEAR);
		if ( anoReferencia > (ano + 1) ) {
			throw new SRHRuntimeException("O ano de Referência não pode ser superior a 1 ano do Ano vigente.");
		}
		
		// valida Período
		if (periodo < 1 || periodo > 2 ) {
			throw new SRHRuntimeException("O Período só pode ser equivalente a 1 ou 2.");
		}

	}


	/**
	 * Regra de Negocio:
	 *  
	 *  O Período de Férias deverá ser no máximo de 30 dias!
	 *  Após digitar o Período Inicial ou Final, o sistema deverá recalcular a qtde de dias.
	 *   O campo Dias será a diferença entre a data Fim e a data Inicio.
	 *   
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException  
	 *   
	 */
	@Override
	public void calculaQtdeDias(Ferias entidade) throws SRHRuntimeException {

		// quando nao for ferias ressalvadas ou em dobro
		if ( !entidade.getTipoFerias().getId().equals(4l) && !entidade.getTipoFerias().getId().equals(5l) )
			entidade.setQtdeDias((long) SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim()));

		if (entidade.getQtdeDias() > 30) {
	    	throw new SRHRuntimeException("A quantidade de Dias não pode ser superior a 30 dias.");
	    }

	}


	/**
	 * Validar:
	 * 
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setado o tipo de ferias.
	 *  Deve ser setado o ano de referencia.
	 *  Deve ser setado o periodo.
	 *  Deve ser setada a data inicial.
	 *  Deve ser setada a data final.
	 *  Deve ser checado se a data inical menor data final.
	 *  Validar a data de publicacao.
	 *  Validar a data do ato.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(Ferias entidade) throws SRHRuntimeException {
		
		// validando o servidor
		if (entidade.getFuncional() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		// validando tipo ferias
		if (entidade.getTipoFerias() == null)
			throw new SRHRuntimeException("O tipo férias é obrigatório.");

		// validando referencia
		if (entidade.getAnoReferencia() == null)
			throw new SRHRuntimeException("A referência é obrigatória.");

		// validando periodo
		if (entidade.getPeriodo() == null)
			throw new SRHRuntimeException("O período é obrigatório.");

		// validando data inicial e final caso nao for ferias ressalvadas ou em dobro
		if ( !entidade.getTipoFerias().getId().equals(4l) && !entidade.getTipoFerias().getId().equals(5l) ) {

			// validar data inicio
			if ( entidade.getInicio() == null )
				throw new SRHRuntimeException("A Data Inicial é obrigatória.");

			// validando data final
			if ( entidade.getFim() == null )
				throw new SRHRuntimeException("A Data Final é obrigatória.");

			// validando se o período inicial é menor que o final
			if ( entidade.getInicio().after(entidade.getFim() ) )
				throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");

		}
		
		// validando o ano da data inicial, deve ser maior ou igual a referencia
		if (entidade.getInicio() != null) {

			Calendar calendarInicio = new GregorianCalendar();
			calendarInicio.setTime( entidade.getInicio() );

			if ( calendarInicio.get( GregorianCalendar.YEAR ) < entidade.getAnoReferencia() ) {
				throw new SRHRuntimeException("O ano da Data Inicial deve ser maior ou igual a " + entidade.getAnoReferencia() +".");
			}

		}		

		// validando caso a data de publicacao seja preenchida, a mesma deve ser maior ou igual a referencia
		if (entidade.getDataPublicacao() != null) {

			Calendar calendarPublicacao = new GregorianCalendar();
			calendarPublicacao.setTime( entidade.getDataPublicacao() );

			if ( calendarPublicacao.get( GregorianCalendar.YEAR ) < entidade.getAnoReferencia() - 1 ) {
				throw new SRHRuntimeException("O ano da Data Publicação deve ser maior ou igual a " + (entidade.getAnoReferencia() - 1) +".");
			}

		}

		// validando caso a data do ato seja preenchida, a mesma deve ser maior ou igual a referencia
		if (entidade.getDataDoAto() != null) {

			Calendar calendarAto = new GregorianCalendar();
			calendarAto.setTime( entidade.getDataDoAto() );

			if ( calendarAto.get( GregorianCalendar.YEAR ) < entidade.getAnoReferencia() - 1 ) {
				throw new SRHRuntimeException("O ano da Data do Ato deve ser maior ou igual a " + (entidade.getAnoReferencia() - 1) +".");
			}

		}

	}


	/**
	 * Regra de Negocio:
	 * 
	 *  Deve ser setado o IDFUNCIONAL conforme a data inicio das ferias.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void setandoFuncionalConformeData(Ferias entidade) {

		// quando nao for ressalva e contagem em dobro
		if ( !entidade.getTipoFerias().getId().equals(4l) && !entidade.getTipoFerias().getId().equals(5l) ) {

			List<Funcional> listaFuncional = funcionalService.findByPessoal( entidade.getFuncional().getPessoal().getId(), " desc" );

			Funcional selecionada = null;

			// verificando com a funcional atual
			for ( Funcional funcional : listaFuncional ) {

				if ( funcional.getExercicio().before(entidade.getInicio()) || funcional.getExercicio().equals(entidade.getInicio()) ) {
					selecionada = funcional;
					entidade.setFuncional(funcional);
					break;
				}
			}

			// caso nenhuma funcional foi encontrada
			if ( selecionada == null )
				throw new SRHRuntimeException("A data das férias não condiz com nenhum período de nomeação do Funcionário. Digite outra data.");

		}

	}


	/**
	 * Regra de Negocio:
	 *  
	 * Validar periodo 2 conforme os cargos: 8, 9, 10 (Conselheiro, Auditor, Procurador). 
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarPeriodo(Ferias entidade) {

		// verificar so quando o periodo for 2
		if ( entidade.getPeriodo().equals(2l) ) {

			// verificando o cargo 8, 9, 10 (Conselheiro, Auditor, Procurador)
			if ( entidade.getFuncional().getOcupacao().getId() < 8 || entidade.getFuncional().getOcupacao().getId() > 10 ) {
				throw new SRHRuntimeException("Somente os cargos Conselheiro, Auditor, Procurador podem ter Período 2.");		
			}	
		}
	}


	/**
	 * Regra de Negocio:
	 *  
	 * O Período Inicial e Final devem ser checados para saber se já foi lançada nenhuma 
	 * ferias para o servidor neste mesmo período.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoFeriasExistentes(Ferias entidade) throws SRHRuntimeException {

		// verificar quando nao for ressalva e contagem em dobro
		if ( !entidade.getTipoFerias().getId().equals(4l) && !entidade.getTipoFerias().getId().equals(5l) ) {

			List<Ferias> ferias = dao.findByPessoal( entidade.getFuncional().getPessoal().getId() );

			// percorrendo ferias
			for (Ferias feriasExistentes : ferias) {

				// verificando se nao eh alteracao ou a mesma ferias
				if (entidade.getId() != null && entidade.getId().equals(feriasExistentes.getId()) ) {
					continue;
				}

				// verificar quando as antigas sao ressalva ou contagem em dobro
				if ( feriasExistentes.getTipoFerias().getId().equals(4l) || feriasExistentes.getTipoFerias().getId().equals(5l) ) {
					continue;
				}

				// verificar o tempo apenas se as ferias são do mesmo tipo
				if ( feriasExistentes.getTipoFerias().equals(entidade.getTipoFerias())) {
				
					// validando periodo de ferias inicial 
					if( feriasExistentes.getInicio().after( entidade.getInicio() )
							&& feriasExistentes.getInicio().before( entidade.getFim() ) ) {
						throw new SRHRuntimeException("Já existe Férias cadastrada nesse período de tempo.");
					}
	
					// validando periodo de ferias final
					if( feriasExistentes.getFim().after( entidade.getInicio() )
							&& feriasExistentes.getFim().before( entidade.getFim() ) ) {
						throw new SRHRuntimeException("Já existe Férias cadastrada nesse período de tempo.");
					}
	
					// validando periodo de ferias no meio
					if( feriasExistentes.getInicio().before( entidade.getInicio() )
							&& feriasExistentes.getFim().after( entidade.getFim() ) ) {
						throw new SRHRuntimeException("Já existe Férias cadastrada nesse período de tempo.");
					}
				}

			}
		}
	}


	/**
	 * Regra de Negocio:
	 *  
	 * O Período e o Ano Referencia devem ser checados para saber quantos dias ja foram utilizados, nao podendo ser lançada 
	 * ferias para o servidor neste mesmo período e ano de referencia com o somatorio > 30 dias.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarQuantDiasAnoReferencia(Ferias entidade) {

		// quando nao for ressalva e contagem em dobro
		if ( !entidade.getTipoFerias().getId().equals(4l) && !entidade.getTipoFerias().getId().equals(5l) ) {

			List<Ferias> ferias = dao.findByPessoalPeriodoReferencia( entidade.getFuncional().getPessoal().getId(), 
					entidade.getPeriodo(), entidade.getAnoReferencia(), entidade.getTipoFerias().getId());

			int somaDias = 0;
			// percorrendo ferias
			for (Ferias feriasExistentes : ferias) {

				if(!feriasExistentes.getId().equals(entidade.getId())){
					if ( !feriasExistentes.getTipoFerias().getId().equals(4l) ) {
						somaDias += feriasExistentes.getQtdeDias().intValue();	
					}
				}
			}

			// somando com o periodo digitado
			somaDias += SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim());

			if ( somaDias > 30)
				throw new SRHRuntimeException("A quantidade total de dias de férias não pode ser maior que 30 dias para o mesmo Período, Ano Referência e Tipo de férias.");

		}
	}


	public void setDAO(FeriasDAO dao){this.dao = dao;}

}
