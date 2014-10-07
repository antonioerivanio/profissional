package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RepresentacaoValorDAO;
import br.gov.ce.tce.srh.domain.RepresentacaoValor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("representacaoValorService")
public class RepresentacaoValorServiceImpl implements RepresentacaoValorService {

	@Autowired
	private RepresentacaoValorDAO dao;


	@Override
	@Transactional
	public void salvar(RepresentacaoValor entidade) throws SRHRuntimeException {

		// validando data inicio e fim
		if (entidade.getFim() != null){
			if (entidade.getInicio().after(entidade.getFim())) {
				throw new SRHRuntimeException("A data inicio deve ser menor que a data fim.");
			}	
		}

		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(RepresentacaoValor entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long cargo) {
		return dao.count(cargo);
	}


	@Override
	public List<RepresentacaoValor> search(Long cargo, int first, int rows) {
		return dao.search(cargo, first, rows);
	}


	public void setDAO(RepresentacaoValorDAO representacaoValorDAO) {this.dao = representacaoValorDAO;}

}
