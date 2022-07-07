package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CodigoCategoria;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface NomeacaoFuncionalService {
	
	public void nomear(Funcional entidade) throws SRHRuntimeException;

	public void alterarNomeacao(Funcional entidade) throws SRHRuntimeException;

	public void excluirNomeacao(Funcional entidade) throws SRHRuntimeException;
	
	public List<CodigoCategoria> getCategoriaListAll() throws SRHRuntimeException;

}