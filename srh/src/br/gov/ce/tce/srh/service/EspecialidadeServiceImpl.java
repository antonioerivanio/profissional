package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EspecialidadeDAO;
import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("especialidadeService")
public class EspecialidadeServiceImpl implements EspecialidadeService {

	@Autowired
	private EspecialidadeDAO dao;


	@Override
	@Transactional
	public void salvar(Especialidade entidade) throws SRHRuntimeException {

		// validando dados obrigatorios
		validarCampos(entidade);

		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
		verificandoSeEntidadeExiste(entidade);

		// persistindo
		dao.salvar(entidade);

	}


	@Override
	@Transactional
	public void excluir(Especialidade entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<Especialidade> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}

	@Override
	public Especialidade getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<Especialidade> findByDescricao(String descricao) {
		return dao.findByDescricao(descricao);
	}


	@Override
	public List<Especialidade> findAll() {
		return dao.findAll();
	}



	/**
	 * Validando os campos obrigatorios da entidade
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *
	 */
	private void validarCampos(Especialidade entidade) throws SRHRuntimeException {

		if (entidade.getDescricao() == null || entidade.getDescricao().equals(""))
			throw new SRHRuntimeException("A descrição é obrigatória.");

		if (entidade.getArea() == null || entidade.getArea().equals(""))
			throw new SRHRuntimeException("A área é obrigatória.");

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe entidade cadastrada com a mesma DESCRICAO.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 *  
	 */
	private void verificandoSeEntidadeExiste(Especialidade entidade) throws SRHRuntimeException {

		Especialidade entidadeJaExiste = dao.getByDescricao( entidade.getDescricao() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Especialidade já cadastrada. Operação cancelada.");

	}


	public void setDAO(EspecialidadeDAO dao) {this.dao = dao;}

}
