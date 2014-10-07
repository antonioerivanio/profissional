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

public class PessoalTest extends DatabaseTestCase {

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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Pessoal.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_PESSOAL");
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

		ITable tableAlunoAtual = baseAtual.getTable("TB_PESSOAL");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 47);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_PESSOAL");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		String id = colunas[0].getSqlTypeName();
		String escolaridade = colunas[1].getSqlTypeName();
		String estadoCivil = colunas[2].getSqlTypeName();
		String raca = colunas[3].getSqlTypeName();
		String codCredorSic = colunas[4].getSqlTypeName();
		String nome = colunas[5].getSqlTypeName();
		String nomeCompleto = colunas[6].getSqlTypeName();
		String nomePesquisa = colunas[7].getSqlTypeName();
		String abreviatura = colunas[8].getSqlTypeName();
		String nomePai = colunas[9].getSqlTypeName();
		String nomeMae = colunas[10].getSqlTypeName();
		String email = colunas[11].getSqlTypeName();
		String sexo = colunas[12].getSqlTypeName();
		String nascimento = colunas[13].getSqlTypeName();
		String obito = colunas[14].getSqlTypeName();
		String tipoSangue = colunas[15].getSqlTypeName();
		String fatorRH = colunas[16].getSqlTypeName();
		String endereco = colunas[17].getSqlTypeName();
		String numero = colunas[18].getSqlTypeName();
		String complemento = colunas[19].getSqlTypeName();
		String bairro = colunas[20].getSqlTypeName();
		String cep = colunas[21].getSqlTypeName();
		String municipio = colunas[22].getSqlTypeName();
		String ufEndereco = colunas[23].getSqlTypeName();
		String naturalidade = colunas[24].getSqlTypeName();
		String uf = colunas[25].getSqlTypeName();
		String pasep = colunas[26].getSqlTypeName();
		String cpf = colunas[27].getSqlTypeName();
		String rg = colunas[28].getSqlTypeName();
		String emissorRg = colunas[29].getSqlTypeName();
		String ufEmissorRg = colunas[30].getSqlTypeName();
		String emissaoRg = colunas[31].getSqlTypeName();
		String tituloEleitoral = colunas[32].getSqlTypeName();
		String zonaEleitoral = colunas[33].getSqlTypeName();
		String secaoEleitoral = colunas[34].getSqlTypeName();
		String documentoMilitar = colunas[35].getSqlTypeName();
		String tipoContaBbd = colunas[36].getSqlTypeName();
		String agenciaBbd = colunas[37].getSqlTypeName();
		String contaBbd = colunas[38].getSqlTypeName();
		String qtdDepsf = colunas[39].getSqlTypeName();
		String qtdDepir = colunas[40].getSqlTypeName();
		String qtdDepprev = colunas[41].getSqlTypeName();
		String foto = colunas[42].getSqlTypeName();
		String categoria = colunas[43].getSqlTypeName();
		String atualizacao = colunas[44].getSqlTypeName();
		String telefone = colunas[45].getSqlTypeName();
		String celular = colunas[46].getSqlTypeName();


		Assert.assertEquals("NUMBER", id);
		Assert.assertEquals("NUMBER", escolaridade);
		Assert.assertEquals("NUMBER", estadoCivil);
		Assert.assertEquals("NUMBER", raca);
		Assert.assertEquals("NUMBER", codCredorSic);
		Assert.assertEquals("VARCHAR2", nome);

		Assert.assertEquals("VARCHAR2", nomeCompleto);
		Assert.assertEquals("VARCHAR2", nomePesquisa);
		Assert.assertEquals("VARCHAR2", abreviatura);
		Assert.assertEquals("VARCHAR2", nomePai);
		Assert.assertEquals("VARCHAR2", nomeMae);
		Assert.assertEquals("VARCHAR2", email);
		Assert.assertEquals("CHAR", sexo);
		Assert.assertEquals("DATE", nascimento);
		Assert.assertEquals("DATE", obito);
		Assert.assertEquals("VARCHAR2", tipoSangue);
		Assert.assertEquals("CHAR", fatorRH);

		Assert.assertEquals("VARCHAR2", endereco);
		Assert.assertEquals("VARCHAR2", numero);
		Assert.assertEquals("VARCHAR2", complemento);
		Assert.assertEquals("VARCHAR2", bairro);
		Assert.assertEquals("CHAR", cep);
		Assert.assertEquals("VARCHAR2", municipio);
		Assert.assertEquals("CHAR", ufEndereco);
		Assert.assertEquals("VARCHAR2", naturalidade);
		Assert.assertEquals("CHAR", uf);
		Assert.assertEquals("CHAR", pasep);
		Assert.assertEquals("CHAR", cpf);
		Assert.assertEquals("VARCHAR2", rg);

		Assert.assertEquals("VARCHAR2", emissorRg);
		Assert.assertEquals("CHAR", ufEmissorRg);
		Assert.assertEquals("DATE", emissaoRg);
		Assert.assertEquals("VARCHAR2", tituloEleitoral);
		Assert.assertEquals("VARCHAR2", zonaEleitoral);
		Assert.assertEquals("VARCHAR2", secaoEleitoral);
		Assert.assertEquals("VARCHAR2", documentoMilitar);
		Assert.assertEquals("NUMBER", tipoContaBbd);
		Assert.assertEquals("CHAR", agenciaBbd);
		Assert.assertEquals("CHAR", contaBbd);

		Assert.assertEquals("NUMBER", qtdDepsf);
		Assert.assertEquals("NUMBER", qtdDepir);
		Assert.assertEquals("NUMBER", qtdDepprev);
		Assert.assertEquals("VARCHAR2", foto);
		Assert.assertEquals("NUMBER", categoria);
		Assert.assertEquals("DATE", atualizacao);
		Assert.assertEquals("CHAR", telefone);
		Assert.assertEquals("CHAR", celular);

		con.close();
	}

}
