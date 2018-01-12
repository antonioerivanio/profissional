package br.gov.ce.tce.srh.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.FeriadoDAO;
import br.gov.ce.tce.srh.domain.Feriado;

@Service("feriadoService")
public class FeriadoServiceImpl {
	
	@Autowired
	private FeriadoDAO feriadoDAO;
	
	
	public boolean isDiaUtil(Date data) {
		
		boolean isDiaUtil = false;
		
		Calendar dataCalendar = new GregorianCalendar();
		dataCalendar.setTime(data);
		
		if(dataCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && dataCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			
			Feriado feriado = feriadoDAO.findByData(data);
			
			if (feriado == null)
				isDiaUtil = true;			
		}		
		
		return isDiaUtil;
		
	}
	
	public Date getProximoDiaUtil(Date data, int incremento) {
		
		Date proximoDiaUtil = null;
		
		Calendar proximoDiaUtilCalendar = new GregorianCalendar();
		proximoDiaUtilCalendar.setTime(data);		
		
		while (proximoDiaUtil == null) {
			
			proximoDiaUtilCalendar.add(Calendar.DAY_OF_MONTH, incremento);
			
			if(isDiaUtil(proximoDiaUtilCalendar.getTime()))
				proximoDiaUtil = proximoDiaUtilCalendar.getTime();			
							
		}		
		
		return proximoDiaUtil;
		
	}

}
