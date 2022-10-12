package br.com.votacao.sindagri.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.votacao.sindagri.dao.UsuarioDAO;
import br.com.votacao.sindagri.domain.Grupo;
import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;

@Service("usuarioService")
public class UsuarioService {
	@Autowired
	private UsuarioDAO dao;

	public Usuario getById(Long id) {
		return this.dao.getById(id);
	}

	public List<Usuario> findAll() {
		return this.dao.findAll();
	}

	public Usuario findByUsername(String username) {
		return this.dao.findByUsername(username);
	}
	
	public Usuario findByUserlogin(String login) {
		return this.dao.findByUserlogin(login);
	}

	/*
	 * public List<Permissao> findPermissoesByUsuarioAndSistema(Usuario usuario,
	 * String siglaSistema) { return
	 * this.dao.findPermissoesByUsuarioAndSistema(usuario, siglaSistema); }
	 */

	public Usuario findByCpf(String cpf) {
		return this.dao.findByCpf(cpf);
	}

	public Pessoal findPessoalByCpf(String cpf) {
		return this.dao.findPessoalByCpf(cpf);
	}

	public Pessoal findPessoalByMatricula(String matricula) throws Exception {
		return this.dao.findPessoalByMatricula(matricula);
	}

	public void setDAO(UsuarioDAO dao) {
		this.dao = dao;
	}

	public Usuario salvar(Usuario entidade) throws Exception {
		Grupo grupo = dao.findByGruponome("ROLE_USUARIO");
		entidade.setLogin(entidade.getMatricula());

		if (entidade.isMembroComissao()) {
			grupo = dao.findByGruponome("ROLE_ADMINISTRADOR");
			entidade.setLogin(entidade.getCpf());
		}

		Usuario user = null;
		if (entidade != null) {
			if (!entidade.isMembroComissao()) {
				user = dao.findByUsuariomatricula(entidade.getMatricula());
				if (user != null)
					throw new Exception("Usuário já foi cadastrado com essa matricula");
			} else {
				user = dao.findByUsuariocpf(entidade.getCpf());
				if (user != null)
					throw new Exception("Usuário já foi cadastrado com esse cpf");
			}
		}
		entidade.setGrupoUsuario(new HashSet<Grupo>());
		entidade.getGrupoUsuario().add(grupo);
		dao.salvar(entidade);
		
		return null;
	}
}
