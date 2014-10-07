package test.br.gov.ce.tce.srh.domain;

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

public class RubricaTest extends DatabaseTestCase {

	@Override
	protected IDatabaseConnection getConnection() throws Exception {

		@SuppressWarnings({ "unused", "rawtypes" })
		Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection jdbcConnection = DriverManager.getConnection(
        		"jdbc:oracle:thin:@localhost:1521:XE", "SRH", "srh");
        jdbcConnection.setAutoCommit(true);

		return new DatabaseConnection(jdbcConnection);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Rubrica.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_RUBRICA");
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

		ITable tableAlunoAtual = baseAtual.getTable("TB_RUBRICA");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 11);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_RUBRICA");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		String id = colunas[0].getSqlTypeName();
		String codigo = colunas[1].getSqlTypeName();
		String ordem = colunas[2].getSqlTypeName();
		String tipo = colunas[3].getSqlTypeName();
		String descricao = colunas[4].getSqlTypeName();
		String emprestimo = colunas[5].getSqlTypeName();
		String consignacao = colunas[6].getSqlTypeName();
		String supsec = colunas[7].getSqlTypeName();
		String irrf = colunas[8].getSqlTypeName();
		String verba = colunas[9].getSqlTypeName();
		String teto = colunas[10].getSqlTypeName();

		Assert.assertEquals("NUMBER", id);
		Assert.assertEquals("CHAR", codigo);
		Assert.assertEquals("NUMBER", ordem);
		Assert.assertEquals("CHAR", tipo);
		Assert.assertEquals("VARCHAR2", descricao);
		Assert.assertEquals("NUMBER", emprestimo);
		Assert.assertEquals("NUMBER", consignacao);
		Assert.assertEquals("NUMBER", supsec);
		Assert.assertEquals("NUMBER", irrf);
		Assert.assertEquals("NUMBER", verba);
		Assert.assertEquals("NUMBER", teto);

		con.close();
	}

}
