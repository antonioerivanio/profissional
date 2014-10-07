package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PublicacaoDAO;
import br.gov.ce.tce.srh.domain.Publicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("publicacaoService")
public class PublicacaoServiceImpl implements PublicacaoService {

	@Autowired
	private PublicacaoDAO dao;


	@Override
	@Transactional
	public void salvar(Publicacao entidade) throws SRHRuntimeException {

		//validando dados obrigatorios
		validaDados(entidade);

		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
		verificandoSeEntidadeExiste(entidade);

		// persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Publicacao entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long tipoDocumento) {
		return dao.count(tipoDocumento);
	}


	@Override
	public List<Publicacao> search(Long tipoDocumento, int first, int rows) {
		return dao.search(tipoDocumento, first, rows);
	}



	/**
	 * Validar:
	 * 
	 * · Deve ser setado o tipo documento.
	 * · Deve ser setado o numero.
	 * · Deve ser setado o ano.
	 * · Deve ser setado a data de vigencia.
	 * · Deve ser setada o tipo de publicacao.
	 * · Deve ser setada a data de publicacao.
	 * · Deve ser setada a ementa.
	 * · Deve ser setado o arquivo.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(Publicacao entidade) {

		// validando o tipo documento
		if ( entidade.getTipoDocumento() == null )
			throw new SRHRuntimeException("O tipo de documento é obrigatório.");

		// validando o numero
		if (entidade.getNumero() == null || entidade.getNumero().equals(0l))
			throw new SRHRuntimeException("O numero é obrigatório.");

		// validando o ano
		if (entidade.getAno() == null || entidade.getAno().equals(0l))
			throw new SRHRuntimeException("O ano é obrigatório.");

		// validando a data de vigencia
		if (entidade.getVigencia() == null)
			throw new SRHRuntimeException("A vigência é obrigatória.");

		// validando o tipo de publicacao
		if (entidade.getDoe() == null)
			throw new SRHRuntimeException("O tipo de publicação é obrigatório.");
		
		// validando a data de publicacao
		if (entidade.getDoe() == null)
			throw new SRHRuntimeException("A data publicação é obrigatória.");

		// validando a ementa
		if (entidade.getEmenta() == null || entidade.getEmenta().equalsIgnoreCase(""))
			throw new SRHRuntimeException("A ementa é obrigatória.");

		// validando o arquivo
		if (entidade.getArquivo() == null || entidade.getArquivo().equals(""))
			throw new SRHRuntimeException("O arquivo a ser publicado é obrigatório.");

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe publicacao cadastrada com a mesma EMENTA
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(Publicacao entidade) {

		// verificando se a publicacao ja foi cadastrada
		Publicacao entidadeJaExiste = dao.getByEmenta( entidade.getEmenta() );
		if ( entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId() ) )
			throw new SRHRuntimeException("Publicação já cadastrada. Operação cancelada.");

	}


	public void setDAO(PublicacaoDAO dao) {this.dao = dao;}

}
