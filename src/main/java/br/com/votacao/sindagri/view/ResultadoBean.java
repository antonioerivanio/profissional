package br.com.votacao.sindagri.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.domain.Voto;
import br.com.votacao.sindagri.service.AuthenticationService;
import br.com.votacao.sindagri.service.UsuarioService;
import br.com.votacao.sindagri.util.TextExporter;
import lombok.Data;

@Data
@Component("resultadoBean")
@Scope("session")
public class ResultadoBean implements Serializable {
  private static final long serialVersionUID = 1L;
  

  static Logger logger = Logger.getLogger(ResultadoBean.class);
  
  @Autowired
  private AuthenticationService authenticationService;
  
  @Autowired
  private UsuarioService usuarioService;
  private Exporter<DataTable> textExporter;
  private Usuario usuario = new Usuario("Erivanio");
  private Usuario usuario2 = new Usuario("Paulo");
  private Usuario usuario3 = new Usuario("André");
  private String voto;
  private Long totalValidos=48l;
  private List<Voto> resultados= new ArrayList<>();
	/*
	 * Voto voto1 = new Voto(1, "Chapa \"A luta não pode parar\"", usuario,48); Voto
	 * voto2 = new Voto(2, "Branco", usuario,5); Voto voto3 = new Voto(3, "Nulo",
	 * usuario,6);
	 * 
	 * {
	 * 
	 * resultados.add(voto1); resultados.add(voto2); resultados.add(voto3);
	 * 
	 * textExporter = new TextExporter(); }
	 */
  
  
  public String salvar() {
	 
	  return null;
  }

  
  
}
