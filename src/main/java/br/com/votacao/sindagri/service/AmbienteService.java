package br.com.votacao.sindagri.service;

import br.com.votacao.sindagri.enums.Ambiente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmbienteService {
  @Value("${ambiente.producao}")
  private boolean ambiente;
  
  public boolean isAmbienteDesenvolvimento() {
    return !ambiente;
  }
  
  public boolean isAmbienteProducao() {
    return ambiente;
  }
}
