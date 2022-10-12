package br.com.votacao.sindagri.auditoria;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.hibernate.envers.RevisionListener;

public class AuditListener implements RevisionListener { 
	   @Override
 public void newRevision(Object revisionEntity) {    	
 	AuditEntity revEntity = (AuditEntity) revisionEntity;  
 	revEntity.setUsuario("erivanio");
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