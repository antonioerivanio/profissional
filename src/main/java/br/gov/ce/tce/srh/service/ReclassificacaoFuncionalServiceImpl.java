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
		 * Validar dados e condições obrigatórias para reclassificação.
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
	 *  Verificando a data saida.
	 *  Validando a ult. data saida da funcional.
	 *  Deve ser setado o cargo/função.
	 *  Deve ser setado a classe/referência.
	 *  Deve ser setado o motivo entrada.
	 *  Deve ser setado a observação entrada.
	 *  Deve ser setado o motivo saída.
	 *  Deve ser setado a observação saída.
	 *  Deve ser setado o exercício.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarReclassificacao(Funcional entidade) {
		
		//RN: Não é possível alterar registro com data final preenchida.
		if(entidade.getSaida() != null)
			throw new SRHRuntimeException("Não é possível alterar registro com data final preenchida.");

		//RN: A data Exercício deverá ser maior que a última data de exercício funcional do servidor.
		for (Funcional funcional : funcionalService.findByPessoal(entidade.getPessoal().getId(), "ASC")) {
			if(funcional.getSaida() != null){
				if(entidade.getExercicio().before(funcional.getSaida())) {
					throw new SRHRuntimeException("A data Exercício deverá ser maior que a última data de exercício funcional do servidor.");
				}
			}
		}

		if ( entidade.getOcupacao() == null )
			throw new SRHRuntimeException("O Cargo/Função é obrigatório.");

		if ( entidade.getClasseReferencia() == null )
			throw new SRHRuntimeException("A Classe/Referência é obrigatória.");

		if ( entidade.getTipoMovimentoEntrada() == null)
			throw new SRHRuntimeException("O Motivo Entrada é obrigatório.");

		if ( entidade.getDescricaoNomeacao() == null || entidade.getDescricaoNomeacao().equals("") )
			throw new SRHRuntimeException("A Observação Entrada é obrigatório.");

		if ( entidade.getTipoMovimentoSaida() == null )
			throw new SRHRuntimeException("O Motivo Saída é obrigatório.");

		if ( entidade.getDescricaoSaida() == null || entidade.getDescricaoSaida().equals("") )
			throw new SRHRuntimeException("A Observação Saída é obrigatório.");

		if ( entidade.getExercicio() == null) 
			throw new SRHRuntimeException("O Exercício é obrigatório.");
	}

}
