package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.DeducaoDAO;
import br.gov.ce.tce.srh.domain.Deducao;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DeducaoServiceImpl;

public class DeducaoServiceTest {

	private DeducaoServiceImpl deducaoService;
	private DeducaoDAO deducaoDAO;

	@Before
	public void beforeTest() {
		deducaoDAO = createMock(DeducaoDAO.class);
		deducaoService = new DeducaoServiceImpl();
		deducaoService.setDAO(deducaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.DeducaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Deducao entidade = new Deducao();

		// validando o funcionario NULO
		try {

			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPessoal( new Pessoal() );
		}


		// validando o ano referencia
		try {

			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com ano referencia nulo!!!");

		} catch (SRHRuntimeException e) {}


		// validando o ano referencia menor que 4 digitos
		try {

			entidade.setAnoReferencia(201l);
			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com ano referencia menor que 4 digitos!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAnoReferencia(2012l);
		}


		// validando o mes referencia
		try {

			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com mes referencia nulo!!!");

		} catch (SRHRuntimeException e) {}


		// validando o mes referencia igual 0
		try {

			entidade.setMesReferencia(0l);
			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com mes referencia igual a 0!!!");

		} catch (SRHRuntimeException e) {
			entidade.setMesReferencia(2l);
		}


		// validando a qtde dias
		try {

			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com qtde dias nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setQtdeDias(30l);
		}


		entidade.setFalta(false);

		// validando a data inicio nula
		try {

			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setInicio( new Date() );
		}

		
		// validando a data fim nula
		try {

			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data fim nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setFim( new Date() );
		}


		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando o data inicio maior que a data fim
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("10/12/2011") );

			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}


		entidade.setFalta(true);

		// validando a ano referencia < 1998
		try {

			entidade.setAnoReferencia(1999l);
			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com ano referencia menor que 1999!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAnoReferencia(1998l);
		}


		// validando o nr. processo
		try {

			entidade.setNrProcesso("123455677888");
			deducaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com numero de processo invalido!!");

		} catch (SRHRuntimeException e) {}
		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.DeducaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Deducao entidade = new Deducao();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setAnoReferencia(2012l);
		entidade.setMesReferencia(5l);
		entidade.setQtdeDias(30l);
		entidade.setFalta(false);
		entidade.setInicio( new Date() );
		entidade.setFim( new Date() );
		entidade.setNrProcesso("");

		expect(deducaoDAO.findByPessoal(1l)).andReturn(new ArrayList<Deducao>());
		expect(deducaoDAO.salvar(entidade)).andReturn(null);
		replay(deducaoDAO);

		deducaoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.DeducaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		deducaoDAO.excluir(null);
		replay(deducaoDAO);
		deducaoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.DeducaoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(deducaoDAO.count(1l)).andReturn(1);
		replay(deducaoDAO);

		Assert.assertEquals( deducaoService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.DeducaoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(deducaoDAO.search(1l, 0, 10)).andReturn(null);
		replay(deducaoDAO);

		Assert.assertNull( deducaoService.search(1l, 0, 10) );
	}



	/**
	 * Test of br.gov.ce.tce.srh.service.DeducaoServiceImpl.findByPessoal
	 */
	@Test
	public void testFindByPessoal() {
		expect(deducaoDAO.findByPessoal(1l)).andReturn(null);
		replay(deducaoDAO);

		Assert.assertNull( deducaoService.findByPessoal(1l) );
	}

}
