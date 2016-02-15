package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalSetorDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("FuncionalSetorService")
public class FuncionalSetorServiceImpl implements FuncionalSetorService {

	@Autowired
	private FuncionalSetorDAO dao;

	@Autowired
	private FuncionalService funcionalService;


	@Override
	@Transactional
	public FuncionalSetor salvar(FuncionalSetor entidade)throws SRHRuntimeException {

		// validando dados obrigatorios.
		validarDados(entidade);

		/*
		 * Regra: 
		 * Verificar lotacao ativa
		 * 
		 */	
		verificarLotacaoAtiva(entidade);

		/*
		 * Regra:
		 * Validar data de inicio
		 *
		 */
		validarDataInicio(entidade);

		/*
		 * Regra
		 * Atualizar o setor atual na funcional ativa
		 * 
		 */
		atualizandoFuncional(entidade);

		// persistindo
		return dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(FuncionalSetor entidade) throws SRHRuntimeException {

		if (entidade.getDataFim() != null) {
			throw new SRHRuntimeException("Não pode ser excluída, data fim preenchida. Operação cancelada.");
		}
		
		dao.excluir(entidade);
	}


	@Override
	public int count(Long pessoal) {
		return dao.count(pessoal);
	}


	@Override
	public void excluirAll(Long idFuncional) throws SRHRuntimeException {
		dao.excluirAll(idFuncional);
	}


	@Override
	public List<FuncionalSetor> search(Long pessoal, int first, int rows) {
		return dao.search(pessoal, first, rows);
	}

	@Override
	public List<FuncionalSetor>findByPessoal(Long idPessoa){
		return dao.findByPessoal(idPessoa);
	}

	@Override
	public FuncionalSetor getAtivoByFuncional(Long idFuncional) {
		return dao.getAtivoByFuncional(idFuncional);
	}


	/**
	 * Validar:
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setado o setor.
	 *  Deve ser setado a data de inicio.
	 *  Deve ser setado a data inicio menor que a data fim.
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(FuncionalSetor entidade) {

		// validando o servidor
		if (entidade.getFuncional() == null)
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite a matrícula ou nome e efetue a pesquisa.");

		// validando o setor
		if (entidade.getSetor() == null)
			throw new SRHRuntimeException("O setor é obrigatório.");

		// validando a data de inicio
		if (entidade.getDataInicio() == null)
			throw new SRHRuntimeException("A data de inicio é obrigatória.");

		// validando a data fim
		if (entidade.getDataFim() != null) {
			if (entidade.getDataInicio().after(entidade.getDataFim())) {
				throw new SRHRuntimeException("A data fim da lotação deve ser posterior a data início.");
			}
		}

	}


	/**
	 * Regra de Negocio:
	 * 
	 * As lotacoes antigas devem ser checadas para saber se já foi setado data fim em todas 
	 * antes de criar uma nova lotacao para o servidor.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificarLotacaoAtiva(FuncionalSetor entidade) {

		List<FuncionalSetor> lista = findByPessoal(entidade.getFuncional().getPessoal().getId());

		for (FuncionalSetor lotacao : lista) {

			if (entidade.getId() == null || !entidade.getId().equals( lotacao.getId() )) {

				if (lotacao.getDataFim() == null)
					throw new SRHRuntimeException("Existe lotação ativa para esse Servidor.");				
			}

		}
		
	}


	/**
	 * Regra de Negocio:
	 * 
	 * Validar se a nova data de início é maior que a última data de fim.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarDataInicio(FuncionalSetor entidade) {

		List<FuncionalSetor> lista = findByPessoal(entidade.getFuncional().getPessoal().getId());

		if (lista.size() > 0) {

			FuncionalSetor ultima = lista.get(0); // pegando a mais atual

			if (entidade.getId() == null || !entidade.getId().equals( ultima.getId())) {

				if (entidade.getDataInicio().before( ultima.getDataFim() )) {
					throw new SRHRuntimeException("A data inicio deve ser maior que a ultima data fim.");
				}

			}

		}
		
	}


	/**
	 * Regra de Negocio:
	 * 
	 * Deve ser atualizada o setor da funcional ativa do servidor.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void atualizandoFuncional(FuncionalSetor entidade) {

		Funcional funcional = funcionalService.getByMatriculaAtivo( entidade.getFuncional().getMatricula() );
		if ( funcional == null )
			throw new SRHRuntimeException("Dados Funcionais do servidor não encontrado.");

		// atualizando
		funcional.setSetor( entidade.getSetor() );
		funcionalService.salvar(funcional);

	}

	
	public void setDao(FuncionalSetorDAO dao) {this.dao = dao;}	
	public void setFuncionalService(FuncionalService funcionalService) {this.funcionalService = funcionalService;}

}
