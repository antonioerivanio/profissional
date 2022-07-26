package br.gov.ce.tce.srh.view;

/**
 * Contém os metodo da classe basica usada nos Controllers
 * @author erivanio.cruz
 *
 * @param <T>
 */
public interface ControllerViewCrudBase {  
 
  void consultar();
  
  void salvar();
  
  void salvar(boolean finalizar);   
}
