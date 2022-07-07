package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.enums.Ambiente;



@Component
public class AmbienteService {
	
	@Value(value = "${database.url}")
	private String ambiente;

	public Ambiente ambiente() {
		if(!ambiente.contains("bdtce")) {
			if(ambiente.contains("bdhom")) {
				return Ambiente.HOM;
			}
			return Ambiente.DEV;
		} else {
			return Ambiente.PROD;
		}
	}

	public boolean isAmbienteDesenvolvimento() {
		return ambiente().isDesenvolvimento() || ambiente().isHomologacao();
	}
	
	public boolean isAmbienteProducao() {
		return ambiente().isProducao();
	}
}
