package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAO;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("pessoalCursoAcademicaService")
public class PessoalCursoAcademicaServiceImpl implements PessoalCursoAcademicaService {

	@Autowired
	private PessoalCursoAcademicaDAO dao;

	@Autowired
	private CompetenciaGraduacaoService competenciaGraduacaoService;


	@Override
	@Transactional
	public void salvar(PessoalCursoAcademica pessoalCursoAcademica, List<CompetenciaGraduacao> listaCompetencias,
			List<CompetenciaGraduacao> listaCompetenciasExcluidas, Date inicioCompetencia, boolean alterar)
			throws SRHRuntimeException {

		// validando dados obrigatorios.
		validarDados(pessoalCursoAcademica, listaCompetencias, inicioCompetencia);

		pessoalCursoAcademica.getPk().setPessoal( pessoalCursoAcademica.getPessoal().getId() );
		pessoalCursoAcademica.getPk().setCursoAcademico( pessoalCursoAcademica.getCursoAcademica().getId() );

		// somente quando for insercao
		if (!alterar) {

			// verificando se o curso ja existe
			PessoalCursoAcademica existe = dao.getByCursoPessoa(pessoalCursoAcademica.getPk().getCursoAcademico(), pessoalCursoAcademica.getPk().getPessoal());
			if (existe != null)
				throw new SRHRuntimeException("Este curso já foi cadastrado para essa pessoa.");

		}

		// salvando os cursos da pessoa
		dao.salvar(pessoalCursoAcademica);

		// excluindo as competencias do curso
		if(listaCompetenciasExcluidas.size() > 0){
			for (CompetenciaGraduacao competenciaGraduacao : listaCompetenciasExcluidas) {
				competenciaGraduacaoService.excluir( pessoalCursoAcademica.getPessoal().getId(), pessoalCursoAcademica.getCursoAcademica().getId(), competenciaGraduacao.getCompetencia().getId());
			}
		}
		
		// salvando as competencias
		for (CompetenciaGraduacao competencia : listaCompetencias) {
			competencia.setInicioCompetencia( inicioCompetencia );
			competenciaGraduacaoService.salvar(competencia);
		}

	}


	@Override
	@Transactional
	public void excluir(PessoalCursoAcademica entidade) {

		// excluindo as competencias do curso
		competenciaGraduacaoService.excluir(entidade.getPk().getPessoal(), entidade.getPk().getCursoAcademico());

		// excluindo o curso das pessoas
		dao.excluir(entidade);
	}


	@Override
	public int count(Long pessoa) {
		return dao.count(pessoa);
	}
	
	@Override
	public int count(Usuario usuarioLogado) {
		return dao.count(usuarioLogado);
	}


	@Override
	public List<PessoalCursoAcademica> search(Long pessoa, int first, int rows) {
		return dao.search(pessoa, first, rows);
	}
	
	@Override
	public List<PessoalCursoAcademica> search(Usuario usuarioLogado, int first,	int rows) {
		return dao.search(usuarioLogado, first,	rows);
	}


	@Override
	public List<PessoalCursoAcademica> findByPessoa(Long pessoal) {
		return dao.findByPessoa(pessoal);
	}


	@Override
	public List<PessoalCursoAcademica> findByCursoAcademica(Long cursoAcademica) {
		return dao.findByCursoAcademica(cursoAcademica);
	}



	/**
	 * Validar:
	 *  Deve ser setado a pessoa.
	 *  Deve ser setado o curso.
	 *  Deve ser setado o instituicao.
	 *  Deve ser setado a data de conclusao do curso.
	 *  Deve ser cadastrado ao menos uma competencia.
	 * 
	 * @param inicioCompetencia 
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(PessoalCursoAcademica entidade, List<CompetenciaGraduacao> listaCompetencias, Date inicioCompetencia) throws SRHRuntimeException {

		// validando a entidade
		if (entidade == null)
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		// validando o servidor
		if ( entidade.getPessoal() == null || entidade.getPessoal().getId().equals(0l) )
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		// validando o curso
		if (entidade.getCursoAcademica() == null)
			throw new SRHRuntimeException("O curso é obrigatório.");

		// validando a instituicao
		if (entidade.getInstituicao() == null)
			throw new SRHRuntimeException("A instituição é obrigatória.");

		// validando se a data de conclusao foi preenchida
		if ( inicioCompetencia == null )
			throw new SRHRuntimeException("A data de conclusão do curso é obrigatória.");

//		// validando a lista de competencias
//		if (listaCompetencias == null || listaCompetencias.size() == 0)
//			throw new SRHRuntimeException("Nenhuma competência cadastrada, é obrigatório pelo menos uma competência.");

	}


	public void setDAO(PessoalCursoAcademicaDAO dao) {this.dao = dao;}
	public void setCompetenciaGraduacaoService(CompetenciaGraduacaoService competenciaGraduacaoService) {this.competenciaGraduacaoService = competenciaGraduacaoService;}

}
