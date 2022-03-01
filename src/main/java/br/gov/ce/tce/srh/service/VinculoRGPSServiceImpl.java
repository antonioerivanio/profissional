package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.VinculoRGPSDAO;
import br.gov.ce.tce.srh.domain.VinculoRGPS;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("VinculoRGPSService")
public class VinculoRGPSServiceImpl implements VinculoRGPSService {

	@Autowired
	private VinculoRGPSDAO dao;

	@Autowired
	private FuncionalService funcionalService;
	
	@Override
	@Transactional
	public void salvar(VinculoRGPS entidade) throws SRHRuntimeException {		
		
		validaDadosObrigatorios(entidade);
				
		dao.salvar(entidade);
	}

	@Override
	@Transactional
	public void excluir(VinculoRGPS entidade) {
		dao.excluir(entidade);		
	}	

	@Override
	public int count(Long idPessoal) {
		return dao.count(idPessoal);
	}

	@Override
	public List<VinculoRGPS> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal, first, rows);
	}

	@Override
	public List<VinculoRGPS> findByPessoal(Long idPessoal) {
		return dao.findByPessoal(idPessoal);
	}
	
	@Override
	public VinculoRGPS findMaisRecenteByPessoal(Long idPessoal) {
		return dao.findMaisRecenteByPessoal(idPessoal);
	}

	@Override
	public List<VinculoRGPS> findByPessoalTipo(Long idPessoal, Long tipo) {
		return dao.findByPessoalTipo(idPessoal, tipo);
	}	

	private void validaDadosObrigatorios(VinculoRGPS entidade) throws SRHRuntimeException {
		
		if (entidade.getFuncional() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

	}	
		
	
	public void setDAO(VinculoRGPSDAO dao){this.dao = dao;}

	@Override
	public List<VinculoRGPS> findByInicio(Date inicio, List<Long> tiposId) {
		return dao.findByInicio(inicio, tiposId);
	}
	
	@Override
	public List<VinculoRGPS> findByInicioETipo(Date inicio, Date fim, List<Long> tiposId) {
		return dao.findByInicioETipo(inicio, fim, tiposId);
	}

}
