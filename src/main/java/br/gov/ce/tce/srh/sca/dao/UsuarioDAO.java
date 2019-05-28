package br.gov.ce.tce.srh.sca.dao;

import java.util.List;

import br.gov.ce.tce.srh.sca.domain.Permissao;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface UsuarioDAO {

	public Usuario getById(Long id);

	public List<Usuario> findAll();

	public Usuario findByUsername(String username);

	public List<Permissao> findPermissoesByUsuarioAndSistema(Usuario usuario,
			String siglaSistema);

	public Usuario findByCpf(String cpf);
	
	public Usuario salvar(Usuario entidade);
}
