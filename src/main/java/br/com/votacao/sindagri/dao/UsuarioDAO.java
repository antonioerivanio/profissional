package br.com.votacao.sindagri.dao;

import br.com.votacao.sindagri.domain.Permissao;
import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsuarioDAO {
  static Logger logger = Logger.getLogger(UsuarioDAO.class);
  
  @PersistenceContext
  private EntityManager entityManager;
  
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  
  public Usuario getById(Long id) {
    return (Usuario)this.entityManager.find(Usuario.class, id);
  }
  
  public List<Usuario> findAll() {
    return this.entityManager.createQuery(
        "SELECT DISTINCT e FROM Usuario e join e.grupoUsuario gu join gu.grupo g join g.sistema s WHERE s.sigla = 'SRH' ORDER BY e.nome ASC")
      .getResultList();
  }
  
  public Usuario findByUsername(String username) {
    try {
      String sql = "SELECT usu FROM Usuario usu WHERE UPPER(usu.username) = UPPER(:username)";
      return (Usuario)this.entityManager.createQuery(sql, Usuario.class).setParameter("username", username).getSingleResult();
    } catch (NoResultException e) {
      return null;
    } 
  }
  
  public Usuario findByUsuariomatricula(String matricula) {
    try {
      return (Usuario)this.entityManager.createNamedQuery("Usuario.findByUsermatricula", Usuario.class)
        .setParameter("matricula", matricula).getSingleResult();
    } catch (NoResultException e) {
      return null;
    } 
  }
  
  public Usuario findByUsuariocpf(String cpf) {
    try {
      return (Usuario)this.entityManager.createNamedQuery("Usuario.findByUsercpf", Usuario.class)
        .setParameter("cpf", cpf).getSingleResult();
    } catch (NoResultException e) {
      return null;
    } 
  }
  
  public List<Permissao> findPermissoesByUsuarioAndSistema(Usuario usuario, String siglaSistema) {
    String sql = "SELECT p FROM GrupoUsuario AS gu INNER JOIN gu.grupo AS g INNER JOIN g.permissoes AS p";
    sql = String.valueOf(sql) + " WHERE g.sistema.sigla = :sigla AND p.sistema.sigla = :sigla AND gu.usuario = :usuario";
    TypedQuery<Permissao> query = this.entityManager.createQuery(sql, Permissao.class);
    query.setParameter("sigla", siglaSistema);
    query.setParameter("usuario", usuario.getId());
    List<Permissao> permissaoList = query.getResultList();
    return permissaoList;
  }
  
  public Usuario findByCpf(String cpf) {
    try {
      String sql = "SELECT usu FROM Usuario usu WHERE UPPER(usu.cpf) = UPPER(:cpf)";
      return (Usuario)this.entityManager.createQuery(sql, Usuario.class).setParameter("cpf", cpf).getSingleResult();
    } catch (NoResultException e) {
      return null;
    } 
  }
  
  public Pessoal findPessoalByCpf(String cpf) {
    try {
      return (Pessoal)this.entityManager.createNamedQuery("Pessoal.findByCpf", Pessoal.class).setParameter("cpf", cpf)
        .getSingleResult();
    } catch (NoResultException e) {
      return null;
    } 
  }
  
  public Pessoal findPessoalByMatricula(String matricula) throws Exception {
    try {
      return (Pessoal)this.entityManager.createNamedQuery("Pessoal.findByMatricula", Pessoal.class)
        .setParameter("matricula", matricula).getSingleResult();
    } catch (NoResultException e) {
      throw new Exception("Nenhum resultado encontrado!");
    } 
  }
  
  @Transactional
  public Usuario salvar(Usuario entidade) throws Exception {
    Usuario user = null;
    if (entidade != null) {
      if (!entidade.isMembroComissao()) {
        user = findByUsuariomatricula(entidade.getMatricula());
      } else {
        user = findByUsuariocpf(entidade.getCpf());
      } 
      if (user != null)
        throw new Exception("Usujfoi cadastrado"); 
    } 
    this.entityManager.persist(entidade);
    entidade = (Usuario)this.entityManager.find(Usuario.class, entidade.getId());
    return entidade;
  }
}
