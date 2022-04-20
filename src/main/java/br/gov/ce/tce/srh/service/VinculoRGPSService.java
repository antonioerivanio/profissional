package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.VinculoRGPS;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface VinculoRGPSService {

	public int count(Long idPessoal);
	public List<VinculoRGPS> search(Long idPessoal, int first, int rows);

	public void salvar(VinculoRGPS entidade) throws SRHRuntimeException;
	public void excluir(VinculoRGPS entidade);

	public List<VinculoRGPS> findByPessoal(Long idPessoal);
	public List<VinculoRGPS> findByPessoalTipo(Long idPessoal, Long tipo);
	
	public VinculoRGPS findMaisRecenteByPessoal(Long idPessoal);
	
	public List<VinculoRGPS> findByInicioETipo(Date inicio, List<Long> tiposId);
	public List<VinculoRGPS> findByInicioETipo(Date inicio, Date fim, List<Long> tiposId);
	
}
