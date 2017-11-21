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
				
		List<Funcional> funcionais = funcionalService.findByPessoal(entidade.getPessoal().getId(), "ASC");
		if(entidade.getInicioCarreira().before(funcionais.get(0).getExercicio()))
			throw new SRHRuntimeException("A Data Início Carreira não pode ser anterior ao Exercício da primeira nomeação do Servidor.");
		
		if(entidade.getInicioCargoAtual().before(funcionais.get(0).getExercicio()))
			throw new SRHRuntimeException("A Data Início Cargo Atual não pode ser anterior ao Exercício da primeira nomeação do Servidor.");
		
								
		List<CarreiraPessoal> carreiras = dao.search(entidade.getPessoal().getId(), null, null);
		if(!carreiras.isEmpty() && carreiras.get(0).getFimCarreira() == null && carreiras.get(0).getId().intValue() != entidade.getId().intValue())
			throw new SRHRuntimeException("O último registro de Carreira do Servidor não foi finalizado.");
		
		
		for (CarreiraPessoal carreira: carreiras) {
			
			if ( entidade.getId() == null || entidade.getId().intValue() != carreira.getId().intValue() ) {

				if( !entidade.getInicioCarreira().before(carreira.getInicioCarreira()) 
						&&  !entidade.getInicioCarreira().after(carreira.getFimCarreira()) ){
					throw new SRHRuntimeException("Existe uma carreira neste período.");
				}
				
				if (entidade.getFimCarreira() != null) {
					
					if( !entidade.getFimCarreira().before(carreira.getInicioCarreira()) 
							&&  !carreira.getFimCarreira().after(carreira.getFimCarreira()) ){
						throw new SRHRuntimeException("Existe uma carreira neste período.");
					}				

					if( entidade.getInicioCarreira().before( carreira.getInicioCarreira() )
							&& entidade.getFimCarreira().after( carreira.getFimCarreira() ) ) {
						throw new SRHRuntimeException("Existe uma carreira neste período.");
					}
				
				}				
			}
		}		
		
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
	public List<CarreiraPessoal> search(Long pessoal, Integer first, Integer rows) {
		return dao.search(pessoal, first, rows);
	}

	
	private void validarDadosObrigatorios(CarreiraPessoal entidade) {

		if (entidade.getPessoal() == null)
			throw new SRHRuntimeException("O Pessoal é obrigatório. Digite o CPF ou nome e efetue a pesquisa.");
		
		if (entidade.getCarreira() == null)
			throw new SRHRuntimeException("A Carreira é obrigatória.");
		
		if (entidade.getInicioCarreira() == null)
			throw new SRHRuntimeException("A Data Início Carreira é obrigatória.");
		
		if (entidade.getFimCarreira() != null && entidade.getInicioCarreira().after(entidade.getFimCarreira()))
			throw new SRHRuntimeException("A Data Fim Carreira não pode ser anterior a A Data Início Carreira.");

		if (entidade.getOcupacao() == null)
			throw new SRHRuntimeException("A Ocupação é obrigatória.");
		
		if (entidade.getInicioCargoAtual() == null)
			throw new SRHRuntimeException("A Data Início Cargo Atual é obrigatória.");
		
		if (entidade.getFimCargoAtual() != null && entidade.getInicioCargoAtual().after(entidade.getFimCargoAtual()))
			throw new SRHRuntimeException("A Data Fim Cargo Atual não pode ser anterior a A Data Início Cargo Atual.");

	}

}
