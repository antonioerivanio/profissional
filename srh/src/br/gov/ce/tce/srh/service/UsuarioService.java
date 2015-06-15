package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.sca.Permissao;
import br.gov.ce.tce.srh.domain.sca.Usuario;

public interface UsuarioService {

	public Usuario getById(Long id);

	public List<Usuario> findAll();

	public Usuario findByUsername(String username);

	public List<Permissao> findPermissoesByUsuarioAndSistema(Usuario usuario,
			String siglaSistema);
	
	public Usuario findByCpf(String cpf);
}
