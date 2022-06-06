package br.gov.ce.tce.srh.view;

/**
 * Contém as classes basicas usada nos Controllers
 * @author erivanio.cruz
 *
 * @param <T>
 */
public interface ControllerViewCrudBase {  
 
  void consultar();
  
  String editar();
  
  void excluir();
  
  void salvar();
  
  void salvar(boolean finalizar);   
}
