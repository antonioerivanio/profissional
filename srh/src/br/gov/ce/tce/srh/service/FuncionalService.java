package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface FuncionalService {

	public int count(Long pessoal, String orderBy);
	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows);
	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy, int first, int rows);
	
	public Funcional salvar(Funcional entidade);
	public void excluir(Funcional entidade);

	public Funcional getById(Long id);
	public Funcional getByPessoaAtivo(Long idPessoa);
	public Funcional getByMatriculaAtivo(String matricula);

	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula);
	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf);
	public Funcional getMatriculaAndNomeByCpf(String cpf);
	public List<Funcional> getMatriculaAndNomeByCpfList(String cpf);
	
	public Funcional getCpfAndNomeByMatricula(String matricula);
	public List<Funcional> findAllByNome(String nome);

	public String getMaxMatricula();

	public List<Funcional> findByNome(String nome);
	public List<Funcional> findByPessoal(Long idPessoal, String orderBy);
	public List<Funcional> findByUsuariologado(Usuario usuarioLogado);
	

}