package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAO;
import br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAO;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademicaPk;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaGraduacaoServiceImpl;
import br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl;

public class PessoalCursoAcademicaServiceTest {

	private PessoalCursoAcademicaServiceImpl pessoalCursoAcademicaService;
	private PessoalCursoAcademicaDAO pessoalCursoAcademicaDAO;

	private CompetenciaGraduacaoServiceImpl competenciaGraduacaoService;
	private CompetenciaGraduacaoDAO competenciaGraduacaoDAO;

	@Before
	public void beforeTest() {

		competenciaGraduacaoDAO = createMock(CompetenciaGraduacaoDAO.class);
		competenciaGraduacaoService = new CompetenciaGraduacaoServiceImpl();
		competenciaGraduacaoService.setDAO(competenciaGraduacaoDAO);

		pessoalCursoAcademicaDAO = createMock(PessoalCursoAcademicaDAO.class);
		pessoalCursoAcademicaService = new PessoalCursoAcademicaServiceImpl();
		pessoalCursoAcademicaService.setDAO(pessoalCursoAcademicaDAO);
		pessoalCursoAcademicaService.setCompetenciaGraduacaoService(competenciaGraduacaoService);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		PessoalCursoAcademica entidade = new PessoalCursoAcademica();

		// validando a entidade NULA
		try {

			pessoalCursoAcademicaService.salvar(null, null, null, false);
			Assert.fail("Nao validou a entidade como nulla!!!");

		} catch (SRHRuntimeException e) {}


		// validando o servidor NULO
		try {

			pessoalCursoAcademicaService.salvar(entidade, null, null, false);
			Assert.fail("Nao validou o servidor como nulla!!!");

		} catch (SRHRuntimeException e) {}
		

		// validando o servidor NAO NULO, mas ZERO
		try {

			entidade.setPessoal( new Pessoal() );
			entidade.getPessoal().setId(0l);
			pessoalCursoAcademicaService.salvar(entidade, null, null, false);
			Assert.fail("Nao validou o servidor como zero!!!");

		} catch (SRHRuntimeException e) {
			entidade.getPessoal().setId( 1l );
		}


		// validando o curso NULO
		try {

			pessoalCursoAcademicaService.salvar(entidade, null, null, false);
			Assert.fail("Nao validou o curso como nulla!!!");

		} catch (SRHRuntimeException e) {
			entidade.setCursoAcademica( new CursoAcademica() );
			entidade.getCursoAcademica().setId( 1l );
		}
		

		// validando a instituicao NULO
		try {

			pessoalCursoAcademicaService.salvar(entidade, null, null, false);
			Assert.fail("Nao validou o curso como nulla!!!");

		} catch (SRHRuntimeException e) {
			entidade.setInstituicao( new Instituicao() );
		}

		
		// validando a data inicio como NULA
		try {

			pessoalCursoAcademicaService.salvar(entidade, null, null, false);
			Assert.fail("Nao validou a data de inicio como nula!!!");

		} catch (SRHRuntimeException e) {}

		
		// validando a lista de competencias NULA
		try {

			pessoalCursoAcademicaService.salvar(entidade, null, new Date(), false);
			Assert.fail("Nao validou a lista de competencias como nulla!!!");

		} catch (SRHRuntimeException e) {}

	
		// validando a lista de competencias NAO NULA, mas ZERO
		try {

			pessoalCursoAcademicaService.salvar(entidade, new ArrayList<CompetenciaGraduacao>(), new Date(), false);
			Assert.fail("Nao validou a lista de competencias como zero!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarCursoExistente() throws SRHRuntimeException {

		// pessoa
		PessoalCursoAcademica pessoalCursoAcademica = new PessoalCursoAcademica();
		pessoalCursoAcademica.setPk( new PessoalCursoAcademicaPk() );
		pessoalCursoAcademica.getPk().setPessoal(1l);
		pessoalCursoAcademica.getPk().setCursoAcademico(1l);
		pessoalCursoAcademica.setCursoAcademica(new CursoAcademica());
		pessoalCursoAcademica.getCursoAcademica().setId(1l);
		pessoalCursoAcademica.setPessoal(new Pessoal());
		pessoalCursoAcademica.getPessoal().setId(1l);
		pessoalCursoAcademica.setInstituicao( new Instituicao() );
		pessoalCursoAcademica.getInstituicao().setId(1l);

		// lista de competencias
		List<CompetenciaGraduacao> competencias = new ArrayList<CompetenciaGraduacao>();
		competencias.add(new CompetenciaGraduacao());

		// validando existe
		try {

			expect(pessoalCursoAcademicaDAO.getByCursoPessoa(1l, 1l)).andReturn(new PessoalCursoAcademica());
			replay(pessoalCursoAcademicaDAO);

			pessoalCursoAcademicaService.salvar(pessoalCursoAcademica, competencias, new Date(), false);
			Assert.fail("Nao validou entidade ja existente!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		// pessoa
		PessoalCursoAcademica pessoalCursoAcademica = new PessoalCursoAcademica();
		pessoalCursoAcademica.setPk( new PessoalCursoAcademicaPk() );
		pessoalCursoAcademica.getPk().setPessoal(1l);
		pessoalCursoAcademica.getPk().setCursoAcademico(1l);
		pessoalCursoAcademica.setCursoAcademica(new CursoAcademica());
		pessoalCursoAcademica.getCursoAcademica().setId(1l);
		pessoalCursoAcademica.setPessoal(new Pessoal());
		pessoalCursoAcademica.getPessoal().setId(1l);
		pessoalCursoAcademica.setInstituicao( new Instituicao() );
		pessoalCursoAcademica.getInstituicao().setId(1l);

		// lista de competencias
		CompetenciaGraduacao competenciaGraduacao = new CompetenciaGraduacao();
		competenciaGraduacao.setCursoAcademica(new CursoAcademica());
		competenciaGraduacao.getCursoAcademica().setId(1l);
		
		List<CompetenciaGraduacao> listaCompetencias = new ArrayList<CompetenciaGraduacao>();
		listaCompetencias.add(competenciaGraduacao);

		// validando existe
		try {

			competenciaGraduacaoDAO.excluir(1l, 1l);
			competenciaGraduacaoDAO.salvar(competenciaGraduacao);
			expect(pessoalCursoAcademicaDAO.salvar(pessoalCursoAcademica)).andReturn(null);

			replay(competenciaGraduacaoDAO);
			replay(pessoalCursoAcademicaDAO);

			pessoalCursoAcademicaService.salvar(pessoalCursoAcademica, listaCompetencias, new Date(), true);

		} catch (Exception e) {
			Assert.fail("Ocorreu algum erro no salvar!!!");
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		PessoalCursoAcademica pessoalCursoAcademica = new PessoalCursoAcademica();
		pessoalCursoAcademica.setPk( new PessoalCursoAcademicaPk() );
		pessoalCursoAcademica.getPk().setPessoal(1l);
		pessoalCursoAcademica.getPk().setCursoAcademico(1l);
		pessoalCursoAcademica.setCursoAcademica(new CursoAcademica());
		pessoalCursoAcademica.getCursoAcademica().setId(1l);
		pessoalCursoAcademica.setPessoal(new Pessoal());
		pessoalCursoAcademica.getPessoal().setId(1l);
		pessoalCursoAcademica.setInstituicao( new Instituicao() );
		pessoalCursoAcademica.getInstituicao().setId(1l);

		competenciaGraduacaoDAO.excluir(1l, 1l);
		replay(competenciaGraduacaoDAO);
		pessoalCursoAcademicaDAO.excluir(pessoalCursoAcademica);
		replay(pessoalCursoAcademicaDAO);
		pessoalCursoAcademicaService.excluir(pessoalCursoAcademica);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(pessoalCursoAcademicaDAO.count(1l)).andReturn(1);
		replay(pessoalCursoAcademicaDAO);

		Assert.assertEquals( pessoalCursoAcademicaService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(pessoalCursoAcademicaDAO.search(1l, 0, 10)).andReturn(null);
		replay(pessoalCursoAcademicaDAO);

		Assert.assertNull( pessoalCursoAcademicaService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.findByPessoa
	 */
	@Test
	public void testFindByPessoa() {
		expect(pessoalCursoAcademicaDAO.findByPessoa(1l)).andReturn(null);
		replay(pessoalCursoAcademicaDAO);
		List<PessoalCursoAcademica> lista = pessoalCursoAcademicaService.findByPessoa(1l);
		Assert.assertNull(lista);
	}

}
