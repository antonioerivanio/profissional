package br.gov.ce.tce.srh.to;

public class TempoServico {
	
	private String nome;
	private String matricula;
	private Long totalDias;
	private Long ano;
	private Long mes;
	private Long dia;
		
	public TempoServico(String nome, String matricula, Long totalDias, Long ano, Long mes, Long dia) {
		this.nome = nome;
		this.matricula = matricula;
		this.totalDias = totalDias;
		this.ano = ano;
		this.mes = mes;
		this.dia = dia;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Long getTotalDias() {
		return totalDias;
	}
	public void setTotalDias(Long totalDias) {
		this.totalDias = totalDias;
	}
	public Long getAno() {
		return ano;
	}
	public void setAno(Long ano) {
		this.ano = ano;
	}
	public Long getMes() {
		return mes;
	}
	public void setMes(Long mes) {
		this.mes = mes;
	}
	public Long getDia() {
		return dia;
	}
	public void setDia(Long dia) {
		this.dia = dia;
	}
	
	

}
