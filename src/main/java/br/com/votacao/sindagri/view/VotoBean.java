package br.com.votacao.sindagri.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.domain.Voto;
import br.com.votacao.sindagri.enums.TipoVoto;
import br.com.votacao.sindagri.service.AuthenticationService;
import br.com.votacao.sindagri.service.UsuarioService;
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

	private TipoVoto tipoVotoEscolhido;

	@Autowired
	VotoService service;

	public void salvar() {		
		try {
			Usuario usuario = loginBean.getUsuarioLogado();
	
			Voto voto = new Voto(tipoVotoEscolhido, usuario);
			service.salvar(voto);
		} catch (Exception e) {
			logger.error("Erro na votação", e);
			FacesUtil.addErroMessage(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		loginBean.logout();
	}

}
