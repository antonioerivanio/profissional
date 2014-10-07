package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.RepresentacaoSetorDAO;
import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl;

public class RepresentacaoSetorServiceTest {

	private RepresentacaoSetorServiceImpl representacaoSetorService;
	private RepresentacaoSetorDAO representacaoSetorDAO;

	@Before
	public void beforeTest() {
		representacaoSetorDAO = createMock(RepresentacaoSetorDAO.class);
		representacaoSetorService = new RepresentacaoSetorServiceImpl();
		representacaoSetorService.setDAO(representacaoSetorDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");
		Date dataInicio = null;
		Date dataFim = null;

		try {
			dataInicio = dateFormate.parse("10/12/2011");
			dataFim = dateFormate.parse("12/12/2011");
		} catch (ParseException e) {
			Assert.fail("Erro na conversao da data");
			e.printStackTrace();
		}

		RepresentacaoSetor entidade = new RepresentacaoSetor();

		// sem data fim
		entidade.setInicio(dataInicio);
		entidade.setFim(dataFim);

		expect(representacaoSetorDAO.getByCargoSetor(null, null)).andReturn(null);
		expect(representacaoSetorDAO.salvar(entidade)).andReturn(null);
		replay(representacaoSetorDAO);

		representacaoSetorService.salvar(entidade);

		// com data fim
		//
		//representacaoSetorService.salvar(entidade);
		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarDataFimMenor() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");
		Date dataInicio = null;
		Date dataFim = null;

		try {
			// com data inicio maoior
			dataInicio = dateFormate.parse("20/12/2011");
			dataFim = dateFormate.parse("12/12/2011");
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.fail("Erro na conversao da data");
		}

		RepresentacaoSetor entidade = new RepresentacaoSetor();
		entidade.setInicio(dataInicio);
		entidade.setFim(dataFim);

		expect(representacaoSetorDAO.getByCargoSetor(null, null)).andReturn(null);
		expect(representacaoSetorDAO.salvar(entidade)).andReturn(null);
		replay(representacaoSetorDAO);

		try {

			representacaoSetorService.salvar(entidade);
			Assert.fail("passou com data inicio maior!!!");

		} catch (SRHRuntimeException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		representacaoSetorDAO.excluir(null);
		replay(representacaoSetorDAO);
		representacaoSetorService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(representacaoSetorDAO.count(1l)).andReturn(1);
		replay(representacaoSetorDAO);

		Assert.assertEquals( representacaoSetorService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(representacaoSetorDAO.search(1l, 0, 10)).andReturn(null);
		replay(representacaoSetorDAO);

		Assert.assertNull( representacaoSetorService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl.getByCargoSetor
	 */
	@Test
	public void testGetByCargoSetor() {
		expect(representacaoSetorDAO.getByCargoSetor(1l, 1l)).andReturn(null);
		replay(representacaoSetorDAO);

		Assert.assertNull( representacaoSetorService.getByCargoSetor(1l, 1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoSetorServiceImpl.findBySetorAtivo
	 */
	@Test
	public void testFindBySetorAtivo() {
		expect(representacaoSetorDAO.findBySetorAtivo(1l, true)).andReturn(null);
		replay(representacaoSetorDAO);

		Assert.assertNull( representacaoSetorService.findBySetorAtivo(1l, true) );
	}

}
