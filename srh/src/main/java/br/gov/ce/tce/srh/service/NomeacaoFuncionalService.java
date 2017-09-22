package br.gov.ce.tce.srh.service;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface NomeacaoFuncionalService {

	public void nomear(Funcional entidade) throws SRHRuntimeException;

	public void alterarNomeacao(Funcional entidade) throws SRHRuntimeException;

	public void excluirNomeacao(Funcional entidade) throws SRHRuntimeException;

}