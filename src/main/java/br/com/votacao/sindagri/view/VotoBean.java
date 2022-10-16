package br.com.votacao.sindagri.view;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.domain.Voto;
import br.com.votacao.sindagri.enums.TipoVoto;
import br.com.votacao.sindagri.service.VotoService;
import br.com.votacao.sindagri.util.FacesUtil;
import lombok.Data;

@Data
@Component("votoBean")
@Scope("session")
public class VotoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	LoginBean loginBean;
	static Logger logger = Logger.getLogger(VotoBean.class);

	private List<TipoVoto> tiposVotos = Arrays.asList(TipoVoto.values());

	private List<Voto> todosVotos;

	private TipoVoto tipoVotoEscolhido;

	@PostConstruct
	private void init() {
		try {
			todosVotos = getResultadoVotos();

			calcularTotal();
		} catch (Exception e) {
			logger.error("Erro na votação", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Autowired
	VotoService service;
	private int total;

	public String salvar() {
		try {
			Usuario usuario = loginBean.getUsuarioLogado();

			if (usuario == null) {
				throw new Exception("Usuário precisa se autenticar para poder votar!");
			}

			Voto voto = new Voto(tipoVotoEscolhido, usuario, Calendar.getInstance());
			service.salvar(voto);
		} catch (Exception e) {
			logger.error("Erro na votação", e);
			FacesUtil.addErroMessage(e.getMessage());
			// this.authenticationService.logout();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return loginBean.sair();
	}

	public List<Voto> getResultadoVotos() throws Exception {
		return service.findAll();
	}

	private void calcularTotal() {
		int votoChapa = 0;
		int votoBranco = 0;
		int votoNulo = 0;
		int qtd = 0;
		for (Voto v : todosVotos) {		
			if (v.getTipo().equals(TipoVoto.ACHAPA_NAO_PARA)) {			
 				votoChapa += 1;
				v.setQuantidadeChapa(votoChapa);
			} else if (v.getTipo().equals(TipoVoto.BRANCO)) {
				votoBranco += 1;
				v.setQuantidadeBranco(votoBranco);
			} else {
				votoNulo += 1;
				v.setQuantidadeNulo(votoNulo);
			}
			
			v.setQuantidade(1);
		}

		total = votoChapa + votoBranco + votoNulo;
	}
}
