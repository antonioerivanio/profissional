/*    */ package br.com.votacao.sindagri.domain;
/*    */ 
/*    */ import javax.persistence.Column;
/*    */ import javax.persistence.Entity;
/*    */ import javax.persistence.Id;
/*    */ import javax.persistence.JoinColumn;
/*    */ import javax.persistence.ManyToOne;
/*    */ import javax.persistence.Table;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Entity
/*    */ @Table(name = "PERMISSAO", schema = "sindagri")
/*    */ public class Permissao
/*    */ {
/*    */   @Id
/*    */   private Long id;
/*    */   @ManyToOne
/*    */   @JoinColumn(name = "SISTEMA")
/*    */   private Sistema sistema;
/*    */   @Column(name = "GRUPO")
/*    */   private Long grupo;
/*    */   @ManyToOne
/*    */   @JoinColumn(name = "SECAO")
/*    */   private Secao secao;
/*    */   
/*    */   public Long getId() {
/* 30 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 34 */     this.id = id;
/*    */   }
/*    */   
/*    */   public Sistema getSistema() {
/* 38 */     return this.sistema;
/*    */   }
/*    */   
/*    */   public void setSistema(Sistema sistema) {
/* 42 */     this.sistema = sistema;
/*    */   }
/*    */   
/*    */   public Long getGrupo() {
/* 46 */     return this.grupo;
/*    */   }
/*    */   
/*    */   public void setGrupo(Long grupo) {
/* 50 */     this.grupo = grupo;
/*    */   }
/*    */   
/*    */   public Secao getSecao() {
/* 54 */     return this.secao;
/*    */   }
/*    */   
/*    */   public void setSecao(Secao secao) {
/* 58 */     this.secao = secao;
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\domain\Permissao.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */