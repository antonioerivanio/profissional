package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface RepresentacaoFuncionalService {

	public int count(Long pessoal);
	public List<RepresentacaoFuncional> search(Long pessoal, int first, int rows);

	public void salvar(RepresentacaoFuncional entidade) throws SRHRuntimeException;
	public void excluir(RepresentacaoFuncional entidade);

	public void exonerar(RepresentacaoFuncional entidade) throws SRHRuntimeException;

	public RepresentacaoFuncional getByid(Long id);
	public List<RepresentacaoFuncional> getByMatricula(String matricula);
	public RepresentacaoFuncional getByCpf(String cpf);
	
	public List<RepresentacaoFuncional> findByNome(String nome);
	public List<RepresentacaoFuncional> findByNomeSetor(String nome, Long idSetor);
	public List<RepresentacaoFuncional> findByPessoal(Long idPessoa);
	public List<RepresentacaoFuncional> findByFuncional(Long idFuncional);
	public List<RepresentacaoFuncional> findByUsuarioLogadoSetor(Usuario usuarioLogado, Long idSetor);
	public List<RepresentacaoFuncional> findByUsuarioLogado(Usuario usuarioLogado);

}
