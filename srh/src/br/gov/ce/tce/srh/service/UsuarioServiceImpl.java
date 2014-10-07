package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.UsuarioDAO;
import br.gov.ce.tce.srh.domain.sca.Usuario;

/**
 * 
 * @author robson
 *
 */
@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioDAO dao;


	@Override
	public Usuario getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<Usuario> findAll() {
		return dao.findAll();
	}


	public void setDAO(UsuarioDAO dao) { this.dao = dao; }

}
