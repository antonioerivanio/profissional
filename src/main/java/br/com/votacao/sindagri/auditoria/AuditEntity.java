package br.com.votacao.sindagri.auditoria;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import lombok.Data;

@Data
@Entity(name="revinfo_cust")
@RevisionEntity(AuditListener.class)
public class AuditEntity implements Serializable{
  
 private static final long serialVersionUID = 1L;
  
	
 @Id
 @GeneratedValue
 @RevisionNumber
 private int id;

 @RevisionTimestamp
 private long timestamp;
 
 private  String usuario;
 private String ip;
  //gets e sets omitidos   
}