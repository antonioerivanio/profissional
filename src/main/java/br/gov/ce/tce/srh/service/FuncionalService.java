package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;

@Service("funcionalService")
public class FuncionalService {

	@Autowired
	private FuncionalDAO dao;
	
	@Autowired 
	private ReferenciaFuncionalService referenciaFuncionalService;

	@Autowired
	private FuncionalSetorService funcionalSetorService;

	@Transactional
	public Funcional salvar(Funcional entidade) {
		return dao.salvar(entidade);
	}

	@Transactional
	public void excluir(Funcional entidade) {

		// verificar se data saida eh NULA
		if ( entidade.getSaida() != null )
			throw new SRHRuntimeException("A nomeação não pode ser excluída pois já foi exonerada.");

		// verificando quantidade de referencias funcionais
		List<ReferenciaFuncional> listaReferencias = referenciaFuncionalService.findByFuncional( entidade.getId() );
		if ( listaReferencias.size() > 1 )
			throw new SRHRuntimeException("A nomeação não pode ser excluída pois já foi teve progressão.");

		// excluindo a referencia funcional
		referenciaFuncionalService.excluirAll( entidade.getId() );

		// excluindo historico de lotacao
		funcionalSetorService.excluirAll( entidade.getId() );

		// excluindo a nomeacao
		dao.excluir(entidade);
	}

	public Funcional getByPessoaAtivo(Long idPessoa) {
		return dao.getByPessoaAtivo(idPessoa);
	}

	public Funcional getByMatriculaAtivo(String matricula) {
		return dao.getByMatriculaAtivo(matricula);
	}

	public List<Funcional> findByNome(String nome) {
		return dao.findByNome(nome);
	}
	
	public List<Funcional> findByUsuariologado(Usuario usuarioLogado) {
		return dao.findByUsuariologado(usuarioLogado);
	}

	public List<Funcional> findByPessoal(Long idPessoal, String orderBy) {
		return dao.findByPessoal(idPessoal, orderBy);
	}

	public Funcional getById(Long id) {
		return dao.getById(id);
	}

	public String getMaxMatricula(){
		return dao.getMaxMatricula();
	}

	public int count(Long pessoal, String orderBy) {
		return dao.count(pessoal, orderBy);
	}

	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows) {
		return dao.search(pessoal, orderBy, first, rows);
	}
	
	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy, int first, int rows) {
		return dao.searchForReclassificacao(pessoal, orderBy, first, rows);
	}

	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula) {
		return dao.getCpfAndNomeByMatriculaAtiva(matricula);
	}
	
	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf) {
		return dao.getMatriculaAndNomeByCpfAtiva(cpf);
	}

	public Funcional getMatriculaAndNomeByCpf(String cpf) {
		return dao.getMatriculaAndNomeByCpf(cpf);
	}

	public List<Funcional> getMatriculaAndNomeByCpfList(String cpf) {
		return dao.getMatriculaAndNomeByCpfList(cpf);
	}

	public void setDAO(FuncionalDAO dao) {this.dao = dao;}
	
	public Funcional getCpfAndNomeByMatricula(String matricula) {
		return dao.getCpfAndNomeByMatricula(matricula);
	}

	public List<Funcional> findAllByNome(String nome) {
		return dao.findAllByNome(nome);
	}
	
	public List<Funcional> findAllAtivos() {
		return dao.findAllAtivos();
	}
	
	public int countResponsavelSetor(long idFuncional, Long idSetor) {
		return dao.countResponsavelSetor(idFuncional, idSetor);
	}

	public List<Funcional> findServidoresEvento2200() {		
		return dao.findServidoresEvento2200();
	}
	
	public List<Funcional> findServidoresEvento2230() {		
		return dao.findServidoresEvento2230();
	}

	public List<Funcional> findBeneficiariosEvento2400() {
		return dao.findBeneficiariosEvento2400();
	}
	
	public List<Funcional> findEstagiarioservidoresEvento2300() {
		return dao.findEstagiariosEventos2300();
	}

	public List<Funcional> findBeneficiariosEvento2410() {
		return dao.findBeneficiariosEvento2410();
	}

	public List<Funcional> findServidoresEvento1200(String anoReferencia, String mesReferencia) {
		return dao.findServidoresEvento1200(anoReferencia, mesReferencia);
	}
	
}
