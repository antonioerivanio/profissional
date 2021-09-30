package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PessoalDAO;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service
public class PessoalService {

	@Autowired
	private PessoalDAO dao;


	@Transactional
	public Pessoal salvar(Pessoal entidade) throws SRHRuntimeException {
		
		SRHUtils.trimReflective(entidade);

		validarDados(entidade);		
		
		verificandoCPFexiste( entidade );
				
		verificandoPASEPExiste( entidade );
		
		//Remove espaços
		entidade.setNomeCompleto(entidade.getNomeCompleto().replaceAll(" +", " "));
		entidade.setNome(entidade.getNomeCompleto());
		if (entidade.getAbreviatura() != null)
			entidade.setAbreviatura(entidade.getAbreviatura().replaceAll(" +", " "));
		
		entidade.setNomePesquisa(entidade.getNomeCompleto());
				
		//Remover acentos e caixa baixa para nome pesquisa
		entidade.setNome(entidade.getNome().toUpperCase());
		entidade.setNomeCompleto( SRHUtils.removerAcentos(entidade.getNomeCompleto().toUpperCase()));
		entidade.setNomePesquisa( SRHUtils.removerAcentos(entidade.getNomePesquisa().toLowerCase()));

		//Remover mascaras. Exceto RG e Documento Militar
		entidade.setCpf(SRHUtils.removerMascara(entidade.getCpf()));
		entidade.setPasep(SRHUtils.removerMascara(entidade.getPasep()));
		entidade.setAgenciaBbd(SRHUtils.removerMascara(entidade.getAgenciaBbd()));
		entidade.setContaBbd(SRHUtils.removerMascara(entidade.getContaBbd()));
		entidade.setCep(SRHUtils.removerMascara(entidade.getCep()));
		entidade.setTelefone(SRHUtils.removerMascara(entidade.getTelefone()));
		entidade.setTelefoneAlternativo(SRHUtils.removerMascara(entidade.getTelefoneAlternativo()));
		entidade.setCelular(SRHUtils.removerMascara(entidade.getCelular()));		
		
		
		if (dao.verificarNomeExistente(entidade.getId(), entidade.getNomeCompleto()) && (entidade.getCpf() == null || entidade.getCpf().isEmpty()))
			throw new SRHRuntimeException("Nome já cadastrado sem CPF. Operação cancelada.");		
		
		
		return dao.salvar(entidade);
	
	}


	@Transactional
	public void excluir(Pessoal entidade) {
		dao.excluir(entidade);
	}


	public int count(String nome, String cpf) {
		return dao.count(nome, cpf);
	}
	
	
	public int count(Usuario usuarioLogado) {
		return dao.count(usuarioLogado);
	}


	public List<Pessoal> search(String nome, String cpf, int first, int rows) {
		return dao.search(nome, cpf, first, rows);
	}
	
	
	public List<Pessoal> search(Usuario usuarioLogado, int first, int rows) {
		return dao.search(usuarioLogado, first, rows);
	}


	public Pessoal getById(Long id) {
		return dao.getById(id);
	}


	public Pessoal getByCpf(String cpf) {
		return dao.getByCPf(cpf);
	}


	public List<Pessoal> findByNome(String nome) {
		return dao.findByNome(nome);
	}

	
	public List<Pessoal> findServidorByNomeOuCpf(String nome, String cpf) {
		return dao.findServidorByNomeOuCpf(nome, cpf);
	}


	private void validarDados(Pessoal entidade) {

		if(entidade.getCategoria() == null)
			throw new SRHRuntimeException("A Categoria é obrigatória.");
			
		if ( entidade.getNomeCompleto() == null || entidade.getNomeCompleto().isEmpty() )
			throw new SRHRuntimeException("O Nome é obrigatório.");

		if ( entidade.getSexo() == null || entidade.getSexo().isEmpty() )
			throw new SRHRuntimeException("O Sexo é obrigatório.");

		if( entidade.getNascimento() == null )
			throw new SRHRuntimeException("A Data Nascimento é obrigatória.");
		
		if( entidade.getPaisNascimento() == null )
			throw new SRHRuntimeException("O País Nascimento é obrigatório.");
		
		if( entidade.getPaisNacionalidade() == null )
			throw new SRHRuntimeException("O País Nacionalidade é obrigatório.");
		
		if(entidade.getPossuiVinculoSocietario() == null) {
			throw new SRHRuntimeException("É necessário informar se você possui Vínculo Societário.");
		}
		
		// Se país de nascimento for Brasil
		if ( entidade.getPaisNascimento().getId().equals(1L) ) {
			
			if ( entidade.getUf() == null )
				throw new SRHRuntimeException("A UF de nascimento é obrigatória.");
			
			if ( entidade.getMunicipioNaturalidade() == null )
				throw new SRHRuntimeException("A Naturalidade é obrigatória.");
		}
		
		// CPF é obrigatório para todos exceto para Dependente menor de 8 anos		
		if ( ! ( entidade.getCategoria().getId().equals(4L) && entidade.getIdade() < 8 ) ) {
			if( entidade.getCpf() == null || entidade.getCpf().isEmpty() )
				throw new SRHRuntimeException("O CPF é obrigatório.");
			
			if( !SRHUtils.validarCPF( entidade.getCpf() ) )
				throw new SRHRuntimeException("CPF inválido.");		
		}
		
		// Se for diferente de dependente e de Beneficiário pensão alimentícia
		if ( !entidade.getCategoria().getId().equals(4L)
				&& !entidade.getCategoria().getId().equals(5L) ) {
			
			if( entidade.getEscolaridade() == null )
				throw new SRHRuntimeException("A Escolaridade é obrigatória.");
			
			if( entidade.getTipoDeficiencia() == null )
				throw new SRHRuntimeException("O Tipo Deficiência é obrigatório.");
			
			if ( entidade.getRaca() == null )
				throw new SRHRuntimeException("A Raça/Cor é obrigatória.");
						
			if( entidade.getCep() == null || entidade.getCep().isEmpty() )
				throw new SRHRuntimeException("O CEP é obrigatório.");
			
			if ( entidade.getTipoLogradouro() == null )
				throw new SRHRuntimeException("O Tipo Logradouro é obrigatório.");
			
			if ( entidade.getEndereco() == null || entidade.getEndereco().isEmpty() )
				throw new SRHRuntimeException("A Descrição Logradouro é obrigatória.");
			
			if ( entidade.getNumero() == null || entidade.getNumero().isEmpty() )
				throw new SRHRuntimeException("O Número do endereço é obrigatório.");
			
			if ( entidade.getUfEndereco() == null )
				throw new SRHRuntimeException("O Estado do endereço é obrigatório.");
			
			if ( entidade.getMunicipioEndereco() == null )
				throw new SRHRuntimeException("O Município do endereço é obrigatório.");			
			
			if ( entidade.getTelefone() != null && !entidade.getTelefone().isEmpty()
					&& entidade.getTelefoneAlternativo() != null && !entidade.getTelefoneAlternativo().isEmpty()
					&& entidade.getTelefone().equals(entidade.getTelefoneAlternativo())) {
				throw new SRHRuntimeException("Telefone deve ser diferente do Telefone Alternativo.");
			}
			
			if ( entidade.getEmail() != null && !entidade.getEmail().isEmpty() && !SRHUtils.validarEmail(entidade.getEmail()))
				throw new SRHRuntimeException("E-mail com formato inválido.");
			
			if ( entidade.getEmailAlternativo() != null && !entidade.getEmailAlternativo().isEmpty() && !SRHUtils.validarEmail(entidade.getEmailAlternativo()))
				throw new SRHRuntimeException("E-mail Alternativo com formato inválido.");
			
			if ( entidade.getEmail() != null && !entidade.getEmail().isEmpty()
					&& entidade.getEmailAlternativo() != null && !entidade.getEmailAlternativo().isEmpty()
					&& entidade.getEmail().equals(entidade.getEmailAlternativo())) {
				throw new SRHRuntimeException("E-mail deve ser diferente do E-mail Alternativo.");
			}			
			
			// Se for Servidor Público
			if ( entidade.getCategoria().getId().equals(1L) ) {
				
				if( entidade.getPasep() == null || entidade.getPasep().isEmpty() )
					throw new SRHRuntimeException("O PIS/PASEP é obrigatório.");			
			}
			
			if( entidade.getPasep() != null && !entidade.getPasep().isEmpty() ) {
				if( !SRHUtils.validarPisPasep( entidade.getPasep() ) ) {
					throw new SRHRuntimeException("PIS/PASEP inválido.");
				}
			}
			
			validarRG(entidade);
			
			validarRegistroEmOrdemDeClasse(entidade);
			
			validarCTPS(entidade);
			
			validarCNH(entidade);			
			
			if( entidade.getAgenciaBbd() != null && !entidade.getAgenciaBbd().isEmpty() ) {
				if( !SRHUtils.validarDVAgenciaBradesco( entidade.getAgenciaBbd() ) ) {
					throw new SRHRuntimeException("Agência inválida.");
				}
			}
			
			if( entidade.getContaBbd() != null && !entidade.getContaBbd().isEmpty() ) {
				if( !SRHUtils.validarDVContaCorrenteBradesco( entidade.getContaBbd() ) ) {
					throw new SRHRuntimeException("Conta Corrente inválida.");
				}
			}						
			
		}		

	}


	private void validarCTPS(Pessoal entidade) {
		if ( ( (entidade.getNrCTPS() != null && !entidade.getNrCTPS().isEmpty()) 
				|| (entidade.getSerieCTPS() != null && !entidade.getSerieCTPS().isEmpty()) 					
				|| entidade.getUfCTPS() != null)
			&& (entidade.getNrCTPS() == null || entidade.getNrCTPS().isEmpty()
				|| entidade.getSerieCTPS() == null || entidade.getSerieCTPS().isEmpty()
				|| entidade.getUfCTPS() == null ) )
			throw new SRHRuntimeException("Caso sejam prenchidas Informações da Carteira de Trabalho e Previdência Social, todos os campos relacionados passam a ser obrigatórios.");
	}


	private void validarRG(Pessoal entidade) {
		if ( ( (entidade.getRg() != null && !entidade.getRg().isEmpty()) 
				|| entidade.getDataEmissaoRg() != null 
				|| (entidade.getEmissorRg() != null && !entidade.getEmissorRg().isEmpty())
				|| entidade.getUfEmissorRg() != null)
			&& (entidade.getRg() == null || entidade.getRg().isEmpty()
				|| entidade.getEmissorRg() == null || entidade.getEmissorRg().isEmpty()
				|| entidade.getUfEmissorRg() == null ) )
			throw new SRHRuntimeException("Caso sejam prenchidas Informações do Registro Geral, os campos RG, Emissor e UF passam a ser obrigatórios.");
	}


	private void validarRegistroEmOrdemDeClasse(Pessoal entidade) {
		
		if ( (entidade.getDataExpedicaoOrgaoClasse() != null 
				|| entidade.getDataValidadeOrgaoClasse() != null
				|| (entidade.getEmissorOrgaoClasse() != null && !entidade.getEmissorOrgaoClasse().isEmpty()) 
				|| (entidade.getNrOrgaoClasse() != null && !entidade.getNrOrgaoClasse().isEmpty()) 
				|| entidade.getUfOrgaoClasse() != null )
			&& (entidade.getNrOrgaoClasse() == null || entidade.getNrOrgaoClasse().isEmpty() 
				|| entidade.getEmissorOrgaoClasse() == null || entidade.getEmissorOrgaoClasse().isEmpty()
				|| entidade.getUfOrgaoClasse() == null ) )
			throw new SRHRuntimeException("Caso sejam prenchidas Informações do número de registro em Órgão de Classe, "
					+ "os campos Número Inscrição, Órgão Emissor e UF passam a ser obrigatórios.");
		
		if ( entidade.getDataExpedicaoOrgaoClasse() != null && entidade.getDataValidadeOrgaoClasse() != null )
			if ( !entidade.getDataExpedicaoOrgaoClasse().before(entidade.getDataValidadeOrgaoClasse()) )
				throw new SRHRuntimeException("A Data Validade do Registro em Órgão de Classe deve ser posterior a Data Expedição.");
		
	}


	private void validarCNH(Pessoal entidade) {
		
		if ( (entidade.getCategoriaCNH() != null 
				|| entidade.getDataExpedicaoCNH() != null 
				|| entidade.getDataPrimeiraCNH() != null 
				|| entidade.getDataValidadeCNH() != null
				|| (entidade.getNrCNH() != null && !entidade.getNrCNH().isEmpty()) 
				|| entidade.getUfCNH() != null) 
			&& (entidade.getCategoriaCNH() == null 
				|| entidade.getDataValidadeCNH() == null
				|| entidade.getNrCNH() == null || entidade.getNrCNH().isEmpty() 
				|| entidade.getUfCNH() == null))
			throw new SRHRuntimeException("Caso sejam prenchidas Informações da Carteira Nacional de Habilitação, "
					+ "os campos Número Registro, UF, Data Validade e Categoria passam a ser obrigatórios.");
		
		if ( entidade.getDataExpedicaoCNH() != null )
			if ( !entidade.getDataExpedicaoCNH().before(entidade.getDataValidadeCNH()) )
				throw new SRHRuntimeException("A Data Validade da CNH deve ser posterior a Data Expedição.");
		
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

		if ( entidade.getCpf() != null && !entidade.getCpf().equals("") ) {
			
			Pessoal entidadeJaExiste = dao.getByCPf( entidade.getCpf() );
			
			if (entidadeJaExiste != null){
				
				// quando for inserir
				if ( entidade.getId() == null )
					throw new SRHRuntimeException("CPF já cadastrado. Operação cancelada.");
				
				// quando for alterar
				if ( entidade.getId() != null && !entidade.getId().equals(new Long(entidadeJaExiste.getId())))
					throw new SRHRuntimeException("CPF já cadastrado. Operação cancelada.");
				
			}			
					
		}			

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


	public List<Pessoal> findAllComFuncional() {
		return dao.findAllComFuncional();
	}
	
	public List<Pessoal> findByCategoria(Long idCategoria) {
		return dao.findByCategoria(idCategoria);
	}
	
	public List<Pessoal> findServidorEfetivoByNomeOuCpf(String nome, String cpf, boolean somenteAtivos) {
		return dao.findServidorEfetivoByNomeOuCpf(nome, cpf, somenteAtivos);
	}

}
