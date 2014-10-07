package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AverbacaoDAO;
import br.gov.ce.tce.srh.domain.Averbacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
*
* @author robstown
* 
*/
@Service("averbacaoService")
public class AverbacaoServiceImpl implements AverbacaoService {

	@Autowired
	private AverbacaoDAO dao;

	
	@Override
	@Transactional
	public void salvar(Averbacao entidade,boolean periodoValido) throws SRHRuntimeException {
		
		//validando dados obrigatorios
		validaDados(entidade);

		// calculado quantidade de dias -- ORIGINAL
		//entidade.setQtdeDias((long) SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim()));
		
		// calculado quantidade de dias -- NOVA CONTAGEM
		entidade.setQtdeDias((long) SRHUtils.diffData(entidade.getInicio(), entidade.getFim()));
		
		/*
		 * Regra:
		 * 
		 * Validando Averbacoes existentes.
		 * 
		 */
		if(!periodoValido)
			verificandoAverbacaoExistentes(entidade);

	    // persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Averbacao entidade) {
		dao.excluir(entidade);
	}
	

	@Override
	public int count(Long funcional) {
		return dao.count(funcional);
	}


	@Override
	public List<Averbacao> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal, first, rows);
	}


	@Override
	public List<Averbacao> findByPessoal(Long idPessoal) {
		return dao.findByPessoal(idPessoal);
	}



	/**
	 * Validar:
	 * 
	 * · Deve ser setado a pessoa (servidor).
	 * · Deve ser setada a data inicial.
	 * · Deve ser setada a data final.
	 * · Deve ser setado a qtde dias.
	 * · Deve ser setada a descricao.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(Averbacao entidade) throws SRHRuntimeException {
		
		// validando o servidor
		if (entidade.getPessoal() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		// validando estado
		if (entidade.getUf() == null )
			throw new SRHRuntimeException("O estado é obrigatório.");

		// validando municipio
		if (entidade.getMunicipio() == null )
			throw new SRHRuntimeException("O município é obrigatório.");

		// validando entidade
		if (entidade.getEntidade() == null || entidade.getEntidade().equals("") )
			throw new SRHRuntimeException("A entidade é obrigatório.");

		// validar data inicio
		if ( entidade.getInicio() == null )
			throw new SRHRuntimeException("A Data Inicial é obrigatório.");

		// validando data final
		if ( entidade.getFim() == null )
			throw new SRHRuntimeException("A Data Final é obrigatório.");

		// validando se o período inicial é menor que o final
		if ( entidade.getInicio().after(entidade.getFim() ) )
			throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");

		// validando esfera
		if (entidade.getEsfera() == null || entidade.getEsfera() == 0l )
			throw new SRHRuntimeException("A esfera é obrigatória.");

		// validando previdencia
		if (entidade.getPrevidencia() == null || entidade.getPrevidencia() == 0l )
			throw new SRHRuntimeException("A previdência é obrigatória.");

		// validando descricao
		if ( entidade.getDescricao() == null || entidade.getDescricao().equals("") ) 
			throw new SRHRuntimeException("A descrição é obrigatório.");

	}


	/**
	 * Regra de Negocio:
	 *  
	 * O Período Inicial e Final devem ser checados para saber se já foi lançada nenhuma 
	 * averbacao para o servidor neste mesmo período.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoAverbacaoExistentes(Averbacao entidade) throws SRHRuntimeException {

		List<Averbacao> averbacoes = dao.findByPessoal( entidade.getPessoal().getId() );
			
		// percorrendo acrescimos
		for (Averbacao averbacaoExistente : averbacoes) {

			// verificando se nao eh alteracao ou a mesma ferias
			if (entidade.getId() != null && entidade.getId().equals(averbacaoExistente.getId()) ) {
				continue;
			}

			// validando periodo de averbacao inicial 
			if( averbacaoExistente.getInicio().after( entidade.getInicio() )
					&& averbacaoExistente.getInicio().before( entidade.getFim() ) ) {
				throw new SRHRuntimeException("Esta averbação tem um período concomitante com outra anteriormente cadastrada. Se você tem certeza sobre o período, clique novamente no botão salvar.");
			}

			// validando periodo de averbacao final
			if( averbacaoExistente.getFim().after( entidade.getInicio() )
					&& averbacaoExistente.getFim().before( entidade.getFim() ) ) {
				throw new SRHRuntimeException("Esta averbação tem um período concomitante com outra anteriormente cadastrada. Se você tem certeza sobre o período, clique novamente no botão salvar.");
			}

			// validando periodo de averbacao no meio
			if( averbacaoExistente.getInicio().before( entidade.getInicio() )
					&& averbacaoExistente.getFim().after( entidade.getFim() ) ) {
				throw new SRHRuntimeException("Esta averbação tem um período concomitante com outra anteriormente cadastrada. Se você tem certeza sobre o período, clique novamente no botão salvar.");
			}

		}
	}


	public void setDAO(AverbacaoDAO dao){this.dao = dao;}

}
