package test.br.gov.ce.tce.srh.domain;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

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

import br.gov.ce.tce.srh.domain.Cbo;
import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.Folha;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.Situacao;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.domain.Vinculo;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

public class FuncionalTest extends DatabaseTestCase {

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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Funcional.xml"));
	}

	@Test
	public void testChavePrimaria() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_FUNCIONAL");
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

		ITable tableAlunoAtual = baseAtual.getTable("TB_FUNCIONAL");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		Assert.assertEquals(colunas.length, 40);

		con.close();
	}

	@Test
	public void testTipoColuna() throws SQLException, Exception {

		IDatabaseConnection con = getConnection();
		IDataSet baseAtual = con.createDataSet();

		ITable tableAlunoAtual = baseAtual.getTable("TB_FUNCIONAL");
		ITableMetaData metaData = tableAlunoAtual.getTableMetaData();

		Column[] colunas = metaData.getColumns();
		String ID = colunas[0].getSqlTypeName();
		String IDPESSOAL = colunas[1].getSqlTypeName();
		String IDORGAOORIGEM = colunas[2].getSqlTypeName();
		String IDSETOR = colunas[3].getSqlTypeName();
		String IDOCUPACAO = colunas[4].getSqlTypeName();
		String IDCLASSEREFERENCIA = colunas[5].getSqlTypeName();
		String IDTIPOMOVIMENTOENTRADA = colunas[6].getSqlTypeName();
		String IDTIPOMOVIMENTOSAIDA = colunas[7].getSqlTypeName();
		String IDCBO = colunas[8].getSqlTypeName();
		String IDFOLHA = colunas[9].getSqlTypeName();
		String NOME = colunas[10].getSqlTypeName();
		String NOMECOMPLETO = colunas[11].getSqlTypeName();
		String NOMEPESQUISA = colunas[12].getSqlTypeName();
		String MATRICULA = colunas[13].getSqlTypeName();
		String MATRICULAESTADUAL = colunas[14].getSqlTypeName();
		String CALCULOPCC = colunas[15].getSqlTypeName();
		String QTDQUINTOS = colunas[16].getSqlTypeName();
		String LEIINCORPORACAO = colunas[17].getSqlTypeName();
		String PONTO = colunas[18].getSqlTypeName();
		String STATUS = colunas[19].getSqlTypeName();
		String ATIVOFP = colunas[20].getSqlTypeName();
		String IRRF = colunas[21].getSqlTypeName();
		String SUPSECINTEGRAL = colunas[22].getSqlTypeName();
		String PROPORCIONALIDADE = colunas[23].getSqlTypeName();
		String SALARIOORIGEM = colunas[24].getSqlTypeName();
		String ABONOPREVIDENCIARIO = colunas[25].getSqlTypeName();
		String DATAPOSSE = colunas[26].getSqlTypeName();
		String DATAEXERCICIO = colunas[27].getSqlTypeName();
		String DATASAIDA = colunas[28].getSqlTypeName();
		String DOENOMEACAO = colunas[29].getSqlTypeName();
		String DOESAIDA = colunas[30].getSqlTypeName();
		String DESCRICAONOMEACAO = colunas[31].getSqlTypeName();
		String DESCRICAOSAIDA = colunas[32].getSqlTypeName();
		String PREVIDENCIA = colunas[33].getSqlTypeName();
		String REGIME = colunas[34].getSqlTypeName();
		String IDTIPOPUBLICACAONOMEACAO = colunas[35].getSqlTypeName();
		String IDTIPOPUBLICACAOSAIDA = colunas[36].getSqlTypeName();
		String IDSITUACAO = colunas[37].getSqlTypeName();
		String IDTIPOVINCULO = colunas[38].getSqlTypeName();

		Assert.assertEquals("NUMBER", ID);
		Assert.assertEquals("NUMBER", IDPESSOAL);
		Assert.assertEquals("NUMBER", IDORGAOORIGEM);
		Assert.assertEquals("NUMBER", IDSETOR);
		Assert.assertEquals("NUMBER", IDOCUPACAO);
		Assert.assertEquals("NUMBER", IDCLASSEREFERENCIA);

		Assert.assertEquals("NUMBER", IDTIPOMOVIMENTOENTRADA);
		Assert.assertEquals("NUMBER", IDTIPOMOVIMENTOSAIDA);
		Assert.assertEquals("NUMBER", IDCBO);
		Assert.assertEquals("NUMBER", IDFOLHA);
		Assert.assertEquals("VARCHAR2", NOME);
		Assert.assertEquals("VARCHAR2", NOMECOMPLETO);

		Assert.assertEquals("VARCHAR2", NOMEPESQUISA);
		Assert.assertEquals("CHAR", MATRICULA);
		Assert.assertEquals("CHAR", MATRICULAESTADUAL);
		Assert.assertEquals("NUMBER", CALCULOPCC);
		Assert.assertEquals("NUMBER", QTDQUINTOS);

		Assert.assertEquals("CHAR", LEIINCORPORACAO);
		Assert.assertEquals("NUMBER", PONTO);
		Assert.assertEquals("NUMBER", STATUS);
		Assert.assertEquals("NUMBER", ATIVOFP);
		Assert.assertEquals("NUMBER", IRRF);

		Assert.assertEquals("NUMBER", SUPSECINTEGRAL);
		Assert.assertEquals("NUMBER", PROPORCIONALIDADE);
		Assert.assertEquals("NUMBER", SALARIOORIGEM);
		Assert.assertEquals("NUMBER", ABONOPREVIDENCIARIO);
		Assert.assertEquals("DATE", DATAPOSSE);
		Assert.assertEquals("DATE", DATAEXERCICIO);
		Assert.assertEquals("DATE", DATASAIDA);

		Assert.assertEquals("DATE", DOENOMEACAO);
		Assert.assertEquals("DATE", DOESAIDA);
		Assert.assertEquals("VARCHAR2", DESCRICAONOMEACAO);
		Assert.assertEquals("VARCHAR2", DESCRICAOSAIDA);
		Assert.assertEquals("NUMBER", PREVIDENCIA);
		Assert.assertEquals("NUMBER", REGIME);

		Assert.assertEquals("NUMBER", IDTIPOPUBLICACAONOMEACAO);
		Assert.assertEquals("NUMBER", IDTIPOPUBLICACAOSAIDA);
		Assert.assertEquals("NUMBER", IDSITUACAO);
		Assert.assertEquals("NUMBER", IDTIPOVINCULO);
	
		con.close();
	}

	@Test
	public void testGetAnSet() throws SQLException, Exception {

		Funcional entidade = new Funcional();

		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.setIdOrgaoOrigem(1l);
		entidade.setSetor(new Setor());
		entidade.setOcupacao(new Ocupacao());
		entidade.setClasseReferencia(new ClasseReferencia());
		entidade.setTipoMovimentoEntrada(new TipoMovimento());
		entidade.setTipoMovimentoSaida(new TipoMovimento());
		entidade.setCbo(new Cbo());
		entidade.setFolha(new Folha());
		entidade.setNome("Marcos");
		entidade.setNomeCompleto("Marcos TCE");
		entidade.setNomePesquisa("Marcos Pesquisa");
		entidade.setMatricula("1234");
		entidade.setMatriculaEstadual("123456");
		entidade.setCalculoPcc(true);
		entidade.setQtdQuintos(new Long(112));
		entidade.setLeiIncorporacao("Lei 1000");
		entidade.setPonto(true);
		entidade.setStatus(1l);
		entidade.setAtipoFp(true);
		entidade.setIRRF(true);
		entidade.setSupSecIntegral(true);
		entidade.setProporcionalidade(new Long(5));
		entidade.setSalarioOrigem(new BigDecimal(5000));
		entidade.setAbonoPrevidenciario(true);
		entidade.setPosse(new Date());
		entidade.setExercicio(new Date());
		entidade.setSaida(new Date());
		entidade.setDoeNomeacao(new Date());
		entidade.setDoeSaida(new Date());
		entidade.setDescricaoNomeacao("nomeacao");
		entidade.setDescricaoSaida("fui");
		entidade.setPrevidencia(new Long(9876));
		entidade.setRegime(new Long(98));
		entidade.setTipoPublicacaoNomeacao(new TipoPublicacao());
		entidade.setTipoPublicacaoSaida(new TipoPublicacao());
		entidade.setSituacao(new Situacao());
		entidade.setVinculo(new Vinculo());

		Assert.assertEquals(entidade.getId(), new Long(1));
		Assert.assertNotNull(entidade.getPessoal());
		Assert.assertEquals(entidade.getIdOrgaoOrigem(), new Long(1));
		Assert.assertNotNull(entidade.getSetor());
		Assert.assertNotNull(entidade.getOcupacao());
		Assert.assertNotNull(entidade.getClasseReferencia());
		Assert.assertNotNull(entidade.getTipoMovimentoEntrada());
		Assert.assertNotNull(entidade.getTipoMovimentoSaida());
		Assert.assertNotNull(entidade.getCbo());
		Assert.assertNotNull(entidade.getFolha());
		Assert.assertEquals(entidade.getNome(), "MARCOS");
		Assert.assertEquals(entidade.getNomeCompleto(), "MARCOS TCE");
		Assert.assertEquals(entidade.getNomePesquisa(), "marcos pesquisa");
		Assert.assertEquals(entidade.getMatricula(), "1234");
		Assert.assertEquals(entidade.getMatriculaEstadual(), "123456");
		Assert.assertTrue( entidade.isCalculoPcc() );
		Assert.assertEquals(entidade.getQtdQuintos(), new Long(112));
		Assert.assertEquals(entidade.getLeiIncorporacao(), "Lei 1000");
		Assert.assertTrue( entidade.isPonto() );
		Assert.assertEquals( entidade.getStatus(), new Long(1) );
		Assert.assertTrue(entidade.isAtipoFp());
		Assert.assertTrue(entidade.isIRRF());
		Assert.assertTrue( entidade.getSupSecIntegral() );
		Assert.assertEquals(entidade.getProporcionalidade(), new Long(5));
		Assert.assertEquals(entidade.getSalarioOrigem(), new BigDecimal(5000));
		Assert.assertTrue( entidade.getAbonoPrevidenciario() );
		Assert.assertNotNull(entidade.getPosse());
		Assert.assertNotNull(entidade.getExercicio());
		Assert.assertNotNull(entidade.getSaida());
		Assert.assertNotNull(entidade.getDoeNomeacao());
		Assert.assertNotNull(entidade.getDoeSaida());
		Assert.assertEquals(entidade.getDescricaoNomeacao(), "nomeacao");
		Assert.assertEquals(entidade.getDescricaoSaida(), "fui");
		Assert.assertEquals(entidade.getPrevidencia(), new Long(9876));
		Assert.assertEquals(entidade.getRegime(), new Long(98));
		Assert.assertNotNull(entidade.getTipoPublicacaoNomeacao());
		Assert.assertNotNull(entidade.getTipoPublicacaoSaida());
		Assert.assertNotNull(entidade.getSituacao());
		Assert.assertNotNull(entidade.getVinculo());
		
		
		
	}

}
