package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AtribuicaoSetorDAO;
import br.gov.ce.tce.srh.domain.AtribuicaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("atribuicaoSetorService")
public class AtribuicaoSetorServiceImpl implements AtribuicaoSetorService{
	
	@Autowired
	private AtribuicaoSetorDAO dao;
	
	@Override
	public int count(Setor setor, int opcaoAtiva) {
		return dao.count(setor, opcaoAtiva);
	}

	@Override
	public List<AtribuicaoSetor> search(Setor setor, int opcaoAtiva, int first, int rows) {
		return dao.search(setor, opcaoAtiva, first, rows);
	}

	@Override
	@Transactional
	public void salvar(AtribuicaoSetor entidade) throws SRHRuntimeException {

		if(entidade.getDescricao().length() > 1000){
			throw new SRHRuntimeException("O campo Descrição ultrapassou o limite de 1000 caracteres.");
		}
		
		if (entidade.getInicio() != null && entidade.getFim() != null) {
			if ( SRHUtils.adiconarSubtrairDiasDeUmaData(entidade.getInicio(), 1).after(entidade.getFim()) )
				throw new SRHRuntimeException("A Data Fim deve ser maior que a Data Início.");
		}
		
		List<AtribuicaoSetor> atribuicaoSetorList = findBySetor(entidade.getSetor());
		
		for (AtribuicaoSetor atribuicaoSetor : atribuicaoSetorList) {
			if(entidade.getTipo() == 1 && entidade.getFim() == null && atribuicaoSetor.getTipo() == 1 && atribuicaoSetor.getFim() == null
					&& ( entidade.getId() == null || entidade.getId() != atribuicaoSetor.getId() ) )
				throw new SRHRuntimeException("Já existe Atribuição Geral ativa cadastrada para este setor.");
		}
		
		dao.salvar(entidade);
	}

	@Override
	@Transactional
	public void excluir(AtribuicaoSetor entidade) {
		dao.excluir(entidade);		
	}

	@Override
	public List<AtribuicaoSetor> findAll() {		
		return dao.findAll();
	}

	@Override
	public AtribuicaoSetor findById(Long id) {		
		return dao.findById(id);
	}

	@Override
	public List<AtribuicaoSetor> findBySetor(Setor setor) {		
		return dao.findBySetor(setor);		
	}
	
}
