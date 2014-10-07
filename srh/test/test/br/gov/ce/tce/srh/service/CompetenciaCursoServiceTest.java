package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;

import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CompetenciaCursoDAO;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaCursoServiceImpl;

public class CompetenciaCursoServiceTest {

	private CompetenciaCursoServiceImpl competenciaCursoService;
	private CompetenciaCursoDAO competenciaCursoDAO;

	@Before
	public void beforeTest() {
		competenciaCursoDAO = createMock(CompetenciaCursoDAO.class);
		competenciaCursoService = new CompetenciaCursoServiceImpl();
		competenciaCursoService.setDAO(competenciaCursoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaCursoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		CompetenciaCurso entidade = new CompetenciaCurso();
		competenciaCursoDAO.salvar(entidade);
		replay(competenciaCursoDAO);		
		competenciaCursoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaCursoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		competenciaCursoDAO.excluir(null);
		replay(competenciaCursoDAO);
		competenciaCursoService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaCursoServiceImpl.findByCargo
	 */
	@Test
	public void testFindByCargo() {
		expect(competenciaCursoDAO.findByCurso(1l)).andReturn(null);
		replay(competenciaCursoDAO);
		List<CompetenciaCurso> lista = competenciaCursoService.findByCurso(1l);
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaCursoServiceImpl.getByPessoalCompetencia
	 */
	@Test
	public void testGetByPessoalCompetencia() {
		expect(competenciaCursoDAO.getByPessoalCompetencia(1l, 1l)).andReturn(null);
		replay(competenciaCursoDAO);
		Assert.assertNull( competenciaCursoService.getByPessoalCompetencia(1l, 1l) );
	}

}
