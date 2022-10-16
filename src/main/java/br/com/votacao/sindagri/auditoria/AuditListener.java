package br.com.votacao.sindagri.auditoria;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.hibernate.envers.RevisionListener;

public class AuditListener implements RevisionListener {
	@Override
	public void newRevision(Object revisionEntity) {

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		String matriculaUsuario = (String) session.getAttribute("usuario");
		
		AuditEntity revEntity = (AuditEntity) revisionEntity;
		revEntity.setUsuario(matriculaUsuario);
		InetAddress IP;
		try {
			IP = InetAddress.getLocalHost();
			revEntity.setIp(IP.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}