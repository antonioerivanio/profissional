package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PessoalDAO;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * 
 * @author robstown
 *
 */
@Service("pessoalService")
public class PessoalServiceImpl implements PessoalService {

	@Autowired
	private PessoalDAO dao;


	@Override
	@Transactional
	public Pessoal salvar(Pessoal entidade) throws SRHRuntimeException {

		// validar dados
		validarDados(entidade);

		entidade.setNomePesquisa(entidade.getNomeCompleto());

		/*
		 * Regra:
		 * 
		 * Nao deixar cadastrar CPF ja existente
		 */
		verificandoCPFexiste( entidade );
		
		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar PIS/PASEP ja existente.
		 * 
		 */
		verificandoPASEPExiste( entidade );

		// persistindo
		return dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Pessoal entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String nome, String cpf) {
		return dao.count(nome, cpf);
	}
	
	@Override
	public int count(Usuario usuarioLogado) {
		return dao.count(usuarioLogado);
	}


	@Override
	public List<Pessoal> search(String nome, String cpf, int first, int rows) {
		return dao.search(nome, cpf, first, rows);
	}
	
	@Override
	public List<Pessoal> search(Usuario usuarioLogado, int first, int rows) {
		return dao.search(usuarioLogado, first, rows);
	}


	@Override
	public Pessoal getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public Pessoal getByCpf(String cpf) {
		return dao.getByCPf(cpf);
	}


	@Override
	public List<Pessoal> findByNome(String nome) {
		return dao.findByNome(nome);
	}

	
	@Override
	public List<Pessoal> findServidorByNome(String nome) {
		return dao.findServidorByNome(nome);
	}


	/**
	 * Validar:
	 *
	 * · Deve ser setado o nome
	 * · Deve ser setado o nome completo
	 * · Deve ser setado o sexo.
	 * · Validar o CPF.
	 * · Validar a agencia.
	 * · Validar a conta corrente.
	 * · Validar o PIS/PASEP.
	 * · Validar numero do processo.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(Pessoal entidade) {

		// validando o nome
		if ( entidade.getNome() == null || entidade.getNome().equalsIgnoreCase("") )
			throw new SRHRuntimeException("O nome é obrigatório.");

		// validando o nome completo
		if ( entidade.getNomeCompleto() == null || entidade.getNomeCompleto().equalsIgnoreCase("") )
			throw new SRHRuntimeException("O nome completo é obrigatório.");

		// validando o sexo
		if ( entidade.getSexo() == null || entidade.getSexo().equalsIgnoreCase("") )
			throw new SRHRuntimeException("O sexo é obrigatório.");

		//validando cpf
		if( entidade.getCpf() == null || entidade.getCpf().equals("") )
			throw new SRHRuntimeException("O CPF é obrigatório.");

		//validando digito verificador do cpf
		if( !SRHUtils.validarCPF( entidade.getCpf() ) )
			throw new SRHRuntimeException("CPF inválido.");

		//validando agencia Bradesco
		if( entidade.getAgenciaBbd() != null && !entidade.getAgenciaBbd().equals("") ) {
			if( !SRHUtils.validarDVAgenciaBradesco( entidade.getAgenciaBbd() ) ) {
				throw new SRHRuntimeException("Agência inválida.");
			}
		}

		//validando conta corrente Bradesco
		if( entidade.getContaBbd() != null && !entidade.getContaBbd().equals("") ) {
			if( !SRHUtils.validarDVContaCorrenteBradesco( entidade.getContaBbd() ) ) {
				throw new SRHRuntimeException("Conta Corrente inválida.");
			}
		}
		
		//validando PIS/PASEP (NIT: Número de Identificação do Trabalhador)
		if( entidade.getPasep() != null && !entidade.getPasep().equals("") ) {
			if( !SRHUtils.validarPisPasep( entidade.getPasep() ) ) {
				throw new SRHRuntimeException("PIS/PASEP inválido.");
			}
		}

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe pessoa com o mesmo CPF
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoCPFexiste(Pessoal entidade) {

		Pessoal entidadeJaExiste = dao.getByCPf( entidade.getCpf() );
		
		// quando for inserir
		if ( entidade.getId() == null && entidadeJaExiste != null )
			throw new SRHRuntimeException("CPF já cadastrado. Operação cancelada.");
		
		// quando for alterar
		if ( entidade.getId() != null && !entidade.getId().equals(new Long(entidadeJaExiste.getId())))
			throw new SRHRuntimeException("CPF já cadastrado. Operação cancelada.");		

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe pessoa com o mesmo PIS/PASEP
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoPASEPExiste(Pessoal entidade) {

		if ( entidade.getPasep() != null && !entidade.getPasep().equals("") ) {

			Pessoal entidadeJaExiste = dao.getByPasep( entidade.getPasep() );
			if ( entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()) )
				throw new SRHRuntimeException("PIS/PASEP já cadastrado. Operação cancelada.");

		}
		
	}


	public void setDAO(PessoalDAO pessoalDAO) {this.dao = pessoalDAO;}


	@Override
	public List<Pessoal> findByCategoria(Long idCategoria) {
		return dao.findByCategoria(idCategoria);
	}

}
