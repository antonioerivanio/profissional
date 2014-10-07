package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * 
 * @author robstown
 *
 */
@Service("reclassificacaoFuncionalService")
public class ReclassificacaoFuncionalServiceImpl implements ReclassificacaoFuncionalService {

	@Autowired
	private FuncionalService funcionalService;

	@Autowired 
	private ReferenciaFuncionalService referenciaFuncionalService;


	@Override
	@Transactional
	public void reclassificar(Funcional entidade) throws Exception {
		
		/*
		 * Regra:
		 * Validar dados e condi��es obrigat�rias para reclassifica��o.
		 * 
		 */	
		validarReclassificacao(entidade);


		// fechando a antiga funcional
		Funcional funcionalAntiga = funcionalService.getById(entidade.getId()); 
		funcionalAntiga.setTipoMovimentoSaida(entidade.getTipoMovimentoSaida());
		funcionalAntiga.setDescricaoSaida(entidade.getDescricaoSaida());
		funcionalAntiga.setSaida(SRHUtils.adiconarSubtrairDiasDeUmaData(entidade.getExercicio(), -1));
		funcionalService.salvar(funcionalAntiga);

		// fechando a antiga referencia funcional
		ReferenciaFuncional referenciaAntiga = referenciaFuncionalService.getAtivoByFuncional( funcionalAntiga.getId() );
		referenciaAntiga.setFim(SRHUtils.adiconarSubtrairDiasDeUmaData(entidade.getExercicio(), -1));
		referenciaFuncionalService.salvar(referenciaAntiga);


		// persistindo a nova funcional
		entidade.setId(null);
		entidade.setPosse( entidade.getExercicio() );
		entidade.setTipoMovimentoSaida(null);
		entidade.setDescricaoSaida(null);
		entidade.setSaida(null);
		funcionalService.salvar(entidade);

		// criando a nova referencia funcional
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

			// persistindo a nova referencia funcional		
			referenciaFuncionalService.salvar(referencia);
		}

	}



	/**
	 * Regra de Negocio:
	 * 
	 * � Verificando a data saida.
	 * � Validando a ult. data saida da funcional.
	 * � Deve ser setado o cargo/fun��o.
	 * � Deve ser setado a classe/refer�ncia.
	 * � Deve ser setado o motivo entrada.
	 * � Deve ser setado a observa��o entrada.
	 * � Deve ser setado o motivo sa�da.
	 * � Deve ser setado a observa��o sa�da.
	 * � Deve ser setado o exerc�cio.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarReclassificacao(Funcional entidade) {
		
		//RN: N�o � poss�vel alterar registro com data final preenchida.
		if(entidade.getSaida() != null)
			throw new SRHRuntimeException("N�o � poss�vel alterar registro com data final preenchida.");

		//RN: A data Exerc�cio dever� ser maior que a �ltima data de exerc�cio funcional do servidor.
		for (Funcional funcional : funcionalService.findByPessoal(entidade.getPessoal().getId(), "ASC")) {
			if(funcional.getSaida() != null){
				if(entidade.getExercicio().before(funcional.getSaida())) {
					throw new SRHRuntimeException("A data Exerc�cio dever� ser maior que a �ltima data de exerc�cio funcional do servidor.");
				}
			}
		}

		if ( entidade.getOcupacao() == null )
			throw new SRHRuntimeException("O Cargo/Fun��o � obrigat�rio.");

		if ( entidade.getClasseReferencia() == null )
			throw new SRHRuntimeException("A Classe/Refer�ncia � obrigat�ria.");

		if ( entidade.getTipoMovimentoEntrada() == null)
			throw new SRHRuntimeException("O Motivo Entrada � obrigat�rio.");

		if ( entidade.getDescricaoNomeacao() == null || entidade.getDescricaoNomeacao().equals("") )
			throw new SRHRuntimeException("A Observa��o Entrada � obrigat�rio.");

		if ( entidade.getTipoMovimentoSaida() == null )
			throw new SRHRuntimeException("O Motivo Sa�da � obrigat�rio.");

		if ( entidade.getDescricaoSaida() == null || entidade.getDescricaoSaida().equals("") )
			throw new SRHRuntimeException("A Observa��o Sa�da � obrigat�rio.");

		if ( entidade.getExercicio() == null) 
			throw new SRHRuntimeException("O Exerc�cio � obrigat�rio.");
	}

}
