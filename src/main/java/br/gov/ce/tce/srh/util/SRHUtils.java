package br.gov.ce.tce.srh.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MaskFormatter;

import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public class SRHUtils {
	
	public static final String FORMATO_DATA = "dd/MM/yyyy";
	
	public static final String FORMATO_DATA_HORA = "dd/MM/yyyy HH:mm:ss";
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
	
	/**
	 * Método responsável por adicionar ou remover número de dias de uma data
	 * Obs.: sinal negativo no parâmetro qtdDias irá fazer com que seja removido o número de dias informado,
	 * caso contrário, será adicionado a mesma, gerando uma nova data.
	 * @param data informada
	 * @param qtdDias
	 * @return data alterada
	 */
	public static Date adiconarSubtrairDiasDeUmaData(Date data, int qtdDias) {
	     data = new Date(data.getTime()+((1000*24*60*60)*qtdDias));  
		return data;
	}
	
	/**
	 * Método responsável por retornar a data inicio do TCE 05/10/1935
	 * @return data inicio
	 */
	public static Date inicioTCE() {
		Calendar dtInicio = new GregorianCalendar();
		dtInicio.set(1935, 9, 5);
		return removeHorasDaData(dtInicio.getTime());
	}
	
	/**
	 * Método responsável por retornar a data de extinção do TCM 21/08/2017
	 * @return data extinção TCM
	 */
	public static Date extincaoTCM() {
		Calendar dtInicio = new GregorianCalendar();
		dtInicio.set(2017, 7, 21);
		return removeHorasDaData(dtInicio.getTime());
	}

	/**
	 * Método responsável por retornar a data a partir da qual não 
	 * se deduz faltas do tempo de serviço
	 * @return data inicio
	 */
	public static Date fimDeducaoDeFaltas() {
		Calendar dtFim = new GregorianCalendar();
		dtFim.set(1998, 11, 16);
		return dtFim.getTime();
	}
	/**
	 * Método para comparar duas datas e retornar o número de dias de diferença
	 * entre elas. Leva em consideração ano bissexto 
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
		// no total de dias. Ja leva em consideracao ano bissexto
		while (curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR)
				|| curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)) {

			int max_day = curTime.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			result_months += max_day;
			curTime.add(GregorianCalendar.MONTH, 1);

		}

		// Marca que é um saldo negativo ou positivo
		result_months = result_months * dif_multiplier;

		// Retorna a diferenca de dias do total dos meses
		result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));

		return result_years + result_months + result_days + 1;
	}
	
	/**
	 * Método com cálculo específico para Averbação que compara duas datas e retorna o numero de dias de diferença
	 * entre elas
	 * 
	 * @param dataLow 
	 * @param dataHigh
	 * 
	 * @return int
	 */
	public static int dataDiffAverbacao(Date dataLow, Date dataHigh) {

		GregorianCalendar startTime = new GregorianCalendar();
		GregorianCalendar endTime = new GregorianCalendar();

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
		
		if (mesEnd < mesStart){
			
			totalAnos = (anoEnd - anoStart - 1);
			totalMes = (mesEnd + 12 - mesStart);
			
			if(diaEnd < diaStart)
				totalDias = (diaEnd + 30 - diaStart) + (totalMes - 1) * 30 + totalAnos * 365;
			else
				totalDias = (diaEnd - diaStart) + totalMes * 30 + totalAnos * 365;
		
		}else{
			
			totalAnos = (anoEnd - anoStart);
			totalMes = (mesEnd - mesStart);
			
			if(diaEnd < diaStart)
				totalDias = (diaEnd + 30 - diaStart) + (totalMes - 1) * 30 + totalAnos * 365;
			else
				totalDias = (diaEnd - diaStart) + totalMes * 30 + totalAnos * 365;
		
		}		

		return totalDias + 1;
	}
	
	
	/**
	 * Método que compara duas datas e retorna o número de anos, meses e dias entre elas.
	 * O cálculo feito no método produz um resultado próximo ao demonstrativo da simulação do
	 * cálculo do tempo de contribuição do INSS
	 * 
	 * @param dataInicio 
	 * @param dataFim
	 * 
	 * @return int[]
	 */
	public static int[] contagemDeTempoDeServico(Date dataInicio, Date dataFim) {

		int anos = 0, meses = 0, dias = 0;
		
		GregorianCalendar inicio = new GregorianCalendar();
		GregorianCalendar fim = new GregorianCalendar();		

		inicio.setTime(dataInicio);
		fim.setTime(dataFim);

		int anoInicio = inicio.get(Calendar.YEAR);
		int mesInicio = inicio.get(Calendar.MONTH) + 1;
		int diaInicio = inicio.get(Calendar.DAY_OF_MONTH);
		int anoFim = fim.get(Calendar.YEAR);
		int mesFim = fim.get(Calendar.MONTH) + 1;
		int diaFim = fim.get(Calendar.DAY_OF_MONTH);		

		// Caso Especial: Data final corresponde ao último dia de fevereiro e data inicial começa no dia 1º
		if ( diaInicio == 1 && mesFim == 2 && diaFim >= 28){
			diaFim = 30;
		}	
		
		if (diaFim < diaInicio) {
			diaFim += 30;
			mesFim --;
		}

		dias = diaFim - diaInicio + 1;
		
		if( dias >= 30 ){
			dias = 0;
			meses++;
		}		
		
		if (mesFim < mesInicio) {
			mesFim += 12;
			anoFim --;
		}

		meses += mesFim - mesInicio;

		if(meses == 12){
			meses = 0;
			anos ++;
		}
		
		anos += anoFim - anoInicio;	
		

		int[] retorno = {anos, meses, dias};
		
		return retorno;
	}	
	
	public static int calculaQtdeDias ( int[] anosMesesDias) {
		return 365*anosMesesDias[0] + 30*anosMesesDias[1] + anosMesesDias[2];		
	}		
	
	
	public static long[] anosMesesDias(long qtdDias) {

		long anos, meses, dias;

		anos = qtdDias / 365;
		
		meses = (qtdDias - (365 * anos)) / 30;
		
		dias =  (qtdDias - (365 * anos) - (30 * meses));						

		long[] retorno = {anos, meses, dias};
		
		return retorno;
	}
	
	
	 /**
	  * Formata uma data de acordo com o parâmetro passado
	  * Ex. parâmetro formato: dd/MM/yyyy, dd/MM/yyyy HH:mm:ss, dd/MM
	  * @param formato
	  * @param data
	  * @return data formatada
	  */
	 public static String formataData(String formato, Date data) {		 
		 SimpleDateFormat sdf = new SimpleDateFormat(formato);		 
		 return sdf.format(data);		 
	 }
	 
	 public static Date removeHorasDaData(Date data) {		 
		 SimpleDateFormat sdf = new SimpleDateFormat(SRHUtils.FORMATO_DATA);
		 try {
			return sdf.parse(sdf.format(data));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		 
	 }
	 
	 public static Date getHoje() {
		 return removeHorasDaData(new Date());
	 }
	 
	 public static Date getAgora() {
		 return new Date();
	 }
	 
	 public static Date getDataHoraAtual() {	
		 Date dataHoraAtual = new Date();
		 String datahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHoraAtual);
		 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
					 
		 try {
			 Date dataFormatada = formato.parse(datahora); 
			return dataFormatada;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		 
	 }
	 
	 public static int getAnoCorrente() {		 
		GregorianCalendar hoje = new GregorianCalendar();		
		hoje.setTime(new Date());

		return hoje.get(Calendar.YEAR);		 
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
		
		//Primeiro resto da divisão por 11.  
		resto = (d1 % 11);
		
		//Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
		if (resto < 2)  
			digito1 = 0;  
		else  
			digito1 = 11 - resto;  
		
		d2 += 2 * digito1;  
		
		//Segundo resto da divisão por 11.  
		resto = (d2 % 11);  
		
		//Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
		if (resto < 2)  
			digito2 = 0;  
		else  
			digito2 = 11 - resto;  
		
		//Digito verificador do CPF que está sendo validado.  
		String nDigVerific = cpf.substring (cpf.length()-2, cpf.length());  
		
		//Concatenando o primeiro resto com o segundo.  
		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);  
		
		//comparar o digito verificador do cpf com o primeiro resto + o segundo resto.  
		return nDigVerific.equals(nDigResult);  
	} 
	
	
	/** 
	 * Metodo que valida CNPJ.
	 * @return Boolean 
	 */  
	public static boolean isCNPJ(String CNPJ) {
		
		CNPJ = removerMascara(CNPJ);
		
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais, vazio ou nulo
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222")
				|| CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
				|| CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14) || CNPJ.isEmpty() || CNPJ == null)
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}
	
	
	
	/**
	 * Método que verifica se o Dígito Verificador de uma Agência Bradesco é válida, 
	 * na qual esta deve ser informada no seguinte padrão: NNNN-D, onde N é um valor numerico
	 * e D um valor numerico que representa o dígito verificador gerado a partir dos outros 4 
	 * números informados antes do hífen (-). 
	 * @param agencia
	 * @return Boolean
	 */
	public static Boolean validarDVAgenciaBradesco(String agencia){

		agencia = removerMascara(agencia);
		
		int soma = Integer.parseInt(agencia.substring(0, 1)) * 5 +
				   Integer.parseInt(agencia.substring(1, 2)) * 4 +
				   Integer.parseInt(agencia.substring(2, 3)) * 3 +
				   Integer.parseInt(agencia.substring(3, 4)) * 2 ;
		
		//calculando dígito verificador
		int digitoVerificador = 11 - (soma % 11);
		
		//condição para dígito verificador ser igual a zero
		if(digitoVerificador > 9)
			digitoVerificador = 0;
		
		if(digitoVerificador == Integer.parseInt(agencia.substring(4, 5))){
			return true;
		} 
		
		return false;
	}
	
	/**
	 * Método que verifica se o Dígito Verificador de uma Conta Corrente Bradesco é válida, 
	 * na qual esta deve ser informada no seguinte padrão: NNNNNNN-D, onde N é um valor numerico
	 * e D um valor numerico que representa o dígito verificador gerado a partir dos outros sete 
	 * números informados antes do hífen (-). 
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
		
		//calculando dígito verificador
		digitoSecundario = 11 - (soma % 11);
		
		//condição para dígito verificador ser igual a zero
		if(digitoSecundario > 9)
			digitoSecundario = 0;
		
		//verifica se o dígito verificador p equivale a zero
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
	 * Método que valida o Dígito Verificado de uma matrícula
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
	 * Método que pega qualquer valor monetário e converte para BigDecimal
	 */
	public static BigDecimal valorMonetarioStringParaBigDecimal(String valorMonetarioString) {
		String r = "";
		for (int i = 0; i < valorMonetarioString.length(); i++) {
			if (valorMonetarioString.charAt(i) != ',' && valorMonetarioString.charAt(i) != '.'&& valorMonetarioString.charAt(i) != 'R'&& valorMonetarioString.charAt(i) != '$')
				r += valorMonetarioString.charAt(i);
		}
		return new BigDecimal(new BigDecimal(r).doubleValue()/100).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public static BigDecimal valorMonetarioStringParaBigDecimal2(String valorMonetarioString) {
		String valor = "";
		for (int i = 0; i < valorMonetarioString.length(); i++) {
			if ( valorMonetarioString.charAt(i) != '.'&& valorMonetarioString.charAt(i) != 'R'&& valorMonetarioString.charAt(i) != '$')
				valor += valorMonetarioString.charAt(i);
		}
		valor = valor.replace(",", ".");
		 // Creating a Double Object 
        Double d = new Double(valor); 

        /// Assigning the bigdecimal value of ln to b 
        BigDecimal b = BigDecimal.valueOf(d); 
		return b;
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
		
		//verificando o resto da divisão
		resto = total % 11;
		
		if(resto != 0)
			resto = 11 - resto;
		
		if(resto != Integer.parseInt(pisPasep.substring(10, 11)))
			return false;
		
		return true;
	}
	
	/**
	 * Método que verifica o Dígito Verificador do Processo
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

		// Resto da divisão por 11.
		resto = (soma % 11);

		// se resto = 10 entao digito = 0 (if dig 10 Then dig = 0)
		if (resto == 10)
			digito = 0;
		else
			digito = resto;

		// Digito verificador do processo que está sendo validado.
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
		if(string != null){
			string = string.replace(" ", "");
			string = string.replace(".", "");
			string = string.replace("-", "");
			string = string.replace("/", "");
			string = string.replace("(", "");
			string = string.replace(")", "");
		}
		return string;
	}
	
	public static String formatarCPF(String texto) throws ParseException {
		MaskFormatter mf = new MaskFormatter("###.###.###-##");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);  		
	}
	
	public static String formatarCNPJ(String texto) throws ParseException {
		MaskFormatter mf = new MaskFormatter("##.###.###/####-##");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);  		
	}
	
	public static String formatarMatricula(String texto) throws ParseException {
		MaskFormatter mf = new MaskFormatter("####-#");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);  		
	}
	
	public static String formatarHora(String texto) throws ParseException {
		MaskFormatter mf = new MaskFormatter("##:##");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);  		
	}
	
	public static String formatarProcesso(String processo) throws ParseException {
		MaskFormatter mf = new MaskFormatter("#####/####-##");
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(processo);  		
	}
		

	//Codigo 0 - Formatar padrão SAP, caso contrário, Retira do padrao SAP e joga no Padrão exibido ao usuário
	public static String formatatarDesformatarNrProcessoPadraoSAP(String nrProcesso, int codigoAcao) {
		nrProcesso = removerMascara(nrProcesso);
		if(codigoAcao == 0){
			return nrProcesso.substring(5, 9) + nrProcesso.substring(0, 5) + nrProcesso.substring(9, 10) ;
		} else {
			return nrProcesso.substring(4, 9) + nrProcesso.substring(0, 4) + nrProcesso.substring(9, 10) ; 
		}
	}	
	
	public static int calculaIdade(Date nascimento, Date obito){
		
		int idade = -1;
		
		if (nascimento != null) {
		
			Calendar dataDeNascimento = new GregorianCalendar();		
			dataDeNascimento.setTime(nascimento);
					
			Calendar dataFinal;
			
			if (obito != null){
			
				dataFinal = new GregorianCalendar();
				dataDeNascimento.setTime(obito);
				
			} else {	
				
				// Cria um objeto calendar com a data atual
				dataFinal = Calendar.getInstance();
			
			}		
							
			// Obtém a idade baseado no ano
			idade = dataFinal.get(Calendar.YEAR) - dataDeNascimento.get(Calendar.YEAR);
			
			dataDeNascimento.add(Calendar.YEAR, idade);		 
			
			//se a data de hoje/obito é antes da data de Nascimento, então diminui 1(um)
			if (dataFinal.before(dataDeNascimento)) {		
				idade--;		
			}
		
		}
		
		return idade;
		
	}
	
	public static Usuario getUsuarioLogado() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return null;
		}
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static String getTipoArquivo(String arquivo){
		int ponto = arquivo.lastIndexOf(".");
		return arquivo.substring(ponto);
	}
	
	public static String getNomeArquivo(String arquivo){
		int ponto = arquivo.lastIndexOf(".");
		return arquivo.substring(0, ponto);
	}
	
	public static boolean validarEmail(String email){
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}
	
	/**
	 * Retirado da seguinte implementação https://github.com/popofibo/TrimObjectGraph/blob/master/src/com/popofibo/utils/SpaceUtil.java
	 * 
	 */
	public static Object trimReflective(Object object) {
		if (object == null)
			return null;

		Class<? extends Object> c = object.getClass();
		try {
			
			// Introspector usage to pick the getters conveniently thereby excluding the Object getters
			for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(c, Object.class).getPropertyDescriptors()) {
				
				Method method = propertyDescriptor.getReadMethod();
				String name = method.getName();

				// If the current level of Property is of type String
				if (method.getReturnType().equals(String.class)) {
					
					String property = (String) method.invoke(object);
					if (property != null) {
						
						Method setter = c.getMethod("set" + name.substring(3), new Class<?>[] { String.class });
						if (setter != null)
							// Setter to trim and set the trimmed String value
							setter.invoke(object, property.trim());
					}
				}				
			}

		} catch (Exception e) {
			throw new SRHRuntimeException("Erro ao remover espaços em branco.");
		}

		return object;

	}
	
	public static boolean sistemaApontandoParaProducao() {
		
		Properties properties = new Properties();
		
		try {
			InputStream resourceAsStream = SRHUtils.class.getResourceAsStream("/application.properties");
			properties.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String databaseUrl = properties.getProperty("database.url");
		
		if(databaseUrl.contains("bdtce"))
			return true;
		
		return false;
		
	}
	
	public static boolean anoExercicioValido(Integer ano) {		
		return ano > 1900;
	}

}
