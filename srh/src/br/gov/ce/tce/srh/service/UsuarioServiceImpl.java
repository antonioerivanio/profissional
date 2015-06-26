package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.UsuarioDAO;
import br.gov.ce.tce.srh.domain.sca.Permissao;
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

	@Override
	public Usuario findByUsername(String username) {
		return dao.findByUsername(username);
	}

	@Override
	public List<Permissao> findPermissoesByUsuarioAndSistema(Usuario usuario, String siglaSistema) {
		return dao.findPermissoesByUsuarioAndSistema(usuario, siglaSistema);
	}

	@Override
	public Usuario findByCpf(String cpf) {
		return dao.findByCpf(cpf);
	}
	
	public void setDAO(UsuarioDAO dao) { this.dao = dao; }
	
	@Override
	public Usuario salvar(Usuario entidade) {
		return dao.salvar(entidade);
	}

}
