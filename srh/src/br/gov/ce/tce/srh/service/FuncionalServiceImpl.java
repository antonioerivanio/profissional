package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("funcionalService")
public class FuncionalServiceImpl implements FuncionalService {

	@Autowired
	private FuncionalDAO dao;
	
	@Autowired 
	private ReferenciaFuncionalService referenciaFuncionalService;

	@Autowired
	private FuncionalSetorService funcionalSetorService;



	@Override
	public Funcional salvar(Funcional entidade) {
		return dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Funcional entidade) {

		// verificar se data saida eh NULA
		if ( entidade.getSaida() != null )
			throw new SRHRuntimeException("A nomeação não pode ser excluida pois ja foi exonerada.");

		// verificando quantidade de referencias funcionais
		List<ReferenciaFuncional> listaReferencias = referenciaFuncionalService.findByFuncional( entidade.getId() );
		if ( listaReferencias.size() > 1 )
			throw new SRHRuntimeException("A nomeação não pode ser excluida pois ja foi teve progressão.");

		// excluindo a referencia funcional
		referenciaFuncionalService.excluirAll( entidade.getId() );

		// excluindo historico de lotacao
		funcionalSetorService.excluirAll( entidade.getId() );

		// excluindo a nomeacao
		dao.excluir(entidade);
	}


	@Override
	public Funcional getByPessoaAtivo(Long idPessoa) {
		return dao.getByPessoaAtivo(idPessoa);
	}


	@Override
	public Funcional getByMatriculaAtivo(String matricula) {
		return dao.getByMatriculaAtivo(matricula);
	}


	@Override
	public List<String> findByNome(String nome) {
		return dao.findByNome(nome);
	}
	
	
	@Override
	public List<String> findByUsuariologado(Usuario usuarioLogado) {
		return dao.findByUsuariologado(usuarioLogado);
	}


	@Override
	public List<Funcional> findByPessoal(Long idPessoal, String orderBy) {
		return dao.findByPessoal(idPessoal, orderBy);
	}


	@Override
	public Funcional getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public String getMaxMatricula(){
		return dao.getMaxMatricula();
	}


	@Override
	public int count(Long pessoal, String orderBy) {
		return dao.count(pessoal, orderBy);
	}

	@Override
	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows) {
		return dao.search(pessoal, orderBy, first, rows);
	}
	
	@Override
	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy, int first, int rows) {
		return dao.searchForReclassificacao(pessoal, orderBy, first, rows);
	}


	@Override
	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula) {
		return dao.getCpfAndNomeByMatriculaAtiva(matricula);
	}


	@Override
	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf) {
		return dao.getMatriculaAndNomeByCpfAtiva(cpf);
	}


	@Override
	public Funcional getMatriculaAndNomeByCpf(String cpf) {
		return dao.getMatriculaAndNomeByCpf(cpf);
	}


	public void setDAO(FuncionalDAO dao) {this.dao = dao;}
	
	@Override
	public Funcional getCpfAndNomeByMatricula(String matricula) {
		return dao.getCpfAndNomeByMatricula(matricula);
	}

	@Override
	public List<String> findAllByNome(String nome) {
		return dao.findAllByNome(nome);
	}
	
}
