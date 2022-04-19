package br.gov.ce.tce.srh.converter;

import java.util.Objects;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.view.AfastamentoFormBean;

/**
 * @author erivanio.cruz
 * Converter 
 *
 */
@FacesConverter(value = "afastamentoConverter")
public class AfastamentoConverter implements Converter {

	@Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String id) {
        ValueExpression vex =
                ctx.getApplication().getExpressionFactory()
                        .createValueExpression(ctx.getELContext(),
                                "#{afastamentoFormBean}", AfastamentoFormBean.class);

        AfastamentoFormBean afastamentoFormBean = (AfastamentoFormBean)vex.getValue(ctx.getELContext());
        
        for(AfastamentoESocial afastamentoESocial: afastamentoFormBean.getAfastamentoESocialList()) {
        	if(afastamentoESocial.getId().equals(Long.valueOf(id))) {
        		return afastamentoESocial;
        	}
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
    	if(Objects.isNull(object))
    		return null;
    	
        return ((AfastamentoESocial) object).getId().toString();
    	
    }
}
