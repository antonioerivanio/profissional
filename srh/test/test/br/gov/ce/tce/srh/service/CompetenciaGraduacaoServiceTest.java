package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;

import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAO;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaGraduacaoServiceImpl;

public class CompetenciaGraduacaoServiceTest {

	private CompetenciaGraduacaoServiceImpl competenciaGraduacaoService;
	private CompetenciaGraduacaoDAO competenciaGraduacaoDAO;

	@Before
	public void beforeTest() {
		competenciaGraduacaoDAO = createMock(CompetenciaGraduacaoDAO.class);
		competenciaGraduacaoService = new CompetenciaGraduacaoServiceImpl();
		competenciaGraduacaoService.setDAO(competenciaGraduacaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaGraduacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		CompetenciaGraduacao entidade = new CompetenciaGraduacao();
		competenciaGraduacaoDAO.salvar(entidade);
		replay(competenciaGraduacaoDAO);		
		competenciaGraduacaoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaGraduacaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		competenciaGraduacaoDAO.excluir(null, null);
		replay(competenciaGraduacaoDAO);
		competenciaGraduacaoService.excluir(null, null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaGraduacaoServiceImpl.findByPessoaCurso
	 */
	@Test
	public void testFindByPessoaCurso() {
		expect(competenciaGraduacaoDAO.findByPessoaCurso(null,  null)).andReturn(null);
		replay(competenciaGraduacaoDAO);
		List<CompetenciaGraduacao> lista = competenciaGraduacaoService.findByPessoaCurso(null,  null);
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaGraduacaoServiceImpl.getByPessoalCompetencia
	 */
	@Test
	public void testGetByPessoalCompetencia() {
		expect(competenciaGraduacaoDAO.getByPessoalCompetencia(1l, 1l)).andReturn(null);
		replay(competenciaGraduacaoDAO);
		Assert.assertNull( competenciaGraduacaoService.getByPessoalCompetencia(1l, 1l) );
	}

}
