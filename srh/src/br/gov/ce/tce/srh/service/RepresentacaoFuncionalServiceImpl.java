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
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

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
		 * Regra
		 * 
		 * Quando for tipo nomeacao TITULAR, o setor selecionado deve ser o mesmo do funcionario
		 */
		if(entidade.getTipoNomeacao() == 1 && entidade.getFuncional().getSetor() != null && entidade.getFuncional().getSetor().getId() != null){
				if ( !entidade.getFuncional().getSetor().getId().equals(entidade.getSetor().getId()))
					throw new SRHRuntimeException("Inclus�o n�o permitida, quando tipo nomea��o for titular, o setor deve ser o mesmo do funcionario.");
			}

		/*
		 * Regra
		 * 
		 * N�o ser�o permitidas altera��es nos registros que possuem data fim preenchidas.
		 * 
		 */
		if ( entidade.getFim() != null )
			throw new SRHRuntimeException("Altera��o n�o permitida para esta representa��o, n�o esta ativa.");


//		// validando o inicio da representacao conforme a funcional
//		if ( entidade.getInicio().before( entidade.getFuncional().getExercicio()) )
//			throw new SRHRuntimeException("O Inicio da Vig�ncia deve ser maior que a data de exercicio do servidor.");


		/*
		 * Regra
		 * 
		 * N�o ser�o permitidas mais de uma representa��o por tipo.
		 * 
		 */
		validarRepresentacaoAnteriorAtiva(entidade);


		/*
		 * Regra
		 * 
		 * Validar limite de pessoas
		 * 
		 */
		validarLimitePessoas(entidade);
		Funcional funcional = funcionalDAO.getById(entidade.getFuncional().getId());
		funcional.setIdRepresentacaoCargo(entidade.getRepresentacaoCargo().getId());
		funcionalDAO.salvar(funcional);
		
		// persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(RepresentacaoFuncional entidade) {
		dao.excluir(entidade);
	}


	@Override
	@Transactional
	public void exonerar(RepresentacaoFuncional entidade) throws SRHRuntimeException {

		/*
		 * Regra:
		 * Validar dados obrigatorios.
		 * 
		 */	
		validarExoneracao(entidade);

		// Altera��o 14/08/2013
		Funcional funcional = funcionalDAO.getById(entidade.getFuncional().getId());
		funcional.setIdRepresentacaoCargo(null);
		funcionalDAO.salvar(funcional);
		
		// persistindo
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
	public RepresentacaoFuncional getByCpf(String cpf) {
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
	public List<RepresentacaoFuncional> findByFuncional(Long idFuncional) {
		return dao.findByFuncional(idFuncional);
	}



	/**
	 * Validar:
	 * 
	 * � Deve ser setado a pessoa (servidor).
	 * � Deve ser setado o tipo de documento.
	 * � Deve ser setado o setor.
	 * � Deve ser setado a representacao do cargo.
	 * � Deve ser setado o inicio da vigencia.
	 * � Deve ser setado o tipo de publica��o.
	 * � Deve ser setado a data da publica��o.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(RepresentacaoFuncional entidade) {

		// validando o servidor
		if (entidade.getFuncional() == null)
			throw new SRHRuntimeException("O Funcion�rio � obrigat�rio. Digite o nome e efetue a pesquisa.");

		// validando o tipo de documento
		if (entidade.getTipoDocumento() == null)
			throw new SRHRuntimeException("O Tipo de Documento � obrigat�rio.");

		// validando o setor
		if (entidade.getSetor() == null)
			throw new SRHRuntimeException("O Setor � obrigat�rio.");

		// validando a representacao cargo
		if (entidade.getRepresentacaoCargo() == null)
			throw new SRHRuntimeException("A Representa��o Cargo � obrigat�ria.");

		// validando inicio vigencia
		if (entidade.getInicio() == null)
			throw new SRHRuntimeException("O Inicio da Vig�ncia � obrigat�rio.");

		// validando o tipo de publicacao
		if (entidade.getTipoPublicacaoNomeacao() == null)
			throw new SRHRuntimeException("O Tipo de Publica��o � obrigat�rio.");

		// validando data publicacao
		if (entidade.getDoeInicio() == null)
			throw new SRHRuntimeException("A Data de Publica��o Inicio da Vig�ncia � obrigat�rio.");

		// validando tipo nomea��o
		if (entidade.getTipoNomeacao() == null || entidade.getTipoNomeacao().equals(0l))
			throw new SRHRuntimeException("O Tipo Nomea��o � obrigat�rio.");

	}


	/**
	 * Regra de Negocio:
	 * 
	 * � So pode ter 1 representacao ativa por tipo. 
	 * � So nao pode ter representacao que for do tipo ocupacao Membro.
	 *  
	 * @param entidade
	 * 
	 */
	private void validarRepresentacaoAnteriorAtiva(RepresentacaoFuncional entidade) {

		// validando representacao ativa por tipo
		RepresentacaoFuncional representacao = dao.getByFuncionalTipo( entidade.getFuncional().getId(), entidade.getTipoNomeacao() );
		if ( representacao != null )
			throw new SRHRuntimeException("N�o ser� permitido inserir nova representa��o, pois este servidor possui outra ativa com o mesmo tipo.");

		// verificando se eh membro
		if(entidade.getFuncional().getOcupacao() != null && entidade.getFuncional().getOcupacao().getTipoOcupacao() != null
				&& entidade.getFuncional().getOcupacao().getTipoOcupacao().getId() != null){
			if ( entidade.getFuncional().getOcupacao().getTipoOcupacao().getId() == 1l ) {
				throw new SRHRuntimeException("N�o � permitido representa��o para servidores do tipo Membro.");
			}
		}
		
		
	}

	/**
	 * Regra de Negocio:
	 * 
	 * Se a data de in�cio for maior ou igual a '01/01/2012', o sistema dever� validar a qtde de pessoas permitidas, 
	 * por tipo de nomea��o e por cargo/setor, levando em considera��o s� os funcion�rios com sem data fim nula, ou seja, v�lidos.
	 * 
	 * N�o permitir que seja inserido novo registro na representa��o funcional, caso a qtde j� inseridas por tipo de nomea��o 
	 * e que estejam v�lidas, seja igual a qtde permitida na representa��o do setor.
	 * 
	 * @param entidade
	 * 
	 */
	private void validarLimitePessoas(RepresentacaoFuncional entidade) {

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
				throw new SRHRuntimeException("Quantidade de funcion�rios neste cargo/setor/tipo nomea��o excedeu o limite permitido.");

		}

	}

	
	/**
	 * Regra de Negocio:
	 * 
	 * � Deve ser setado o tipo de publicacao saida.
	 * � Deve ser setado a data de publicacao.
	 * � Deve ser setado o tipo de saida.
	 * � Deve ser setado a data saida.
	 * 
	 * @param entidade Funcional
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void validarExoneracao(RepresentacaoFuncional entidade) {

		// validando o tipo de publicacao
		if (entidade.getTipoPublicacaoSaida() == null)
			throw new SRHRuntimeException("O Tipo de Publica��o � obrigat�rio.");

		// validando a data da publicacao
 		if (entidade.getDoeFim() == null)
			throw new SRHRuntimeException("A Publica��o � obrigat�ria.");

 		// validando o tipo de saida
		if (entidade.getTipoMovimentoFim() == null)
			throw new SRHRuntimeException("O Tipo Saida � obrigat�rio.");

		// validando a data final
		if (entidade.getFim() == null)
			throw new SRHRuntimeException("A Data Final � obrigat�ria.");

	}

}
