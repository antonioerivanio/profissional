package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.EspecialidadeCargoDAO;
import br.gov.ce.tce.srh.dao.OcupacaoDAO;
import br.gov.ce.tce.srh.dao.SimboloDAO;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EspecialidadeCargoServiceImpl;
import br.gov.ce.tce.srh.service.OcupacaoServiceImpl;
import br.gov.ce.tce.srh.service.SimboloServiceImpl;

public class OcupacaoServiceTest {

	private OcupacaoServiceImpl ocupacaoService;
	private OcupacaoDAO ocupacaoDAO;

	private SimboloDAO simboloDAO;
	private SimboloServiceImpl simboloService;

	private EspecialidadeCargoDAO especialidadeCargoDAO;
	private EspecialidadeCargoServiceImpl especialidadeCargoService;


	@Before
	public void beforeTest() {

		simboloDAO = createMock(SimboloDAO.class);
		simboloService = new SimboloServiceImpl();
		simboloService.setDAO(simboloDAO);

		ocupacaoDAO = createMock(OcupacaoDAO.class);
		ocupacaoService = new OcupacaoServiceImpl();
		ocupacaoService.setDAO(ocupacaoDAO);

		especialidadeCargoDAO = createMock(EspecialidadeCargoDAO.class);
		especialidadeCargoService = new EspecialidadeCargoServiceImpl();
		especialidadeCargoService.setDAO(especialidadeCargoDAO);

		ocupacaoService.setSimboloService(simboloService);
		ocupacaoService.setEspecialidadeCargoService(especialidadeCargoService);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		Ocupacao entidade = new Ocupacao();
		entidade.setId(0l);

		Ocupacao existente = new Ocupacao();
		existente.setId(2l);

		List<EspecialidadeCargo> listaEspecialidade = new ArrayList<EspecialidadeCargo>();
		List<Simbolo> listaSimbolo = new ArrayList<Simbolo>();

		expect(ocupacaoDAO.getByNomenclatura(null)).andReturn(existente);
		expect(ocupacaoDAO.salvar(entidade)).andReturn(entidade);
		replay(ocupacaoDAO);

		try {

			ocupacaoService.salvar(entidade, listaEspecialidade, listaSimbolo);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Ocupacao entidade = new Ocupacao();
		entidade.setId(0l);

		List<EspecialidadeCargo> listaEspecialidade = new ArrayList<EspecialidadeCargo>();
		List<Simbolo> listaSimbolo = new ArrayList<Simbolo>();

		expect(ocupacaoDAO.getByNomenclatura(null)).andReturn(null);
		expect(ocupacaoDAO.salvar(entidade)).andReturn(entidade);
		replay(ocupacaoDAO);

		simboloDAO.excluirAll(0l);
		replay(simboloDAO);

		especialidadeCargoDAO.excluirAll(0l);
		replay(especialidadeCargoDAO);

		ocupacaoService.salvar(entidade, listaEspecialidade, listaSimbolo);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarComSimbologia() throws SRHRuntimeException{

		Ocupacao entidade = new Ocupacao();
		entidade.setId(0l);

		List<EspecialidadeCargo> listaEspecialidade = new ArrayList<EspecialidadeCargo>();
		
		List<Simbolo> listaSimbolo = new ArrayList<Simbolo>();
		Simbolo simbolo = new Simbolo();
		listaSimbolo.add(simbolo);

		expect(ocupacaoDAO.getByNomenclatura(null)).andReturn(null);
		expect(ocupacaoDAO.salvar(entidade)).andReturn(entidade);
		replay(ocupacaoDAO);

		simboloDAO.excluirAll(0l);
		expect(simboloDAO.getByOcupacaoSimbolo(0l, null)).andReturn(null);
		expect(simboloDAO.salvar(simbolo)).andReturn(null);
		replay(simboloDAO);

		ocupacaoService.salvar(entidade, listaEspecialidade, listaSimbolo);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarComEspecialidade() throws SRHRuntimeException{

		Ocupacao entidade = new Ocupacao();
		entidade.setId(0l);

		List<EspecialidadeCargo> listaEspecialidade = new ArrayList<EspecialidadeCargo>();
		EspecialidadeCargo especialidade = new EspecialidadeCargo();
		listaEspecialidade.add(especialidade);
	
		List<Simbolo> listaSimbolo = new ArrayList<Simbolo>();

		expect(ocupacaoDAO.getByNomenclatura(null)).andReturn(null);
		expect(ocupacaoDAO.salvar(entidade)).andReturn(entidade);
		replay(ocupacaoDAO);
		
		simboloDAO.excluirAll(0l);
		replay(simboloDAO);

		especialidadeCargoDAO.excluirAll(0l);

		expect(especialidadeCargoDAO.salvar(especialidade)).andReturn(null);
		replay(especialidadeCargoDAO);

		ocupacaoService.salvar(entidade, listaEspecialidade, listaSimbolo);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		Ocupacao entidade = new Ocupacao();
		entidade.setId(0l);

		ocupacaoDAO.excluir(entidade);
		replay(ocupacaoDAO);

		simboloDAO.excluirAll(0l);
		replay(simboloDAO);

		especialidadeCargoDAO.excluirAll(0l);
		replay(especialidadeCargoDAO);

		ocupacaoService.excluir(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(ocupacaoDAO.count(null)).andReturn(1);
		replay(ocupacaoDAO);

		Assert.assertEquals( ocupacaoService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.count
	 */
	@Test
	public void testCountSituacao() {
		expect(ocupacaoDAO.count(null, 1l)).andReturn(1);
		replay(ocupacaoDAO);

		Assert.assertEquals( ocupacaoService.count(null, 1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(ocupacaoDAO.search(null, 0, 10)).andReturn(null);
		replay(ocupacaoDAO);

		Assert.assertNull( ocupacaoService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.search
	 */
	@Test
	public void testSearchSituacao() {
		expect(ocupacaoDAO.search(null, 1l, 0, 10)).andReturn(null);
		replay(ocupacaoDAO);

		Assert.assertNull( ocupacaoService.search(null, 1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(ocupacaoDAO.getById(1l)).andReturn(null);
		replay(ocupacaoDAO);
		Assert.assertNull( ocupacaoService.getById(1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(ocupacaoDAO.findAll()).andReturn(null);
		replay(ocupacaoDAO);
		Assert.assertNull( ocupacaoService.findAll() );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.OcupacaoServiceImpl.findByTipoOcupacao
	 */
	@Test
	public void testFindByTipoOcupacao() {
		expect(ocupacaoDAO.findByTipoOcupacao(1l)).andReturn(null);
		replay(ocupacaoDAO);
		List<Ocupacao> lista = ocupacaoService.findByTipoOcupacao(1l);
		Assert.assertNull(lista);
	}

}
