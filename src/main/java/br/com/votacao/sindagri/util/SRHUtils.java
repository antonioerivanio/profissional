/*     */ package br.com.votacao.sindagri.util;
/*     */ 
/*     */ import br.com.votacao.sindagri.domain.Usuario;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.text.Normalizer;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.ZoneId;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.InputMismatchException;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.text.MaskFormatter;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import org.springframework.security.core.context.SecurityContextHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SRHUtils
/*     */ {
/*     */   public static final String FORMATO_DATA = "dd/MM/yyyy";
/*     */   public static final String FORMATO_DATA_SEM_BARRAS = "ddMMyyyy";
/*     */   public static final String FORMATO_DATA_ANO = "yyyy";
/*     */   public static final String FORMATO_DATA_HORA = "dd/MM/yyyy HH:mm:ss";
/*     */   private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
/*  48 */   private static final Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date adiconarSubtrairDiasDeUmaData(Date data, int qtdDias) {
/*  61 */     data = new Date(data.getTime() + (86400000 * qtdDias));
/*  62 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date inicioTCE() {
/*  71 */     Calendar dtInicio = new GregorianCalendar();
/*  72 */     dtInicio.set(1935, 9, 5);
/*  73 */     return removeHorasDaData(dtInicio.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date extincaoTCM() {
/*  82 */     Calendar dtInicio = new GregorianCalendar();
/*  83 */     dtInicio.set(2017, 7, 21);
/*  84 */     return removeHorasDaData(dtInicio.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date fimDeducaoDeFaltas() {
/*  94 */     Calendar dtFim = new GregorianCalendar();
/*  95 */     dtFim.set(1998, 11, 16);
/*  96 */     return dtFim.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int dataDiff(Date dataLow, Date dataHigh) {
/* 110 */     GregorianCalendar startTime = new GregorianCalendar();
/* 111 */     GregorianCalendar endTime = new GregorianCalendar();
/*     */     
/* 113 */     GregorianCalendar curTime = new GregorianCalendar();
/* 114 */     GregorianCalendar baseTime = new GregorianCalendar();
/*     */     
/* 116 */     startTime.setTime(dataLow);
/* 117 */     endTime.setTime(dataHigh);
/*     */     
/* 119 */     int dif_multiplier = 1;
/*     */ 
/*     */     
/* 122 */     if (dataLow.compareTo(dataHigh) < 0) {
/* 123 */       baseTime.setTime(dataHigh);
/* 124 */       curTime.setTime(dataLow);
/* 125 */       dif_multiplier = 1;
/*     */     } else {
/* 127 */       baseTime.setTime(dataLow);
/* 128 */       curTime.setTime(dataHigh);
/* 129 */       dif_multiplier = -1;
/*     */     } 
/*     */     
/* 132 */     int result_years = 0;
/* 133 */     int result_months = 0;
/* 134 */     int result_days = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     while (curTime.get(1) < baseTime.get(1) || 
/* 140 */       curTime.get(2) < baseTime.get(2)) {
/*     */       
/* 142 */       int max_day = curTime.getActualMaximum(5);
/* 143 */       result_months += max_day;
/* 144 */       curTime.add(2, 1);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 149 */     result_months *= dif_multiplier;
/*     */ 
/*     */     
/* 152 */     result_days += endTime.get(5) - startTime.get(5);
/*     */     
/* 154 */     return result_years + result_months + result_days + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int dataDiffAverbacao(Date dataLow, Date dataHigh) {
/* 168 */     GregorianCalendar startTime = new GregorianCalendar();
/* 169 */     GregorianCalendar endTime = new GregorianCalendar();
/*     */     
/* 171 */     startTime.setTime(dataLow);
/* 172 */     endTime.setTime(dataHigh);
/*     */     
/* 174 */     int anoStart = startTime.get(1);
/* 175 */     int mesStart = startTime.get(2);
/* 176 */     int diaStart = startTime.get(5);
/*     */     
/* 178 */     int anoEnd = endTime.get(1);
/* 179 */     int mesEnd = endTime.get(2);
/* 180 */     int diaEnd = endTime.get(5);
/*     */     
/* 182 */     int totalAnos = 0;
/* 183 */     int totalMes = 0;
/* 184 */     int totalDias = 0;
/*     */     
/* 186 */     if (mesEnd < mesStart) {
/*     */       
/* 188 */       totalAnos = anoEnd - anoStart - 1;
/* 189 */       totalMes = mesEnd + 12 - mesStart;
/*     */       
/* 191 */       if (diaEnd < diaStart) {
/* 192 */         totalDias = diaEnd + 30 - diaStart + (totalMes - 1) * 30 + totalAnos * 365;
/*     */       } else {
/* 194 */         totalDias = diaEnd - diaStart + totalMes * 30 + totalAnos * 365;
/*     */       } 
/*     */     } else {
/*     */       
/* 198 */       totalAnos = anoEnd - anoStart;
/* 199 */       totalMes = mesEnd - mesStart;
/*     */       
/* 201 */       if (diaEnd < diaStart) {
/* 202 */         totalDias = diaEnd + 30 - diaStart + (totalMes - 1) * 30 + totalAnos * 365;
/*     */       } else {
/* 204 */         totalDias = diaEnd - diaStart + totalMes * 30 + totalAnos * 365;
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     return totalDias + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] contagemDeTempoDeServico(Date dataInicio, Date dataFim) {
/* 223 */     int anos = 0, meses = 0, dias = 0;
/*     */     
/* 225 */     GregorianCalendar inicio = new GregorianCalendar();
/* 226 */     GregorianCalendar fim = new GregorianCalendar();
/*     */     
/* 228 */     inicio.setTime(dataInicio);
/* 229 */     fim.setTime(dataFim);
/*     */     
/* 231 */     int anoInicio = inicio.get(1);
/* 232 */     int mesInicio = inicio.get(2) + 1;
/* 233 */     int diaInicio = inicio.get(5);
/* 234 */     int anoFim = fim.get(1);
/* 235 */     int mesFim = fim.get(2) + 1;
/* 236 */     int diaFim = fim.get(5);
/*     */ 
/*     */ 
/*     */     
/* 240 */     if (diaInicio == 1 && mesFim == 2 && diaFim >= 28) {
/* 241 */       diaFim = 30;
/*     */     }
/*     */     
/* 244 */     if (diaFim < diaInicio) {
/* 245 */       diaFim += 30;
/* 246 */       mesFim--;
/*     */     } 
/*     */     
/* 249 */     dias = diaFim - diaInicio + 1;
/*     */     
/* 251 */     if (dias >= 30) {
/* 252 */       dias = 0;
/* 253 */       meses++;
/*     */     } 
/*     */     
/* 256 */     if (mesFim < mesInicio) {
/* 257 */       mesFim += 12;
/* 258 */       anoFim--;
/*     */     } 
/*     */     
/* 261 */     meses += mesFim - mesInicio;
/*     */     
/* 263 */     if (meses == 12) {
/* 264 */       meses = 0;
/* 265 */       anos++;
/*     */     } 
/*     */     
/* 268 */     anos += anoFim - anoInicio;
/*     */     
/* 270 */     int[] retorno = { anos, meses, dias };
/*     */     
/* 272 */     return retorno;
/*     */   }
/*     */   
/*     */   public static int calculaQtdeDias(int[] anosMesesDias) {
/* 276 */     return 365 * anosMesesDias[0] + 30 * anosMesesDias[1] + anosMesesDias[2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long[] anosMesesDias(long qtdDias) {
/* 283 */     long anos = qtdDias / 365L;
/*     */     
/* 285 */     long meses = (qtdDias - 365L * anos) / 30L;
/*     */     
/* 287 */     long dias = qtdDias - 365L * anos - 30L * meses;
/*     */     
/* 289 */     long[] retorno = { anos, meses, dias };
/*     */     
/* 291 */     return retorno;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formataData(String formato, Date data) {
/* 303 */     SimpleDateFormat sdf = new SimpleDateFormat(formato);
/* 304 */     return sdf.format(data);
/*     */   }
/*     */   
/*     */   public static Date removeHorasDaData(Date data) {
/* 308 */     SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
/*     */     try {
/* 310 */       return sdf.parse(sdf.format(data));
/* 311 */     } catch (ParseException e) {
/* 312 */       e.printStackTrace();
/* 313 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Date getHoje() {
/* 318 */     return removeHorasDaData(new Date());
/*     */   }
/*     */   
/*     */   public static Date getAgora() {
/* 322 */     return new Date();
/*     */   }
/*     */   
/*     */   public static Date getDataHoraAtual() {
/* 326 */     Date dataHoraAtual = new Date();
/* 327 */     String datahora = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(dataHoraAtual);
/* 328 */     SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
/*     */     
/*     */     try {
/* 331 */       Date dataFormatada = formato.parse(datahora);
/* 332 */       return dataFormatada;
/* 333 */     } catch (ParseException e) {
/* 334 */       e.printStackTrace();
/* 335 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getAnoCorrente() {
/* 340 */     GregorianCalendar hoje = new GregorianCalendar();
/* 341 */     hoje.setTime(new Date());
/*     */     
/* 343 */     return hoje.get(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validarCPF(String cpf) {
/* 353 */     cpf = removerMascara(cpf);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 358 */     int resto = 0, digito2 = resto, digito1 = digito2, d2 = digito1, d1 = d2;
/*     */     
/* 360 */     for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
/* 361 */       int digitoCPF = Integer.valueOf(cpf.substring(nCount - 1, nCount)).intValue();
/*     */ 
/*     */       
/* 364 */       d1 += (11 - nCount) * digitoCPF;
/*     */ 
/*     */ 
/*     */       
/* 368 */       d2 += (12 - nCount) * digitoCPF;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 373 */     resto = d1 % 11;
/*     */ 
/*     */ 
/*     */     
/* 377 */     if (resto < 2) {
/* 378 */       digito1 = 0;
/*     */     } else {
/* 380 */       digito1 = 11 - resto;
/*     */     } 
/* 382 */     d2 += 2 * digito1;
/*     */ 
/*     */     
/* 385 */     resto = d2 % 11;
/*     */ 
/*     */ 
/*     */     
/* 389 */     if (resto < 2) {
/* 390 */       digito2 = 0;
/*     */     } else {
/* 392 */       digito2 = 11 - resto;
/*     */     } 
/*     */     
/* 395 */     String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());
/*     */ 
/*     */     
/* 398 */     String nDigResult = String.valueOf(String.valueOf(digito1)) + String.valueOf(digito2);
/*     */ 
/*     */     
/* 401 */     return nDigVerific.equals(nDigResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCNPJ(String CNPJ) {
/* 411 */     CNPJ = removerMascara(CNPJ);
/*     */ 
/*     */ 
/*     */     
/* 415 */     if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222") || 
/* 416 */       CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") || 
/* 417 */       CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888") || 
/* 418 */       CNPJ.equals("99999999999999") || CNPJ.length() != 14 || CNPJ.isEmpty() || CNPJ == null) {
/* 419 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*     */       char dig13, dig14;
/*     */ 
/*     */       
/* 427 */       int sm = 0;
/* 428 */       int peso = 2; int i;
/* 429 */       for (i = 11; i >= 0; i--) {
/*     */ 
/*     */ 
/*     */         
/* 433 */         int num = CNPJ.charAt(i) - 48;
/* 434 */         sm += num * peso;
/* 435 */         peso++;
/* 436 */         if (peso == 10) {
/* 437 */           peso = 2;
/*     */         }
/*     */       } 
/* 440 */       int r = sm % 11;
/* 441 */       if (r == 0 || r == 1) {
/* 442 */         dig13 = '0';
/*     */       } else {
/* 444 */         dig13 = (char)(11 - r + 48);
/*     */       } 
/*     */       
/* 447 */       sm = 0;
/* 448 */       peso = 2;
/* 449 */       for (i = 12; i >= 0; i--) {
/* 450 */         int num = CNPJ.charAt(i) - 48;
/* 451 */         sm += num * peso;
/* 452 */         peso++;
/* 453 */         if (peso == 10) {
/* 454 */           peso = 2;
/*     */         }
/*     */       } 
/* 457 */       r = sm % 11;
/* 458 */       if (r == 0 || r == 1) {
/* 459 */         dig14 = '0';
/*     */       } else {
/* 461 */         dig14 = (char)(11 - r + 48);
/*     */       } 
/*     */       
/* 464 */       if (dig13 == CNPJ.charAt(12) && dig14 == CNPJ.charAt(13)) {
/* 465 */         return true;
/*     */       }
/* 467 */       return false;
/* 468 */     } catch (InputMismatchException erro) {
/* 469 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Boolean validarDVAgenciaBradesco(String agencia) {
/* 484 */     agencia = removerMascara(agencia);
/*     */     
/* 486 */     int soma = Integer.parseInt(agencia.substring(0, 1)) * 5 + Integer.parseInt(agencia.substring(1, 2)) * 4 + 
/* 487 */       Integer.parseInt(agencia.substring(2, 3)) * 3 + Integer.parseInt(agencia.substring(3, 4)) * 2;
/*     */ 
/*     */     
/* 490 */     int digitoVerificador = 11 - soma % 11;
/*     */ 
/*     */     
/* 493 */     if (digitoVerificador > 9) {
/* 494 */       digitoVerificador = 0;
/*     */     }
/* 496 */     if (digitoVerificador == Integer.parseInt(agencia.substring(4, 5))) {
/* 497 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/* 500 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Boolean validarDVContaCorrenteBradesco(String conta) {
/* 514 */     conta = removerMascara(conta);
/*     */     
/* 516 */     int digitoPrincipal = 0, digitoSecundario = 0;
/*     */     
/* 518 */     int soma = Integer.parseInt(conta.substring(1, 2)) * 7 + Integer.parseInt(conta.substring(2, 3)) * 6 + 
/* 519 */       Integer.parseInt(conta.substring(3, 4)) * 5 + Integer.parseInt(conta.substring(4, 5)) * 4 + 
/* 520 */       Integer.parseInt(conta.substring(5, 6)) * 3 + Integer.parseInt(conta.substring(6, 7)) * 2;
/*     */ 
/*     */     
/* 523 */     digitoSecundario = 11 - soma % 11;
/*     */ 
/*     */     
/* 526 */     if (digitoSecundario > 9) {
/* 527 */       digitoSecundario = 0;
/*     */     }
/*     */     
/* 530 */     if (conta.substring(7, 8).equals("0")) {
/* 531 */       digitoSecundario = 0;
/*     */     } else {
/* 533 */       digitoPrincipal = Integer.parseInt(conta.substring(7, 8));
/*     */     } 
/*     */     
/* 536 */     if (digitoSecundario == digitoPrincipal) {
/* 537 */       return Boolean.valueOf(true);
/*     */     }
/* 539 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validarMatricula(String matricula) {
/* 549 */     int dv = 0;
/* 550 */     dv = (Integer.valueOf(matricula.substring(0, 1)).intValue() * 8 + Integer.valueOf(matricula.substring(1, 2)).intValue() * 9 + 
/* 551 */       Integer.valueOf(matricula.substring(2, 3)).intValue() * 8 + Integer.valueOf(matricula.substring(3, 4)).intValue() * 7) % 
/* 552 */       10;
/* 553 */     if (dv == Integer.parseInt(matricula.substring(5, 6)))
/* 554 */       return true; 
/* 555 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal valorMonetarioStringParaBigDecimal(String valorMonetarioString) {
/* 562 */     String r = "";
/* 563 */     for (int i = 0; i < valorMonetarioString.length(); i++) {
/* 564 */       if (valorMonetarioString.charAt(i) != ',' && valorMonetarioString.charAt(i) != '.' && 
/* 565 */         valorMonetarioString.charAt(i) != 'R' && valorMonetarioString.charAt(i) != '$')
/* 566 */         r = String.valueOf(r) + valorMonetarioString.charAt(i); 
/*     */     } 
/* 568 */     return (new BigDecimal((new BigDecimal(r)).doubleValue() / 100.0D)).setScale(2, RoundingMode.HALF_EVEN);
/*     */   }
/*     */   
/*     */   public static BigDecimal valorMonetarioStringParaBigDecimal2(String valorMonetarioString) {
/* 572 */     String valor = "";
/* 573 */     for (int i = 0; i < valorMonetarioString.length(); i++) {
/* 574 */       if (valorMonetarioString.charAt(i) != '.' && valorMonetarioString.charAt(i) != 'R' && 
/* 575 */         valorMonetarioString.charAt(i) != '$')
/* 576 */         valor = String.valueOf(valor) + valorMonetarioString.charAt(i); 
/*     */     } 
/* 578 */     valor = valor.replace(",", ".");
/*     */     
/* 580 */     Double d = new Double(valor);
/*     */ 
/*     */     
/* 583 */     BigDecimal b = BigDecimal.valueOf(d.doubleValue());
/* 584 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Boolean validarPisPasep(String pisPasep) {
/* 589 */     String fTap = "3298765432";
/*     */     
/* 591 */     int total = 0, resto = 0;
/*     */     
/* 593 */     pisPasep = removerMascara(pisPasep);
/*     */ 
/*     */ 
/*     */     
/* 597 */     if (pisPasep.length() != 11) {
/* 598 */       return Boolean.valueOf(false);
/*     */     }
/* 600 */     if (Double.parseDouble(pisPasep) == 0.0D) {
/* 601 */       return Boolean.valueOf(false);
/*     */     }
/* 603 */     for (int i = 0; i < 10; i++) {
/* 604 */       total += Integer.parseInt(pisPasep.substring(i, i + 1)) * Integer.parseInt(fTap.substring(i, i + 1));
/*     */     }
/*     */     
/* 607 */     resto = total % 11;
/*     */     
/* 609 */     if (resto != 0) {
/* 610 */       resto = 11 - resto;
/*     */     }
/* 612 */     if (resto != Integer.parseInt(pisPasep.substring(10, 11))) {
/* 613 */       return Boolean.valueOf(false);
/*     */     }
/* 615 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validarProcesso(String processo) {
/* 630 */     int resto = 0, digito = resto, soma = digito;
/*     */     
/* 632 */     for (int posicao = 1; posicao <= processo.length() - 1; posicao++) {
/* 633 */       int digitoProcesso = Integer.valueOf(processo.substring(posicao - 1, posicao)).intValue();
/* 634 */       soma += (11 - posicao) * digitoProcesso;
/*     */     } 
/*     */ 
/*     */     
/* 638 */     resto = soma % 11;
/*     */ 
/*     */     
/* 641 */     if (resto == 10) {
/* 642 */       digito = 0;
/*     */     } else {
/* 644 */       digito = resto;
/*     */     } 
/*     */     
/* 647 */     String nDigVerific = processo.substring(processo.length() - 1, processo.length());
/*     */     
/* 649 */     String nDigResult = String.valueOf(digito);
/*     */ 
/*     */     
/* 652 */     return nDigVerific.equals(nDigResult);
/*     */   }
/*     */   
/*     */   public static String removerAcentos(String string) {
/* 656 */     CharSequence cs = new StringBuilder((string == null) ? "" : string);
/* 657 */     return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
/*     */   }
/*     */   
/*     */   public static String removerMascara(String string) {
/* 661 */     if (string != null) {
/* 662 */       string = string.replace(" ", "");
/* 663 */       string = string.replace(".", "");
/* 664 */       string = string.replace("-", "");
/* 665 */       string = string.replace("/", "");
/* 666 */       string = string.replace("(", "");
/* 667 */       string = string.replace(")", "");
/*     */     } 
/* 669 */     return string;
/*     */   }
/*     */   
/*     */   public static String formatarCPF(String texto) throws ParseException {
/* 673 */     MaskFormatter mf = new MaskFormatter("###.###.###-##");
/* 674 */     mf.setValueContainsLiteralCharacters(false);
/* 675 */     return mf.valueToString(texto);
/*     */   }
/*     */   
/*     */   public static String formatarCNPJ(String texto) throws ParseException {
/* 679 */     MaskFormatter mf = new MaskFormatter("##.###.###/####-##");
/* 680 */     mf.setValueContainsLiteralCharacters(false);
/* 681 */     return mf.valueToString(texto);
/*     */   }
/*     */   
/*     */   public static String formatarMatricula(String texto) throws ParseException {
/* 685 */     MaskFormatter mf = new MaskFormatter("####-#");
/* 686 */     mf.setValueContainsLiteralCharacters(false);
/* 687 */     return mf.valueToString(texto);
/*     */   }
/*     */   
/*     */   public static String removeHifenMatricula(String matricula) {
/* 691 */     String novoMatricula = matricula;
/*     */     
/* 693 */     if (matricula != null && matricula.contains("-")) {
/* 694 */       novoMatricula = matricula.replaceAll("-", "").trim();
/*     */     }
/*     */     
/* 697 */     return novoMatricula;
/*     */   }
/*     */   
/*     */   public static String formatarHora(String texto) throws ParseException {
/* 701 */     MaskFormatter mf = new MaskFormatter("##:##");
/* 702 */     mf.setValueContainsLiteralCharacters(false);
/* 703 */     return mf.valueToString(texto);
/*     */   }
/*     */   
/*     */   public static String formatarProcesso(String processo) throws ParseException {
/* 707 */     MaskFormatter mf = new MaskFormatter("#####/####-##");
/* 708 */     mf.setValueContainsLiteralCharacters(false);
/* 709 */     return mf.valueToString(processo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatatarDesformatarNrProcessoPadraoSAP(String nrProcesso, int codigoAcao) {
/* 716 */     nrProcesso = removerMascara(nrProcesso);
/* 717 */     if (codigoAcao == 0) {
/* 718 */       return String.valueOf(nrProcesso.substring(5, 9)) + nrProcesso.substring(0, 5) + nrProcesso.substring(9, 10);
/*     */     }
/* 720 */     return String.valueOf(nrProcesso.substring(4, 9)) + nrProcesso.substring(0, 4) + nrProcesso.substring(9, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int calculaIdade(Date nascimento, Date obito) {
/* 726 */     int idade = -1;
/*     */     
/* 728 */     if (nascimento != null) {
/*     */       
/* 730 */       Calendar dataFinal, dataDeNascimento = new GregorianCalendar();
/* 731 */       dataDeNascimento.setTime(nascimento);
/*     */ 
/*     */ 
/*     */       
/* 735 */       if (obito != null) {
/*     */         
/* 737 */         dataFinal = new GregorianCalendar();
/* 738 */         dataDeNascimento.setTime(obito);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 743 */         dataFinal = Calendar.getInstance();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 748 */       idade = dataFinal.get(1) - dataDeNascimento.get(1);
/*     */       
/* 750 */       dataDeNascimento.add(1, idade);
/*     */ 
/*     */       
/* 753 */       if (dataFinal.before(dataDeNascimento)) {
/* 754 */         idade--;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 759 */     return idade;
/*     */   }
/*     */ 
/*     */   
/*     */   public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
/* 764 */     return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
/*     */   }
/*     */   
/*     */   public static Usuario getUsuarioLogado() {
/* 768 */     if (SecurityContextHolder.getContext().getAuthentication() == null) {
/* 769 */       return null;
/*     */     }
/* 771 */     return (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
/*     */   }
/*     */   
/*     */   public static String getTipoArquivo(String arquivo) {
/* 775 */     int ponto = arquivo.lastIndexOf(".");
/* 776 */     return arquivo.substring(ponto);
/*     */   }
/*     */   
/*     */   public static String getNomeArquivo(String arquivo) {
/* 780 */     int ponto = arquivo.lastIndexOf(".");
/* 781 */     return arquivo.substring(0, ponto);
/*     */   }
/*     */   
/*     */   public static boolean validarEmail(String email) {
/* 785 */     Matcher matcher = pattern.matcher(email);
/* 786 */     return matcher.matches();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object trimReflective(Object object) {
/* 795 */     if (object == null) {
/* 796 */       return null;
/*     */     }
/* 798 */     Class<? extends Object> c = (Class)object.getClass();
/*     */ 
/*     */     
/*     */     try {
/*     */       PropertyDescriptor[] arrayOfPropertyDescriptor;
/*     */       
/* 804 */       int i = (arrayOfPropertyDescriptor = Introspector.getBeanInfo(c, Object.class).getPropertyDescriptors()).length; byte b = 0; for (; b < i; b++) { PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/*     */         
/* 806 */         Method method = propertyDescriptor.getReadMethod();
/* 807 */         String name = method.getName();
/*     */ 
/*     */         
/* 810 */         if (method.getReturnType().equals(String.class)) {
/*     */           
/* 812 */           String property = (String)method.invoke(object, new Object[0]);
/* 813 */           if (property != null) {
/*     */             
/* 815 */             Method setter = c.getMethod("set" + name.substring(3), new Class[] { String.class });
/* 816 */             if (setter != null)
/*     */             {
/* 818 */               setter.invoke(object, new Object[] { property.trim() });
/*     */             }
/*     */           } 
/*     */         }  }
/*     */     
/* 823 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 827 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean sistemaApontandoParaProducao() {
/* 833 */     Properties properties = new Properties();
/*     */     
/*     */     try {
/* 836 */       InputStream resourceAsStream = SRHUtils.class.getResourceAsStream("/application.properties");
/* 837 */       properties.load(resourceAsStream);
/* 838 */     } catch (IOException e) {
/* 839 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 842 */     String databaseUrl = properties.getProperty("database.url");
/*     */     
/* 844 */     if (databaseUrl.contains("bdtce")) {
/* 845 */       return true;
/*     */     }
/* 847 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean anoExercicioValido(Integer ano) {
/* 852 */     return (ano.intValue() > 1900);
/*     */   }
/*     */   
/*     */   public static List<Integer> popularComboAno(int quantidade) {
/* 856 */     List<Integer> comboAno = new ArrayList<>();
/* 857 */     Calendar c = Calendar.getInstance();
/* 858 */     Integer ano = Integer.valueOf(c.get(1));
/* 859 */     for (int i = 0; i < quantidade; i++) {
/* 860 */       if (comboAno.size() > 0) {
/* 861 */         ano = Integer.valueOf(ano.intValue() - 1);
/* 862 */         comboAno.add(ano);
/*     */       } else {
/* 864 */         comboAno.add(ano);
/*     */       } 
/*     */     } 
/* 867 */     return comboAno;
/*     */   }
/*     */   
/*     */   public static String getDadosParametroProperties(String key) {
/* 871 */     String resourceName = "application.properties";
/* 872 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 873 */     Properties props = new Properties();
/* 874 */     InputStream resourceStream = null;
/*     */     try {
/* 876 */       resourceStream = loader.getResourceAsStream(resourceName);
/* 877 */       props.load(resourceStream);
/*     */       
/* 879 */       return props.getProperty(key);
/* 880 */     } catch (IOException ex) {
/* 881 */       ex.printStackTrace();
/*     */     } finally {
/* 883 */       if (resourceStream != null) {
/*     */         try {
/* 885 */           resourceStream.close();
/* 886 */         } catch (IOException e) {
/* 887 */           e.printStackTrace();
/*     */         } 
/*     */       }
/*     */     } 
/* 891 */     return null;
/*     */   }
/*     */   
/*     */   public static long getIdade(int dia, int mes, int ano) {
/* 895 */     LocalDate startDate = LocalDate.of(ano, mes, dia);
/* 896 */     LocalDate endDate = LocalDate.now();
/*     */     
/* 898 */     long idade = ChronoUnit.YEARS.between(startDate, endDate);
/*     */     
/* 900 */     return idade;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMesAniversarioEdataMenorQueDataAtual(int diaAniversario, int mesAniversario, int anoAniversario) {
/* 912 */     LocalDate endDate = LocalDate.now();
/* 913 */     int mesAtual = endDate.getMonthValue();
/* 914 */     int diaMesAtual = endDate.getDayOfMonth();
/* 915 */     int anoAtual = endDate.getYear();
/* 916 */     boolean isMesAniversario = Boolean.FALSE.booleanValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 922 */     if (anoAniversario < anoAtual && mesAniversario == mesAtual) {
/* 923 */       isMesAniversario = Boolean.TRUE.booleanValue();
/*     */     }
/*     */     
/* 926 */     return isMesAniversario;
/*     */   }
/*     */   
/*     */   public static String getPeriodoApuracaoStr(String mesReferencia, String anoReferencia) {
/* 930 */     String periodoApuracaoStr = "";
/*     */     
/* 932 */     if (mesReferencia.equals("13")) {
/* 933 */       periodoApuracaoStr = anoReferencia;
/*     */     } else {
/* 935 */       periodoApuracaoStr = String.valueOf(anoReferencia) + "-" + mesReferencia;
/*     */     } 
/* 937 */     return periodoApuracaoStr;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getStringMD5HashFromObject(byte[] bytesObjeto) {
/*     */     try {
/* 943 */       MessageDigest md = MessageDigest.getInstance("MD5");
/* 944 */       md.update(bytesObjeto);
/* 945 */       byte[] digest = md.digest();
/* 946 */       String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
/*     */       
/* 948 */       return myHash;
/* 949 */     } catch (NoSuchAlgorithmException e) {
/*     */       
/* 951 */       e.printStackTrace();
/*     */ 
/*     */       
/* 954 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagr\\util\SRHUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */