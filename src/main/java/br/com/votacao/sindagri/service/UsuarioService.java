package br.com.votacao.sindagri.service;

import br.com.votacao.sindagri.dao.UsuarioDAO;
import br.com.votacao.sindagri.domain.Permissao;
import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  
  public List<Permissao> findPermissoesByUsuarioAndSistema(Usuario usuario, String siglaSistema) {
    return this.dao.findPermissoesByUsuarioAndSistema(usuario, siglaSistema);
  }
  
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
    return this.dao.salvar(entidade);
  }
}
