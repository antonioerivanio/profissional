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

import br.gov.ce.tce.srh.dao.AverbacaoDAO;
import br.gov.ce.tce.srh.domain.Averbacao;
import br.gov.ce.tce.srh.domain.Municipio;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.Uf;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AverbacaoServiceImpl;


public class AverbacaoServiceTest {

	private AverbacaoServiceImpl averbacaoService;
	private AverbacaoDAO averbacaoDAO;

	@Before
	public void beforeTest() {
		averbacaoDAO = createMock(AverbacaoDAO.class);
		averbacaoService = new AverbacaoServiceImpl();
		averbacaoService.setDAO(averbacaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AverbacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Averbacao entidade = new Averbacao();

		// validando o funcionario NULO
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPessoal( new Pessoal() );
		}

		
		// validando o uf
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com UF nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setUf( new Uf() );
		}

		
		// validando o municipio
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com municipio nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setMunicipio( new Municipio() );
		}
		

		// validando o entidade nula
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com entidade nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setEntidade("");
		}


		// validando o entidade vazia
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com entidade vazia!!!");

		} catch (SRHRuntimeException e) {
			entidade.setEntidade("caixa");
		}


		// validando a data inicio nula
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setInicio( new Date() );
		}

		
		// validando a data fim nula
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data fim nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setFim( new Date() );
		}


		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando o data inicio maior que a data fim
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("10/12/2011") );

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}


		// validando a esfera nula
		try {

			entidade.setEsfera(null);
			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com esfera nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setEsfera(0l);
		}


		// validando a esfera vazia
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com esfera vazia!!!");

		} catch (SRHRuntimeException e) {
			entidade.setEsfera(1l);
		}


		// validando a previdencia nula
		try {

			entidade.setPrevidencia(null);
			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com previdencia nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPrevidencia(0l);
		}


		// validando a previdencia vazia
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com previdencia vazia!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPrevidencia(1l);
		}


		// validando a descricao nula
		try {

			entidade.setDescricao(null);
			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDescricao("");
		}


		// validando a descricao vazia
		try {

			averbacaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao vazia!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDescricao("teste");
		}
	
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AverbacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Averbacao entidade = new Averbacao();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setInicio( new Date() );
		entidade.setFim( new Date() );
		entidade.setQtdeDias(30l);
		entidade.setEntidade("caixa");
		entidade.setMunicipio( new Municipio() );
		entidade.setUf( new Uf() );
		entidade.setEsfera(2l);
		entidade.setPrevidencia(2l);
		entidade.setDescricao("teste");

		expect(averbacaoDAO.findByPessoal(1l)).andReturn(new ArrayList<Averbacao>());
		expect(averbacaoDAO.salvar(entidade)).andReturn(null);
		replay(averbacaoDAO);

		averbacaoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AverbacaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		averbacaoDAO.excluir(null);
		replay(averbacaoDAO);
		averbacaoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AverbacaoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(averbacaoDAO.count(1l)).andReturn(1);
		replay(averbacaoDAO);

		Assert.assertEquals( averbacaoService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AverbacaoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(averbacaoDAO.search(1l, 0, 10)).andReturn(null);
		replay(averbacaoDAO);

		Assert.assertNull( averbacaoService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AverbacaoServiceImpl.findByPessoal
	 */
	@Test
	public void testFindByPessoal() {
		expect(averbacaoDAO.findByPessoal(1l)).andReturn(null);
		replay(averbacaoDAO);

		Assert.assertNull( averbacaoService.findByPessoal(1l) );
	}

}