package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.sca.Usuario;

public interface UsuarioService {

	public Usuario getById(Long id);

	public List<Usuario> findAll();

}
