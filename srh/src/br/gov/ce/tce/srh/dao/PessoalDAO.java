package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.sca.Usuario;

public interface PessoalDAO {

	public int count(String nome, String cpf);
	public int count(Usuario usuarioLogado);
	public List<Pessoal> search(String nome, String cpf, int first, int rows);
	public List<Pessoal> search(Usuario usuarioLogado, int first, int rows);

	public Pessoal salvar(Pessoal entidade);
	public void excluir(Pessoal entidade);

	public Pessoal getById(Long id);
	public Pessoal getByCPf(String cpf);
	public Pessoal getByPasep(String pasep);

	public List<Pessoal> findByNome(String nome);
	
	public List<Pessoal> findAllComFuncional();

}