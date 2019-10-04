package br.gov.ce.tce.srh.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalDAO;
import br.gov.ce.tce.srh.dao.RepresentacaoFuncionalDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;

/**
 * 
 * @author robstown
 *
 */
@Service("representacaoFuncionalService")
public class RepresentacaoFuncionalServiceImpl implements RepresentacaoFuncionalService {

	@Autowired
	private RepresentacaoFuncionalDAO dao;

	@Autowired
	private RepresentacaoSetorService representacaoSetorService;
	
	@Autowired
	private FuncionalDAO funcionalDAO;


	@Override
	@Transactional
	public void salvar(RepresentacaoFuncional entidade) throws SRHRuntimeException {

		// validando dados obrigatorios.
		validarDados(entidade);

		/*
		 * Regra temporariamente inválida em 20/05/2015, para evitar o uso do IDSETORDESIGNADO 
		 * 
		 * Quando for tipo nomeacao TITULAR, o setor selecionado deve ser o mesmo do funcionario
		 */
//		if(entidade.getTipoNomeacao() == 1 && entidade.getFuncional().getSetor() != null && entidade.getFuncional().getSetor().getId() != null){
//				if ( !entidade.getFuncional().getSetor().getId().equals(entidade.getSetor().getId()))
//					throw new SRHRuntimeException("Inclusão não permitida, quando tipo nomeação for titular, o setor deve ser o mesmo do funcionario.");
//		}

		/*
		 * Regra
		 * 
		 * Não serão permitidas alterações nos registros que possuem data fim preenchidas.
		 * 
		 */
		if ( entidade.getFim() != null )
			throw new SRHRuntimeException("Alteração não permitida para esta representação, pois não está ativa.");
		
		
		/*
		 * Regra
		 * 
		 * So nao pode ter representacao quem for do tipo ocupacao Membro.
		 * 
		 */
		if(entidade.getId() == null 
				&& entidade.getFuncional().getOcupacao() != null 
				&& entidade.getFuncional().getOcupacao().getTipoOcupacao() != null 
				&& entidade.getFuncional().getOcupacao().getTipoOcupacao().getId() == 1l ) {
			throw new SRHRuntimeException("Não é permitido representação para servidores do tipo Membro.");
		}


//		// validando o inicio da representacao conforme a funcional
//		if ( entidade.getInicio().before( entidade.getFuncional().getExercicio()) )
//			throw new SRHRuntimeException("O Inicio da Vigência deve ser maior que a data de exercicio do servidor.");

		
		validarRepresentacaoAnterior(entidade);		

		
		validarLimitePessoas(entidade);
		
		
		if(atualizaIdRepresentacaoCargo(entidade)){

			Funcional funcional = funcionalDAO.getById(entidade.getFuncional().getId());
			funcional.setIdRepresentacaoCargo(entidade.getRepresentacaoCargo().getId());
			funcionalDAO.salvar(funcional);
		
		}
		
		// persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(RepresentacaoFuncional entidade) {
		
		if ( entidade.getFim() != null )
			throw new SRHRuntimeException("A representação não pode ser excluída pois já foi exonerada.");
		
		if ( atualizaIdRepresentacaoCargo(entidade) ){
			
			Funcional funcional = funcionalDAO.getById(entidade.getFuncional().getId());
			funcional.setIdRepresentacaoCargo(null);
			funcionalDAO.salvar(funcional);
		
		}
		
		
		dao.excluir(entidade);
	}


	@Override
	@Transactional
	public void exonerar(RepresentacaoFuncional entidade) throws SRHRuntimeException {

		List<RepresentacaoFuncional> representacaoFuncionalList = dao.findByFuncional(entidade.getFuncional().getId());
		
		Long ultimoTitular = null;
		Long ultimoSubstituicao = null;
		Long ultimoDesignado = null;
		
		for (RepresentacaoFuncional representacaoFuncional : representacaoFuncionalList) {
			if (ultimoTitular == null && representacaoFuncional.getTipoNomeacao() == 1){
				ultimoTitular = representacaoFuncional.getId();
			} else if (ultimoSubstituicao == null && representacaoFuncional.getTipoNomeacao() == 2){
				ultimoSubstituicao = representacaoFuncional.getId();
			} else if (ultimoDesignado == null && representacaoFuncional.getTipoNomeacao() == 3){
				ultimoDesignado = representacaoFuncional.getId();
			}
		}
		
		if(!entidade.getId().equals(ultimoTitular) && !entidade.getId().equals(ultimoSubstituicao) && !entidade.getId().equals(ultimoDesignado))
			throw new SRHRuntimeException("Exoneração não permitida. Para cada tipo de nomeação, somente o último registro de Representação pode ser exonerado/editado.");
		
		validarExoneracao(entidade);

		if(atualizaIdRepresentacaoCargo(entidade)){
			
			Funcional funcional = funcionalDAO.getById(entidade.getFuncional().getId());
			funcional.setIdRepresentacaoCargo(null);
			funcionalDAO.salvar(funcional);
		
		}
		
		
		entidade.setAtivo(false);
		dao.salvar(entidade);
		
	}


	@Override
	public int count(Long pessoal) {
		return dao.count(pessoal);
	}


	@Override
	public List<RepresentacaoFuncional> search(Long pessoal, int first, int rows) {
		return dao.search(pessoal, first, rows);
	}


	@Override
	public RepresentacaoFuncional getByid(Long id) {
		return dao.getById(id);
	}

	
	@Override
	public List<RepresentacaoFuncional> getByMatricula(String matricula) {
		return dao.getByMatricula(matricula);
	}


	@Override
	public List<RepresentacaoFuncional> getByCpf(String cpf) {
		return dao.getByCpf(cpf);
	}


	@Override
	public List<RepresentacaoFuncional> findByNome(String nome) {
		return dao.findByNome(nome);
	}
	
	@Override
	public List<RepresentacaoFuncional> findByUsuarioLogado(Usuario usuarioLogado) {
		return dao.findByUsuarioLogado(usuarioLogado);
	}


	@Override
	public List<RepresentacaoFuncional> findByNomeSetor(String nome, Long idSetor) {
		return dao.findByNomeSetor(nome, idSetor);
	}
	
	@Override
	public List<RepresentacaoFuncional> findByUsuarioLogadoSetor(
			Usuario usuarioLogado, Long idSetor) {
		return dao.findByUsuarioLogadoSetor(usuarioLogado, idSetor);
	}


	@Override
	public List<RepresentacaoFuncional> findByPessoal(Long idPessoa) {
		return dao.findByPessoal(idPessoa);
	}
	
	@Override
	public boolean temAtivaByPessoal(Long idPessoa) {
		
		List<RepresentacaoFuncional> representacaoList = this.findByFuncional(idPessoa);
		for (RepresentacaoFuncional representacaoFuncional : representacaoList) {
			if(representacaoFuncional.getFim() == null) {
				return true;
			}					
		}
		
		return false;
	}


	@Override
	public List<RepresentacaoFuncional> findByFuncional(Long idFuncional) {
		return dao.findByFuncional(idFuncional);
	}



	/**
	 * Validar:
	 * 
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setado o tipo de documento.
	 *  Deve ser setado o setor.
	 *  Deve ser setado a representacao do cargo.
	 *  Deve ser setado o inicio da vigencia.
	 *  Deve ser setado o tipo de publicação.
	 *  Deve ser setado a data da publicação.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(RepresentacaoFuncional entidade) {

		// validando o servidor
		if (entidade.getFuncional() == null)
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		// validando o tipo de documento
		if (entidade.getTipoDocumento() == null)
			throw new SRHRuntimeException("O Tipo de Documento é obrigatório.");

		// validando o setor
		if (entidade.getSetor() == null)
			throw new SRHRuntimeException("O Setor é obrigatório.");

		// validando a representacao cargo
		if (entidade.getRepresentacaoCargo() == null)
			throw new SRHRuntimeException("A Representação Cargo é obrigatória.");

		// validando inicio vigencia
		if (entidade.getInicio() == null)
			throw new SRHRuntimeException("O Início da Vigência é obrigatório.");

		// validando o tipo de publicacao
		if (entidade.getTipoPublicacaoNomeacao() == null)
			throw new SRHRuntimeException("O Tipo de Publicação é obrigatório.");

		// validando data publicacao
		if (entidade.getDoeInicio() == null)
			throw new SRHRuntimeException("A Data de Publicação Início da Vigência é obrigatória.");

		// validando tipo nomeação
		if (entidade.getTipoNomeacao() == null || entidade.getTipoNomeacao().equals(0l))
			throw new SRHRuntimeException("O Tipo Nomeação é obrigatório.");

	}


	/**
	 * Regra de Negocio:
	 * 
	 *  So pode ter 1 representacao ativa por tipo.
	 *  O início da vigência da nova representação deve ser posterior ao último dia trabalhado da representação anterior. 
	 *  
	 * @param entidade
	 * 
	 */
	private void validarRepresentacaoAnterior(RepresentacaoFuncional entidade) {

		if ( entidade.getId() == null ) {
			
			// validando representacao ativa por tipo
			RepresentacaoFuncional representacao = dao.getByFuncionalTipo( entidade.getFuncional().getId(), entidade.getTipoNomeacao() );
			if ( representacao != null )
				throw new SRHRuntimeException("Não será permitido inserir nova representação, pois este servidor possui outra ativa com o mesmo tipo.");
			
			// se for uma nomeção do tipo Titular, validar a data início com a data fim da representação Titular anterior 
			if (entidade.getTipoNomeacao() == 1){
				
				List<RepresentacaoFuncional> representacoes = dao.findByFuncional(entidade.getFuncional().getId());
				
				if(representacoes != null && representacoes.size() > 0) {
					
					RepresentacaoFuncional representacaoTitular = null;
					
					for (RepresentacaoFuncional representacaoFuncional : representacoes) {
						if(representacaoFuncional.getTipoNomeacao() == 1){
							representacaoTitular = representacaoFuncional;
							break;
						}						
					}				
				
					if(representacaoTitular != null && !entidade.getInicio().after(representacaoTitular.getFim()))
						throw new SRHRuntimeException("O início da vigência da nova representação deve ser posterior ao último dia trabalhado da representação anterior do tipo Titular.");
				}
				
			}
		}		
	}

	/**
	 * Regra de Negocio:
	 * 
	 * Se a data de início for maior ou igual a '01/01/2012', o sistema deverá validar a qtde de pessoas permitidas, 
	 * por tipo de nomeação e por cargo/setor, levando em consideração só os funcionários com sem data fim nula, ou seja, válidos.
	 * 
	 * Não permitir que seja inserido novo registro na representação funcional, caso a qtde já inseridas por tipo de nomeação 
	 * e que estejam válidas, seja igual a qtde permitida na representação do setor.
	 * 
	 * @param entidade
	 * 
	 */
	private void validarLimitePessoas(RepresentacaoFuncional entidade) {

		RepresentacaoFuncional representacaoBD = entidade.getId() != null ? this.dao.getById(entidade.getId()): null;
		
		// crítica deve ser excutada em caso de inclusão ou alteração de tipo de nomeacao
		if (representacaoBD == null || representacaoBD.getTipoNomeacao().intValue() != entidade.getTipoNomeacao().intValue()){
			
			// verificando data de inicio da vigencia
			Calendar calendarInicio = new GregorianCalendar();
			calendarInicio.setTime( entidade.getInicio() );

			if( calendarInicio.get( GregorianCalendar.YEAR ) > 2011 ) {

				// pegando a qtde de representacoes ativas, por tipo de nomeacao / cargo / setor
				List<RepresentacaoFuncional> representacoesAtivas = dao.findByTipoNomeacaoCargoSetor( entidade.getTipoNomeacao(), 
						entidade.getRepresentacaoCargo().getId(), entidade.getSetor().getId() );

				// validando com a quantidade maxima permitida
				RepresentacaoSetor representacaoSetor = representacaoSetorService.getByCargoSetor( entidade.getRepresentacaoCargo().getId(), 
						entidade.getSetor().getId() );

				if ( representacoesAtivas.size() >= representacaoSetor.getQuantidade() )
					throw new SRHRuntimeException("Quantidade de funcionários neste cargo/setor/tipo nomeação excedeu o limite permitido.");
		
			}
		}

	}

	
	/**
	 * Regra de Negocio:
	 * 
	 *  Deve ser setado o tipo de publicacao saida.
	 *  Deve ser setado a data de publicacao.
	 *  Deve ser setado o tipo de saida.
	 *  Deve ser setado a data saida.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarExoneracao(RepresentacaoFuncional entidade) {

		// validando o tipo de publicacao
		if (entidade.getTipoPublicacaoSaida() == null)
			throw new SRHRuntimeException("O Tipo de Publicação é obrigatório.");

		// validando a data da publicacao
 		if (entidade.getDoeFim() == null)
			throw new SRHRuntimeException("A Publicação é obrigatória.");

 		// validando o tipo de saida
		if (entidade.getTipoMovimentoFim() == null)
			throw new SRHRuntimeException("O Tipo Saida é obrigatório.");

		// validando a data final
		if (entidade.getFim() == null)
			throw new SRHRuntimeException("O último dia trabalhado é obrigatório.");
		else if (entidade.getInicio().after(entidade.getFim()))
			throw new SRHRuntimeException("O último dia trabalhado deve ser posterior a data de início da vigência.");

	}
	
	
	/**
	 * Regra de Negocio:
	 * 
	 * 1. Ao nomear ou exonerar 1-TITULAR, o campo IDREPRESENTACAOCARGO da TB_FUNCIONAL deverá ser atualizado.
	 * 2. Ao nomear ou exonerar 2-SUBSTITUTO, não alterar o campo IDREPRESENTACAOCARGO da TB_FUNCIONAL;
	 * 3. Ao nomear ou exonerar 3-DESIGNADO, atualizar o campo IDREPRESENTACAOCARGO da TB_FUNCIONAL se quem estiver sendo nomeado/exonerado não for titular de outro cargo;
	 *	
	 * 
	 * @param entidade
	 * 
	 */
	private boolean atualizaIdRepresentacaoCargo(RepresentacaoFuncional entidade) {

		if(entidade.getTipoNomeacao() == 1){ // TITULAR
			return true;		
		}else if(entidade.getTipoNomeacao() == 2){ // SUBSTITUICAO
			return false;
		}else if(entidade.getTipoNomeacao() == 3){ // DESIGNADO
			RepresentacaoFuncional representacao = dao.getByFuncionalTipo( entidade.getFuncional().getId(), 1L ); // TITULAR
			if(representacao == null){
				return true;
			}			
		}
		
		return false;

	}
	

}