package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CategoriaFuncionalSetorDAO;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("categoriaFuncionalSetorService")
public class CategoriaFuncionalSetorServiceImpl implements
		CategoriaFuncionalSetorService {

	@Autowired
	private CategoriaFuncionalSetorDAO dao;
	
	public void setDAO(CategoriaFuncionalSetorDAO dao) {this.dao = dao;}

	
	@Override
	public int count(Setor setor) {
		return dao.count(setor);
	}

	
	@Override
	public List<CategoriaFuncionalSetor> search(Setor setor, int first,
			int rows) {
		return dao.search(setor, first, rows);
	}

	@Override
	@Transactional
	public CategoriaFuncionalSetor salvar(CategoriaFuncionalSetor entidade) throws SRHRuntimeException {
		validaCampos(entidade);
		return dao.salvar(entidade);
	}

	@Override
	@Transactional
	public void excluir(CategoriaFuncionalSetor entidade) {
		dao.excluir(entidade);
	}

	@Override
	public CategoriaFuncionalSetor findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<CategoriaFuncionalSetor> findAll() {
		return dao.findAll();
	}
	
	private void validaCampos(CategoriaFuncionalSetor entidade) throws SRHRuntimeException{
		
		if(entidade.getSetor()==null||entidade.getSetor().getId()==null){
			throw new SRHRuntimeException("O setor � obrigat�rio.");
		}
		
		if(entidade.getCategoriaFuncional() ==null||entidade.getCategoriaFuncional().getId()==null){
			throw new SRHRuntimeException("A Categoria Funcional � obrigat�ria.");
		}		
		
	}


	@Override
	public List<CategoriaFuncionalSetor> findBySetor(Setor setor) {
		return dao.findBySetor(setor);
	}

	
}
