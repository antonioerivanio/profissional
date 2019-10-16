package br.gov.ce.tce.srh.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class VigenciaConverter implements Converter {

	public static final SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");


	/**
	 * Converte uma String no formato MM/yyyy para um objeto Date.
	 */
	@Override
	public Object getAsObject(FacesContext fc, UIComponent ui, String value) {

		try {
			return ( value == null || "".equals(value) || "  /    ".equals(value) ) ? null : df.parse(value);
		} catch (Exception e) {
			throw new ConverterException("Erro na conversão da data: " + value);
		}
	}


	/**
	 * Converte um  objeto Date para uma String no formato MM/yyyy.
	 */
	@Override
	public String getAsString(FacesContext fc, UIComponent ui, Object value) {

		if(value == null || "".equals(value))
			return null;

		if( !(value instanceof Date )){
			throw new ConverterException(value + "deve ser do tipo java.util.Date" );
		}

		return df.format(value);
	}

}
