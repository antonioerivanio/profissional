package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * 
 * @author robstown
 *
 */
@Service("nomeacaoFuncionalService")
public class NomeacaoFuncionalServiceImpl implements NomeacaoFuncionalService {

	@Autowired
	private FuncionalService funcionalService;

	@Autowired 
	private ReferenciaFuncionalService referenciaFuncionalService;

	@Autowired
	private FuncionalSetorService funcionalSetorService;

	@Autowired
	private OcupacaoService ocupacaoService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;

	private String tipoEntradaDescricao = null;

	@Override
	@Transactional
	public void nomear(Funcional entidade) throws SRHRuntimeException {

		// validando dados obrigatorios.
		validarNomeacao(entidade);

		/*
		 * Regra
		 * Validar qtd. quintos e a proporcionalidade
		 * 
		 */
		validarQtdQuintosProporcionalidade(entidade);

		/*
		 * Regra
		 * Nomea��o Anterior V�lida
		 */
		validarNomeacaoAnteriorAtiva(entidade);
		
		/*
		 * Regra
		 * Validar a matricula unica
		 * 
		 */
		if ( entidade.getId() == null ) {
			Funcional funcional = funcionalService.getByMatriculaAtivo( entidade.getMatricula() );
			if ( funcional != null )
				throw new SRHRuntimeException("Esta matr�cula esta ativa.");	
		}

		// persistindo
		entidade = funcionalService.salvar(entidade);


		// criando a referencia da nomeacao
		if ( !entidade.getOcupacao().isCargoIsolado() ) {

			ReferenciaFuncional referencia = new ReferenciaFuncional();

			referencia.setFuncional( entidade );
			referencia.setClasseReferencia( entidade.getClasseReferencia() );
			referencia.setTipoMovimento( entidade.getTipoMovimentoEntrada() );
			referencia.setDescricao( entidade.getDescricaoNomeacao() );
			referencia.setInicio( entidade.getExercicio() );
			referencia.setDataAto( entidade.getExercicio() );
			referencia.setTipoPublicacao( entidade.getTipoPublicacaoNomeacao() );
			referencia.setDoeAto( entidade.getDoeNomeacao() );

			// persistindo a referencia funcional		
			referenciaFuncionalService.salvar(referencia);
		}

		// criando o historico de lotacao
		FuncionalSetor funcionalSetor = new FuncionalSetor();
		funcionalSetor.setDataInicio( entidade.getExercicio() );
		funcionalSetor.setFuncional( entidade );
		funcionalSetor.setObservacao( entidade.getDescricaoNomeacao() );
		funcionalSetor.setSetor( entidade.getSetor() );
		funcionalSetorService.salvar( funcionalSetor );
	}

	
	@Override
	@Transactional
	public void alterarNomeacao(Funcional entidade) throws SRHRuntimeException {

		// verificar se data saida eh NULA
		if ( entidade.getSaida() != null )
			throw new SRHRuntimeException("A nomea��o n�o pode ser alterada pois ja foi exonerada.");

		// verificando quantidade de referencias funcionais
		List<ReferenciaFuncional> listaReferencias = referenciaFuncionalService.findByFuncional( entidade.getId() );
		if ( listaReferencias.size() > 1 )
			throw new SRHRuntimeException("A nomea��o n�o pode ser alterada pois ja foi teve progress�o.");

		// alterando
		funcionalService.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluirNomeacao(Funcional entidade) {

		// verificar se data saida eh NULA
		if ( entidade.getSaida() != null )
			throw new SRHRuntimeException("A nomea��o n�o pode ser excluida pois ja foi exonerada.");

		// verificando quantidade de referencias funcionais
		List<ReferenciaFuncional> listaReferencias = referenciaFuncionalService.findByFuncional( entidade.getId() );
		if ( listaReferencias.size() > 1 )
			throw new SRHRuntimeException("A nomea��o n�o pode ser excluida pois ja foi teve progress�o.");

		// excluindo a referencia funcional
		referenciaFuncionalService.excluirAll( entidade.getId() );

		// excluindo historico de lotacao
		funcionalSetorService.excluirAll( entidade.getId() );

		// excluindo a nomeacao
		funcionalService.excluir(entidade);
	}



	/**
	 * Regra de Negocio:
	 * 
	 * � Deve ser setado a pessoa (servidor).
	 * � Deve ser setada a matricula.
	 * � Deve ser setado o tipo de movimento de entrada.
	 * � Validar tipo de publicacao.
	 * � Validar data de publicacao.
	 * � Deve ser setado a data de posse.
	 * � Deve ser setado a data de exercicio.
	 * � Deve ser setado a Observa��o.
	 * � Deve ser setado o tipo de folha.
	 * � Deve ser setado o cargo/funcao.
	 * � Deve ser setado a classe/referencia.
	 * � Deve ser setado a situacao.
	 * � Deve ser setado o vinculo.
	 * � Deve ser setado o status.
	 * � Deve ser setado o setor atual.
	 * � Deve ser setado a proporcionalidade.
	 * � Deve ser setado a previdencia.
	 * � Deve ser setado o regime.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarNomeacao(Funcional entidade) throws SRHRuntimeException {

		//validando o servidor
		if( entidade.getPessoal() == null )
			throw new SRHRuntimeException("O Funcion�rio � obrigat�rio. Digite o nome e efetue a pesquisa.");

		//validando campo DV da matr�cula, caso este tenha sido informado 
		if( entidade.getMatricula() != null && !entidade.getMatricula().equals("") ) {
			if(!SRHUtils.validarMatricula(entidade.getMatricula())){
				throw new SRHRuntimeException("Matr�cula inv�lida.");
			}
		} else {
			//Gerando uma matr�cula
			entidade.setMatricula(funcionalService.getMaxMatricula());
		}

		
		
			//validando se campo Tipo de Entrada foi informado 
			if( entidade.getTipoMovimentoEntrada() == null )
				throw new SRHRuntimeException("O Tipo de Entrada � obrigat�rio.");
			//esta trazendo id e n�o a descri��o
					
					if (entidade.getTipoMovimentoEntrada() != null) {
						
						
						
						 for (TipoMovimento tipoMovimento : tipoMovimentoService.findByDescricaoForId(entidade.getTipoMovimentoEntrada().getId()) ) {
							this.tipoEntradaDescricao = tipoMovimento.getDescricao(); 
						 }
						 				       
						if (this.tipoEntradaDescricao.equalsIgnoreCase("Concurso P�blico")) {	
									// validando tipo de publicacao
									if (entidade.getTipoPublicacaoNomeacao() == null ) {
										if ( entidade.getDoeNomeacao() != null ) {
											throw new SRHRuntimeException("Quando a Data de Publica��o for informado, � necess�rio informar o Tipo de Publica��o.");
										}
									}
								
				
						// validando data de publicacao
						if (entidade.getDoeNomeacao() == null) {
							if (entidade.getTipoPublicacaoNomeacao() != null ) {
								throw new SRHRuntimeException("Quando o Tipo de Publica��o for informado, � necess�rio informar a Data de Publica��o.");
							}
						}
						
						
				
						//verificando se campo posse foi informado
						if( entidade.getPosse() == null )
							throw new SRHRuntimeException("A Posse � obrigat�rio.");
				
						//verificando se campo exercicio foi informado
						if( entidade.getExercicio() == null )
							throw new SRHRuntimeException("O Exerc�cio � obrigat�rio.");		
		
				}
				
					}
				
		//validando caso a data de publicacao seja preenchida, a mesma deve ser menor que a posse
		if (this.tipoEntradaDescricao.equalsIgnoreCase("Concurso P�blico")) {
				if ( entidade.getOcupacao().getId()!=15 && entidade.getDoeNomeacao() != null && entidade.getDoeNomeacao().after( entidade.getPosse() ) )
					throw new SRHRuntimeException("Data Publica��o deve ser anterior a Data da Posse.");
			
				//validando a data da posse deve ser menor que a data de exercicio
				if ( entidade.getPosse().after( entidade.getExercicio() ) )
					throw new SRHRuntimeException("Data da Posse deve ser anterior a Data de Exercicio.");
		}
		//validando se o campo observacao foi informado
		if ( entidade.getDescricaoNomeacao() == null || entidade.getDescricaoNomeacao().equals(""))
			throw new SRHRuntimeException("O observa��o � obrigat�rio.");

		//validando se campo Tipo Folha foi informado 
		if( entidade.getFolha() == null )
			throw new SRHRuntimeException("O Tipo de Folha � obrigat�rio.");

		//validando se campo Tipo Ocupacao foi informado 
		if( entidade.getOcupacao() == null )
			throw new SRHRuntimeException("O Cargo/Fun��o � obrigat�rio.");

		//validando se campo Classe/Referencia foi informado 
		entidade.setOcupacao( ocupacaoService.getById(entidade.getOcupacao().getId()) );
		if( !entidade.getOcupacao().isCargoIsolado() && entidade.getClasseReferencia() == null )
			throw new SRHRuntimeException("A Classe/Refer�ncia � obrigat�rio.");

		//validando se campo Situacao foi informado 
		if( entidade.getSituacao() == null )
			throw new SRHRuntimeException("A Situa��o � obrigat�rio.");

		//validando se campo V�nculo foi informado 
		if( entidade.getVinculo() == null )
			throw new SRHRuntimeException("O V�nculo � obrigat�rio.");

		//validando se campo Status foi informado 
		if( entidade.getStatus() == null )
			throw new SRHRuntimeException("O Status � obrigat�rio.");

		//validando se campo Setor Atual foi informado 
		if( entidade.getSetor() == null )
			throw new SRHRuntimeException("O Setor Atual � obrigat�rio.");

		//validando se campo Proporcionalidade foi informado
		if( entidade.getProporcionalidade() == null )
			throw new SRHRuntimeException("A Proporcionalidade � obrigat�rio.");

		//validando a previdencia
		if( entidade.getPrevidencia() == null || entidade.getPrevidencia().equals(0l) )
			throw new SRHRuntimeException("A Previd�ncia � obrigat�rio.");

		//validando o regime
		if( entidade.getRegime() == null || entidade.getRegime().equals(0l) )
			throw new SRHRuntimeException("O Regime � obrigat�rio.");

	}


	/**
	 * Regra de Negocio:
	 * 
	 * � A Qtde de Quintos ser� a quantidade de quintos incorporados pela Lei 11.847/1999, s� ser� edit�vel caso o usu�rio 
	 *   tenha informado a Lei Incorpora��o = 11.847/1999. S� ser� permitido os valores de 0 a 5.
	 *   
	 * � A Proporcionalidade s� ser� permitida valores >= a 50 e <= a 100.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	public void validarQtdQuintosProporcionalidade(Funcional entidade) throws SRHRuntimeException {

		//verifica se o campo lei de incorpara��o foi prenchido com valor 11.847/1991 e se a quantidade de
		//quintos foi informado entre o intervalo de 0-5
		if (entidade.getLeiIncorporacao() != null && entidade.getLeiIncorporacao().equals("11.847/1991") ) {
			if ( entidade.getQtdQuintos() == null || !(entidade.getQtdQuintos() >= 0 && entidade.getQtdQuintos() <= 5)) {
				throw new SRHRuntimeException("Quantidade de Quintos s� aceita valores entre 0 - 5.");
			}
		}

		// verifica se a proporcionalidade foi informado entre o intervalo de 50-100
		if( entidade.getProporcionalidade() != null ) {
			if(!(entidade.getProporcionalidade() >=50 && entidade.getProporcionalidade() <=100)){
				throw new SRHRuntimeException("Proporcionalidade s� aceita valores de 50 a 100.");
			}
		}

	}


	/**
	 * Regra de Negocio:
	 * 
	 * � Deve ser verificado ao inserir uma nomea��o, se existe um registro v�lido para este servidor, ou seja, 
	 * existe um registro anterior para ele com Data Final Nula;
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarNomeacaoAnteriorAtiva(Funcional entidade) {

		Funcional funcional = funcionalService.getByPessoaAtivo( entidade.getPessoal().getId() );
		if ( funcional != null )
			throw new SRHRuntimeException("N�o ser� permitido inserir nova nomea��o, pois este servidor possui outra ativa.");

	}

}
