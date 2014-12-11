package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface PessoalService {

	public int count(String nome, String cpf);
	public int count(Usuario usuarioLogado);
	public List<Pessoal> search(String nome, String cpf, int first, int rows);
	public List<Pessoal> search(Usuario usuarioLogado, int first, int rows);

	public Pessoal salvar(Pessoal entidade) throws SRHRuntimeException;
	public void excluir(Pessoal entidade);

	public Pessoal getById(Long id);
	public Pessoal getByCpf(String cpf);

	public List<Pessoal> findByNome(String nome);

	public List<Pessoal> findAllComFuncional();

	public List<Pessoal> findByCategoria(Long idCategoria);
	
	public List<Pessoal> findServidorByNome(String nome);

}
