package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CategoriaFuncionalSetorResponsabilidadeDAO;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetorResponsabilidade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("categoriaFuncionalSetorResponsabilidadeService")
public class CategoriaFuncionalSetorResponsabilidadeServiceImpl implements CategoriaFuncionalSetorResponsabilidadeService{
	
	@Autowired
	private CategoriaFuncionalSetorResponsabilidadeDAO dao;

	@Override
	public int count(Setor setor, int opcaoAtiva) {
		return dao.count(setor, opcaoAtiva);
	}

	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> search(Setor setor, int opcaoAtiva, int first, int rows) {
		return dao.search(setor, opcaoAtiva, first, rows);
	}

	@Transactional
	@Override
	public CategoriaFuncionalSetorResponsabilidade salvar(CategoriaFuncionalSetorResponsabilidade entidade) {
		
		if(entidade.getDescricao().length() > 1000){
			throw new SRHRuntimeException("O campo Descrição ultrapassou o limite de 1000 caracteres.");
		}
		
		if (entidade.getInicio() != null && entidade.getFim() != null) {
			if ( SRHUtils.adiconarSubtrairDiasDeUmaData(entidade.getInicio(), 1).after(entidade.getFim()) )
				throw new SRHRuntimeException("A Data Fim deve ser maior que a Data Início.");
		}
				
		List<CategoriaFuncionalSetorResponsabilidade> respList = dao.findByCategoriaFuncionalSetor(entidade.getCategoriaFuncionalSetor());
		
		for (CategoriaFuncionalSetorResponsabilidade resp : respList) {
			if(entidade.getTipo() == 1 && entidade.getFim() == null && resp.getTipo() == 1 && resp.getFim() == null
					&& ( entidade.getId() == null || entidade.getId() != resp.getId() ) )
				throw new SRHRuntimeException("Já existe Sumário da Função ativo cadastrado para esta função.");
		}
		
		
		return dao.salvar(entidade);
	}

	@Transactional
	@Override
	public void excluir(CategoriaFuncionalSetorResponsabilidade entidade) {
		dao.excluir(entidade);		
	}

	@Override
	public CategoriaFuncionalSetorResponsabilidade findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> findAll() {
		return dao.findAll();
	}

	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> findBySetor(Setor setor) {
		return dao.findBySetor(setor);
	}

}
