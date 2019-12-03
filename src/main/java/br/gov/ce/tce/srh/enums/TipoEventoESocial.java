package br.gov.ce.tce.srh.enums;

public enum TipoEventoESocial {
	
	S1000(1000l),
	S1005(1005l),
	S1010(1010l),
	S1020(1020l),
	S1030(1030l),
	S1035(1035l),
	S1040(1040l),
	S1050(1050l),
	S1060(1060l),
	S1070(1070l),
	S1080(1080l),
	S1200(1200l),
	S1202(1202l),
	S1207(1207l),
	S1210(1210l),
	S1250(1250l),
	S1260(1260l),
	S1270(1270l),
	S1280(1280l),
	S1295(1295l),
	S1298(1298l),
	S1299(1299l),
	S1300(1300l),
	S2200(2200l),
	S2205(2205l),
	S2206(2206l),
	S2210(2210l),
	S2220(2220l),
	S2230(2230l),
	S2240(2240l),
	S2250(2250l),
	S2260(2260l),
	S2298(2298l),
	S2299(2299l),
	S2300(2300l),
	S2306(2306l),
	S2399(2399l),
	S2400(2400l),
	S3000(3000l),
	S5001(5001l),
	S5002(5002l),
	S5011(5011l),
	S5012(5012l);
	
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
