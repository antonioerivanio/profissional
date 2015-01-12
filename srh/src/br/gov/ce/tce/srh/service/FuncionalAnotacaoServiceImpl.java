package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalAnotacaoDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalAnotacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("funcionalAnotacaoService")
public class FuncionalAnotacaoServiceImpl implements FuncionalAnotacaoService {

	@Autowired
	private FuncionalAnotacaoDAO dao;

	@Autowired
	private FuncionalService funcionalService;


	@Override
	@Transactional
	public FuncionalAnotacao salvar(FuncionalAnotacao entidade)throws SRHRuntimeException {

		// validando dados obrigatorios.
		validarDados(entidade);

		/*
		 * Regra
		 * Setar o IDFUNCIONAL conforme a data
		 */
		setandoFuncionalConformeData(entidade);

		// persistindo
		return dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(FuncionalAnotacao entidade) throws SRHRuntimeException {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long pessoal) {
		return dao.count(pessoal);
	}


	@Override
	public List<FuncionalAnotacao> search(Long pessoal, int first, int rows) {
		return dao.search(pessoal, first, rows);
	}


	@Override
	public List<FuncionalAnotacao> findByPessoal(Long idPessoa){
		return dao.findByPessoal(idPessoa);
	}


	/**
	 * Validar:
	 *  Deve ser setado a funcional (servidor).
	 *  Deve ser setado a data.
	 *  Deve ser setado a anotacao.
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(FuncionalAnotacao entidade) {

		// validando o servidor
		if (entidade.getFuncional() == null)
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite a matricula ou nome e efetue a pesquisa.");

		// validando a data de inicio
		if (entidade.getData() == null)
			throw new SRHRuntimeException("A data é obrigatória.");

		// validando a data fim
		if (entidade.getAnotacao() == null || entidade.getAnotacao().equals("") ) {
			throw new SRHRuntimeException("A anotação é obrigatória.");
		}

	}

	
	/**
	 * Regra de Negocio:
	 * 
	 *  Deve ser setado o IDFUNCIONAL conforme a data da anotacao.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void setandoFuncionalConformeData(FuncionalAnotacao entidade) {

		List<Funcional> listaFuncional = funcionalService.findByPessoal( entidade.getFuncional().getPessoal().getId(), " desc" );

		Funcional selecionada = null;

		// verificando com a funcional atual
		for ( Funcional funcional : listaFuncional ) {

			if ( funcional.getExercicio().before(entidade.getData()) || funcional.getExercicio().equals(entidade.getData()) ) {
				selecionada = funcional;
				entidade.setFuncional(funcional);
				break;
			}
		}

		// caso nenhuma funcional foi encontrada
		if ( selecionada == null )
			throw new SRHRuntimeException("A data da anotação não condiz com nenhum periodo de nomeação do Funcionário. Digite outra data.");

	}


	public void setDAO(FuncionalAnotacaoDAO dao) {this.dao = dao;}

}
