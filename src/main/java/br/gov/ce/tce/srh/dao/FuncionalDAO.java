package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface FuncionalDAO {

	public Funcional getById(Long id);
	
	public String getMaxMatricula();	
	
	public Funcional salvar(Funcional entidade) throws SRHRuntimeException;
	public void excluir(Funcional entidade);
	
	public int count(Long pessoal, String orderBy);
	
	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows);
	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy, int first, int rows);
	
	
	public Funcional getByPessoaAtivo(Long idPessoa);
	public Funcional getByPessoaAtivoFp(Long idPessoa);
	
	public Funcional getByMatriculaAtivo(String matricula);

	public Funcional getMatriculaAndNomeByCpf(String cpf);	
	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf);
	public List<Funcional> getMatriculaAndNomeByCpfList(String cpf);	
	
	public Funcional getCpfAndNomeByMatricula(String matricula);
	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula);
	
	public List<Funcional> findByNome(String nome);
	public List<Funcional> findAllByNome(String nome);

	public List<Funcional> findByUsuariologado(Usuario usuarioLogado);
	public List<Funcional> findByPessoal(Long idPessoal, String orderBy);
	

}
