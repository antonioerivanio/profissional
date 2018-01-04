package br.gov.ce.tce.srh.alerta;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.service.FeriadoServiceImpl;
import br.gov.ce.tce.srh.service.FeriasService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service
public class EmissorDeAlerta {
	
	@Autowired
	private FeriasService feriasService;
	
	@Autowired
	private FeriadoServiceImpl feriadoService;
	
	
	@Scheduled(cron="0 0 7 * * ?")
	public void alertarFerias() {		
	
		alertaDeFerias20DiasAntes();
		alertaDeFerias1diaUtilAntes();
		
	}	
	
	public void alertaDeFerias20DiasAntes() {
		
		Date data = this.addDias(new Date(), 20);		
		
		List<Ferias> feriasList = feriasService.findByInicioETipo(data, Arrays.asList(1L, 3L));
		
		for (Ferias ferias : feriasList) {
			
			// Se não for férias de membros
			if(ferias.getFuncional().getOcupacao().getTipoOcupacao().getId() != 1L) {
				this.emitirAlertaServidor(ferias);
			}
		
		}		
		
	}	
	
	public void alertaDeFerias1diaUtilAntes() {
		
		Date hoje = SRHUtils.removeHorasDaData(new Date());
		
		if (feriadoService.isDiaUtil(hoje)) {			

			List<Ferias> feriasList = feriasService.findByInicioETipo(this.addDias(hoje, 11), this.addDias(hoje, 20), Arrays.asList(1L, 3L));
			
			for (Ferias ferias : feriasList) {
				
				// Se não for férias de membros
				if(ferias.getFuncional().getOcupacao().getTipoOcupacao().getId() != 1L) {
					
					Date dataDoAlerta = this.dataDoAlerta(ferias.getInicio());
					
					if (dataDoAlerta.getTime() == hoje.getTime() ) {					
						this.emitirAlertaServidor(ferias);						
					}	
				
				}				
			
			}
		
		}		
	}
	
	private void emitirAlertaServidor(Ferias ferias) {		
		
		EmissorDeEmail emissorDeEmail = new EmissorDeEmail();
		emissorDeEmail.setEmail("marcos@tce.ce.gov.br");
		emissorDeEmail.setAssunto("Alerta de início de férias");
		
		emissorDeEmail.setMensagem(
				"Atenção " + ferias.getFuncional().getPessoal().getNomeCompleto()
				+ ", verificamos que existe um período férias cadastrado no Sistema de Recursos Humanos que se iniciará em " + SRHUtils.formataData("dd/MM/yyyy", ferias.getInicio())
				+ ". Lembramos que conforme Resolução Administrativa 08/2017 o prazo para solicitações de alterações de férias por parte do servidor,"
				+ " será de até 10 dias antes do início das férias, portanto "  + SRHUtils.formataData("dd/MM/yyyy", this.addDias(ferias.getInicio(), -10)) 
				+ " <br><br><br>\"Email gerado automaticamente, favor não responder. Para quaisquer esclarecimentos entre em contato com a Gerência de Atos Funcionais.\"" );
		
		emissorDeEmail.enviarEmail();
		
	}	
	
	private Date addDias (Date data, int dias) {
		
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
