package br.gov.ce.tce.srh.alerta;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.service.FeriadoServiceImpl;
import br.gov.ce.tce.srh.service.FeriasService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Component
public class EmissorDeAlerta {

	static Logger logger = Logger.getLogger(EmissorDeAlerta.class);

	@Autowired
	private EmissorDeEmail emissorDeEmail;
	
	@Autowired
	private FeriasService feriasService;

	@Autowired
	private FeriadoServiceImpl feriadoService;
	
	

	public void alertarFerias() {

		if (SRHUtils.sistemaApontandoParaProducao()) {

			logger.info("Rotina de alerta de férias executada às " + SRHUtils.formataData("dd/MM/yyyy HH:mm:ss", new Date()));

			alertaDeFerias20DiasAntes();
			alertaDeFerias1diaUtilAntes();

		}	

	}

	public void alertaDeFerias20DiasAntes() {

		Date data = this.addDias(new Date(), 20);

		List<Ferias> feriasList = feriasService.findByInicioETipo(data, Arrays.asList(1L, 3L));

		for (Ferias ferias : feriasList) {

			// Se não for férias de exonerados nem de membros
			if (ferias.getFuncional().getSaida() == null
					&& ferias.getFuncional().getOcupacao().getTipoOcupacao().getId() != 1L) {
				this.emitirAlertaServidor(ferias);
			}

		}

	}

	public void alertaDeFerias1diaUtilAntes() {

		Date hoje = SRHUtils.getHoje();

		if (feriadoService.isDiaUtil(hoje)) {

			List<Ferias> feriasList = feriasService.findByInicioETipo(this.addDias(hoje, 11), this.addDias(hoje, 20),
					Arrays.asList(1L, 3L));

			for (Ferias ferias : feriasList) {

				// Se não for férias de exonerados nem de membros
				if (ferias.getFuncional().getSaida() == null
						&& ferias.getFuncional().getOcupacao().getTipoOcupacao().getId() != 1L) {

					Date dataDoAlerta = this.dataDoAlerta(ferias.getInicio());

					if (dataDoAlerta.getTime() == hoje.getTime()) {
						this.emitirAlertaServidor(ferias);
					}

				}

			}

		}
	}

	private void emitirAlertaServidor(Ferias ferias) {

		String email = ferias.getFuncional().getPessoal().getEmail();

		if (email != null && !email.isEmpty()) {

			Map<String, String> parametrosTemplate = new HashMap<String, String>();
			parametrosTemplate.put("nomePessoal", ferias.getFuncional().getPessoal().getNomeCompleto());
			parametrosTemplate.put("inicioFerias", SRHUtils.formataData("dd/MM/yyyy", ferias.getInicio()));
			parametrosTemplate.put("ultimoDiaAlteracao",
					SRHUtils.formataData("dd/MM/yyyy", this.addDias(ferias.getInicio(), -10)));
			
			emissorDeEmail.setEmail(email);
			emissorDeEmail.setAssunto("Alerta de início de férias");

			emissorDeEmail.preencherTemplateEmail("alerta-ferias.txt", parametrosTemplate);

			emissorDeEmail.enviarEmail();
		}

	}

	private Date addDias(Date data, int dias) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, dias);

		return calendar.getTime();
	}

	private Date dataDoAlerta(Date data) {

		Date dataDoAlerta = this.addDias(data, -10);
		return feriadoService.getProximoDiaUtil(dataDoAlerta, -1);

	}

}
