 package br.com.votacao.sindagri.exception;
 
 public class UsuarioException
   extends RuntimeException {
   private static final long serialVersionUID = 4826710407790249131L;
   
   public UsuarioException(String msg) {
    super(msg);
   }
 }
