package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RepresentacaoSetorDAO;
import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("representacaoSetorService")
public class RepresentacaoSetorServiceImpl implements RepresentacaoSetorService {

	@Autowired
	private RepresentacaoSetorDAO dao;


	@Override
	@Transactional
	public void salvar(RepresentacaoSetor entidade) throws SRHRuntimeException {

		// validando data inicio e fim
		if (entidade.getFim() != null){
			if (entidade.getInicio().after(entidade.getFim())) {
				throw new SRHRuntimeException("A data inicio deve ser menor que a data fim.");
			}	
		}

		if (entidade.getFim() != null)
			entidade.setAtivo(false);

		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(RepresentacaoSetor entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long cargo) {
		return dao.count(cargo);
	}

	@Override
	public List<RepresentacaoSetor> search(Long cargo, int first, int rows) {
		return dao.search(cargo, first, rows);
	}


	@Override
	public RepresentacaoSetor getByCargoSetor(Long cargo, Long setor) {
		return dao.getByCargoSetor(cargo, setor);
	}

	
	@Override
	public List<RepresentacaoSetor> findBySetorAtivo(Long setor, boolean ativo) {
		return dao.findBySetorAtivo(setor, ativo);
	}


	public void setDAO(RepresentacaoSetorDAO dao) {this.dao = dao;}

}
