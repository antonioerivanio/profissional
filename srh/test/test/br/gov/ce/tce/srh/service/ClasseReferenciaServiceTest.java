package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.ClasseReferenciaDAO;
import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ClasseReferenciaServiceImpl;


public class ClasseReferenciaServiceTest {

	private ClasseReferenciaServiceImpl service;
	private ClasseReferenciaDAO dao;

	@Before
	public void beforeTest() {
		dao = createMock(ClasseReferenciaDAO.class);
		service = new ClasseReferenciaServiceImpl();
		service.setDAO(dao);
	}



	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCursoAcademicaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		ClasseReferencia entidade = new ClasseReferencia();

		// validando a simbolo NULO
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou o simbolo como nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setSimbolo( new Simbolo() );
		}


		// validando a escolaridade NULA
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a escolaridade como nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setEscolaridade( new Escolaridade() );
		}
		

		// validando a referencia NULA
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a referencia como nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setReferencia( 10l );
		}


		// validando a classe NULA
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a classe como nula!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.ClasseReferenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		ClasseReferencia entidade = new ClasseReferencia();
		entidade.setId(1l);
		entidade.setSimbolo( new Simbolo() );
		entidade.getSimbolo().setId(1l);
		entidade.setEscolaridade( new Escolaridade() );
		entidade.setClasse("A");
		entidade.setReferencia(10l);

		ClasseReferencia existente = new ClasseReferencia();
		existente.setId(2l);

		expect(dao.getBySimboloReferencia(1l, 10l)).andReturn(existente);
		expect(dao.salvar(entidade)).andReturn(null);
		replay(dao);

		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.ClasseReferenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		ClasseReferencia entidade = new ClasseReferencia();
		entidade.setClasse("A");
		entidade.setEscolaridade( new Escolaridade() );
		entidade.setReferencia(10l);
		entidade.setSimbolo( new Simbolo() );
		entidade.getSimbolo().setId( 1l );

		expect(dao.getBySimboloReferencia(1l, 10l)).andReturn(null);
		expect(dao.salvar(entidade)).andReturn(null);
		replay(dao);		
		service.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.ClasseReferenciaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		dao.excluir(null);
		replay(dao);
		service.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.ClasseReferenciaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(dao.count(1l)).andReturn(1);
		replay(dao);

		Assert.assertEquals( service.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.ClasseReferenciaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(dao.search(1l, 0, 10)).andReturn(null);
		replay(dao);

		Assert.assertNull( service.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.ClasseReferenciaServiceImpl.findByCargo
	 */
	@Test
	public void testFindByCargo() {
		expect(dao.findByCargo(1l)).andReturn(null);
		replay(dao);
		List<ClasseReferencia> lista = service.findByCargo(1l);
		Assert.assertNull(lista);
	}

}
