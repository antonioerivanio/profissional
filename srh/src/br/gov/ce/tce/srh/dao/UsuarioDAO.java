package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.sca.Usuario;

public interface UsuarioDAO {

	public Usuario getById(Long id);

	public List<Usuario> findAll();

}
