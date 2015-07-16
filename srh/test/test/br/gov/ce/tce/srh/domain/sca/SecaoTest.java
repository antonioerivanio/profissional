package test.br.gov.ce.tce.srh.domain.sca;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

import br.gov.ce.tce.srh.sca.domain.Secao;

public class SecaoTest extends DatabaseTestCase {

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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\sca\\Secao.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();

		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("SECAO");

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
		ITable tableAlunoAtual = baseAtual.getTable("SECAO");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 8);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();

		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("SECAO");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		String ID = colunas[0].getSqlTypeName();
		String SISTEMA = colunas[1].getSqlTypeName();
		String NOME = colunas[2].getSqlTypeName();
		String LINK = colunas[3].getSqlTypeName();
		String SECAOPAI = colunas[4].getSqlTypeName();
		String ORDEM = colunas[5].getSqlTypeName();
		String MENU = colunas[6].getSqlTypeName();
		String NIVEL = colunas[7].getSqlTypeName();

		Assert.assertEquals("NUMBER", ID);
		Assert.assertEquals("NUMBER", SISTEMA);
		Assert.assertEquals("VARCHAR2", NOME);
		Assert.assertEquals("VARCHAR2", LINK);
		Assert.assertEquals("NUMBER", SECAOPAI);
		Assert.assertEquals("NUMBER", ORDEM);
		Assert.assertEquals("NUMBER", MENU);
		Assert.assertEquals("NUMBER", NIVEL);

		con.close();
	}

	@Test
	public void testGetAnSet() throws SQLException, Exception {

		Secao entidade = new Secao();

		entidade.setId(1l);
		entidade.setNome("COMPETENCIA_FUNCIONAL");

		Assert.assertEquals(entidade.getId(), new Long(1));
		Assert.assertEquals(entidade.getNome(), "COMPETENCIA_FUNCIONAL");

	}

}
