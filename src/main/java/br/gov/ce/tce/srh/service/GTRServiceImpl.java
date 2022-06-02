package br.gov.ce.tce.srh.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.GTRDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.GTR;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("gtrService")
public class GTRServiceImpl implements GTRService {


	@Autowired
	private GTRDAO dao;

	@Autowired
	private FuncionalService funcionalService;

	@Override
	@Transactional
	public void salvar(GTR entidade) throws SRHRuntimeException {

		validaDadosObrigatorios(entidade);

		validaDados(entidade);

		dao.salvar(entidade);
	}

	private void validaDados(GTR entidade) {

		if(entidade.getFim() != null && entidade.getInicio().after(entidade.getFim())) {
			throw new SRHRuntimeException("O fim não pode ser antes do início");
		}

		List<GTR> g = findByPessoal(entidade.getFuncional().getPessoal().getId());
		for (GTR gtr : g) {
			if(gtr.getFim() == null) {
				throw new SRHRuntimeException("O servidor já tem gtr em aberto");
			}
		}
	}

	@Override
	@Transactional
	public void excluir(GTR entidade) {
		dao.excluir(entidade);		
	}	

	@Override
	public int count(Long idPessoal) {
		
		int i = dao.count(idPessoal);
		
		return i;
	}

	@Override
	public List<GTR> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal, first, rows);
	}

	@Override
	public List<GTR> findByPessoal(Long idPessoal) {
		return dao.findByPessoal(idPessoal);
	}


	private void validaDadosObrigatorios(GTR entidade) throws SRHRuntimeException {
		
		if (entidade.getFuncional() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");
		
		if ( entidade.getInicio() == null )
			throw new SRHRuntimeException("A Data Inicial é obrigatória.");

	}	
		
	public void setDAO(GTRDAO dao){this.dao = dao;}

}
