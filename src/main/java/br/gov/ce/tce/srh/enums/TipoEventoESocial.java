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
	S1070(10l),
	S1080(11l),
	S1200(12l),
	S1202(13l),
	S1207(14l),
	S1210(15l),
	S1250(16l),
	S1260(17l),
	S1270(18l),
	S1280(19l),
	S1295(20l),
	S1298(21l),
	S1299(22l),
	S1300(23l),
	S2200(24l),
	S2205(25l),
	S2206(26l),
	S2210(27l),
	S2220(28l),
	S2230(29l),
	S2240(30l),
	S2250(31l),
	S2260(32l),
	S2298(33l),
	S2299(34l),
	S2300(35l),
	S2306(36l),
	S2399(37l),
	S2400(38l),
	S3000(39l),
	S5001(40l),
	S5002(41l),
	S5011(42l),
	S5012(43l);
	
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
