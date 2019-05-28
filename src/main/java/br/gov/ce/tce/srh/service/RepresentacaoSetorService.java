package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface RepresentacaoSetorService {
	
	public int count(Long id);
	public List<RepresentacaoSetor> search(Long cargo, int first, int rows);

	public void salvar(RepresentacaoSetor entidade) throws SRHRuntimeException;
	public void excluir(RepresentacaoSetor entidade);

	public RepresentacaoSetor getByCargoSetor(Long cargo, Long setor);

	public List<RepresentacaoSetor> findBySetorAtivo(Long setor, boolean ativo);

}
