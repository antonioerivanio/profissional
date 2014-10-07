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

import br.gov.ce.tce.srh.dao.FuncionalDAO;
import br.gov.ce.tce.srh.dao.FuncionalSetorDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalServiceImpl;
import br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl;

public class FuncionalSetorServiceTest {

	private FuncionalSetorServiceImpl funcionalSetorService;
	private FuncionalSetorDAO funcionalSetorDAO;

	private FuncionalServiceImpl funcionalService;
	private FuncionalDAO funcionalDAO;


	@Before
	public void beforeTest() {
		funcionalDAO = createMock(FuncionalDAO.class);
		funcionalService = new FuncionalServiceImpl();
		funcionalService.setDAO(funcionalDAO);
		
		funcionalSetorDAO = createMock(FuncionalSetorDAO.class);
		funcionalSetorService = new FuncionalSetorServiceImpl();
		funcionalSetorService.setDao(funcionalSetorDAO);
		funcionalSetorService.setFuncionalService(funcionalService);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() {

		FuncionalSetor entidade = new FuncionalSetor();

		// validando o funcionario NULO
		try {

			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setFuncional( new Funcional() );
		}


		// validando o funcionario VAZIO
		try {

			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario vazia!!!");

		} catch (SRHRuntimeException e) {
			entidade.getFuncional().setId(2l);
		}


		// validando o setor NULO
		try {

			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com setor nulo!!!");

		} catch (SRHRuntimeException e) {}


		// validando o setor VAZIO
		try {

			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com setor vazia!!!");

		} catch (SRHRuntimeException e) {
			entidade.setSetor(new Setor());
		}


		// validando a data inicio NULA
		try {

			entidade.setDataInicio(null);
			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDataInicio(new Date());
		}

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando o data inicio maior que a data fim
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("10/12/2011") );

			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarLocataoAtiva() throws SRHRuntimeException {

		FuncionalSetor entidade = new FuncionalSetor();
		entidade.setId(1l);
		entidade.setFuncional(new Funcional());
		entidade.getFuncional().setId(1l);
		entidade.getFuncional().setPessoal(new Pessoal());
		entidade.getFuncional().getPessoal().setId(1l);
		entidade.setSetor(new Setor());
		entidade.getSetor().setId(1l);
		entidade.setObservacao("teste");
		entidade.setDataInicio(new Date());

		FuncionalSetor existente = new FuncionalSetor();
		existente.setId(2l);
		existente.setFuncional(new Funcional());
		existente.getFuncional().setId(1l);
		existente.setSetor(new Setor());
		existente.getSetor().setId(1l);
		existente.setObservacao("teste");
		existente.setDataInicio(new Date());

		try {

			// ativa
			existente.setDataFim(null);

			List<FuncionalSetor> lista = new ArrayList<FuncionalSetor>();
			lista.add(existente);

			expect(funcionalSetorDAO.findByPessoal(1l)).andReturn(lista);
			expect(funcionalSetorDAO.salvar(entidade)).andReturn(null);
			replay(funcionalSetorDAO);

			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente ativa!!!");

		} catch (SRHRuntimeException e) {
			
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarDataInicio() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		FuncionalSetor entidade = new FuncionalSetor();
		entidade.setId(1l);
		entidade.setFuncional(new Funcional());
		entidade.getFuncional().setId(1l);
		entidade.getFuncional().setPessoal(new Pessoal());
		entidade.getFuncional().getPessoal().setId(1l);
		entidade.setSetor(new Setor());
		entidade.getSetor().setId(1l);
		entidade.setObservacao("teste");

		FuncionalSetor existente = new FuncionalSetor();
		existente.setId(2l);
		existente.setFuncional(new Funcional());
		existente.getFuncional().setId(1l);
		existente.setSetor(new Setor());
		existente.getSetor().setId(1l);
		existente.setObservacao("teste");

		// validando data inicio no intervalo
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("15/12/2011") );

			existente.setDataInicio( dateFormate.parse("15/12/2011") );
			existente.setDataFim( dateFormate.parse("20/12/2011") );

			List<FuncionalSetor> lista = new ArrayList<FuncionalSetor>();
			lista.add(existente);

			expect(funcionalSetorDAO.findByPessoal(1l)).andReturn(lista);
			expect(funcionalSetorDAO.findByPessoal(1l)).andReturn(lista);
			expect(funcionalSetorDAO.salvar(entidade)).andReturn(null);
			replay(funcionalSetorDAO);

			funcionalSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data inicio maior que a data fim do ultimo!!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Funcional funcional = new Funcional();
		funcional.setId(1l);
		funcional.setMatricula("0025-1");
		funcional.setPessoal(new Pessoal());
		funcional.getPessoal().setId(1l);
		
		FuncionalSetor entidade = new FuncionalSetor();
		entidade.setId(1l);
		entidade.setFuncional(funcional);
		entidade.setSetor(new Setor());
		entidade.getSetor().setId(1l);
		entidade.setObservacao("teste");
		entidade.setDataInicio(new Date());

		expect(funcionalSetorDAO.findByPessoal(1l)).andReturn(new ArrayList<FuncionalSetor>());
		expect(funcionalSetorDAO.findByPessoal(1l)).andReturn(new ArrayList<FuncionalSetor>());
		expect(funcionalDAO.getByMatriculaAtivo(funcional.getMatricula())).andReturn(funcional);
		expect(funcionalDAO.salvar(funcional)).andReturn(funcional);
		expect(funcionalSetorDAO.salvar(entidade)).andReturn(null);
		replay(funcionalDAO);
		replay(funcionalSetorDAO);

		funcionalSetorService.salvar(entidade);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		FuncionalSetor entidade = new FuncionalSetor();
		funcionalSetorDAO.excluir(entidade);
		replay(funcionalSetorDAO);

		try {

			funcionalSetorService.excluir(entidade);

		} catch (SRHRuntimeException e) {
			Assert.fail("Ocorreu algum erro!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.excluir
	 */
	@Test
	public void testExcluirComDataFim() {

		FuncionalSetor entidade = new FuncionalSetor();
		entidade.setDataFim(new Date());
		funcionalSetorDAO.excluir(entidade);
		replay(funcionalSetorDAO);

		try {

			funcionalSetorService.excluir(entidade);
			Assert.fail("Nao validou a exclusao com data fim!!!");

		} catch (SRHRuntimeException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(funcionalSetorDAO.count(1l)).andReturn(1);
		replay(funcionalSetorDAO);

		Assert.assertEquals( funcionalSetorService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(funcionalSetorDAO.search(1l, 0, 10)).andReturn(null);
		replay(funcionalSetorDAO);

		Assert.assertNull( funcionalSetorService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalSetorServiceImpl.findByPessoa
	 */
	@Test
	public void testFindByPessoa() {
		expect(funcionalSetorDAO.findByPessoal(1l)).andReturn(null);
		replay(funcionalSetorDAO);
		List<FuncionalSetor> lista = funcionalSetorService.findByPessoal(1l);
		Assert.assertNull(lista);
	}

}
