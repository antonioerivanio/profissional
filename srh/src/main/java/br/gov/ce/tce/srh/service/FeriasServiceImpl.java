package br.gov.ce.tce.srh.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
				
		validaDataInicial(entidade);
		
		validaAnoReferencia (entidade);

		validaDatasEAnoReferencia(entidade);		

		validaPeriodo(entidade);
 
		validaQtdeDias(entidade);
		
		validaQtdeDiasParaOMesmoAnoReferenciaPeriodoETipo(entidade);
		
		validaQtdeDiasParaOMesmoAnoReferenciaEPeriodo(entidade);

		setaFuncionalConformeData(entidade);
	    
	    verificaFeriasExistentes(entidade);
	    
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

		if (entidade.getAnoReferencia() == null || entidade.getAnoReferencia().intValue() <= 0)
			throw new SRHRuntimeException("O Ano Referência é obrigatório.");

		if (entidade.getPeriodo() == null || entidade.getPeriodo().intValue() <= 0)
			throw new SRHRuntimeException("O Período é obrigatório.");
		
		if ( !entidade.getTipoFerias().consideraSomenteQtdeDias() ) {

			if ( entidade.getInicio() == null )
				throw new SRHRuntimeException("A Data Inicial é obrigatória.");

			if ( entidade.getFim() == null )
				throw new SRHRuntimeException("A Data Final é obrigatória.");

			if ( entidade.getInicio().after(entidade.getFim() ) )
				throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");

		}
		
		if (entidade.getQtdeDias() == null || entidade.getQtdeDias().intValue() <= 0)
			throw new SRHRuntimeException("A quantidade de dias deve ser maior que zero.");

	}	
	
	private void validaAnoReferencia(Ferias entidade) throws SRHRuntimeException {
		
		if (entidade.getAnoReferencia().intValue() < 1935) {
			throw new SRHRuntimeException("O ano de Referência não pode ser menor que 1935.");
		}		
		
		Calendar cal = GregorianCalendar.getInstance();
	    int ano = cal.get(Calendar.YEAR);
		if ( entidade.getAnoReferencia().intValue() > (ano + 1) ) {
			throw new SRHRuntimeException("O ano de Referência não pode ser superior a 1 ano do Ano vigente.");
		}
		
		if ( entidade.getTipoFerias().getId().equals(5l) && entidade.getAnoReferencia().intValue() > 1998 )
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
	
	private void setaFuncionalConformeData(Ferias entidade) {

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
	 * O Período Inicial e Final devem ser checados para saber se já foi lançada ferias para o servidor neste mesmo período.
	 * 
	 * Checagem não deve ser feita para ferias com tipo que considera somente qtde de dias e Periodo de pagamento do terco constitucional (tipo ferias id = 2)
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificaFeriasExistentes(Ferias entidade) throws SRHRuntimeException {
		
		if ( !entidade.getTipoFerias().consideraSomenteQtdeDias() && entidade.getTipoFerias().getId().intValue() != 2) {

			List<Ferias> ferias = dao.findByPessoal( entidade.getFuncional().getPessoal().getId() );
			String mensagemDeErro = "Já existe Férias cadastrada nesse intervalo de datas.";
			
			
			for (Ferias feriasExistentes : ferias) {

				// evita de comparar com o mesmo registro de férias
				if (entidade.getId() != null && entidade.getId().equals(feriasExistentes.getId()) ) {
					continue;
				}

				// passa as férias que não possuem data inicio e fim
				if ( feriasExistentes.getTipoFerias().consideraSomenteQtdeDias() ) {
					continue;
				}
				
				// nao compara com ferias do tipo Periodo de pagamento do terco constitucional
				if ( feriasExistentes.getTipoFerias().getId().intValue() == 2 ) {
					continue;
				}				
				
				// validando periodo inicial
				if( (feriasExistentes.getInicio().after(entidade.getInicio()) || feriasExistentes.getInicio().equals(entidade.getInicio())) 
						&& ( feriasExistentes.getInicio().before(entidade.getFim()) || feriasExistentes.getInicio().equals(entidade.getFim()))){
					throw new SRHRuntimeException(mensagemDeErro);
				}

				// validando periodo final
				if( (feriasExistentes.getFim().after(entidade.getInicio()) || feriasExistentes.getFim().equals(entidade.getInicio())) 
						&& ( feriasExistentes.getFim().before(entidade.getFim()) || feriasExistentes.getFim().equals(entidade.getFim()))){
					throw new SRHRuntimeException(mensagemDeErro);
				}

				// validando periodo no meio
				if( feriasExistentes.getInicio().before( entidade.getInicio() )
						&& feriasExistentes.getFim().after( entidade.getFim() ) ) {
					throw new SRHRuntimeException(mensagemDeErro);
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
	private void validaQtdeDiasParaOMesmoAnoReferenciaPeriodoETipo(Ferias entidade) {		

		List<Ferias> ferias = dao.findByPessoalPeriodoReferencia( entidade.getFuncional().getPessoal().getId(), entidade.getPeriodo(), entidade.getAnoReferencia(), entidade.getTipoFerias().getId());

		long somaDias = 0;
		
		
		for (Ferias feriasExistentes : ferias) {

			if(!feriasExistentes.getId().equals(entidade.getId())){
				somaDias += feriasExistentes.getQtdeDias();	
			}
		}

		
		somaDias += entidade.getQtdeDias();

		
		if(entidade.getTipoFerias().getId() == 7 && somaDias > 10)
			throw new SRHRuntimeException("A quantidade total de dias de férias não pode ser maior que 10 dias para Abono pecuniário de férias no mesmo Ano Referência e Período.");
		
		if ( somaDias > 30 )
			throw new SRHRuntimeException("A quantidade total de dias de férias não pode ser maior que 30 dias para o mesmo Período, Ano Referência e Tipo de férias.");

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
	private void validaQtdeDiasParaOMesmoAnoReferenciaEPeriodo(Ferias entidade) {		

		List<Ferias> ferias = dao.findByPessoalPeriodoReferencia( entidade.getFuncional().getPessoal().getId(), entidade.getPeriodo(), entidade.getAnoReferencia());

		long diasDeFuicao = 0;
		long diasDeSomenteTerco = 0;
		long diasDeFruicaoETerco = 0;
		long diasDeResalvada = 0;
		long diasDeContadaEmDobro = 0;
		long diasDeIndenizadas = 0;
		long diasDeAbono = 0;
		
		
		if (entidade.getTipoFerias().getId().intValue() == 1){
			diasDeFuicao += entidade.getQtdeDias();
		} else if (entidade.getTipoFerias().getId().intValue() == 2){
			diasDeSomenteTerco += entidade.getQtdeDias();
		} else if (entidade.getTipoFerias().getId().intValue() == 3){
			diasDeFruicaoETerco += entidade.getQtdeDias();
		} else if (entidade.getTipoFerias().getId().intValue() == 4){
			diasDeResalvada += entidade.getQtdeDias();
		} else if (entidade.getTipoFerias().getId().intValue() == 5){
			diasDeContadaEmDobro += entidade.getQtdeDias();
		} else if (entidade.getTipoFerias().getId().intValue() == 6){
			diasDeIndenizadas += entidade.getQtdeDias();
		} else if (entidade.getTipoFerias().getId().intValue() == 7){
			diasDeAbono += entidade.getQtdeDias();
		}
		
		
		for (Ferias feriasExistentes : ferias) {

			if(!feriasExistentes.getId().equals(entidade.getId())){
				
				if (feriasExistentes.getTipoFerias().getId().intValue() == 1){
					diasDeFuicao += feriasExistentes.getQtdeDias();
				} else if (feriasExistentes.getTipoFerias().getId().intValue() == 2){
					diasDeSomenteTerco += feriasExistentes.getQtdeDias();
				} else if (feriasExistentes.getTipoFerias().getId().intValue() == 3){
					diasDeFruicaoETerco += feriasExistentes.getQtdeDias();
				} else if (feriasExistentes.getTipoFerias().getId().intValue() == 4){
					diasDeResalvada += feriasExistentes.getQtdeDias();
				} else if (feriasExistentes.getTipoFerias().getId().intValue() == 5){
					diasDeContadaEmDobro += feriasExistentes.getQtdeDias();
				} else if (feriasExistentes.getTipoFerias().getId().intValue() == 6){
					diasDeIndenizadas += feriasExistentes.getQtdeDias();
				} else if (feriasExistentes.getTipoFerias().getId().intValue() == 7){
					diasDeAbono += feriasExistentes.getQtdeDias();
				}			
			}
		}
		
		if ( diasDeFruicaoETerco > 0 && ( diasDeFuicao > 0 || diasDeSomenteTerco > 0))
			throw new SRHRuntimeException("Ação não permitida. Verifique os lançamentos de Período de fruição de férias e Período de pagamento do terço constitucional para o Ano Referência e Período informados.");
		
		
		if ( diasDeResalvada + diasDeContadaEmDobro + diasDeIndenizadas + diasDeAbono > 30
			 || diasDeFuicao + diasDeResalvada + diasDeContadaEmDobro + diasDeIndenizadas + diasDeAbono > 30
			 || diasDeFruicaoETerco + diasDeResalvada + diasDeContadaEmDobro + diasDeIndenizadas + diasDeAbono > 30			 
			 || diasDeSomenteTerco + diasDeContadaEmDobro > 30 )
			throw new SRHRuntimeException("Quantidade de dias maior que o permitido para o mesmo Ano Referência e Período.");

	}
	
	private void validaDataInicial(Ferias entidade){
		if(!entidade.getTipoFerias().consideraSomenteQtdeDias()) {
		
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date data = formato.parse("05/10/1988");
				
				if (entidade.getInicio().before(data) && (entidade.getTipoFerias().getId().intValue() == 2 || entidade.getTipoFerias().getId().intValue() == 3) ){
					throw new SRHRuntimeException("A Data Inicial deve ser posterior a 04/10/1988 para os tipos Período pagamento do terço constitucional e Período de fruição das férias e do pagamento do terço constitucional.");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		}
	}
	

	public void setDAO(FeriasDAO dao){this.dao = dao;}

	@Override
	public List<Ferias> findByInicioETipo(Date inicio, List<Long> tiposId) {
		return dao.findByInicioETipo(inicio, tiposId);
	}
	
	@Override
	public List<Ferias> findByInicioETipo(Date inicio, Date fim, List<Long> tiposId) {
		return dao.findByInicioETipo(inicio, fim, tiposId);
	}

}
