package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.LicencaEspecialDAO;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("LicencaEspecialService")
public class LicencaEspecialServiceImpl implements LicencaEspecialService {

	@Autowired
	private LicencaEspecialDAO dao;


	@Override
	@Transactional
	public LicencaEspecial salvar(LicencaEspecial entidade) throws SRHRuntimeException {

		//validando dados obrigatorios
		validaDados(entidade);

		/*
		 * Regra:
		 * 
		 * Verificar se o saldo de dias nao eh negativo
		 */
		if (entidade.getSaldodias() != null && entidade.getSaldodias() < 0l)
			throw new SRHRuntimeException("O saldo de dias da Licença Especial não pode ser negativo. Operação cancelada.");

		/*
		 * Regra: 
		 * Verificar licenças especiais antigas
		 * 
		 */
		verificarLicencasAntigas(entidade);

		/*
		 * Regra:
		 * 
		 * O campo Qtde de Dias deverá validar o período Inicial e Final.
		 * 
		 */
		entidade.setQtdedias( calcularQtdeDias(entidade.getAnoinicial(), entidade.getAnofinal() ));

		/*
		 * Regra:
		 * 
		 * O campo Saldo Dias receberá inicialmente o mesmo valor do campo Qtde Dias. 
		 * Ele será alterado sempre que houver lançamentos no usufruto das licenças 
		 * do servidor.
		 * 
		 * TODO verificar se pode ser alterado a entidade quando ja estiver em uso
		 * 
		 */
		if (entidade.getId() == null || entidade.getId() == 0l)
			entidade.setSaldodias( entidade.getQtdedias() );

		return dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(LicencaEspecial entidade) throws SRHRuntimeException {

		/*
		 * Regra: 
		 * Verificar se a licença especial esta em uso, ou seja, saldo de dias menor que a qtde de dias
		 * 
		 */
		if (entidade.getSaldodias() < entidade.getQtdedias())
			throw new SRHRuntimeException("Licença Especial não pode ser excluida, esta em uso. Operação cancelada.");

		dao.excluir(entidade);
	}


	@Override
	public int count(Long pessoal) {
		return dao.count(pessoal);
	}


	@Override
	public List<LicencaEspecial> search(Long pessoal, int first, int rows) {
		return dao.search(pessoal, first, rows);
	}


	@Override
	public LicencaEspecial getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<LicencaEspecial> findByPessoal(Long pessoal){
		return dao.findByPessoal(pessoal);
	}


	@Override
	public List<LicencaEspecial> findByPessoalComSaldo(Long pessoal){
		return dao.findByPessoalComSaldo(pessoal);
	}
	
	@Override
	public List<LicencaEspecial> findByPessoalContaEmDobro(Long pessoal){
		return dao.findByPessoalContaEmDobro(pessoal);
	}



	/**
	 * Validar:
	 * 
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setado o ano inicial.
	 *  Deve ser setado o ano final.
	 *  Deve ser setada a descricao.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(LicencaEspecial entidade) {

		//valida a pessoa
		if ( entidade.getPessoal() == null )
			throw new SRHRuntimeException("O Funcionário é obrigatório. Digite a matricula ou nome e efetue a pesquisa.");

		//validando ano inicial
		if ( entidade.getAnoinicial() == null || entidade.getAnoinicial().equals(0l))
			throw new SRHRuntimeException("O ano inicial é obrigatório.");

		//validando ano final
		if ( entidade.getAnofinal() == null || entidade.getAnofinal().equals(0l))
			throw new SRHRuntimeException("O ano final é obrigatório.");

		//validando a descricao
		if ( entidade.getDescricao() == null || entidade.getDescricao().equals("") )
			throw new SRHRuntimeException("A descrição é obrigatória.");

	}


	/**
	 * Regra de Negocio:
	 * 
	 * O Período Inicial e Final devem ser checados para saber se já foi lançada alguma 
	 * licença especial para o servidor neste período.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificarLicencasAntigas(LicencaEspecial entidade) throws SRHRuntimeException {

		for (LicencaEspecial licencaAntigas : findByPessoal( entidade.getPessoal().getId() )) {

			if ( !licencaAntigas.getId().equals(entidade.getId()) ) {
				
				if(entidade.getAnoinicial() >= licencaAntigas.getAnoinicial()  
						&& entidade.getAnoinicial() < licencaAntigas.getAnofinal() ) {
					throw new SRHRuntimeException("Já existe uma licença especial com esse período");
				}

				if(entidade.getAnofinal() > licencaAntigas.getAnoinicial()
						&& entidade.getAnofinal() <= licencaAntigas.getAnofinal()) {
					throw new SRHRuntimeException("Já existe uma licença especial com esse período");
				}
				
				if(entidade.getAnoinicial() <= licencaAntigas.getAnoinicial()
						&& entidade.getAnofinal() >= licencaAntigas.getAnofinal()) {
					throw new SRHRuntimeException("Já existe uma licença especial com esse período");
				}
				

			}

		}

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Validar ano inicial e ano final.
	 *  O ano inicial não pode ser inferior a 1935.
	 *  O ano final não pode ser superior a 1999.
	 * 
	 * E calcular a quantidade de dias conforme o ano inicial e o final.
	 * 
	 *   Se for 10 anos a qtde máxima é de 180 dias
	 *   Se for 5 anos a qtde máxima é de 90 dias
	 *  
	 * 
	 * @param anoInicial
	 * @param anoFial
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	public Long calcularQtdeDias(Long anoInicial, Long anoFinal) throws SRHRuntimeException {

		if(anoInicial < 1935) {
			throw new SRHRuntimeException("O ano inicial de ser superior a 1934");
		}

		if(anoFinal > 1999) {
			throw new SRHRuntimeException("O ano final deve ser menor que 2000");
		}

		if( (anoFinal - anoInicial != 5) && (anoFinal - anoInicial != 10) ) {
			throw new SRHRuntimeException("Ano final deve ter um intervalo do ano inicial de 5 ou 10 anos");
		}

	    return (anoFinal - anoInicial) * 18;
	}


	/**
	 * Regra de Negocio:
	 * 
	 * Quando for INSERIR:
	 *   Caso exista uma exclusão de lançamentos de licença, o sistema deverá creditar a qtde correspondente
	 *   na licença especial do funcionário.
	 * 
	 * Quando for REMOVER:
	 *   As licenças especiais que forem lançadas como usufruídas, deverão ter seu saldo de dias de licença 
	 *   especial correpondente atualizado (debitar da qtd de dias utilizados).
	 * 
	 * 
	 * @param entidade
	 * @param acao: INSERIR, REMOVER
	 * 
	 * @throws SRHRuntimeException 
	 * 
	 */
	@Override
	public void ajustarSaldo(Long idLicencaEspecial, String acao, int qtdDias) throws SRHRuntimeException {

		LicencaEspecial licencaEspecial = dao.getById( idLicencaEspecial );

		// quando for insercao
		if (acao.equalsIgnoreCase("INSERIR"))
			licencaEspecial.setSaldodias( licencaEspecial.getSaldodias() + (qtdDias) );

		// quando for exclusao
		if (acao.equalsIgnoreCase("REMOVER"))
			licencaEspecial.setSaldodias( licencaEspecial.getSaldodias() - (qtdDias) );

		/*
		 * Regra:
		 * Verificar se o saldo de dias não é negativo
		 */
		if (licencaEspecial.getSaldodias() != null && licencaEspecial.getSaldodias() < 0l)
			throw new SRHRuntimeException("O saldo de dias da Licença Especial não pode ser negativo. Operação cancelada.");

		dao.ajustarSaldo( idLicencaEspecial, licencaEspecial.getSaldodias() );

	}


	public void setDAO(LicencaEspecialDAO dao) {this.dao = dao;}

}
