package br.gov.ce.tce.srh.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalDAO;
import br.gov.ce.tce.srh.dao.LicencaDAO;
import br.gov.ce.tce.srh.dao.SituacaoDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.domain.Situacao;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * 
 * @author robstown
 *
 */
@Service("licencaService")
public class LicencaServiceImpl implements LicencaService {

	@Autowired
	private LicencaDAO dao;
	
	@Autowired
	private FuncionalDAO funcionalDAO;
	
	@Autowired
	private SituacaoDAO situacaoDAO;

	@Autowired
	private TipoLicencaService tipoLicencaService;
	
	@Autowired
	private LicencaEspecialService licencaEspecialService;
	



	@Override
	@Transactional
	public String salvar(Licenca entidade) throws SRHRuntimeException {

		
		String mensagemRetorno = null;
		
		
		if ( entidade.getTipoLicenca() != null)
			entidade.setTipoLicenca( tipoLicencaService.getById( entidade.getTipoLicenca().getId() ) );

		
		if ( entidade.getContarDiasEmDobro() > 0 ){
			entidade.setInicio(entidade.getDoe());
			entidade.setFim(entidade.getDoe());
		}	
		
		
		validarDados(entidade);

		
		if (entidade.getNrprocessoPuro() != null && !entidade.getNrprocessoPuro().equals(""))
			entidade.setNrprocesso(SRHUtils.formatatarDesformatarNrProcessoPadraoSAP(entidade.getNrprocessoPuro(), 0));
		

		if ( entidade.getContarDiasEmDobro() == null || entidade.getContarDiasEmDobro().intValue() == 0 ) {			
				
			validarConformeSexo(entidade);		
		
			validarMaxDiasPermitidoPorTipoLicenca(entidade);
			
			verificarDatasDeLicencasAntigas(entidade);
		
			validarPeriodoInicioLicencaEspecial(entidade);
		
			ajustarSaldoLicencaEspecial(entidade);		
			
			alterarFuncional(entidade);		
			
			dao.salvar(entidade);
			
		} else {			
				
			mensagemRetorno = tratarContagemDeDiasEmDobro(entidade);	
			
		}		
		
		
		return mensagemRetorno;
			
	}


	private String tratarContagemDeDiasEmDobro(Licenca entidade) {
		
		
		// Inclusão
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
		
			
			if ( entidade.getTipoLicenca().isEspecial() ) {		
				
				LicencaEspecial licencaEspecial = licencaEspecialService.getById(entidade.getLicencaEspecial().getId());
					
				if (licencaEspecial.getSaldodias() < entidade.getContarDiasEmDobro()) {
					
					throw new SRHRuntimeException("O valor informado para Contar Dias em Dobro é maior que o saldo da Licença Especial.");
				
				} else if ( licencaEspecial.getSaldodias() == entidade.getContarDiasEmDobro() ){
					
					licencaEspecial.setContaremdobro(true);
					licencaEspecialService.salvar(licencaEspecial);
					
					return "O registro da Licença Especial foi alterado para considerar o saldo em dobro.";						
					
				} else {
					
					licencaEspecialService.ajustarSaldo( licencaEspecial.getId() , "REMOVER",	entidade.getContarDiasEmDobro().intValue() );
					dao.salvar(entidade);
					
				}
			
			} else {
			
				throw new SRHRuntimeException("Não foi possível concluir a operação.");
			}
			
		// Alteração	
		} else {
			
			dao.salvar(entidade);
			
		}
		
		return null;
	}


	private void alterarFuncional(Licenca entidade) {
		
		//finalLicenca é utilizado para saber se a licença cadastrada já terminou
		Calendar finalLicenca = Calendar.getInstance();
		finalLicenca.setTime(entidade.getFim());
		finalLicenca.add(Calendar.DATE, 1);

		/*
		 * Regra:
		 * Alterar, se a licença não tiver terminado, Situacao e ativoFp do Funcional quando TipoLicenca for Suspensão de Vínculo ou Interesse Particular
		 */		
		if(finalLicenca.getTime().compareTo(new Date()) == 1){
			
			Funcional funcional = funcionalDAO.getByPessoaAtivo(entidade.getPessoal().getId());
			if (entidade.getTipoLicenca().getId() == 1) // Suspensão de Vínculo
			{
				Situacao situacao = situacaoDAO.getById(new Long(7));
				funcional.setSituacao(situacao);
				funcional.setAtipoFp(false);
			}
			else if (entidade.getTipoLicenca().getId() == 2) // Interesse Particular
			{
				Situacao situacao = situacaoDAO.getById(new Long(5));
				funcional.setSituacao(situacao);
				funcional.setAtipoFp(false);
			}
			funcionalDAO.salvar(funcional);
			
		}
		
	}


	private void ajustarSaldoLicencaEspecial(Licenca entidade) {
		/*
		 * Regra:
		 * Ajustar saldo quando for licenca especial 
		 */
		
		
		if (entidade.getId() == null || entidade.getId().equals(0l)) 
		{
			 		
			if( entidade.getTipoLicenca().isEspecial() )
				licencaEspecialService.ajustarSaldo( entidade.getLicencaEspecial().getId(), "REMOVER",	SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim()) );
		}
		else{
			
			if( entidade.getTipoLicenca().isEspecial() )
			{
				//remove
				Licenca licencaEspecialBD = dao.getById(entidade.getId());
				
				if ( licencaEspecialBD.getLicencaEspecial() != null )
					licencaEspecialService.ajustarSaldo( licencaEspecialBD.getLicencaEspecial().getId(), "INSERIR", 
							SRHUtils.dataDiff(licencaEspecialBD.getInicio(), licencaEspecialBD.getFim()) );
				
				//adicona
				licencaEspecialService.ajustarSaldo( entidade.getLicencaEspecial().getId(), "REMOVER", 
						SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim()) );
			} 
		}
		
	}


	@Override
	@Transactional
	public void excluir(Licenca entidade) throws SRHRuntimeException {

		/*
		 * Regra:
		 * Ajustar saldo quando for licenca especial 
		 */
		if( entidade.getTipoLicenca().getId() == 5 ) {
			
			if ( entidade.getContarDiasEmDobro() > 0 ) {
				
				licencaEspecialService.ajustarSaldo( entidade.getLicencaEspecial().getId(), "INSERIR", entidade.getContarDiasEmDobro().intValue() );
				
			} else {
				
				licencaEspecialService.ajustarSaldo( entidade.getLicencaEspecial().getId(), "INSERIR", SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim()) );
				
			}			
		}
		
		
		
		/*
		 * Regra:
		 * Alterar Situacao e ativoFp do Funcional quando TipoLicenca for Suspensão de Vínculo ou Interesse Particular
		 */
		Funcional funcional = funcionalDAO.getByPessoaAtivo(entidade.getPessoal().getId());
		Situacao situacao;
		
		if (entidade.getTipoLicenca().getId() == 1 || entidade.getTipoLicenca().getId() == 2) // Suspensão de Vínculo
		{
			situacao = situacaoDAO.getById(new Long(1));
			funcional.setSituacao(situacao);
			funcional.setAtipoFp(true);
		}
		funcionalDAO.salvar(funcional);
		
		
		
		// removendo
		dao.excluir(entidade);	
	
	
	}
	
	
	@Override
	@Transactional
	public void finalizar(Licenca entidade) throws SRHRuntimeException {
		
		/*
		 * Regra:
		 * Alterar Situacao e ativoFp do Funcional quando TipoLicenca for Suspensão de Vínculo ou Interesse Particular
		 */
		Funcional funcional = funcionalDAO.getByPessoaAtivoFp(entidade.getPessoal().getId());
		Situacao situacao = situacaoDAO.getById(new Long(1));
		funcional.setAtipoFp(true);
		funcional.setSituacao(situacao);
		funcionalDAO.salvar(funcional);
		
	}


	@Override
	public int count(Long idPessoa) {
		return dao.count(idPessoa);
	}


	@Override
	public int count(Long idPessoa, Long tipoLicenca) {
		return dao.count(idPessoa, tipoLicenca);
	}	

	@Override
	public List<Licenca> search(Long idPessoa, int first, int rows) {
		return dao.search(idPessoa, first, rows);
	}


	@Override
	public List<Licenca> search(Long idPessoa, Long tipoLicenca, int first, int rows) {
		return dao.search(idPessoa, tipoLicenca, first, rows);
	}

	@Override
	public List<Licenca> search(String nome, int first, int rows) {
		List<Licenca> licencaList = dao.search(nome, first, rows);
		List<Licenca> licencaListReturn = new ArrayList<Licenca>();
		Funcional funcional;
		for (Licenca licenca: licencaList) {
			funcional = funcionalDAO.getByPessoaAtivoFp(licenca.getPessoal().getId());
			if (funcional != null) {
				licencaListReturn.add(licenca);
			}
		}
		return licencaListReturn;
	}
	
	@Override
	public List<Licenca> search(String nome, TipoLicenca tipoLicenca, int first, int rows) {
		List<Licenca> licencaList = dao.search(nome, tipoLicenca, first, rows);
		List<Licenca> licencaListReturn = new ArrayList<Licenca>();
		Funcional funcional;
		for (Licenca licenca: licencaList) {
			funcional = funcionalDAO.getByPessoaAtivoFp(licenca.getPessoal().getId());
			if (funcional != null) {
				licencaListReturn.add(licenca);
			}
		}
		return licencaListReturn;
	}
	
	@Override
	public List<Licenca> findByPessoa(Long idPessoa) {
		return dao.findByPessoa(idPessoa);
	}


	@Override
	public List<Licenca> findByPessoaLicencaEspecial(Long idPessoa) {
		return dao.findByPessoaLicencaEspecial(idPessoa);
	}

	@Override
	public List<Licenca> findByPessoaLicencasExcluirTempoServico(Long idPessoa) {
		return dao.findByPessoaLicencaExcluidaTempoServico(idPessoa);
	}
	

	/**
	 * Validar:
	 * 
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setado a data inicial.
	 *  Deve ser setado a data final.
	 *  Deve ser checado o periodo inical menor o periodo final.
	 *  Validar a data de publicacao.
	 *  Deve ser setado o tipo de licenca.
	 *  Validar tipo de licenca especial.
	 *  Validar tipo de publicacao.
	 *  Validar data de publicacao.
	 *  Validar numero do processo.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(Licenca entidade) throws SRHRuntimeException {

		// validando o servidor
		if (entidade.getPessoal() == null)
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		// validando o tipo de licenca
		if (entidade.getTipoLicenca() == null)
			throw new SRHRuntimeException("O Tipo de Licença é obrigatório.");

		// validando tipo de licenca especial
		if (entidade.getTipoLicenca().isEspecial() && entidade.getLicencaEspecial() == null )
			throw new SRHRuntimeException("Quando o Tipo de Licença for 'Licença Especial', é obrigatório selecionar uma Licença Especial.");

		if ( ! (entidade.getContarDiasEmDobro() > 0) ){
		
			// validando periodo inical
			if (entidade.getInicio() == null)
				throw new SRHRuntimeException("A Data Inicial é obrigatória.");
	
			// validando periodo final
			if (entidade.getFim() == null)
				throw new SRHRuntimeException("A Data Final é obrigatória.");
	
			// validando se o periodo inical é menor que o periodo final
			if (entidade.getInicio().after(entidade.getFim()))
				throw new SRHRuntimeException("A Data Inicial não pode ser maior que a Data Final.");

		}
		
		// validando caso a data de publicacao seja preenchida, a mesma deve ser menor que o periodo inicial
		if (entidade.getDoe() != null) {
			if (entidade.getDoe().before(SRHUtils.inicioTCE()) ) {
				throw new SRHRuntimeException("Data Publicação não deve ser anterior a " + SRHUtils.inicioTCE().toString() + ".");
			}
			if (entidade.getDoe().after(new Date())) {
				throw new SRHRuntimeException("Data Publicação não pode ser maior que a data atual.");
			}
		}

		// validando tipo de publicacao
		if (entidade.getTipoPublicacao() == null ) {
			if ( entidade.getDoe() != null ) {
				throw new SRHRuntimeException("Quando a Data de Publicação for informada, é necessário informar o Tipo de Publicação.");
			}
		}

		// validando data de publicacao
		if (entidade.getDoe() == null) {
			if (entidade.getTipoPublicacao() != null ) {
				throw new SRHRuntimeException("Quando o Tipo de Publicação for informado, é necessário informar a Data de Publicação.");
			}
			if(entidade.getContarDiasEmDobro() != null && entidade.getContarDiasEmDobro().intValue() > 0){
				throw new SRHRuntimeException("Quando \"Contar Dias em Dobro\" for maior que zero, é necessário informar a Data de Publicação.");
			}
		}

		// validando o nr do processo
		if (entidade.getNrprocesso() != null && !entidade.getNrprocesso().equals("")) {
			if (!SRHUtils.validarProcesso(SRHUtils.formatatarDesformatarNrProcessoPadraoSAP(entidade.getNrprocessoPuro(), 0))){
				throw new SRHRuntimeException("O Número do Processo informado é inválido.");
			}
		}		
		

		
	}


	/**
	 * Regra de Negocio:
	 * 
	 * Validar conforme o sexo valido do tipo da licenca
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarConformeSexo(Licenca entidade) {

		// verificando se eh para ambos
		if ( !entidade.getTipoLicenca().getSexoValido().equalsIgnoreCase("A") ) {

			// verificando se eh diferente do sexo da pessoa selecionada
			if ( !entidade.getTipoLicenca().getSexoValido().equals( entidade.getPessoal().getSexo() )) {
				throw new SRHRuntimeException("Este Tipo de Licença não é compatível com o sexo da pessoa selecionada.");
			}

		}		
	}


	/**
	 * Regra de Negocio:
	 * 
	 * Validar a quantidade maxima de dias permitida conforme o tipo de licenca selecionada.
	 * @param entidade 
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarMaxDiasPermitidoPorTipoLicenca(Licenca entidade) {

		int qtdDias = SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim());
		
		if ( qtdDias > entidade.getTipoLicenca().getQtdeMaximoDias() )
			throw new SRHRuntimeException("Quantidade de dias maior que o máximo permitido para esse tipo de licença.");

	}


	/**
	 * Regra de Negocio:
	 * 
	 * Validar se o período Inicial esta dentro dos anos iniciais e finais da licenca especial.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarPeriodoInicioLicencaEspecial(Licenca entidade) {

		// verificando se eh licenca especial
		if ( entidade.getTipoLicenca().isEspecial() ) {

			Calendar calendarInicio = new GregorianCalendar();
			calendarInicio.setTime( entidade.getInicio() );

			LicencaEspecial especial = licencaEspecialService.getById( entidade.getLicencaEspecial().getId() );

			// validando periodo inicial
			if( calendarInicio.get( GregorianCalendar.YEAR ) < especial.getAnofinal() ) {
				throw new SRHRuntimeException("O ano da Data Inicial deve ser maior ou igual ao ano final do período aquisitivo da licença especial selecionada.");
			}

		}
	}


	/**
	 * Regra de Negocio:
	 * 
	 * O Período Inicial e Final deve ser checado para saber se já foi lançada alguma licença 
	 * para o servidor neste período.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificarDatasDeLicencasAntigas(Licenca entidade) throws SRHRuntimeException {

		List<Licenca> licencas = dao.findByPessoa(entidade.getPessoal().getId());

		// percorrendo licencas antigas
		for (Licenca licencaAntigas: licencas) {

			if ( entidade.getId() == null || !entidade.getId().equals(licencaAntigas.getId()) ) {

				// validando periodo inicial
				if( (licencaAntigas.getInicio().after(entidade.getInicio()) || licencaAntigas.getInicio().equals(entidade.getInicio())) 
						&& ( licencaAntigas.getInicio().before(entidade.getFim()) || licencaAntigas.getInicio().equals(entidade.getFim()))){
					throw new SRHRuntimeException("Já existe uma licença com esse período.");
				}

				// validando periodo final
				if( (licencaAntigas.getFim().after(entidade.getInicio()) || licencaAntigas.getFim().equals(entidade.getInicio())) 
						&& ( licencaAntigas.getFim().before(entidade.getFim()) || licencaAntigas.getFim().equals(entidade.getFim()))){
					throw new SRHRuntimeException("Já existe uma licença com esse período.");
				}

				// validando periodo no meio
				if( licencaAntigas.getInicio().before( entidade.getInicio() )
						&& licencaAntigas.getFim().after( entidade.getFim() ) ) {
					throw new SRHRuntimeException("Já existe uma licença com esse período.");
				}
			}
		}
	}


	public void setDAO(LicencaDAO licencaDAO) {	this.dao = licencaDAO; }
	public void setLicencaEspecialService(LicencaEspecialService licencaEspecialService) {this.licencaEspecialService = licencaEspecialService;}
	public void setTipoLicencaService(TipoLicencaService tipoLicencaService) {this.tipoLicencaService = tipoLicencaService;}


	@Override
	public Licenca getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<Licenca> search(Funcional funcional, List<Integer> listaCodigo) {
		// TODO Auto-generated method stub
		return dao.search(funcional, listaCodigo);
	}

}
