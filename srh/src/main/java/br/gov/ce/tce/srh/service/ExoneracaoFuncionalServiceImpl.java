package br.gov.ce.tce.srh.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.service.SetorService;

/**
 * 
 * @author robstown
 *
 */
@Service("exoneracaoFuncionalService")
public class ExoneracaoFuncionalServiceImpl implements ExoneracaoFuncionalService {

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired 
	private FuncionalSetorService funcionalSetorService;
	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	
	@Autowired
	private OcupacaoService ocupacaoService;
		
	@Override
	@Transactional
	public void exonerar(Funcional entidade) throws SRHRuntimeException {

		// Verifica a existência de Representação ativa
		List<RepresentacaoFuncional> representacaoFuncionalList = representacaoFuncionalService.findByFuncional(entidade.getId());
		for (RepresentacaoFuncional representacaoFuncional : representacaoFuncionalList) {
			if(representacaoFuncional.getFim() == null)
				throw new SRHRuntimeException("Exoneração não permitida. O funcionário possui Representação ativa.");
		}
		
		
		/*
		 * Regra:
		 * Validar dados obrigatorios.
		 * 
		 */	
		validarExoneracao(entidade);
		
		
		//Alteração 25/02/2014 - verificando se o tipo de saída é 'Aposentadoria' e alterando funcional
		if (entidade.getTipoMovimentoSaida().getId() == 26L) {

			entidade.setStatus(5L);
			entidade.getFolha().setId(7L);
			if (entidade.getOcupacao().getId() == 8L)
				entidade.getFolha().setId(6L);
			
		} else {
			//Id=8 é referente a situação exonerado
			entidade.getSituacao().setId(8L);
			entidade.setAtipoFp(false);
		}
		

		entidade.setSetor(setorService.getById(113L));
		entidade.setIdSetorDesignado(null);
		entidade.setPonto(false);
		
		entidade.setTipoMovimentoSaida(tipoMovimentoService.getById(entidade.getTipoMovimentoSaida().getId()));
				
		if(entidade.getTipoMovimentoSaida().getIdSituacao() != null)			
			entidade.getSituacao().setId(entidade.getTipoMovimentoSaida().getIdSituacao());	
        
		
		//Alteração 19/09/2013 - Gravando a data fim no Tb_FuncionalSetor - Evitar problema de "Existe lotação ativa para esse Servidor"
		FuncionalSetor funcionalSetor = funcionalSetorService.getAtivoByFuncional(entidade.getId());
		if(funcionalSetor!= null){
			funcionalSetor.setDataFim(entidade.getSaida());
			
			String novaObservacao = funcionalSetor.getObservacao() + " Exonerado(a) em " + new SimpleDateFormat("dd/MM/yyyy").format(entidade.getSaida()) + ".";
			if (novaObservacao.length() <= 500)
				funcionalSetor.setObservacao(novaObservacao);
			
			funcionalSetorService.salvar(funcionalSetor);
		}
		
		
		// Decrementar a coluna QUANTIDADE da tabela TB_OCUPACAO quando for lançada uma exoneração de cargo extinto ao vagar
		if(entidade.getOcupacao().getSituacao().intValue() == 2) {
			
			int quantidade = entidade.getOcupacao().getQuantidade().intValue();
			
			if (quantidade > 0) {
				entidade.getOcupacao().setQuantidade(Long.valueOf(--quantidade));
				ocupacaoService.salvar(entidade.getOcupacao());
			}
		}
		
		
		// persistindo
		funcionalService.salvar(entidade);
	}


	/**
	 * Regra de Negocio:
	 * 
	 *  Deve ser setado o tipo de publicacao saida.
	 *  Deve ser setado a data de publicacao.
	 *  Deve ser setado o tipo de saida.
	 *  Deve ser setado a data da saida.
	 *  Deve ser preenchido o campo observacao da saida.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarExoneracao(Funcional entidade) {
		
		//Alteração 01/10/2013 - Tipo Publicação e E Data publicação não será campos obrigatórios para estagiários.
		if(entidade.getOcupacao().getId() != 14 && entidade.getOcupacao().getId() != 15)
		{
			// validando o tipo de publicacao
			if (entidade.getTipoPublicacaoSaida() == null)
				throw new SRHRuntimeException("O Tipo de Publicação é obrigatório.");
	
			// validando a data da publicacao
	 		if (entidade.getDoeSaida() == null)
				throw new SRHRuntimeException("A Publicação é obrigatória.");
		}
		
 		// validando o tipo de saida
		if (entidade.getTipoMovimentoSaida() == null)
			throw new SRHRuntimeException("O Tipo de Saida é obrigatório.");

		// validando a data final
		if (entidade.getSaida() == null)
			throw new SRHRuntimeException("O último dia trabalhado é obrigatório.");
//		else {		
//			if (entidade.getExercicio().after(entidade.getSaida())) {
//				throw new SRHRuntimeException("O último dia trabalhado deve ser posterior a data de Exercício.");
//			}
//		}

		// validando a observacao da saida
		if (entidade.getDescricaoSaida() == null || entidade.getDescricaoSaida().equals(""))
			throw new SRHRuntimeException("A Observação da Saída é obrigatória.");

	}

}