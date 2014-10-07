package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ClasseReferenciaDAO;
import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("classeReferenciaService")
public class ClasseReferenciaServiceImpl implements ClasseReferenciaService {

	@Autowired
	private ClasseReferenciaDAO dao;


	@Override
	@Transactional
	public void salvar(ClasseReferencia entidade) throws SRHRuntimeException {

		//validando dados obrigatorios
		validaDados(entidade);

		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
		verificandoSeEntidadeExiste(entidade);

		// persistencia
		dao.salvar(entidade);

	}


	@Override
	@Transactional
	public void excluir(ClasseReferencia entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long id) {
		return dao.count(id);
	}

	
	@Override
	public List<ClasseReferencia> search(Long id, int first, int rows) {
		return dao.search(id, first, rows);
	}


	@Override
	public List<ClasseReferencia> findByCargo(Long cargo) {
		return dao.findByCargo(cargo);
	}



	/**
	 * Validar:
	 * 
	 * · Deve ser setado o simbolo.
	 * · Deve ser setado a escolaridade.
	 * · Deve ser setado a referencia.
	 * · Deve ser setado a classe.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(ClasseReferencia entidade) {

		// validando simbolo
		if ( entidade.getSimbolo() == null )
			throw new SRHRuntimeException("O símbolo é obrigatório.");

		// validando escolaridade
		if ( entidade.getEscolaridade() == null )
			throw new SRHRuntimeException("A escolaridade é obrigatório.");

		// validando a referencia
		if ( entidade.getReferencia() == null )
			throw new SRHRuntimeException("A referência é obrigatória.");

		// validando a classe
		if ( entidade.getClasse() == null )
			throw new SRHRuntimeException("A classe é obrigatória.");

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe entidade cadastrada com a mesma SIMBOLO para a mesma REFERENCIA.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 *  
	 */
	private void verificandoSeEntidadeExiste(ClasseReferencia entidade) throws SRHRuntimeException {
		ClasseReferencia entidadeJaExiste = dao.getBySimboloReferencia(entidade.getSimbolo().getId(), entidade.getReferencia());
		if (entidadeJaExiste != null && entidade.getId() != null){
			if(entidade.getId().equals(entidadeJaExiste.getId())){
				throw new SRHRuntimeException("Classe e Referência do Cargo já cadastrada. Operação cancelada.");
			}
		}
	}


	public void setDAO(ClasseReferenciaDAO dao) {this.dao = dao;}

}
