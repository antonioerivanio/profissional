package br.gov.ce.tce.srh.service.sca;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.ce.tce.srh.domain.Revisao;
import br.gov.ce.tce.srh.domain.sca.Usuario;


public class RevisaoListener implements RevisionListener {

	@Override
	public void newRevision(Object revisaoObj) {
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			Revisao revisao = (Revisao) revisaoObj;
			revisao.setUsuario((Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
	}

}
