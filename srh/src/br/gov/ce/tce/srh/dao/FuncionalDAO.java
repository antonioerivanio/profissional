package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface FuncionalDAO {

	public int count(Long pessoal, String orderBy);
	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows);

	public Funcional salvar(Funcional entidade) throws SRHRuntimeException;
	public void excluir(Funcional entidade);

	public Funcional getById(Long id);
	
	public Funcional getByPessoaAtivo(Long idPessoa);
	public Funcional getByPessoaAtivoFp(Long idPessoa);
	public Funcional getByMatriculaAtivo(String matricula);

	public Funcional getMatriculaAndNomeByCpf(String cpf);
	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula);
	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf);
	
	
	public Funcional getCpfAndNomeByMatricula(String matricula);
	public List<String> findAllByNome(String nome);
	
	public String getMaxMatricula();

	public List<String> findByNome(String nome);
	public List<Funcional> findByPessoal(Long idPessoal, String orderBy);
	public List<String> findByUsuariologado(Usuario usuarioLogado);
	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy,
			int first, int rows);

}
