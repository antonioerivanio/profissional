package br.com.votacao.sindagri.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.votacao.sindagri.dao.VotoDAO;
import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.domain.Voto;

@Service("VotoService")
public class VotoService {
	@Autowired
	private VotoDAO dao;

	@Autowired
	private UsuarioService userservice;

	public Pessoal findPessoalByCpf(String cpf) {
		return this.dao.findPessoalByCpf(cpf);
	}

	public Pessoal findPessoalByMatricula(String matricula) throws Exception {
		return this.dao.findPessoalByMatricula(matricula);
	}

	public void setDAO(VotoDAO dao) {
		this.dao = dao;
	}

	@Transactional
	public Voto salvar(Voto entidade) throws Exception {
		Voto votou = dao.findUsuarioByVotouser(entidade.getUsuario());
		if (votou != null) {
			throw new IllegalAccessException("Usuário já vou");
		}
		// entidade.setUsuario(user);
		dao.salvar(entidade);

		return null;
	}
}
