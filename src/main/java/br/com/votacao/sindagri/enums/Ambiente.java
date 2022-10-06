/*    */ package br.com.votacao.sindagri.enums;
/*    */ 
/*    */ public enum Ambiente
/*    */ {
/*  5 */   DEV("Ambiente de Desenvolvimento"),
/*  6 */   HOM("Ambiente de Homologação"),
/*  7 */   PROD("Ambiente de Produção");
/*    */   
/*    */   private String descricao;
/*    */   
/*    */   Ambiente(String descricao) {
/* 12 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public boolean isDesenvolvimento() {
/* 16 */     return equals(DEV);
/*    */   }
/*    */   
/*    */   public boolean isHomologacao() {
/* 20 */     return equals(HOM);
/*    */   }
/*    */   
/*    */   public boolean isProducao() {
/* 24 */     return equals(PROD);
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 28 */     return this.descricao;
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\enums\Ambiente.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */