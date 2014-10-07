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

public class LicencaEspecialTest extends DatabaseTestCase {

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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\LicencaEspecial.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_DIREITOLICENCAESP");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();
		Column[] pkAtual = metaData.getPrimaryKeys();

		Assert.assertEquals(pkAtual.length, 1);
		Assert.assertTrue(pkAtual[0].getColumnName().equalsIgnoreCase("ID"));

		con.close();
	}

	@Test
	public void testQtdColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_DIREITOLICENCAESP");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 8);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_DIREITOLICENCAESP");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();
		Column[] colunas = metaData.getColumns();

		String ID = colunas[0].getSqlTypeName();
		String IDPESSOAL = colunas[1].getSqlTypeName();
		String QTDEDIAS = colunas[2].getSqlTypeName();
		String ANOINICIAL = colunas[3].getSqlTypeName();
		String ANOFINAL = colunas[4].getSqlTypeName();
		String DESCRICAO = colunas[5].getSqlTypeName();
		String SALDODIAS = colunas[6].getSqlTypeName();
		String CONTAREMDOBRO = colunas[7].getSqlTypeName();

		Assert.assertEquals("NUMBER", ID);
		Assert.assertEquals("NUMBER", IDPESSOAL);
		Assert.assertEquals("NUMBER", QTDEDIAS);
		Assert.assertEquals("NUMBER", ANOINICIAL);
		Assert.assertEquals("NUMBER", ANOFINAL);
		Assert.assertEquals("VARCHAR2", DESCRICAO);
		Assert.assertEquals("NUMBER", SALDODIAS);
		Assert.assertEquals("NUMBER", CONTAREMDOBRO);

		con.close();
	}

}
