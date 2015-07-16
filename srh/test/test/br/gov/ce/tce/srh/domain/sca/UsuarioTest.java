package test.br.gov.ce.tce.srh.domain.sca;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Assert;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import br.gov.ce.tce.srh.sca.domain.GrupoUsuario;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public class UsuarioTest extends DatabaseTestCase {

	@Override
	protected IDatabaseConnection getConnection() throws Exception {

		@SuppressWarnings({ "unused", "rawtypes" })
		Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection jdbcConnection = DriverManager.getConnection(
        		"jdbc:oracle:thin:@localhost:1521:XE", "SCA", "sca");
        jdbcConnection.setAutoCommit(true);

		return new DatabaseConnection(jdbcConnection);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\sca\\Usuario.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		
		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("USUARIO");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();
		Column[] pkAtual = metaData.getPrimaryKeys();

		Assert.assertEquals(pkAtual.length, 1);
		Assert.assertTrue(pkAtual[0].getColumnName().equalsIgnoreCase("id"));

		con.close();
	}

	@Test
	public void testQtdColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();

		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("USUARIO");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 13);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();

		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("USUARIO");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		String ID = colunas[0].getSqlTypeName();
		String NOME = colunas[1].getSqlTypeName();
		String LOGIN = colunas[2].getSqlTypeName();
		String SENHA = colunas[3].getSqlTypeName();
		String TIPO = colunas[4].getSqlTypeName();
		String ATIVO = colunas[5].getSqlTypeName();
		String TENTATIVAS_LOGIN = colunas[6].getSqlTypeName();
		String SENHAEXPIRADA = colunas[7].getSqlTypeName();
		String CPF = colunas[8].getSqlTypeName();
		String DATAALTERACAO = colunas[9].getSqlTypeName();
		String DATAINCLUSAO = colunas[10].getSqlTypeName();
		String EMAIL = colunas[11].getSqlTypeName();
		String OBSERVACAO = colunas[12].getSqlTypeName();


		Assert.assertEquals("NUMBER", ID);;
		Assert.assertEquals("VARCHAR2", NOME);
		Assert.assertEquals("VARCHAR2", LOGIN);
		Assert.assertEquals("VARCHAR2", SENHA);
		Assert.assertEquals("NUMBER", TIPO);
		Assert.assertEquals("NUMBER", ATIVO);
		Assert.assertEquals("NUMBER", SENHAEXPIRADA);
		Assert.assertEquals("VARCHAR2", CPF);
		Assert.assertEquals("DATE", DATAALTERACAO);
		Assert.assertEquals("DATE", DATAINCLUSAO);
		Assert.assertEquals("VARCHAR2", EMAIL);
		Assert.assertEquals("VARCHAR2", OBSERVACAO);
		Assert.assertEquals("NUMBER", TENTATIVAS_LOGIN);

		con.close();
	}

	@Test
	public void testGetAnSet() throws SQLException, Exception {

		Usuario entidade = new Usuario();

		entidade.setId(1l);
		entidade.setUsername("marcos");
		entidade.setPassword("1234");
		entidade.setAtivo(true);
		entidade.setSenhaExpirada(false);
		entidade.setGrupoUsuario(new ArrayList<GrupoUsuario>());
		entidade.setAuthorities(new HashSet<GrantedAuthority>());

		Assert.assertEquals(entidade.getId(), new Long(1));
		Assert.assertEquals(entidade.getUsername(), "marcos");
		Assert.assertEquals(entidade.getPassword(), "1234");
		Assert.assertNotNull(entidade.getGrupoUsuario());
		Assert.assertNotNull(entidade.getAuthorities());
		Assert.assertTrue(entidade.isEnabled());
		Assert.assertTrue(entidade.isCredentialsNonExpired());
		Assert.assertTrue(entidade.isAccountNonExpired());
		Assert.assertTrue(entidade.isAccountNonLocked());

		entidade.setSenhaExpirada(true);
		Assert.assertFalse(entidade.isAccountNonExpired());

	}

}
