package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AverbacaoDAO;
import br.gov.ce.tce.srh.domain.Averbacao;
import br.gov.ce.tce.srh.exception.PeriodoConcomitanteException;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service
public class AverbacaoService {

	@Autowired
	private AverbacaoDAO dao;
	
	@Transactional
	public void salvar(Averbacao entidade, boolean verificaAverbacaoExistente) throws SRHRuntimeException {
		
		validarDados(entidade);

		if(entidade.getInicio() != null && entidade.getFim() != null && verificaAverbacaoExistente )
			verificandoAverbacaoExistentes(entidade);

		dao.salvar(entidade);
	
	}


	@Transactional
	public void excluir(Averbacao entidade) {
		dao.excluir(entidade);
	}
	

	public int count(Long funcional) {
		return dao.count(funcional);
	}


	public List<Averbacao> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal, first, rows);
	}


	public List<Averbacao> findByPessoal(Long idPessoal) {
		return dao.findByPessoal(idPessoal);
	}


	private void validarDados(Averbacao entidade) throws SRHRuntimeException {

		if (entidade.getPessoal() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		if (entidade.getUf() == null )
			throw new SRHRuntimeException("O estado é obrigatório.");

		if (entidade.getMunicipio() == null )
			throw new SRHRuntimeException("O município é obrigatório.");

		
		if (entidade.getEntidade() == null || entidade.getEntidade().equals("") )
			throw new SRHRuntimeException("A entidade é obrigatório.");
		
		if ( entidade.getQtdeDias() == null || entidade.getQtdeDias() <= 0 )
			throw new SRHRuntimeException("A quantidade de dias calculada ou informada é inválida.");	
		
		if ( entidade.getQtdeDias() > 14600 )
			throw new SRHRuntimeException("A quantidade de dias não pode exceder 14.600 dias (40 anos).");

		// validando esfera
		if (entidade.getEsfera() == null || entidade.getEsfera() == 0l )
			throw new SRHRuntimeException("A esfera é obrigatória.");

		// validando previdencia
		if (entidade.getPrevidencia() == null || entidade.getPrevidencia() == 0l )
			throw new SRHRuntimeException("A previdência é obrigatória.");

		// validando descricao
		if ( entidade.getDescricao() == null || entidade.getDescricao().equals("") ) 
			throw new SRHRuntimeException("A descrição é obrigatória.");

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
	private void verificandoAverbacaoExistentes(Averbacao entidade) throws PeriodoConcomitanteException {

		List<Averbacao> averbacoes = dao.findByPessoal( entidade.getPessoal().getId() );
			
		// percorrendo acrescimos
		for (Averbacao averbacaoExistente : averbacoes) {

			// verificando se nao eh alteracao ou a mesma ferias
			if (entidade.getId() != null && entidade.getId().equals(averbacaoExistente.getId()) ) {
				continue;
			}
			
			if (averbacaoExistente.getInicio() != null && averbacaoExistente.getFim() != null) {

				// validando periodo de averbacao inicial 
				if( averbacaoExistente.getInicio().after( entidade.getInicio() )
						&& averbacaoExistente.getInicio().before( entidade.getFim() ) ) {
					throw new PeriodoConcomitanteException("Esta averbação tem um período concomitante com outra anteriormente cadastrada. Se você tem certeza sobre o período, clique novamente no botão salvar.");
				}
	
				// validando periodo de averbacao final
				if( averbacaoExistente.getFim().after( entidade.getInicio() )
						&& averbacaoExistente.getFim().before( entidade.getFim() ) ) {
					throw new PeriodoConcomitanteException("Esta averbação tem um período concomitante com outra anteriormente cadastrada. Se você tem certeza sobre o período, clique novamente no botão salvar.");
				}
	
				// validando periodo de averbacao no meio
				if( averbacaoExistente.getInicio().before( entidade.getInicio() )
						&& averbacaoExistente.getFim().after( entidade.getFim() ) ) {
					throw new PeriodoConcomitanteException("Esta averbação tem um período concomitante com outra anteriormente cadastrada. Se você tem certeza sobre o período, clique novamente no botão salvar.");
				}
			
			}

		}
	}


	public void setDAO(AverbacaoDAO dao){this.dao = dao;}

}
