package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

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
	private ReferenciaFuncionalService referenciaFuncionalService;
	
	@Autowired 
	private FuncionalSetorService funcionalSetorService;
	

	@Override
	@Transactional
	public void exonerar(Funcional entidade) throws SRHRuntimeException {

		//Alteração 25/02/2014 - verificando se o tipo de saída é 'Aposentadoria' e alterando funcional
		if (entidade.getTipoMovimentoSaida().getId() == 26L) {

			entidade.setStatus(5L);
			entidade.setPonto(false);
			
			entidade.getFolha().setId(7L);
			if (entidade.getOcupacao().getId() == 8L)
				entidade.getFolha().setId(6L);
			
		} else {
			//Id=8 é referente a situação exonerado
			entidade.getSituacao().setId(8L);
			entidade.setAtipoFp(false);
		}

		/*
		 * Regra:
		 * Validar dados obrigatorios.
		 * 
		 */	
		validarExoneracao(entidade);
        //Alteração 06/06/2013 - verificando se o servidor é de cargo apenas comicionado ( verificar se a ocupação é um cargo isolado)
		Ocupacao ocupacao =  entidade.getOcupacao();
		if(!ocupacao.isCargoIsolado()){
			// finalizando a referencia funcional
			ReferenciaFuncional referencia = referenciaFuncionalService.getAtivoByFuncional( entidade.getId() );
			if ( referencia == null )
				throw new SRHRuntimeException("Ocorreu algum erro ao salvar, a referencia do cargo não foi encontrada. Operação cancelada.");
			referencia.setFim( entidade.getSaida() );
			referencia.setDoeAto( entidade.getDoeSaida() );
			
			// persistindo a referencia funcional		
			referenciaFuncionalService.salvar(referencia);
			
		}
		
		//Alteração 19/09/2013 - Gravando a data fim no Tb_FuncionalSetor - Evitar problema de "Existe lotação ativa para esse Servidor"
			FuncionalSetor funcionalSetor = funcionalSetorService.getAtivoByFuncional(entidade.getId());
			if(funcionalSetor!= null){
				funcionalSetor.setDataFim(entidade.getSaida());
				funcionalSetorService.salvar(funcionalSetor);
			}
		// persistindo
		funcionalService.salvar(entidade);
	}


	/**
	 * Regra de Negocio:
	 * 
	 * · Deve ser setado o tipo de publicacao saida.
	 * · Deve ser setado a data de publicacao.
	 * · Deve ser setado o tipo de saida.
	 * · Deve ser setado a data da saida.
	 * · Deve ser preenchido o campo observacao da saida.
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
			throw new SRHRuntimeException("A Data Final é obrigatória.");

		// validando a observacao da saida
		if (entidade.getDescricaoSaida() == null || entidade.getDescricaoSaida().equals(""))
			throw new SRHRuntimeException("A Observação da Saída é obrigatória.");

	}

}
