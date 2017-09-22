package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AreaSetorDAO;
import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("areaSetorService")
public class AreaSetorServiceImpl implements AreaSetorService {

	@Autowired
	private AreaSetorDAO dao;


	@Override
	@Transactional
	public void salvar(AreaSetor entidade) throws SRHRuntimeException {

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
	public void excluir(AreaSetor entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long setor, String descricao) {
		return dao.count(setor, descricao);
	}

	@Override
	public List<AreaSetor> search(Long setor, String descricao, int first, int rows) {
		return dao.search(setor, descricao, first, rows);
	}

	@Override
	public List<AreaSetor> findBySetor(Long setor) {
		return dao.findBySetor(setor);
	}



	/**
	 * Validando os campos obrigatorios da entidade
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *
	 */
	private void validarCampos(AreaSetor entidade) throws SRHRuntimeException {
		
		if (entidade.getSetor() == null )
			throw new SRHRuntimeException("O setor é obrigatório.");

		if (entidade.getDescricao() == null || entidade.getDescricao().equals(""))
			throw new SRHRuntimeException("A descrição é obrigatória.");

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe entidade cadastrada com a mesma DESCRICAO para o mesmo SETOR.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 *  
	 */
	private void verificandoSeEntidadeExiste(AreaSetor entidade) throws SRHRuntimeException {

		AreaSetor entidadeJaExiste = dao.getBySetorDescricao( entidade.getSetor().getId(), entidade.getDescricao() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Área do setor já cadastrada. Operação cancelada.");

	}


	public void setDAO(AreaSetorDAO dao) {this.dao = dao;}

}
