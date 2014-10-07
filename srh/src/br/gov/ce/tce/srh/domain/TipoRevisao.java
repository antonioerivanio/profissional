package br.gov.ce.tce.srh.domain;

public enum TipoRevisao {
	INCLUSAO(1L,"Inclusão"), ALTERACAO(2L,"Alteração"), EXCLUSAO(3L,"Exclusão");
	
	private TipoRevisao(Long id, String descricao) {
		this.setId(id);
		this.descricao = descricao;
	}
	
	private Long id;
	private String descricao;
	
	
	public static TipoRevisao getById(Long id){
		for (TipoRevisao tipoRevisao : TipoRevisao.values()) {
			if(tipoRevisao.getId().equals(id)){
				return tipoRevisao;
			}
		}
		return null;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}