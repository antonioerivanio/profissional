package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalDAO;
import br.gov.ce.tce.srh.dao.ReferenciaFuncionalDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * 
 * @author robstown
 *
 */
@Service("referenciaFuncionalServiceImpl")
public class ReferenciaFuncionalServiceImpl implements ReferenciaFuncionalService {

	@Autowired
	private ReferenciaFuncionalDAO dao;
	
	@Autowired
	private FuncionalDAO funcionalDAO;

	@Autowired
	private TipoMovimentoService tipoMovimentoService;


	@Override
	@Transactional
	public void salvar(ReferenciaFuncional entidade) throws SRHRuntimeException {

		// validando dados obrigatorios.
		validar(entidade);

		// persistindo
		dao.salvar(entidade);	
		
		// Atualizando a classe referência na tabela Funcional
		Funcional funcional = entidade.getFuncional();
		funcional.setClasseReferencia(entidade.getClasseReferencia());
		funcionalDAO.salvar(funcional);
	}


	@Override
	@Transactional
	public void progressao(ReferenciaFuncional entidade) throws SRHRuntimeException {

		// validando dados obrigatorios.
		validar(entidade);


		/*
		 * Regra: 
		 * Não será permitido alterar uma nova progressão com data Fim preenchidos e que o Tipo Movimento != 3 
		 * 
		 */
		entidade.setTipoMovimento( tipoMovimentoService.getById( entidade.getTipoMovimento().getId() ));
		if ( entidade.getFim() != null || entidade.getTipoMovimento().getTipo() != 3 )
			throw new SRHRuntimeException("Não é permitido alterar a Progressão Funcional que contenha data Fim preenchida ou Movimento do tipo 3.");

		
		/*
		 * Regra: 
		 * Quando incluir uma progressão funcional, o registro anterior será automaticamente encerrado,
		 * pois o Sistem deverá atualizar a data de fim que está nula, para a data de início-1 que está sendo
		 * inserido o novo registro.
		 *  
		 */
		ReferenciaFuncional referenciaFuncional = getById( entidade.getId() ); // findFimIsNull(entidade.getFuncional().getId());


		/*
		 * Regra 
		 * A data inicio tem que ser maior que da ultima data inicio
		 *
		 */
		if ( !entidade.getInicio().after( referenciaFuncional.getInicio() ))
			throw new SRHRuntimeException("O Início deve ser maior que o início da última Progressão Funcional.");


		// fechando a ult referencia
		if( referenciaFuncional != null ) {
			referenciaFuncional.setFim(SRHUtils.adiconarSubtrairDiasDeUmaData(entidade.getInicio(), -1));
			dao.salvar(referenciaFuncional);
		}

		// persistindo
		entidade.setId(null);
		dao.salvar(entidade);
		
		// Atualizando a classe referência na tabela Funcional
		Funcional funcional = entidade.getFuncional();
		funcional.setClasseReferencia(entidade.getClasseReferencia());
		funcionalDAO.salvar(funcional);
	}


	@Override
	public void excluirAll(Long idFuncional) throws SRHRuntimeException {
		dao.excluirAll(idFuncional);
	}


	@Override
	public int count(String matricula) {
		return dao.count(matricula);
	}


	@Override
	public List<ReferenciaFuncional> search(String matricula, int first, int rows) {
		return dao.search(matricula, first, rows);
	}


	@Override
	public List<ReferenciaFuncional> findByPessoa(Long idPessoa) {
		return dao.findByPessoa(idPessoa);
	}


	@Override
	public ReferenciaFuncional getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public ReferenciaFuncional getAtivoByFuncional(Long idFuncional) {
		return dao.getAtivoByFuncional(idFuncional);
	}


	@Override
	public List<ReferenciaFuncional> findByFuncional(Long idFuncional) {
		return dao.findByFuncional(idFuncional);
	}



	/**
	 * Regra de Negocio:
	 * 
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setada a classe referencia.
	 *  Deve ser setado o tipo de movimento de entrada.
	 *  Deve ser setado a data inicio.
	 *  Deve ser setado a descricao.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validar(ReferenciaFuncional entidade) {

		// validando o servidor
		if ( entidade.getFuncional() == null )
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");
		
		// validando a classe referencia
		if ( entidade.getClasseReferencia() == null )
			throw new SRHRuntimeException("A Classe/Referência é obrigatória.");

		// validando o tipo movimento
		if ( entidade.getTipoMovimento() == null )
			throw new SRHRuntimeException("O Tipo de Movimento é obrigatório.");

		// validando a data inicio
		if ( entidade.getInicio() == null )
			throw new SRHRuntimeException("O Início é obrigatório.");

		// validando a descricao
		if ( entidade.getDescricao() == null || entidade.getDescricao().equals("") )
			throw new SRHRuntimeException("Descrição não informada.");

	}


	public void setDAO(ReferenciaFuncionalDAO dao) {this.dao = dao;}
	
}
