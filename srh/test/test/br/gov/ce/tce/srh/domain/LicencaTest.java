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

public class LicencaTest extends DatabaseTestCase {

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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Licenca.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_LICENCA");
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

		ITable tableAlunoAtual = baseAtual.getTable("TB_LICENCA");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 12);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_LICENCA");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();
		Column[] colunas = metaData.getColumns();

		String ID = colunas[0].getSqlTypeName();
		String IDTIPOLICENCA = colunas[1].getSqlTypeName();
		String INICIO = colunas[2].getSqlTypeName();
		String FIM = colunas[3].getSqlTypeName();
		String EXCLUITEMPOSERV = colunas[4].getSqlTypeName();
		String EXCLUIFINANCEIRO = colunas[5].getSqlTypeName();
		String IDLICENCAESP = colunas[6].getSqlTypeName();
		String IDPESSOAL = colunas[7].getSqlTypeName();
		String NRPROCESSO = colunas[8].getSqlTypeName();
		String OBS = colunas[9].getSqlTypeName();
		String DOE = colunas[10].getSqlTypeName();
		String IDTIPOPUBLICACAO = colunas[11].getSqlTypeName();

		Assert.assertEquals("NUMBER", ID);
		Assert.assertEquals("NUMBER", IDTIPOLICENCA);
		Assert.assertEquals("DATE", INICIO);
		Assert.assertEquals("DATE", FIM);
		Assert.assertEquals("NUMBER", EXCLUITEMPOSERV);
		Assert.assertEquals("NUMBER", EXCLUIFINANCEIRO);
		Assert.assertEquals("NUMBER", IDLICENCAESP);
		Assert.assertEquals("NUMBER", IDPESSOAL);
		Assert.assertEquals("CHAR", NRPROCESSO);
		Assert.assertEquals("VARCHAR2", OBS);
		Assert.assertEquals("DATE", DOE);
		Assert.assertEquals("NUMBER", IDTIPOPUBLICACAO);

		con.close();
	}

}
