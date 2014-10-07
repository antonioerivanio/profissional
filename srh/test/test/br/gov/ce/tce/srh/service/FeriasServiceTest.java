package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.FeriasDAO;
import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.TipoFerias;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FeriasServiceImpl;

public class FeriasServiceTest {

	private FeriasDAO feriasDAO;
	private FeriasServiceImpl feriasService;

	@Before
	public void beforeTest() {
		feriasDAO = createMock(FeriasDAO.class);
		feriasService = new FeriasServiceImpl();
		feriasService.setDAO(feriasDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Ferias entidade = new Ferias();
		entidade.setQtdeDias(30l);

		// validando o funcionario NULO
		try {

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setFuncional( new Funcional() );
		}


		// validando tipo de ferias NULA
		try {

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com tipo de ferias nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setTipoFerias( new TipoFerias() );
			entidade.getTipoFerias().setId(1l);
		}


		// validando ano refencia NULA
		try {

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com ano referencia nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAnoReferencia(2012l);
		}


		// validando o periodo NULO
		try {

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com periodo nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPeriodo(1l);
		}


		// validando o data inicio NULA
		try {

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setInicio( new Date() );
		}

		// validando o data fim NULA
		try {

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data fim nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setFim( new Date() );
		}


		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando o data inicio maior que a data fim
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("10/12/2011") );

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}


		// validando o data de publicacao maior que a data de inicio
		try {

			entidade.setDataPublicacao( dateFormate.parse("15/12/2011") );
			entidade.setFim( dateFormate.parse("20/12/2011") );

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarVerificaQtdeDeDias() throws SRHRuntimeException {

		Ferias entidade = new Ferias();
		entidade.setFuncional( new Funcional() );
		entidade.setTipoFerias( new TipoFerias() );
		entidade.getTipoFerias().setId(4l);
		entidade.setAnoReferencia(2012l);
		entidade.setPeriodo(1l);
		entidade.setInicio(new Date());
		entidade.setFim(new Date());

		// validando o ano de referencia < 1935
		try {

			entidade.setAnoReferencia(1934l);
			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com ano referencia < 1935!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAnoReferencia(1935l);
		}


		// validando ano refencia maior que 2 anos do ano vigente
		try {

			entidade.setAnoReferencia(2100l);
			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com ano referencia 2 anos maior que o ano vigente!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAnoReferencia(2012l);
		}


		// validando o periodo 1 ou 2
		try {

			entidade.setPeriodo(3l);
			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com periodo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setInicio( new Date() );
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarCalculaQtdeDias() throws SRHRuntimeException {

		Ferias entidade = new Ferias();
		entidade.setFuncional( new Funcional() );
		entidade.setTipoFerias( new TipoFerias() );
		entidade.getTipoFerias().setId(4l);
		entidade.setAnoReferencia(2012l);
		entidade.setPeriodo(1l);
		entidade.setInicio(new Date());
		entidade.setFim(new Date());

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando a quantidade de dias maior que 30
		try {

			entidade.setInicio( dateFormate.parse("01/10/2011") );
			entidade.setFim( dateFormate.parse("10/11/2011") );

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade com a quantidade de dias maior que 30!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataInicioIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		Ferias entidade = new Ferias();
		entidade.setFuncional( new Funcional() );
		entidade.getFuncional().setId(1l);
		entidade.setTipoFerias( new TipoFerias() );
		entidade.getTipoFerias().setId(4l);
		entidade.setAnoReferencia(2012l);
		entidade.setPeriodo(1l);
		entidade.setInicio(new Date());
		entidade.setFim(new Date());

		Ferias existente = new Ferias();
		existente.setId(2l);

		// validando data inicio no intervalo
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("20/12/2011") );
			
			existente.setInicio( dateFormate.parse("15/12/2011") );
			existente.setFim( dateFormate.parse("25/12/2011") );

			List<Ferias> lista = new ArrayList<Ferias>();
			lista.add(existente);

			expect(feriasDAO.findByPessoal(1l)).andReturn(lista);
			expect(feriasDAO.salvar(entidade)).andReturn(null);
			replay(feriasDAO);

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data inicio no intervalo!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataFimIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		Ferias entidade = new Ferias();
		entidade.setFuncional( new Funcional() );
		entidade.getFuncional().setId(1l);
		entidade.setTipoFerias( new TipoFerias() );
		entidade.getTipoFerias().setId(4l);
		entidade.setAnoReferencia(2012l);
		entidade.setPeriodo(1l);
		entidade.setInicio(new Date());
		entidade.setFim(new Date());

		Ferias existente = new Ferias();
		existente.setId(2l);


		// validando data fim no intervalo
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("20/12/2011") );
			
			existente.setInicio( dateFormate.parse("10/12/2011") );
			existente.setFim( dateFormate.parse("15/12/2011") );

			List<Ferias> lista = new ArrayList<Ferias>();
			lista.add(existente);

			expect(feriasDAO.findByPessoal(1l)).andReturn(lista);
			expect(feriasDAO.salvar(entidade)).andReturn(null);
			replay(feriasDAO);

			feriasService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data fim no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Ferias entidade = new Ferias();
		entidade.setFuncional( new Funcional() );
		entidade.getFuncional().setId(1l);
		entidade.setTipoFerias( new TipoFerias() );
		entidade.getTipoFerias().setId(4l);
		entidade.setAnoReferencia(2012l);
		entidade.setPeriodo(1l);
		entidade.setInicio(new Date());
		entidade.setFim(new Date());
		entidade.setQtdeDias(30l);

		expect(feriasDAO.findByPessoal(1l)).andReturn(new ArrayList<Ferias>());
		expect(feriasDAO.salvar(entidade)).andReturn(null);
		replay(feriasDAO);

		feriasService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		Ferias entidade = new Ferias();
		entidade.setId(1l);

		feriasDAO.excluir(entidade);
		replay(feriasDAO);

		feriasService.excluir(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(feriasDAO.count(1l)).andReturn(1);
		replay(feriasDAO);

		Assert.assertEquals( feriasService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(feriasDAO.search(1l, 0, 10)).andReturn(null);
		replay(feriasDAO);

		Assert.assertNull( feriasService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.findByPessoal
	 */
	@Test
	public void testFindByPessoal() {
		expect(feriasDAO.findByPessoal(1l)).andReturn(null);
		replay(feriasDAO);
		List<Ferias> lista = feriasService.findByPessoal(1l);
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FeriasServiceImpl.findByPessoalTipo
	 */
	@Test
	public void testFindByPessoalTipo() {
		expect(feriasDAO.findByPessoalTipo(1l, 1l)).andReturn(null);
		replay(feriasDAO);
		List<Ferias> lista = feriasService.findByPessoalTipo(1l, 1l);
		Assert.assertNull(lista);
	}

}
