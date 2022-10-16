package br.com.votacao.sindagri.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class teste {
	public static void main(String[] args) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		
		   String dataTermino = "15/10/2022 23:54";
	        LocalDateTime formatDateTime = LocalDateTime.parse(dataTermino, formatter);
	        
	        System.out.println("Data termino : " + dataTermino);
	        
		// Get current date time
		LocalDateTime now = LocalDateTime.now();

		String dataAtual = now.format(formatter);

		System.out.println("After : " + dataAtual);
		
		if(dataAtual.equals(dataTermino)) {
			System.out.println("Terminou");
		}

	}
	
	
	
}
