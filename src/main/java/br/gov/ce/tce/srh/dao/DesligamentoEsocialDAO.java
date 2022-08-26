package br.gov.ce.tce.srh.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.Desligamento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.service.AmbienteService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class DesligamentoEsocialDAO {

  static Logger logger = Logger.getLogger(DesligamentoEsocialDAO.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private AmbienteService ambienteService;

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  private Long getMaxId() {
    Query query = entityManager.createQuery("Select max(e.id) from Desligamento e ");
    return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
  }

  public Desligamento salvar(Desligamento entidade) {
    if (entidade.getId() == null || entidade.getId().equals(0l)) {
      entidade.setId(getMaxId());
    }

    return entityManager.merge(entidade);
  }


  public void excluir(Desligamento entidade) {
    entidade = entityManager.merge(entidade);
    entityManager.remove(entidade);
  }

  public int count(String nome, String cpf) {

    StringBuffer sql = new StringBuffer();

    sql.append(" Select count(b) FROM Desligamento b inner join b.funcional f WHERE 1=1 ");

    if (nome != null && !nome.isEmpty()) {
      sql.append("  and upper( f.nome ) like :nome ");
    }

    if (cpf != null && !cpf.isEmpty()) {
      sql.append("  AND f.cpf = :cpf ");
    }

    Query query = entityManager.createQuery(sql.toString());

    if (nome != null && !nome.isEmpty()) {
      query.setParameter("nome", "%" + nome.toUpperCase() + "%");
    }

    if (cpf != null && !cpf.isEmpty()) {
      query.setParameter("cpf", SRHUtils.removerMascara(cpf));
    }
    return ((Long) query.getSingleResult()).intValue();
  }

  @SuppressWarnings("unchecked")
  public List<Desligamento> search(String nome, String cpf, Integer first, Integer rows) {

    StringBuffer sql = new StringBuffer();

    sql.append("  SELECT b FROM Desligamento b inner join fetch b.funcional f WHERE 1=1 ");

    if (nome != null && !nome.isEmpty()) {
      sql.append("  and upper( f.nome ) like :nome ");
    }

    if (cpf != null && !cpf.isEmpty())
      sql.append("  AND f.cpf = :cpf ");

    sql.append("  ORDER BY f.nome ");

    Query query = entityManager.createQuery(sql.toString());

    if (nome != null && !nome.isEmpty()) {
      query.setParameter("nome", "%" + nome.toUpperCase() + "%");
    }

    if (cpf != null && !cpf.isEmpty()) {
      query.setParameter("cpf", SRHUtils.removerMascara(cpf));
    }
    if (first != null && first >= 0)
      query.setFirstResult(first);
    if (rows != null && rows > 0)
      query.setMaxResults(rows);

    return query.getResultList();
  }

  public Desligamento getEventoS2299yServidor(Funcional servidorFuncional) {
    try {
      Query query = entityManager.createNativeQuery(getSQLEventoS2299(), Desligamento.class);
      query.setParameter("idFuncional", servidorFuncional.getId());
      Object local = (Object) query.getSingleResult();
      return (Desligamento) local;
    } catch (NoResultException e) {
      return null;
    }

  }

  public String getSQLEventoS2299() {
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT ");
    sql.append("  0  AS id, ");
    sql.append("  f.id  idfuncional, ");
    sql.append("  f.id || '-' || to_char(f.datasaida, 'ddMMyyyy') AS referencia, ");
    sql.append("  p.cpf  AS cpf_trab, ");
    sql.append("  '0'||f.matricula  AS matricula, ");
    sql.append("  null  AS mtv_deslig, ");
    sql.append("  f.datasaida  AS dt_deslig, ");
    sql.append("  null  AS nr_proctrabalho, ");
    sql.append("  null  AS observacao, ");
    sql.append("  1     AS tp_insc_empregador, ");
    sql.append("  null  AS nr_insc_empregador, ");
    sql.append("  1  AS cod_lotacao, ");
    sql.append("  1     AS tp_insc_lotacao, ");
    sql.append("  null  AS nr_insc_lotacao, ");
    sql.append("  1     AS grau_exp ");
    sql.append("FROM  srh.tb_funcional f ");
    sql.append(" INNER JOIN srh.tb_pessoal  p ON f.idpessoal = p.id ");
    sql.append("WHERE f.id = :idFuncional ");

    return sql.toString();
  }


  public Desligamento getDesligamentoById(Long id) {
    try {
      Query query = entityManager.createQuery("SELECT d FROM Desligamento d JOIN fetch d.funcional where d.id = :id", Desligamento.class);
      query.setParameter("id", id);
      return (Desligamento) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Funcional getFuncionalById(Long idFuncional) {
    try {
      Query query = entityManager.createQuery("SELECT f FROM Funcional f JOIN f.pessoal p where f.id = :id");
      query.setParameter("id", idFuncional);
      return (Funcional) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}

