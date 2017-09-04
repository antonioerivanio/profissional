package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.OcupacaoDAO;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("ocupacaoService")
public class OcupacaoServiceImpl implements OcupacaoService {

	@Autowired
	private OcupacaoDAO dao;

	@Autowired
	private SimboloService simboloService;

	@Autowired
	private EspecialidadeCargoService especialidadeCargoService;

	@Override
	public Ocupacao salvar(Ocupacao entidade) {
		return dao.salvar(entidade);
	}	

	@Override
	@Transactional
	public void salvar(Ocupacao entidade, List<EspecialidadeCargo> especialidades, List<Simbolo> simbologias) 
			throws SRHRuntimeException {

		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
		verificandoSeEntidadeExiste(entidade);

		// salvando a ocupacao
		entidade = dao.salvar(entidade);

		// salvando as simbologias
		for( Simbolo simbolo : simbologias ) {
			if (simbolo.getId() == null) {
				simbolo.setOcupacao(entidade);
				simboloService.salvar(simbolo);	
			}
		}

		// salvando as especialidades
		for( EspecialidadeCargo especialidade : especialidades ) {
			if (especialidade.getId() == null) {
				especialidade.setOcupacao(entidade);
				especialidadeCargoService.salvar(especialidade);	
			}
		}

	}


	@Override
	@Transactional
	public void excluir(Ocupacao entidade) {

		// excluindo todas as simbologias da ocupacao
		simboloService.excluirAll(entidade.getId());

		// excluindo todas as especialidade da ocupacao
		especialidadeCargoService.excluirAll(entidade.getId());

		// excluindo a ocupacao
		dao.excluir(entidade);
	}

	
	@Override
	public int count(String nomenclatura) {
		return dao.count(nomenclatura);
	}


	@Override
	public int count(String nomenclatura, Long situacao) {
		return dao.count(nomenclatura, situacao);
	}

	
	@Override
	public List<Ocupacao> search(String nomenclatura, int first, int rows) {
		return dao.search(nomenclatura, first, rows);
	}


	@Override
	public List<Ocupacao> search(String nomenclatura, Long situacao, int first, int rows) {
		return dao.search(nomenclatura, situacao, first, rows);
	}


	@Override
	public Ocupacao getById(Long idOcupacao) {
		return dao.getById(idOcupacao);
	}


	@Override
	public List<Ocupacao> findAll() {
		return dao.findAll();
	}


	@Override
	public List<Ocupacao> findByTipoOcupacao(Long tipoOcupacao) {
		return dao.findByTipoOcupacao(tipoOcupacao);
	}

	@Override
	public List<Ocupacao> findByPessoa(Long idPessoal) {
		return dao.findByPessoa(idPessoal);
	}

	
	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe entidade cadastrada com a mesma NOMENCLATURA.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 *  
	 */
	private void verificandoSeEntidadeExiste(Ocupacao entidade) throws SRHRuntimeException {

		Ocupacao entidadeJaExiste = dao.getByNomenclatura(entidade.getNomenclatura());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Cargo já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(OcupacaoDAO dao) {this.dao = dao;}
	public void setSimboloService(SimboloService simboloService) {this.simboloService = simboloService;}
	public void setEspecialidadeCargoService(EspecialidadeCargoService especialidadeCargoService) {this.especialidadeCargoService = especialidadeCargoService;}

}
