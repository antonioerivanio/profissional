package br.gov.ce.tce.srh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CategoriaFuncionalDAO;
import br.gov.ce.tce.srh.dao.CategoriaFuncionalSetorDAO;
import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Service("cadastroCategoriaService")
public class CadastroCategoriaFuncionalServiceImpl implements
		CadastroCategoriaFuncionalService {

	@Autowired
	private CategoriaFuncionalDAO categoriaDAO;
	
	@Autowired
	private CategoriaFuncionalSetorDAO categoriaFuncionalSetorDAO;

	@Override
	@Transactional
	public void salvar(CategoriaFuncional entidade) throws SRHRuntimeException {
		List<CategoriaFuncional> categorias = new ArrayList<CategoriaFuncional>();
		categorias = categoriaDAO.buscarCategorias();
		for (CategoriaFuncional categoria : categorias) {
			if (entidade.getDescricao().equals(categoria.getDescricao()) && entidade.getId() == null) {
				throw new SRHRuntimeException(
						"Já existe uma Categoria com esta descrição");
			}
		}
		categoriaDAO.salvar(entidade);

	}

	@Override
	@Transactional
	public List<CategoriaFuncional> listarCategoriasFuncionais() {
		return categoriaDAO.buscarCategorias();
	}

	@Override
	public int count(String descricaoCategoria) {
		return categoriaDAO.count(descricaoCategoria);
	}

	@Override
	public List<CategoriaFuncional> search(String descricaoCategoria,
			int first, int rows) {
		return categoriaDAO.search(descricaoCategoria, first, rows);
	}

	@Override
	@Transactional
	public void excluir(CategoriaFuncional entidade) {
		categoriaDAO.excluir(entidade);

	}

	@Override
	public List<CategoriaFuncional> findAll() {
		return categoriaDAO.findAll();
	}

	@Override
	public CategoriaFuncional getById(Long id) {
		return categoriaDAO.getById(id);
	}
	
	@Override
	@Transactional
	public List<CategoriaFuncional> findBySetor(Setor setor) {
		List<CategoriaFuncional> CategoriaFuncionalList = new ArrayList<CategoriaFuncional>();

		List<CategoriaFuncionalSetor> categoriaFuncionalSetorList = categoriaFuncionalSetorDAO.findBySetor(setor);
		for (CategoriaFuncionalSetor categoriaFuncionalSetor: categoriaFuncionalSetorList) {
			if (categoriaFuncionalSetor.getSetor().equals(setor)) {
				CategoriaFuncionalList.add(categoriaFuncionalSetor.getCategoriaFuncional());
			}
		}
		
		return CategoriaFuncionalList;
	}

}
