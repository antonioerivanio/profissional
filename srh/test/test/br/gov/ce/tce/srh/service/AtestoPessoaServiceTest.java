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

import br.gov.ce.tce.srh.dao.AtestoPessoaDAO;
import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl;

public class AtestoPessoaServiceTest {

	private AtestoPessoaDAO atestoPessoaDAO;
	private AtestoPessoaServiceImpl atestoPessoaService;


	@Before
	public void beforeTest() {
		atestoPessoaDAO = createMock(AtestoPessoaDAO.class);
		atestoPessoaService = new AtestoPessoaServiceImpl();
		atestoPessoaService.setDAO(atestoPessoaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		AtestoPessoa entidade = new AtestoPessoa();

		// validando o funcionario NULO
		try {

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPessoal( new Pessoal() );
			entidade.getPessoal().setId(0l);
		}


		// validando o funcionario VAZIO
		try {

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.getPessoal().setId(1l);
		}

		// validando a competencia NULA
		try {

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com competencia nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setCompetencia( new Competencia() );
		}


		// validando data inicio NULA
		try {

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDataInicio( new Date() );
		}


		// validando o data fim NULA
		try {

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data fim nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDataFim( new Date() );
		}
	

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando o data inicio maior que a data fim
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("10/12/2011") );

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}


		// validando o responsavel NULO
		try {

			entidade.setDataFim(null);
			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com responsavel nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setResponsavel( new RepresentacaoFuncional() );
			entidade.getResponsavel().setId(0l);
		}


		// validando o responsavel VAZIO
		try {

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario vazio!!!");

		} catch (SRHRuntimeException e) {

		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarCompetenciaParaMesmoServidorInicioIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		AtestoPessoa entidade = new AtestoPessoa();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setCompetencia( new Competencia() );
		entidade.getCompetencia().setId(1l);
		entidade.setResponsavel(new RepresentacaoFuncional());
		entidade.getResponsavel().setId(1l);

		AtestoPessoa existente = new AtestoPessoa();
		existente.setId(2l);


		// validando data inicio no intervalo
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("20/12/2011") );
			
			existente.setDataInicio( dateFormate.parse("15/12/2011") );
			existente.setDataFim( dateFormate.parse("25/12/2011") );

			List<AtestoPessoa> lista = new ArrayList<AtestoPessoa>();
			lista.add(existente);

			expect(atestoPessoaDAO.findByPessoaCompetencia(1l, 1l)).andReturn(lista);
			expect(atestoPessoaDAO.salvar(entidade)).andReturn(null);
			replay(atestoPessoaDAO);

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data inicio no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarCompetenciaParaMesmoServidorFimIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		AtestoPessoa entidade = new AtestoPessoa();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setCompetencia( new Competencia() );
		entidade.getCompetencia().setId(1l);
		entidade.setResponsavel(new RepresentacaoFuncional());
		entidade.getResponsavel().setId(1l);

		AtestoPessoa existente = new AtestoPessoa();
		existente.setId(2l);


		// validando data fim no intervalo
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("20/12/2011") );

			existente.setDataInicio( dateFormate.parse("10/12/2011") );
			existente.setDataFim( dateFormate.parse("15/12/2011") );

			List<AtestoPessoa> lista = new ArrayList<AtestoPessoa>();
			lista.add(existente);

			expect(atestoPessoaDAO.findByPessoaCompetencia(1l, 1l)).andReturn(lista);
			expect(atestoPessoaDAO.salvar(entidade)).andReturn(null);
			replay(atestoPessoaDAO);

			atestoPessoaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data fim no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		AtestoPessoa entidade = new AtestoPessoa();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setCompetencia( new Competencia() );
		entidade.getCompetencia().setId(1l);
		entidade.setResponsavel(new RepresentacaoFuncional());
		entidade.getResponsavel().setId(1l);
		entidade.setDataInicio(new Date());

		expect(atestoPessoaDAO.findByPessoaCompetencia(1l, 1l)).andReturn(new ArrayList<AtestoPessoa>());
		expect(atestoPessoaDAO.salvar(entidade)).andReturn(null);
		replay(atestoPessoaDAO);

		atestoPessoaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		AtestoPessoa entidade = new AtestoPessoa();
		entidade.setId(1l);

		atestoPessoaDAO.excluir(entidade);
		replay(atestoPessoaDAO);

		atestoPessoaService.excluir(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(atestoPessoaDAO.count(1l)).andReturn(1);
		replay(atestoPessoaDAO);

		Assert.assertEquals( atestoPessoaService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(atestoPessoaDAO.search(1l, 0, 10)).andReturn(null);
		replay(atestoPessoaDAO);

		Assert.assertNull( atestoPessoaService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AtestoPessoaServiceImpl.getByPessoalCompetencia
	 */
	@Test
	public void testGetByPessoalCompetencia() {
		expect(atestoPessoaDAO.getByPessoalCompetencia(1l, 1l)).andReturn(null);
		replay(atestoPessoaDAO);

		Assert.assertNull( atestoPessoaService.getByPessoalCompetencia(1l, 1l));
	}

}
