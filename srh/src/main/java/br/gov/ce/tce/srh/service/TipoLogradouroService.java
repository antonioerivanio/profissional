package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.TipoLogradouroDAO;
import br.gov.ce.tce.srh.domain.TipoLogradouro;

@Service
public class TipoLogradouroService{

	@Autowired
	private TipoLogradouroDAO dao;


	public List<TipoLogradouro> findAll() {
		return dao.findAll();
	}
	
	public TipoLogradouro getTipoLogradouroInString(List<TipoLogradouro> tipoLogradouroList, String logradouro) {
		
		TipoLogradouro tipoLogradouro = null;
		
		for(TipoLogradouro tipo: tipoLogradouroList){
			
			if(logradouro.contains(tipo.getDescricao()))
				tipoLogradouro = tipo;			
		}	
		
		return tipoLogradouro;
		
	}


	public void setDAO(TipoLogradouroDAO tipoLogradouroDAO) {this.dao = tipoLogradouroDAO;}

}
