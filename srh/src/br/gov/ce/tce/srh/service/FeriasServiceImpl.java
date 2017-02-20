package br.gov.ce.tce.srh.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FeriasDAO;
import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("feriasService")
public class FeriasServiceImpl implements FeriasService {

	@Autowired
	private FeriasDAO dao;

	@Autowired
	private FuncionalService funcionalService;
	
	@Override
	@Transactional
	public void salvar(Ferias entidade) throws SRHRuntimeException {		
		
		validaDadosObrigatorios(entidade);
		
		validaAnoReferencia (entidade);

		validaDatasEAnoReferencia(entidade);		

		validaPeriodo(entidade);
 
		validaQtdeDias(entidade);
		
		validarQtdeDiasNoAnoReferenciaEPeriodo(entidade);		

		setandoFuncionalConformeData(entidade);
	    
	    verificandoFeriasExistentes(entidade);
	    
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

	private void validaDadosObrigatorios(Ferias entidade) throws SRHRuntimeException {
		
		if (entidade.getFuncional() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		if (entidade.getTipoFerias() == null)
			throw new SRHRuntimeException("O Tipo de Férias é obrigatório.");

		if (entidade.getAnoReferencia() == null || entidade.getAnoReferencia() <= 0L)
			throw new SRHRuntimeException("O Ano Referência é obrigatório.");

		if (entidade.getPeriodo() == null || entidade.getPeriodo() <= 0L)
			throw new SRHRuntimeException("O Período é obrigatório.");
		
		if ( !entidade.getTipoFerias().consideraSomenteQtdeDias() ) {

			if ( entidade.getInicio() == null )
				throw new SRHRuntimeException("A Data Inicial é obrigatória.");

			if ( entidade.getFim() == null )
				throw new SRHRuntimeException("A Data Final é obrigatória.");

			if ( entidade.getInicio().after(entidade.getFim() ) )
				throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");

		}
		
		if (entidade.getQtdeDias() == null || entidade.getQtdeDias() <= 0L)
			throw new SRHRuntimeException("A quantidade de dias deve ser maior que zero.");

	}	
	
	private void validaAnoReferencia(Ferias entidade) throws SRHRuntimeException {
		
		if (entidade.getAnoReferencia() < 1935) {
			throw new SRHRuntimeException("O ano de Referência não pode ser menor que 1935.");
		}		
		
		Calendar cal = GregorianCalendar.getInstance();
	    int ano = cal.get(Calendar.YEAR);
		if ( entidade.getAnoReferencia() > (ano + 1) ) {
			throw new SRHRuntimeException("O ano de Referência não pode ser superior a 1 ano do Ano vigente.");
		}
		
		if ( entidade.getTipoFerias().getId().equals(5l) && entidade.getAnoReferencia() > 1998 )
			throw new SRHRuntimeException("A referência deve ser menor ou igual a 1998 para Férias contadas em dobro.");		

	}
	
	private void validaDatasEAnoReferencia(Ferias entidade) throws SRHRuntimeException {
		
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
	
	private void validaPeriodo (Ferias entidade) throws SRHRuntimeException{
		
		if (entidade.getPeriodo() < 1 || entidade.getPeriodo() > 2 ) {
			throw new SRHRuntimeException("O Período só pode ser equivalente a 1 ou 2.");
		}
		
		
		if ( entidade.getPeriodo().equals(2l) ) {

			// verificando o cargo 8, 9, 10 (Conselheiro, Auditor, Procurador)
			if ( entidade.getFuncional().getOcupacao().getId() < 8 || entidade.getFuncional().getOcupacao().getId() > 10 ) {
				throw new SRHRuntimeException("Somente os cargos Conselheiro, Auditor, Procurador podem ter Período 2.");		
			}	
		}
	}
	
	private void validaQtdeDias(Ferias entidade) throws SRHRuntimeException {

		if (entidade.getQtdeDias() > 30) {
	    	throw new SRHRuntimeException("A quantidade de dias não pode ser superior a 30 dias.");
	    }
		
		if (entidade.getTipoFerias().getId() == 7L && entidade.getQtdeDias() > 10) {
			throw new SRHRuntimeException("A quantidade de dias não pode ser superior a 10 dias para Abono pecuniário de férias.");
		}

	}	
	
	private void setandoFuncionalConformeData(Ferias entidade) {

		if ( !entidade.getTipoFerias().consideraSomenteQtdeDias() ) {

			List<Funcional> listaFuncional = funcionalService.findByPessoal( entidade.getFuncional().getPessoal().getId(), " desc" );

			Funcional selecionada = null;

			for ( Funcional funcional : listaFuncional ) {

				if ( funcional.getExercicio().before(entidade.getInicio()) || funcional.getExercicio().equals(entidade.getInicio()) ) {
					selecionada = funcional;
					entidade.setFuncional(funcional);
					break;
				}
			}
			
			if ( selecionada == null )
				throw new SRHRuntimeException("A data das férias não condiz com nenhum período de nomeação do Funcionário. Digite outra data.");

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
		
		if ( !entidade.getTipoFerias().consideraSomenteQtdeDias() ) {

			List<Ferias> ferias = dao.findByPessoal( entidade.getFuncional().getPessoal().getId() );
			
			for (Ferias feriasExistentes : ferias) {

				// evita de comparar com o mesmo registro de férias
				if (entidade.getId() != null && entidade.getId().equals(feriasExistentes.getId()) ) {
					continue;
				}

				// passa as férias que não possuem data inicio e fim
				if ( feriasExistentes.getTipoFerias().consideraSomenteQtdeDias() ) {
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
	private void validarQtdeDiasNoAnoReferenciaEPeriodo(Ferias entidade) {

		
		if ( !entidade.getTipoFerias().consideraSomenteQtdeDias() ) {

			List<Ferias> ferias = dao.findByPessoalPeriodoReferencia( entidade.getFuncional().getPessoal().getId(), entidade.getPeriodo(), entidade.getAnoReferencia(), entidade.getTipoFerias().getId());

			long somaDias = 0;
			
			
			for (Ferias feriasExistentes : ferias) {

				if(!feriasExistentes.getId().equals(entidade.getId())){
					somaDias += feriasExistentes.getQtdeDias();	
				}
			}

			
			somaDias += entidade.getQtdeDias();

			
			if ( somaDias > 30 )
				throw new SRHRuntimeException("A quantidade total de dias de férias não pode ser maior que 30 dias para o mesmo Período, Ano Referência e Tipo de férias.");

		}
	}


	public void setDAO(FeriasDAO dao){this.dao = dao;}

}
