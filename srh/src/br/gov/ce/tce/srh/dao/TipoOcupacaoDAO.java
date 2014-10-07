package br.gov.ce.tce.srh.dao;

import java.util.List;
import br.gov.ce.tce.srh.domain.TipoOcupacao;

public interface TipoOcupacaoDAO {

	public TipoOcupacao getById(Long id);

	public List<TipoOcupacao> findAll();

}
