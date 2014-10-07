package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CompetenciaCursoDAO;
import br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAO;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaCursoServiceImpl;
import br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl;

public class PessoalCursoProfissionalServiceTest {

	private PessoalCursoProfissionalServiceImpl pessoalCursoProfissionalService;
	private PessoalCursoProfissionalDAO pessoalCursoProfissionalDAO;

	private CompetenciaCursoServiceImpl competenciaCursoService;
	private CompetenciaCursoDAO competenciaCursoDAO;

	@Before
	public void beforeTest() {

		competenciaCursoDAO = createMock(CompetenciaCursoDAO.class);
		competenciaCursoService = new CompetenciaCursoServiceImpl();
		competenciaCursoService.setDAO(competenciaCursoDAO);

		pessoalCursoProfissionalDAO = createMock(PessoalCursoProfissionalDAO.class);
		pessoalCursoProfissionalService = new PessoalCursoProfissionalServiceImpl();
		pessoalCursoProfissionalService.setDAO(pessoalCursoProfissionalDAO);
		pessoalCursoProfissionalService.setCompetenciaCursoService(competenciaCursoService);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		List<PessoalCursoProfissional> listaPessoaCurso = new ArrayList<PessoalCursoProfissional>();
		List<CompetenciaCurso> listaCompetencias = new ArrayList<CompetenciaCurso>();

		// validando listaCompetencias NULA
		try {

			pessoalCursoProfissionalService.salvar(listaPessoaCurso, null, false);
			Assert.fail("nao validou lista de competencias nullas!!!");

		} catch (SRHRuntimeException e) {
			// TODO: handle exception
		}

		// validando listaCompetencias VAZIA
		try {

			pessoalCursoProfissionalService.salvar(listaPessoaCurso, listaCompetencias, false);
			Assert.fail("nao validou lista de competencias nullas!!!");

		} catch (SRHRuntimeException e) {
			// TODO: handle exception
		}

		listaCompetencias.add(new CompetenciaCurso());
		
		// validando listaPessoa NULA
		try {

			pessoalCursoProfissionalService.salvar(null, listaCompetencias, false);
			Assert.fail("nao validou lista de competencias nullas!!!");

		} catch (SRHRuntimeException e) {
			// TODO: handle exception
		}

		// validando listaPessoa VAZIA
		try {

			pessoalCursoProfissionalService.salvar(listaPessoaCurso, listaCompetencias, false);
			Assert.fail("nao validou lista de competencias nullas!!!");

		} catch (SRHRuntimeException e) {
			// TODO: handle exception
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarCursoExistente() throws SRHRuntimeException {

		CursoProfissional cursoProfissional = new CursoProfissional();
		cursoProfissional.setId(1l);
		
		PessoalCursoProfissional pessoalCurso = new PessoalCursoProfissional();
		pessoalCurso.setCursoProfissional(cursoProfissional);

		CompetenciaCurso competenciaCurso = new CompetenciaCurso();
		competenciaCurso.setCursoProfissional(cursoProfissional);

		List<PessoalCursoProfissional> listaPessoaCurso = new ArrayList<PessoalCursoProfissional>();
		listaPessoaCurso.add(pessoalCurso);
		
		List<CompetenciaCurso> listaCompetencias = new ArrayList<CompetenciaCurso>();
		listaCompetencias.add(competenciaCurso);

		// validando existe
		try {

			expect(pessoalCursoProfissionalDAO.getByCurso(1l)).andReturn(new PessoalCursoProfissional());
			replay(pessoalCursoProfissionalDAO);

			pessoalCursoProfissionalService.salvar(listaPessoaCurso, listaCompetencias, false);
			Assert.fail("Nao validou entidade ja existente!!!");

		} catch (SRHRuntimeException e) {
			// TODO: handle exception
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		CursoProfissional cursoProfissional = new CursoProfissional();
		cursoProfissional.setId(1l);
		
		PessoalCursoProfissional pessoalCurso = new PessoalCursoProfissional();
		pessoalCurso.setCursoProfissional(cursoProfissional);

		CompetenciaCurso competenciaCurso = new CompetenciaCurso();
		competenciaCurso.setCursoProfissional(cursoProfissional);

		List<PessoalCursoProfissional> listaPessoaCurso = new ArrayList<PessoalCursoProfissional>();
		listaPessoaCurso.add(pessoalCurso);
		
		List<CompetenciaCurso> listaCompetencias = new ArrayList<CompetenciaCurso>();
		listaCompetencias.add(competenciaCurso);

		// validando existe
		try {

			expect(pessoalCursoProfissionalDAO.getByCurso(1l)).andReturn(null);
			competenciaCursoDAO.excluir(1l);
			pessoalCursoProfissionalDAO.excluir(1l);
			competenciaCursoDAO.salvar(competenciaCurso);
			expect(pessoalCursoProfissionalDAO.salvar(pessoalCurso)).andReturn(null);
			
		
			replay(competenciaCursoDAO);
			replay(pessoalCursoProfissionalDAO);

			pessoalCursoProfissionalService.salvar(listaPessoaCurso, listaCompetencias, false);

		} catch (Exception e) {
			Assert.fail("Ocorreu algum erro no salvar!!!");
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl.excluir
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testExcluir() throws Exception {
		competenciaCursoDAO.excluir(1l);
		replay(competenciaCursoDAO);
		pessoalCursoProfissionalDAO.excluir(1l);
		replay(pessoalCursoProfissionalDAO);
		pessoalCursoProfissionalService.excluir(1l);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(pessoalCursoProfissionalDAO.count(1l, null)).andReturn(1);
		replay(pessoalCursoProfissionalDAO);

		Assert.assertEquals( pessoalCursoProfissionalService.count(1l, null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(pessoalCursoProfissionalDAO.search(1l, null, 0, 10)).andReturn(null);
		replay(pessoalCursoProfissionalDAO);

		Assert.assertNull( pessoalCursoProfissionalService.search(1l, null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoProfissionalServiceImpl.findByCurso
	 */
	@Test
	public void testFindByCurso() {
		expect(pessoalCursoProfissionalDAO.findByCurso(1l)).andReturn(null);
		replay(pessoalCursoProfissionalDAO);
		List<PessoalCursoProfissional> lista = pessoalCursoProfissionalService.findByCurso(1l);
		Assert.assertNull(lista);
	}

}
