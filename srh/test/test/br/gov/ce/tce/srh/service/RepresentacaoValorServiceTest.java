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

import br.gov.ce.tce.srh.dao.RepresentacaoValorDAO;
import br.gov.ce.tce.srh.domain.RepresentacaoValor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoValorServiceImpl;

public class RepresentacaoValorServiceTest {

	private RepresentacaoValorServiceImpl representacaoValorService;
	private RepresentacaoValorDAO representacaoValorDAO;

	@Before
	public void beforeTest() {
		representacaoValorDAO = createMock(RepresentacaoValorDAO.class);
		representacaoValorService = new RepresentacaoValorServiceImpl();
		representacaoValorService.setDAO(representacaoValorDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoValorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		RepresentacaoValor entidade = new RepresentacaoValor();
		expect(representacaoValorDAO.salvar(entidade)).andReturn(null);
		replay(representacaoValorDAO);		
		representacaoValorService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoValorServiceImpl.salvar
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

		RepresentacaoValor entidade = new RepresentacaoValor();
		entidade.setInicio(dataInicio);
		entidade.setFim(dataFim);

		expect(representacaoValorDAO.salvar(entidade)).andReturn(null);
		replay(representacaoValorDAO);

		try {

			representacaoValorService.salvar(entidade);
			Assert.fail("passou com data inicio maior!!!");

		} catch (SRHRuntimeException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoValorServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		representacaoValorDAO.excluir(null);
		replay(representacaoValorDAO);
		representacaoValorService.excluir(null);		
	}

	
	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoValorServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(representacaoValorDAO.count(1l)).andReturn(1);
		replay(representacaoValorDAO);

		Assert.assertEquals( representacaoValorService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoValorServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(representacaoValorDAO.search(1l, 0, 10)).andReturn(null);
		replay(representacaoValorDAO);

		Assert.assertNull( representacaoValorService.search(1l, 0, 10) );
	}

}
