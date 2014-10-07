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

public class FeriasTest extends DatabaseTestCase {

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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Ferias.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_FERIAS");
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

		ITable tableAlunoAtual = baseAtual.getTable("TB_FERIAS");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 12);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_FERIAS");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();
		Column[] colunas = metaData.getColumns();

		String ID = colunas[0].getSqlTypeName();
		String IDFUNCIONAL = colunas[1].getSqlTypeName();
		String TIPOFERIAS = colunas[2].getSqlTypeName();
		String ANOREFERENCIA = colunas[3].getSqlTypeName();
		String INICIO = colunas[4].getSqlTypeName();
		String FIM = colunas[5].getSqlTypeName();
		String DATADOATO = colunas[6].getSqlTypeName();
		String DATAPUBLICACAO = colunas[7].getSqlTypeName();
		String OBS = colunas[8].getSqlTypeName();
		String QTDEDIAS = colunas[9].getSqlTypeName();
		String PERIODO = colunas[10].getSqlTypeName();

		Assert.assertEquals("NUMBER", ID);
		Assert.assertEquals("NUMBER", IDFUNCIONAL);
		Assert.assertEquals("NUMBER", TIPOFERIAS);
		Assert.assertEquals("NUMBER", ANOREFERENCIA);
		Assert.assertEquals("DATE", INICIO);
		Assert.assertEquals("DATE", FIM);
		Assert.assertEquals("DATE", DATADOATO);
		Assert.assertEquals("DATE", DATAPUBLICACAO);
		Assert.assertEquals("VARCHAR2", OBS);
		Assert.assertEquals("NUMBER", QTDEDIAS);
		Assert.assertEquals("NUMBER", PERIODO);

		con.close();
	}

}
