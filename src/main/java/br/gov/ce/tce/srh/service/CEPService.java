package br.gov.ce.tce.srh.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.to.CEP;

@Service
public class CEPService {

	public CEP buscarCep(String cep) throws IOException {

		CEP cepJson = null;

		try {
			
			URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
	
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
	
			ObjectMapper mapper = new ObjectMapper();
			cepJson = mapper.readValue(readAll(br), CEP.class);
			
		} catch (IOException e) {
			throw new SRHRuntimeException("CEP não encontrado");
		}
		
		return cepJson;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}	

}
