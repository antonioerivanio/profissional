package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.PublicacaoDAO;
import br.gov.ce.tce.srh.domain.Publicacao;
import br.gov.ce.tce.srh.domain.TipoDocumento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.PublicacaoServiceImpl;

public class PublicacaoServiceTest {

	private PublicacaoServiceImpl service;
	private PublicacaoDAO publicacaoDAO;

	@Before
	public void beforeTest() {
		publicacaoDAO = createMock(PublicacaoDAO.class);
		service = new PublicacaoServiceImpl();
		service.setDAO(publicacaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PublicacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Publicacao entidade = new Publicacao();

		// validando o tipo documento NULO
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou o tipo documento como nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setTipoDocumento( new TipoDocumento() );
		}


		// validando o numero NULO
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou o numero como nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setNumero(0l);
		}
		

		// validando o numero NAO NULO, mas ZERO
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou o numero como zero!!!");

		} catch (SRHRuntimeException e) {
			entidade.setNumero(1l);
		}


		// validando o ano NULO
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou o ano como nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAno(0l);
		}
		

		// validando o ano NAO NULO, mas ZERO
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou o ano como zero!!!");

		} catch (SRHRuntimeException e) {
			entidade.setAno(2012l);
		}

	
		// validando a data vigencia NULA
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a data vigencia como nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setVigencia( new Date() );
		}


		// validando o DOE NULO
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou o DOE como nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDoe( new Date() );
		}

		
		// validando a ementa NULA
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a ementa como NULA!!!");

		} catch (SRHRuntimeException e) {
			entidade.setEmenta("");
		}

		
		// validando a ementa vazia
		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a ementa como vazia!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PublicacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		Publicacao entidade = new Publicacao();
		entidade.setId(1l);
		entidade.setTipoDocumento( new TipoDocumento() );
		entidade.setNumero(10l);
		entidade.setAno(2012l);
		entidade.setVigencia(new Date());
		entidade.setDoe(new Date());
		entidade.setEmenta("Teste");

		Publicacao existente = new Publicacao();
		existente.setId(2l);

		expect(publicacaoDAO.getByEmenta("Teste")).andReturn(existente);
		expect(publicacaoDAO.salvar(entidade)).andReturn(null);
		replay(publicacaoDAO);

		try {

			service.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PublicacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		Publicacao entidade = new Publicacao();
		entidade.setTipoDocumento( new TipoDocumento() );
		entidade.setNumero(10l);
		entidade.setAno(2012l);
		entidade.setVigencia( new Date() );
		entidade.setDoe( new Date() );
		entidade.setEmenta("Teste");
		entidade.setArquivo("teste.pdf");

		expect(publicacaoDAO.getByEmenta("Teste")).andReturn(null);
		expect(publicacaoDAO.salvar(entidade)).andReturn(null);
		replay(publicacaoDAO);

		service.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PublicacaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		publicacaoDAO.excluir(null);
		replay(publicacaoDAO);
		service.excluir(null);		
	}

	
	/**
	 * Test of br.gov.ce.tce.srh.service.PublicacaoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(publicacaoDAO.count(1l)).andReturn(1);
		replay(publicacaoDAO);

		Assert.assertEquals( service.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PublicacaoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(publicacaoDAO.search(1l, 0, 10)).andReturn(null);
		replay(publicacaoDAO);

		Assert.assertNull( service.search(1l, 0, 10) );
	}

}
