package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface OcupacaoService {

	public int count(String nomenclatura);
	public int count(String nomenclatura, Long situacao);

	public List<Ocupacao> search(String nomenclatura, int first, int rows);
	public List<Ocupacao> search(String nomenclatura, Long situacao, int first, int rows);

	public Ocupacao salvar(Ocupacao entidade);
	public void salvar(Ocupacao entidade, List<EspecialidadeCargo> listaEspecialidade, List<Simbolo> simbologias) throws SRHRuntimeException;
	public void excluir(Ocupacao entidade);

	public Ocupacao getById(Long idOcupacao);

	public List<Ocupacao> findByTipoOcupacao(Long tipoOcupacao);
	public List<Ocupacao> findAll();
	List<Ocupacao> findByPessoa(Long idPessoal);

}
