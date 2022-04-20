package br.gov.ce.tce.srh.converter;

import java.text.ParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Converter para criar mascara para valores de CNPJ.
 *
 */
public class CNPJConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return arg2;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {

		if(arg2 == null){  
            return "";  
        }

		try {
			return SRHUtils.formatarCNPJ((SRHUtils.removerMascara(arg2.toString())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
}
