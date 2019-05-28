package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CategoriaSetorPessoalDAO;
import br.gov.ce.tce.srh.domain.CategoriaSetorPessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("categoriaSetorPessoalService")
public class CategoriaSetorPessoalServiceImpl implements
		CategoriaSetorPessoalService {

	@Autowired
	private CategoriaSetorPessoalDAO dao;

	@Override
	public int count(Long pessoa) {
		return dao.count(pessoa);
	}

	@Override
	public List<CategoriaSetorPessoal> search(Long pessoa, int first, int rows) {
		return dao.search(pessoa, first, rows);
	}

	@Override
	@Transactional
	public void excluir(CategoriaSetorPessoal entidade) {
		dao.excluir(entidade);
	}

	@Override
	@Transactional
	public void salvar(CategoriaSetorPessoal entidade) {
		if (entidade.getInicio() != null && entidade.getFim() != null) {
			if (entidade.getInicio().after(entidade.getFim())) {
				throw new SRHRuntimeException(
						"A data inicio deve ser maior que a data fim.");
			}
		}
		dao.salvar(entidade);
	}
}
