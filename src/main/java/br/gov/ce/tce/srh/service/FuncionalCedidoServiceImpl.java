package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalCedidoDAO;
import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.domain.VinculoRGPS;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
 * 
 * @author robstown
 *
 */
@Service("FuncionalCedidoServiceImpl")
public class FuncionalCedidoServiceImpl implements FuncionalCedidoService {

	@Autowired
	private FuncionalCedidoDAO dao;

	@Override
	@Transactional
	public FuncionalCedido salvar(FuncionalCedido entidade) throws SRHRuntimeException {	
		
		return dao.salvar(entidade);
	}
	
	/**
	 * validar e retorna TRUE se todos os campos foram preenchidos
	 * @return boolean true |false
	 */
	@Override
	public boolean isOk(FuncionalCedido entidade) {
		if(entidade.getFuncional().getRegimeTrabalhistaIsnull()) {
			FacesUtil.addErroMessage("Regime Trabalhista é obrigatório!");
			return Boolean.FALSE;
		}
		
		if(entidade.getFuncional().getRegimePrevidenciarioIsnull()) {
			FacesUtil.addErroMessage("Regime Previdênciario é obrigatório!");
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}

	@Override
	public int count(String matricula) {
		return dao.count(matricula);
	}
	
	@Override
	public FuncionalCedido getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public FuncionalCedido getByCodigo(Long codigo) {
		return dao.getByCodigo(codigo);
	}


	@Override
	public List<FuncionalCedido> findAll() {
		return dao.findAll();
	}


	@Override
	public List<FuncionalCedido> findByCodigo(Long codigo) {
		return dao.findByCodigo(codigo);
	}

	@Override
	public List<FuncionalCedido> search(String matricula, int first, int rows){
		return dao.search(matricula, first, rows);
	}

	public void setDAO(FuncionalCedidoDAO dao) {this.dao = dao;}

  @Override
  public void excluir(FuncionalCedido entidade) {
    dao.excluir(entidade);
  }

}
