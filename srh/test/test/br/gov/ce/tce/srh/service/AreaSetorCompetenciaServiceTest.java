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

import br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAO;
import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl;


public class AreaSetorCompetenciaServiceTest {

	private AreaSetorCompetenciaServiceImpl areaSetorCompetenciaService;
	private AreaSetorCompetenciaDAO areaSetorCompetenciaDAO;

	@Before
	public void beforeTest() {
		areaSetorCompetenciaDAO = createMock(AreaSetorCompetenciaDAO.class);
		areaSetorCompetenciaService = new AreaSetorCompetenciaServiceImpl();
		areaSetorCompetenciaService.setDAO(areaSetorCompetenciaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		AreaSetorCompetencia entidade = new AreaSetorCompetencia();

		// validando a area do setor NULO
		try {

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com area do setor nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAreaSetor( new AreaSetor() );
		}	


		// validando a competencia NULO
		entidade.getAreaSetor().setId(2l);

		try {

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com competencia nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setCompetencia( new Competencia() );
		}


		// validando a data inicio NULO
		entidade.getCompetencia().setId(2l);

		try {

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDataInicio( new Date() );
		}
		

		// validando a motivo inicio NULO
		entidade.setDataInicio(new java.util.Date());

		try {

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com motivo inicio nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setMotivoInicio("");
		}


		// validando a motivo inicio VAZIA
		try {

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com motivo inicio vazio!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataFimNula() throws SRHRuntimeException {

		AreaSetorCompetencia entidade = new AreaSetorCompetencia();
		entidade.setId(1l);
		entidade.setAreaSetor( new AreaSetor() );
		entidade.getAreaSetor().setId(1l);
		entidade.setCompetencia( new Competencia() );
		entidade.getCompetencia().setId(1l);
		entidade.setDataInicio(new java.util.Date());
		entidade.setMotivoInicio("teste");

		AreaSetorCompetencia existente = new AreaSetorCompetencia();
		entidade.setId(2l);
		existente.setAreaSetor( new AreaSetor() );
		existente.getAreaSetor().setId(1l);
		existente.setCompetencia( new Competencia() );
		existente.getCompetencia().setId(1l);
		existente.setDataInicio(new java.util.Date());
		existente.setMotivoInicio("teste");

		List<AreaSetorCompetencia> lista = new ArrayList<AreaSetorCompetencia>();
		lista.add(existente);

		expect(areaSetorCompetenciaDAO.findByAreaCompetencia(1l, 1l)).andReturn(lista);
		expect(areaSetorCompetenciaDAO.salvar(entidade)).andReturn(null);
		replay(areaSetorCompetenciaDAO);

		// validando data fim nula
		try {

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data final nula!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataInicioIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		AreaSetorCompetencia entidade = new AreaSetorCompetencia();
		entidade.setId(1l);
		entidade.setAreaSetor( new AreaSetor() );
		entidade.getAreaSetor().setId(1l);
		entidade.setCompetencia( new Competencia() );
		entidade.getCompetencia().setId(1l);
		entidade.setDataInicio(new java.util.Date());
		entidade.setMotivoInicio("teste");

		AreaSetorCompetencia existente = new AreaSetorCompetencia();
		entidade.setId(2l);
		existente.setAreaSetor( new AreaSetor() );
		existente.getAreaSetor().setId(1l);
		existente.setCompetencia( new Competencia() );
		existente.getCompetencia().setId(1l);
		existente.setDataInicio(new java.util.Date());
		existente.setMotivoInicio("teste");

		// validando data inicio no intervalo
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("20/12/2011") );
			
			existente.setDataInicio( dateFormate.parse("15/12/2011") );
			existente.setDataFim( dateFormate.parse("25/12/2011") );

			List<AreaSetorCompetencia> lista = new ArrayList<AreaSetorCompetencia>();
			lista.add(existente);

			expect(areaSetorCompetenciaDAO.findByAreaCompetencia(1l, 1l)).andReturn(lista);
			expect(areaSetorCompetenciaDAO.salvar(entidade)).andReturn(null);
			replay(areaSetorCompetenciaDAO);

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data inicio no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {

		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataFimIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		AreaSetorCompetencia entidade = new AreaSetorCompetencia();
		entidade.setId(1l);
		entidade.setAreaSetor( new AreaSetor() );
		entidade.getAreaSetor().setId(1l);
		entidade.setCompetencia( new Competencia() );
		entidade.getCompetencia().setId(1l);
		entidade.setDataInicio(new java.util.Date());
		entidade.setMotivoInicio("teste");

		AreaSetorCompetencia existente = new AreaSetorCompetencia();
		entidade.setId(2l);
		existente.setAreaSetor( new AreaSetor() );
		existente.getAreaSetor().setId(1l);
		existente.setCompetencia( new Competencia() );
		existente.getCompetencia().setId(1l);
		existente.setDataInicio(new java.util.Date());
		existente.setMotivoInicio("teste");


		// validando data fim no intervalo
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("20/12/2011") );
	
			existente.setDataInicio( dateFormate.parse("10/12/2011") );
			existente.setDataFim( dateFormate.parse("15/12/2011") );

			List<AreaSetorCompetencia> lista = new ArrayList<AreaSetorCompetencia>();
			lista.add(existente);

			expect(areaSetorCompetenciaDAO.findByAreaCompetencia(1l, 1l)).andReturn(lista);
			expect(areaSetorCompetenciaDAO.salvar(entidade)).andReturn(null);
			replay(areaSetorCompetenciaDAO);

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data fim no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {

		}


	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarDataFimNaoNula() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		AreaSetorCompetencia entidade = new AreaSetorCompetencia();
		entidade.setId(1l);
		entidade.setAreaSetor( new AreaSetor() );
		entidade.getAreaSetor().setId(1l);
		entidade.setCompetencia( new Competencia() );
		entidade.getCompetencia().setId(1l);
		entidade.setDataInicio(new java.util.Date());
		entidade.setMotivoInicio("teste");

		expect(areaSetorCompetenciaDAO.findByAreaCompetencia(1l, 1l)).andReturn(new ArrayList<AreaSetorCompetencia>());
		expect(areaSetorCompetenciaDAO.salvar(entidade)).andReturn(null);
		replay(areaSetorCompetenciaDAO);

		// validando data final menor que a inicio
		try {

			entidade.setDataInicio( dateFormate.parse("12/12/2011") );
			entidade.setDataFim( dateFormate.parse("10/12/2011") );

			areaSetorCompetenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data fim menor que a data inicio!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {

		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		AreaSetorCompetencia entidade = new AreaSetorCompetencia();
		entidade.setAreaSetor(new AreaSetor());
		entidade.getAreaSetor().setId(1l);
		entidade.setCompetencia(new Competencia());
		entidade.getCompetencia().setId(1l);
		entidade.setDataInicio(new Date(System.currentTimeMillis()));
		entidade.setMotivoInicio("teste");

		expect(areaSetorCompetenciaDAO.findByAreaCompetencia(1l, 1l)).andReturn(new ArrayList<AreaSetorCompetencia>());
		expect(areaSetorCompetenciaDAO.salvar(entidade)).andReturn(null);
		replay(areaSetorCompetenciaDAO);

		areaSetorCompetenciaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.excluir
	 */
	@Test
	public void testExcluir(){
		areaSetorCompetenciaDAO.excluir(null);
		replay(areaSetorCompetenciaDAO);
		areaSetorCompetenciaService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(areaSetorCompetenciaDAO.count(1l)).andReturn(1);
		replay(areaSetorCompetenciaDAO);

		Assert.assertEquals( areaSetorCompetenciaService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.count
	 */
	@Test
	public void testCountCompetencia() {
		expect(areaSetorCompetenciaDAO.count(1l, 1l)).andReturn(1);
		replay(areaSetorCompetenciaDAO);

		Assert.assertEquals( areaSetorCompetenciaService.count(1l, 1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(areaSetorCompetenciaDAO.search(1l, 0, 10)).andReturn(null);
		replay(areaSetorCompetenciaDAO);

		Assert.assertNull( areaSetorCompetenciaService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.search
	 */
	@Test
	public void testSearchCompetencia() {
		expect(areaSetorCompetenciaDAO.search(1l, 1l, 0, 10)).andReturn(null);
		replay(areaSetorCompetenciaDAO);

		Assert.assertNull( areaSetorCompetenciaService.search(1l, 1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorCompetenciaServiceImpl.findByArea
	 */
	@Test
	public void testFindByArea() {
		expect(areaSetorCompetenciaDAO.findByArea(new Long(1))).andReturn(null);
		replay(areaSetorCompetenciaDAO);
		List<AreaSetorCompetencia> lista = areaSetorCompetenciaService.findByArea(1l);
		Assert.assertNull(lista);
	}

}
