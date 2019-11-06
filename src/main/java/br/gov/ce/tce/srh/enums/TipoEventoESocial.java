package br.gov.ce.tce.srh.enums;

public enum TipoEventoESocial {
	
	S1000(1l),
	S1005(2l),
	S1010(3l),
	S1020(4l),
	S1030(5l),
	S1035(6l),
	S1040(7l),
	S1050(8l),
	S1060(9l),
	S1070(10l);
	
	private Long codigo;

	private TipoEventoESocial(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
}
