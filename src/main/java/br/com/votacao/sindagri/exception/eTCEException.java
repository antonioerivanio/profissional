/*    */ package br.com.votacao.sindagri.exception;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class eTCEException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8715912847813835227L;
/*    */   private List<String> messages;
/*    */   
/*    */   public eTCEException(Throwable throwable) {
/* 18 */     super(throwable);
/*    */   }
/*    */   
/*    */   public eTCEException(String message) {
/* 22 */     super(message);
/*    */   }
/*    */   
/*    */   public eTCEException(String message, Exception exception) {
/* 26 */     super(message, exception);
/*    */   }
/*    */   
/*    */   public eTCEException(List<String> messages) {
/* 30 */     this.messages = messages;
/*    */   }
/*    */   
/*    */   public List<String> getMessages() {
/* 34 */     return this.messages;
/*    */   }
/*    */   
/*    */   public void setMessages(List<String> messages) {
/* 38 */     this.messages = messages;
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\exception\eTCEException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */