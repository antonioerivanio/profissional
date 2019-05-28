package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Publicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface PublicacaoService {

	public int count(Long tipoDocumento);
	public List<Publicacao> search(Long tipoDocumento, int first, int rows);

	public void salvar(Publicacao entidade) throws SRHRuntimeException;
	public void excluir(Publicacao entidade);
	
}
