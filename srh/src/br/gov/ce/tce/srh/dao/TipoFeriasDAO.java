package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoFerias;

/**
 * 
 * @author joel.barbosa
 *
 */
public interface TipoFeriasDAO {

	public List<TipoFerias> findAll();
	
}
