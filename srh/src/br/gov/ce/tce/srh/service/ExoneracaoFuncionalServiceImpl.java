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

		//Altera��o 25/02/2014 - verificando se o tipo de sa�da � 'Aposentadoria' e alterando funcional
		if (entidade.getTipoMovimentoSaida().getId() == 26L) {

			entidade.setStatus(5L);
			entidade.setPonto(false);
			
			entidade.getFolha().setId(7L);
			if (entidade.getOcupacao().getId() == 8L)
				entidade.getFolha().setId(6L);
			
		} else {
			//Id=8 � referente a situa��o exonerado
			entidade.getSituacao().setId(8L);
			entidade.setAtipoFp(false);
		}

		/*
		 * Regra:
		 * Validar dados obrigatorios.
		 * 
		 */	
		validarExoneracao(entidade);
        //Altera��o 06/06/2013 - verificando se o servidor � de cargo apenas comicionado ( verificar se a ocupa��o � um cargo isolado)
		Ocupacao ocupacao =  entidade.getOcupacao();
		if(!ocupacao.isCargoIsolado()){
			// finalizando a referencia funcional
			ReferenciaFuncional referencia = referenciaFuncionalService.getAtivoByFuncional( entidade.getId() );
			if ( referencia == null )
				throw new SRHRuntimeException("Ocorreu algum erro ao salvar, a referencia do cargo n�o foi encontrada. Opera��o cancelada.");
			referencia.setFim( entidade.getSaida() );
			referencia.setDoeAto( entidade.getDoeSaida() );
			
			// persistindo a referencia funcional		
			referenciaFuncionalService.salvar(referencia);
			
		}
		
		//Altera��o 19/09/2013 - Gravando a data fim no Tb_FuncionalSetor - Evitar problema de "Existe lota��o ativa para esse Servidor"
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
	 * � Deve ser setado o tipo de publicacao saida.
	 * � Deve ser setado a data de publicacao.
	 * � Deve ser setado o tipo de saida.
	 * � Deve ser setado a data da saida.
	 * � Deve ser preenchido o campo observacao da saida.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarExoneracao(Funcional entidade) {
		
		//Altera��o 01/10/2013 - Tipo Publica��o e E Data publica��o n�o ser� campos obrigat�rios para estagi�rios.
		if(entidade.getOcupacao().getId() != 14 && entidade.getOcupacao().getId() != 15)
		{
			// validando o tipo de publicacao
			if (entidade.getTipoPublicacaoSaida() == null)
				throw new SRHRuntimeException("O Tipo de Publica��o � obrigat�rio.");
	
			// validando a data da publicacao
	 		if (entidade.getDoeSaida() == null)
				throw new SRHRuntimeException("A Publica��o � obrigat�ria.");
		}
		
 		// validando o tipo de saida
		if (entidade.getTipoMovimentoSaida() == null)
			throw new SRHRuntimeException("O Tipo de Saida � obrigat�rio.");

		// validando a data final
		if (entidade.getSaida() == null)
			throw new SRHRuntimeException("A Data Final � obrigat�ria.");

		// validando a observacao da saida
		if (entidade.getDescricaoSaida() == null || entidade.getDescricaoSaida().equals(""))
			throw new SRHRuntimeException("A Observa��o da Sa�da � obrigat�ria.");

	}

}
