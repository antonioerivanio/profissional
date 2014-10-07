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

import br.gov.ce.tce.srh.dao.AcrescimoDAO;
import br.gov.ce.tce.srh.domain.Acrescimo;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AcrescimoServiceImpl;

public class AcrescimoServiceTest {

	private AcrescimoServiceImpl acrescimoService;
	private AcrescimoDAO acrescimoDAO;

	@Before
	public void beforeTest() {
		acrescimoDAO = createMock(AcrescimoDAO.class);
		acrescimoService = new AcrescimoServiceImpl();
		acrescimoService.setDAO(acrescimoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AcrescimoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Acrescimo entidade = new Acrescimo();

		// validando o funcionario NULO
		try {

			acrescimoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPessoal( new Pessoal() );
		}


		// validando a data inicio nula
		try {

			acrescimoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setInicio( new Date() );
		}

		
		// validando a data fim nula
		try {

			acrescimoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data fim nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setFim( new Date() );
		}


		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando o data inicio maior que a data fim
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("10/12/2011") );

			acrescimoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}


		// validando a qtde dias
		try {

			acrescimoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com qtde dias nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setQtdeDias(30l);
		}


		// validando a descricao
		try {

			acrescimoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao nula!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AcrescimoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Acrescimo entidade = new Acrescimo();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setDescricao("teste");
		entidade.setInicio( new Date() );
		entidade.setFim( new Date() );
		entidade.setQtdeDias(30l);

		expect(acrescimoDAO.findByPessoal(1l)).andReturn(new ArrayList<Acrescimo>());
		expect(acrescimoDAO.salvar(entidade)).andReturn(null);
		replay(acrescimoDAO);

		acrescimoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AcrescimoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		acrescimoDAO.excluir(null);
		replay(acrescimoDAO);
		acrescimoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AcrescimoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(acrescimoDAO.count(1l)).andReturn(1);
		replay(acrescimoDAO);

		Assert.assertEquals( acrescimoService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AcrescimoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(acrescimoDAO.search(1l, 0, 10)).andReturn(null);
		replay(acrescimoDAO);

		Assert.assertNull( acrescimoService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AcrescimoServiceImpl.findByPessoal
	 */
	@Test
	public void testFindByPessoal() {
		expect(acrescimoDAO.findByPessoal(1l)).andReturn(null);
		replay(acrescimoDAO);

		Assert.assertNull( acrescimoService.findByPessoal(1l) );
	}

}
