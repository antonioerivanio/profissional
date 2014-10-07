package br.gov.ce.tce.srh.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.text.MaskFormatter;

import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.ce.tce.srh.domain.sca.Usuario;

/**
 * @author robson.castro
 *
 */
public class SRHUtils {
	
	public static final String FORMATO_DATA = "dd/MM/yyyy"; 
	
	/**
	 * M�todo respons�vel por adicionar ou remover n�mero de dias de uma data
	 * Obs.: sinal negativo no par�metro qtdDias ir� fazer com que seja removido o n�mero de dias informado,
	 * caso contr�rio, ser� adicionado a mesma, gerando uma nova data.
	 * @param data informada
	 * @param qtdDias
	 * @return data alterada
	 */
	public static Date adiconarSubtrairDiasDeUmaData(Date data, int qtdDias) {
	     data = new Date(data.getTime()+((1000*24*60*60)*qtdDias));  
		return data;
	}
	
	/**
	 * M�todo respons�vel por retornar a data inicio do TCE 05/10/1935
	 * @return data inicio
	 */
	public static Date inicioTCE() {
		Calendar dtInicio = new GregorianCalendar();
		dtInicio.set(1935, 9, 5);
		return dtInicio.getTime();
	}

	/**
	 * M�todo respons�vel por retornar a data inicio do TCE 15/12/1998
	 * @return data inicio
	 */
	public static Date inicioDeducao() {
		Calendar dtInicio = new GregorianCalendar();
		dtInicio.set(1998, 11, 15);
		return dtInicio.getTime();
	}
	/**
	 * M�todo para comparar as das e retornar o numero de dias de diferen�a
	 * entre elas
	 * 
	 * @param dataLow 
	 * @param dataHigh
	 * 
	 * @return int
	 */
	public static int dataDiff(Date dataLow, Date dataHigh) {

		GregorianCalendar startTime = new GregorianCalendar();
		GregorianCalendar endTime = new GregorianCalendar();

		GregorianCalendar curTime = new GregorianCalendar();
		GregorianCalendar baseTime = new GregorianCalendar();

		startTime.setTime(dataLow);
		endTime.setTime(dataHigh);

		int dif_multiplier = 1;

		// Verifica a ordem de inicio das datas
		if (dataLow.compareTo(dataHigh) < 0) {
			baseTime.setTime(dataHigh);
			curTime.setTime(dataLow);
			dif_multiplier = 1;
		} else {
			baseTime.setTime(dataLow);
			curTime.setTime(dataHigh);
			dif_multiplier = -1;
		}

		int result_years = 0;
		int result_months = 0;
		int result_days = 0;

		// Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import
		// acumulando
		// no total de dias. Ja leva em consideracao ano bissesto
		while (curTime.get(GregorianCalendar.YEAR) < baseTime
				.get(GregorianCalendar.YEAR)
				|| curTime.get(GregorianCalendar.MONTH) < baseTime
						.get(GregorianCalendar.MONTH)) {

			int max_day = curTime
					.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			result_months += max_day;
			curTime.add(GregorianCalendar.MONTH, 1);

		}

		// Marca que � um saldo negativo ou positivo
		result_months = result_months * dif_multiplier;

		// Retirna a diferenca de dias do total dos meses
		result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime
				.get(GregorianCalendar.DAY_OF_MONTH));

		return result_years + result_months + result_days + 1;
	}
	
	/**
	 * M�todo para comparar as das e retornar o numero de dias de diferen�a
	 * entre elas
	 * 
	 * @param dataLow 
	 * @param dataHigh
	 * 
	 * @return int
	 */
	public static int diffData(Date dataLow, Date dataHigh) {

		GregorianCalendar startTime = new GregorianCalendar();
		GregorianCalendar endTime = new GregorianCalendar();

		//GregorianCalendar curTime = new GregorianCalendar();
		//GregorianCalendar baseTime = new GregorianCalendar();

		startTime.setTime(dataLow);
		endTime.setTime(dataHigh);
		
		int anoStart = startTime.get(GregorianCalendar.YEAR);
		int mesStart = startTime.get(GregorianCalendar.MONTH);
		int diaStart = startTime.get(GregorianCalendar.DAY_OF_MONTH);
		
		int anoEnd = endTime.get(GregorianCalendar.YEAR);
		int mesEnd = endTime.get(GregorianCalendar.MONTH);
		int diaEnd = endTime.get(GregorianCalendar.DAY_OF_MONTH);
		
		int totalAnos = 0;
		int totalMes = 0;
		int totalDias = 0;
		
		if (mesEnd < mesStart)
		{
			totalAnos = (anoEnd - anoStart -1);
			totalMes = (mesEnd + 12 - mesStart);
			
			if(diaEnd < diaStart)
				totalDias = (diaEnd + 30 - diaStart) + (totalMes-1)*30 + totalAnos*365;
			else
				totalDias = (diaEnd - diaStart) + totalMes*30 + totalAnos*365;
		}
		else
		{
			totalAnos = (anoEnd - anoStart);
			totalMes = (mesEnd - mesStart);
			
			if(diaEnd < diaStart)
				totalDias = (diaEnd + 30 - diaStart) + (totalMes-1)*30 + totalAnos*365;
			else
				totalDias = (diaEnd - diaStart) + totalMes*30 + totalAnos*365;
		}

		

		return totalDias;
	}
	
	 /**
	  * Formata uma data de acordo com o par�metro passado
	  * Ex. par�metro formato: dd/MM/yyyy, dd/MM/yyyy HH:mm:ss, dd/MM
	  * @param formato
	  * @param data
	  * @return data formatada
	  */
	 public static String formataData(String formato, Date data) {
		 
		 SimpleDateFormat sdf = new SimpleDateFormat(formato);
		 
		 return sdf.format(data);
		 
	 }
	
	/** 
	 * Metodo que valida CPF.
	 * @return Boolean 
	 */  
	public static boolean validarCPF(String cpf) { 

		 cpf = removerMascara(cpf);
		
		int d1, d2, digito1, digito2, resto, digitoCPF;  
		String  nDigResult;  
		
		d1 = d2 = digito1 = digito2 = resto = 0;  
		
		for (int nCount = 1; nCount < cpf.length() -1; nCount++)  
		{  
			digitoCPF = Integer.valueOf (cpf.substring(nCount -1, nCount)).intValue();  
			//multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.  
			d1 = d1 + ( 11 - nCount ) * digitoCPF;  
			//para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.  
			d2 = d2 + ( 12 - nCount ) * digitoCPF;  
		};
		
		//Primeiro resto da divis�o por 11.  
		resto = (d1 % 11);
		
		//Se o resultado for 0 ou 1 o digito � 0 caso contr�rio o digito � 11 menos o resultado anterior.  
		if (resto < 2)  
			digito1 = 0;  
		else  
			digito1 = 11 - resto;  
		
		d2 += 2 * digito1;  
		
		//Segundo resto da divis�o por 11.  
		resto = (d2 % 11);  
		
		//Se o resultado for 0 ou 1 o digito � 0 caso contr�rio o digito � 11 menos o resultado anterior.  
		if (resto < 2)  
			digito2 = 0;  
		else  
			digito2 = 11 - resto;  
		
		//Digito verificador do CPF que est� sendo validado.  
		String nDigVerific = cpf.substring (cpf.length()-2, cpf.length());  
		
		//Concatenando o primeiro resto com o segundo.  
		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);  
		
		//comparar o digito verificador do cpf com o primeiro resto + o segundo resto.  
		return nDigVerific.equals(nDigResult);  
	} 
	
	/**
	 * M�todo que verifica se o D�gito Verificador de uma Ag�ncia Bradesco � v�lida, 
	 * na qual esta deve ser informada no seguinte padr�o: NNNN-D, onde N � um valor n�merico
	 * e D um valor n�merico que representa o d�gito verificador gerado a partir dos outros 4 
	 * n�meros informados antes do h�fen (-). 
	 * @param agencia
	 * @return Boolean
	 */
	public static Boolean validarDVAgenciaBradesco(String agencia){

		agencia = removerMascara(agencia);
		
		int soma = Integer.parseInt(agencia.substring(0, 1)) * 5 +
				   Integer.parseInt(agencia.substring(1, 2)) * 4 +
				   Integer.parseInt(agencia.substring(2, 3)) * 3 +
				   Integer.parseInt(agencia.substring(3, 4)) * 2 ;
		
		//calculando d�gito verificador
		int digitoVerificador = 11 - (soma % 11);
		
		//condi��o para d�gito verificador ser igual a zero
		if(digitoVerificador > 9)
			digitoVerificador = 0;
		
		if(digitoVerificador == Integer.parseInt(agencia.substring(4, 5))){
			return true;
		} 
		
		return false;
	}
	
	/**
	 * M�todo que verifica se o D�gito Verificador de uma Conta Corrente Bradesco � v�lida, 
	 * na qual esta deve ser informada no seguinte padr�o: NNNNNNN-D, onde N � um valor n�merico
	 * e D um valor n�merico que representa o d�gito verificador gerado a partir dos outros sete 
	 * n�meros informados antes do h�fen (-). 
	 * @param conta
	 * @return Boolean
	 */
	public static Boolean validarDVContaCorrenteBradesco(String conta){
		
		conta = removerMascara(conta);
		
		int soma, digitoPrincipal = 0, digitoSecundario = 0; 
		
		soma = Integer.parseInt(conta.substring(1, 2)) * 7 +
			    Integer.parseInt(conta.substring(2, 3)) * 6 +
				Integer.parseInt(conta.substring(3, 4)) * 5 +
				Integer.parseInt(conta.substring(4, 5)) * 4 +
				Integer.parseInt(conta.substring(5, 6)) * 3 +
				Integer.parseInt(conta.substring(6, 7)) * 2 ;
		
		//calculando d�gito verificador
		digitoSecundario = 11 - (soma % 11);
		
		//condi��o para d�gito verificador ser igual a zero
		if(digitoSecundario > 9)
			digitoSecundario = 0;
		
		//verifica se o d�gito verificador p equivale a zero
		if(conta.substring(7, 8).equals("0")){
			digitoSecundario = 0;
		} else {
			digitoPrincipal = Integer.parseInt(conta.substring(7, 8));
		}
		
		if(digitoSecundario == digitoPrincipal)
			return true;
		
		return false;
	}
	
	/**
	 * M�todo que valida o D�gito Verificado de uma matr�cula
	 * @param matricula
	 * @return
	 */
	public static boolean validarMatricula(String matricula) {
		int dv = 0;
		dv = ((Integer.valueOf(matricula.substring(0, 1)) * 8)
				+ (Integer.valueOf(matricula.substring(1, 2)) * 9)
				+ (Integer.valueOf(matricula.substring(2, 3)) * 8) 
				+ (Integer.valueOf(matricula.substring(3, 4)) * 7)) % 10;
		if (dv == Integer.parseInt(matricula.substring(5, 6)))
			return true;
		return false;
	}
	
	/**
	 * M�todo que pega qualquer valor monet�rio e converte para BigDecimal
	 */
	public static BigDecimal valorMonetarioStringParaBigDecimal(String valorMonetarioString) {
		String r = "";
		for (int i = 0; i < valorMonetarioString.length(); i++) {
			if (valorMonetarioString.charAt(i) != ',' && valorMonetarioString.charAt(i) != '.')
				r += valorMonetarioString.charAt(i);
		}
		return new BigDecimal(new BigDecimal(r).doubleValue()/100).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public static Boolean validarPisPasep(String pisPasep){
		
		String fTap = "3298765432";
		
		int total = 0, resto = 0;
		
		pisPasep = removerMascara(pisPasep);
		
		//verifica se a quantidade de caracteres que compoe o PIS/PASEP foram informados.
		if(pisPasep.length() != 11)
			return false;
		
		if(Double.parseDouble(pisPasep) == 0)
			return false;
		
		for (int i = 0; i < 10; i++) 
			total = total + Integer.parseInt(pisPasep.substring(i, i+1)) * Integer.parseInt(fTap.substring(i, i+1));
		
		//verificando o resto da divis�o
		resto = total % 11;
		
		if(resto != 0)
			resto = 11 - resto;
		
		if(resto != Integer.parseInt(pisPasep.substring(10, 11)))
			return false;
		
		return true;
	}
	
	/**
	 * M�todo que verifica o D�gito Verificador do Processo
	 * @param processo
	 * @return
	 */
	public static boolean validarProcesso(String processo) {
		int soma;
		int digito, resto;
		int digitoProcesso;
		String nDigResult;

		soma = digito = resto = 0;

		for (int posicao = 1; posicao <= processo.length() - 1; posicao++) {
			digitoProcesso = Integer.valueOf(processo.substring(posicao - 1, posicao)).intValue();
			soma = soma + (11 - posicao) * digitoProcesso;
		}

		// Resto da divis�o por 11.
		resto = (soma % 11);

		// se resto = 10 entao digito = 0 (if dig 10 Then dig = 0)
		if (resto == 10)
			digito = 0;
		else
			digito = resto;

		// Digito verificador do processo que est� sendo validado.
		String nDigVerific = processo.substring(processo.length() - 1,
				processo.length());

		nDigResult = String.valueOf(digito);

		// comparar o digito verificador do processo com o resto
		return nDigVerific.equals(nDigResult);
	}
	
	public static String removerAcentos(String string) {  
	    CharSequence cs = new StringBuilder(string == null ? "" : string);  
	    return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");  
	}
	
	public static String removerMascara(String string) {
		string = string.replace(".", "");
		string = string.replace("-", "");
		string = string.replace("/", "");
		string = string.replace("(", "");
		string = string.replace(")", "");
		return string;
	}
	
	public static String formatarCPF(String texto) throws ParseException {
		MaskFormatter mf = new MaskFormatter("###.###.###-##");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);  		
	}
	
	public static String formatarMatricula(String texto) throws ParseException {
		MaskFormatter mf = new MaskFormatter("####-#");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);  		
	}
	
	public static String formatarProcesso(String texto) throws ParseException {
		MaskFormatter mf = new MaskFormatter("#####/####-##");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);  		
	}
	
	/**
	 * Obt�m o usu�rio logado na sess�o
	 * @return
	 */
	public static Usuario getUsuarioLogado() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return null;
		}
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	//Codigo 0 - Formatar padr�o SAP, caso contr�rio, Retira do padrao SAP e joga no Padr�o exibido ao us�rio
	public static String formatatarDesformatarNrProcessoPadraoSAP(String nrProcesso, int codigoAcao) {
		nrProcesso = removerMascara(nrProcesso);
		if(codigoAcao == 0){
			return nrProcesso.substring(5, 9) + nrProcesso.substring(0, 5) + nrProcesso.substring(9, 10) ;
		} else {
			return nrProcesso.substring(4, 9) + nrProcesso.substring(0, 4) + nrProcesso.substring(9, 10) ; 
		}
	}

}
