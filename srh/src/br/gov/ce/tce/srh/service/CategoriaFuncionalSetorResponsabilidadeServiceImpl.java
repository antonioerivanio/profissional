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
	public int count(Setor setor) {
		return dao.count(setor);
	}

	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> search(Setor setor, int first, int rows) {
		return dao.search(setor, first, rows);
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
		
		if(entidade.getId() == null){
			List<CategoriaFuncionalSetorResponsabilidade> listAtivas = dao.findAtivaByCategoriaFuncionalSetor(entidade.getCategoriaFuncionalSetor());
			if(listAtivas != null && listAtivas.size() > 0){
				throw new SRHRuntimeException("Existe registro de Responsabilidades ativo relacionado a esta Categoria Funcional neste Setor.");
			}
		}
		
		List<CategoriaFuncionalSetorResponsabilidade> categoriaFuncionalSetorResponsabilidades = dao.findByCategoriaFuncionalSetor(entidade.getCategoriaFuncionalSetor());
		
		for (CategoriaFuncionalSetorResponsabilidade categoriaFuncionalSetorResponsabilidade : categoriaFuncionalSetorResponsabilidades) {
			if ( entidade.getId() == null || !entidade.getId().equals(categoriaFuncionalSetorResponsabilidade.getId()) ) {

				Long anteriorInicio = categoriaFuncionalSetorResponsabilidade.getInicio().getTime();
				Long anteriorFim = categoriaFuncionalSetorResponsabilidade.getFim().getTime();
				Long novaInicio = entidade.getInicio().getTime();
				Long novaFim = entidade.getFim().getTime();
				
				// validando periodo inicial
				if( novaInicio <= anteriorInicio && novaFim >= anteriorInicio ){
					throw new SRHRuntimeException("Já existe registo de Responsabilidades cadastrado neste período para esta Categoria Funcional neste Setor.");
				}

				// validando periodo final
				if( novaInicio <= anteriorFim && novaFim >= anteriorFim  ){
					throw new SRHRuntimeException("Já existe registo de Responsabilidades cadastrado neste período para esta Categoria Funcional neste Setor.");
				}

				// validando periodo no meio
				if( novaInicio > anteriorInicio  && novaFim < anteriorFim ) {
					throw new SRHRuntimeException("Já existe registo de Responsabilidades cadastrado neste período para esta Categoria Funcional neste Setor.");
				}
			}
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
