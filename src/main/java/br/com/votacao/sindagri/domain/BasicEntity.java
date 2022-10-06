package br.com.votacao.sindagri.domain;

public abstract class BasicEntity<TipoId> {
  public abstract TipoId getId();
  
  public abstract void setId(TipoId paramTipoId);
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    BasicEntity<TipoId> other = (BasicEntity<TipoId>)obj;
    if (getId() == null) {
      if (other.getId() != null)
        return false; 
    } else if (!getId().equals(other.getId())) {
      return false;
    } 
    return true;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((getId() == null) ? 0 : getId().hashCode());
    return result;
  }
}
