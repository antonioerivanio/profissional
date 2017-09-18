package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CursoProfissionalDAO;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.enums.EnumTipoCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * 
 * @author robstown
 *
 */
@Service("cursoProfissionalService")
public class CursoProfissionalServiceImpl implements CursoProfissionalService {

	@Autowired
	private CursoProfissionalDAO dao;


	@Override
	@Transactional
	public void salvar(CursoProfissional entidade) throws SRHRuntimeException {

		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
		verificandoSeEntidadeExiste(entidade);
		
		
		validarCurso(entidade);
		
		
		dao.salvar(entidade);
	}


	private void validarCurso(CursoProfissional entidade) {
		
		if (entidade.getInicio() != null ){
		
			
			if ( entidade.getFim() != null ){
			
				if ( entidade.getInicio().after(entidade.getFim() ) )
					throw new SRHRuntimeException("O período início deve ser menor que o período fim.");			
			
				
				int qtdeDias = SRHUtils.dataDiff(entidade.getInicio(), entidade.getFim());
				
				
				if( entidade.getPosGraduacao().equals(EnumTipoCursoProfissional.ESPECIALIZACAO) && qtdeDias < 90 ) {
					
					throw new SRHRuntimeException("O período deve ter no mínimo 90 dias para cursos de especialização.");
				
				} else if ( (entidade.getPosGraduacao().equals(EnumTipoCursoProfissional.MESTRADO)
								|| entidade.getPosGraduacao().equals(EnumTipoCursoProfissional.DOUTORADO)) 
						&& qtdeDias < 365){
					
					throw new SRHRuntimeException("O período deve ter no mínimo 365 dias para cursos de mestrado e doutorado.");
					
				}				
			
			}
			
			
			if ( ! entidade.getInicio().after(SRHUtils.inicioTCE()) ) {
				throw new SRHRuntimeException("O período início deve ser maior que 05/10/1935.");
			}
		
		}
		
		
		if ( entidade.getPosGraduacao().equals(EnumTipoCursoProfissional.ESPECIALIZACAO) && entidade.getCargaHoraria() < 360 ){
			throw new SRHRuntimeException("A carga horária mínima para cursos de pós-graduação é de 360 horas.");
		}
		
		
		
		
	}


	@Override
	@Transactional
	public void excluir(CursoProfissional entidade) {
		dao.excluir(entidade);
	}

	@Override
	public int count(String descricao, Long idArea, Date inicio, Date fim) {
		return dao.count(descricao, idArea, inicio, fim);
	}


	@Override
	public List<CursoProfissional> search(String descricao, Long idArea, Date inicio, Date fim, int first, int rows) {
		return dao.search(descricao, idArea, inicio, fim, first, rows);
	}


	@Override
	public CursoProfissional getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<CursoProfissional> findByArea(Long area) {
		return dao.findByArea(area);
	}


	@Override
	public List<CursoProfissional> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe curso profissional cadastrada com a mesma DESCRICAO e AREA e INSTITUICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(CursoProfissional entidade) throws SRHRuntimeException {

		CursoProfissional entidadeJaExiste = dao.getByCursoAreaInstituicao( entidade.getDescricao(), entidade.getArea().getId(), entidade.getInstituicao().getId() );
		if (entidadeJaExiste != null && !entidadeJaExiste.getId().equals(entidade.getId()))
			throw new SRHRuntimeException("Curso de Formação Profissional já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(CursoProfissionalDAO cursoProfissionalDAO) { this.dao = cursoProfissionalDAO; }

}
