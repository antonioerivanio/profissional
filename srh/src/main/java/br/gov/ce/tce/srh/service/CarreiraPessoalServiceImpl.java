package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CarreiraPessoalDAO;
import br.gov.ce.tce.srh.domain.CarreiraPessoal;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("carreiraPessoalService")
public class CarreiraPessoalServiceImpl implements CarreiraPessoalService {

	@Autowired
	private CarreiraPessoalDAO dao;
	
	@Autowired
	private FuncionalService funcionalService;

	@Override
	@Transactional
	public CarreiraPessoal salvar(CarreiraPessoal entidade) throws SRHRuntimeException {

		validarDadosObrigatorios(entidade);
		
		List<CarreiraPessoal> carreiras = dao.findByPessoal(entidade.getPessoal().getId());
		if(!carreiras.isEmpty() && carreiras.get(0).getFimCarreira() == null)
			throw new SRHRuntimeException("O último registro de Carreira do Servidor não foi finalizado.");
		
		
		List<Funcional> funcionais = funcionalService.findByPessoal(entidade.getPessoal().getId(), "ASC");
		
		if(entidade.getInicioCarreira().before(funcionais.get(0).getExercicio()))
			throw new SRHRuntimeException("O início da carreira não pode ser anterior ao Exercício da primeira nomeação do Servidor.");		
		
		
		return dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(CarreiraPessoal entidade) throws SRHRuntimeException {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long pessoal) {
		return dao.count(pessoal);
	}


	@Override
	public List<CarreiraPessoal> search(Long pessoal, int first, int rows) {
		return dao.search(pessoal, first, rows);
	}


	@Override
	public List<CarreiraPessoal> findByPessoal(Long idPessoa){
		return dao.findByPessoal(idPessoa);
	}

	
	private void validarDadosObrigatorios(CarreiraPessoal entidade) {

		if (entidade.getPessoal() == null)
			throw new SRHRuntimeException("O Pessoal é obrigatório. Digite o CPF ou nome e efetue a pesquisa.");
		
		if (entidade.getCarreira() == null)
			throw new SRHRuntimeException("A Carreira é obrigatória.");
		
		if (entidade.getInicioCarreira() == null)
			throw new SRHRuntimeException("A Data Início Carreira é obrigatória.");

		if (entidade.getOcupacao() == null)
			throw new SRHRuntimeException("A Ocupação é obrigatória.");
		
		if (entidade.getInicioCargoAtual() == null)
			throw new SRHRuntimeException("A Data Início Cargo Atual é obrigatória.");		

	}

}
