package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AbonoPermanenciaDAO;
import br.gov.ce.tce.srh.domain.AbonoPermanencia;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("AbonoPermanenciaService")
public class AbonoPermanenciaServiceImpl implements AbonoPermanenciaService{

	@Autowired
	private AbonoPermanenciaDAO abonoPermanenciaDAO;
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Override
	@Transactional	
	public void salvar(AbonoPermanencia entidade, boolean alterar) throws SRHRuntimeException {
		validarDados(entidade, alterar);
		
		corrigeNumeroProcesso(entidade);
		
		atualizarFuncional(entidade);		
		
		abonoPermanenciaDAO.salvar(entidade);		
	}

	private void corrigeNumeroProcesso(AbonoPermanencia entidade) {
		entidade.setProcesso(SRHUtils.formatatarDesformatarNrProcessoPadraoSAP(SRHUtils.removerMascara(entidade.getProcesso()), 0));
	}

	private void atualizarFuncional(AbonoPermanencia entidade) {
		Funcional funcional = funcionalService.getById(entidade.getFuncional().getId());
		funcional.setAbonoPrevidenciario(true);
		
		funcionalService.salvar(funcional);
	}

	@Override
	@Transactional
	public void excluir(AbonoPermanencia entidade) {		
		abonoPermanenciaDAO.excluir(entidade);		
	}

	@Override	
	public List<AbonoPermanencia> findAll() {
		return abonoPermanenciaDAO.findAll();
	}

	@Override
	public AbonoPermanencia getById(Long id) {
		return abonoPermanenciaDAO.getById(id);
	}
	
	@Override
	public AbonoPermanencia getByPessoalId(Long pessoalId) {
		return abonoPermanenciaDAO.getByPessoalId(pessoalId);
	}

	@Override
	public List<AbonoPermanencia> search( String cpf, int first, int rows) {
		return abonoPermanenciaDAO.search(SRHUtils.removerMascara(cpf), first, rows);
	}
	
	private void validarDados(AbonoPermanencia entidade, boolean alterar) throws SRHRuntimeException{
		
		if(entidade.getFuncional() == null || entidade.getFuncional().getId() == 0L)
			throw new SRHRuntimeException("O Funcional é obrigatório. Digite o nome do Servidor, efetue a pesquisa e selecione o registro Funcional.");		
				
		if (!alterar){
			if(abonoPermanenciaDAO.getByPessoalId(entidade.getFuncional().getPessoal().getId()) != null)
				throw new SRHRuntimeException("O Servidor já possui Abono de Permanência cadastrado.");
		}
		
		if(entidade.getProcesso() == null || entidade.getProcesso().isEmpty()) {
			throw new SRHRuntimeException("O Processo é obrigatório.");
		}
		
		if (!SRHUtils.validarProcesso( SRHUtils.removerMascara(entidade.getProcesso()) ) ) {
			throw new SRHRuntimeException("O Número do Processo informado é inválido.");
		}
		
		if(entidade.getDataImplantacao() == null) {
			throw new SRHRuntimeException("A Data de implantação é obrigatório.");
		}
		
		if(entidade.getDataImplantacao().before(entidade.getFuncional().getExercicio()) || entidade.getDataImplantacao().after(new Date())) {
			throw new SRHRuntimeException("A Data de implantação não pode ser menor do que a data de exercício nem maior do que a data atual.");		
		}
		
	}	

}
