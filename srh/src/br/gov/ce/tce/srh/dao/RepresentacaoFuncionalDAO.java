package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface RepresentacaoFuncionalDAO {
	
	public int count(Long pessoal);
	public List<RepresentacaoFuncional> search(Long pessoal, int first, int rows);

	public void salvar(RepresentacaoFuncional entidade);
	public void excluir(RepresentacaoFuncional entidade);

	public RepresentacaoFuncional getById(Long id);
	public List<RepresentacaoFuncional> getByMatricula(String matricula);
	public RepresentacaoFuncional getByCpf(String cpf);
	public RepresentacaoFuncional getByFuncionalTipo(Long funcional, Long tipoNomeacao);

	public List<RepresentacaoFuncional> findByNome(String nome);
	public List<RepresentacaoFuncional> findByNomeSetor(String nome, Long idSetor);
	public List<RepresentacaoFuncional> findByPessoal(Long idPessoa);
	public List<RepresentacaoFuncional> findByFuncional(Long idFuncional);
	public List<RepresentacaoFuncional> findByTipoNomeacaoCargoSetor(Long tipoNomeacao, Long cargo, Long setor);
	public List<RepresentacaoFuncional> findByCargoSetor(Long cargo, Long setor);
	public List<RepresentacaoFuncional> findByUsuarioLogadoSetor(Usuario usuarioLogado, Long idSetor);
	public List<RepresentacaoFuncional> findByUsuarioLogado(Usuario usuarioLogado);

}
