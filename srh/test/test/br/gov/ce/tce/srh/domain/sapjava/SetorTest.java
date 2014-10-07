package test.br.gov.ce.tce.srh.domain.sapjava;

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

public class SetorTest extends DatabaseTestCase {

	@Override
	protected IDatabaseConnection getConnection() throws Exception {

		@SuppressWarnings({ "unused", "rawtypes" })
		Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection jdbcConnection = DriverManager.getConnection(
        		"jdbc:oracle:thin:@localhost:1521:XE", "SAPJAVA", "sapjava");
        jdbcConnection.setAutoCommit(true);

		return new DatabaseConnection(jdbcConnection);
	}

	
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\sapjava\\Setor.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();

		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("SETOR");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();
		Column[] pkAtual = metaData.getPrimaryKeys();

		Assert.assertEquals(pkAtual.length, 1);
		Assert.assertTrue(pkAtual[0].getColumnName().equalsIgnoreCase("IDSETOR"));

		con.close();
	}

	@Test
	public void testQtdColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();

		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("SETOR");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 11);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();

		IDataSet baseAtual = con.createDataSet();
		ITable tableAlunoAtual = baseAtual.getTable("SETOR");

		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		String id = colunas[0].getSqlTypeName();
		String nome = colunas[1].getSqlTypeName();
		String tipo = colunas[2].getSqlTypeName();
		String membro = colunas[3].getSqlTypeName();
		String abrev = colunas[4].getSqlTypeName();
		String email = colunas[5].getSqlTypeName();
		String area = colunas[6].getSqlTypeName();
		String ativo = colunas[7].getSqlTypeName();
		String devolucao = colunas[8].getSqlTypeName();
		String participa = colunas[9].getSqlTypeName();
		String superior = colunas[10].getSqlTypeName();

		Assert.assertEquals("NUMBER", id);
		Assert.assertEquals("VARCHAR2", nome);
		Assert.assertEquals("NUMBER", tipo);
		Assert.assertEquals("NUMBER", membro);
		Assert.assertEquals("VARCHAR2", abrev);
		Assert.assertEquals("VARCHAR2", email);
		Assert.assertEquals("NUMBER", area);
		Assert.assertEquals("NUMBER", ativo);
		Assert.assertEquals("NUMBER", devolucao);
		Assert.assertEquals("NUMBER", participa);
		Assert.assertEquals("NUMBER", superior);

		con.close();
	}

}
