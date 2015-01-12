package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CompetenciaSetorFuncionalDAO;
import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CompetenciaSetorFuncional;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("competenciaSetorFuncionalService")
public class CompetenciaSetorFuncionalServiceImpl implements CompetenciaSetorFuncionalService {

	@Autowired
	private CompetenciaSetorFuncionalDAO dao;


	@Override
	@Transactional
	public void salvar(CompetenciaSetorFuncional entidade, Setor setor) throws SRHRuntimeException {

		entidade.getCategoria().setSetor(setor);
		validar(entidade);
		
		
		// persistindo
		dao.salvar(entidade);
	}


	private void validar(CompetenciaSetorFuncional entidade) {
		// validar data inicio
		if ( entidade.getInicio() == null )
			throw new SRHRuntimeException("A Data Inicial é obrigatória.");

/*		// validando data final
		if ( entidade.getFim() == null )
			throw new SRHRuntimeException("A Data Final é obrigatória.");
*/
		// validando se o período inicial é menor que o final
		if (entidade.getFim() != null && entidade.getInicio().after(entidade.getFim() ) )
			throw new SRHRuntimeException("A Data Inicial deve ser menor que a Data Final.");		

		// validando dados obrigatorios
		if( entidade.getCompetencia() == null )
			throw new SRHRuntimeException("A competência é obrigatória.");
	
		// validando dados obrigatorios
		if( entidade.getCategoria() == null ){
			throw new SRHRuntimeException("A categoria é obrigatória.");
		} else {
			if( entidade.getCategoria().getSetor() == null )
				throw new SRHRuntimeException("O setor é obrigatório.");
		}
		
	}


	@Override
	@Transactional
	public void excluir(CompetenciaSetorFuncional entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Setor setor, String tipo, CategoriaFuncional categoriaFuncional) {
		return dao.count(setor, tipo, categoriaFuncional);
	}


	@Override
	public List<CompetenciaSetorFuncional> search(Setor setor, String tipo, CategoriaFuncional categoriaFuncional, int first, int rows) {
		return dao.search(setor, tipo, categoriaFuncional, first, rows);
	}

}
