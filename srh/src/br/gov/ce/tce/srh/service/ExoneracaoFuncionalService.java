package br.gov.ce.tce.srh.service;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface ExoneracaoFuncionalService {

	public void exonerar(Funcional entidade) throws SRHRuntimeException;

}