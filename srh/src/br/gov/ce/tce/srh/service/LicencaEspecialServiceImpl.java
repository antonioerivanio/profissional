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
			throw new SRHRuntimeException("O saldo de dias da Licen�a Especial n�o pode ser negativo. Opera��o cancelada.");

		/*
		 * Regra: 
		 * Verificar licen�as especiais antigas
		 * 
		 */
		verificarLicencasAntigas(entidade);

		/*
		 * Regra:
		 * 
		 * O campo Qtde de Dias dever� validar o per�odo Inicial e Final.
		 * 
		 */
		entidade.setQtdedias( calcularQtdeDias(entidade.getAnoinicial(), entidade.getAnofinal() ));

		/*
		 * Regra:
		 * 
		 * O campo Saldo Dias receber� inicialmente o mesmo valor do campo Qtde Dias. 
		 * Ele ser� alterado sempre que houver lan�amentos no usufruto das licen�as 
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
		 * Verificar se a licen�a especial esta em uso, ou seja, saldo de dias menor que a qtde de dias
		 * 
		 */
		if (entidade.getSaldodias() < entidade.getQtdedias())
			throw new SRHRuntimeException("Licen�a Especial n�o pode ser excluida, esta em uso. Opera��o cancelada.");

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



	/**
	 * Validar:
	 * 
	 * � Deve ser setado a pessoa (servidor).
	 * � Deve ser setado o ano inicial.
	 * � Deve ser setado o ano final.
	 * � Deve ser setada a descricao.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validaDados(LicencaEspecial entidade) {

		//valida a pessoa
		if ( entidade.getPessoal() == null )
			throw new SRHRuntimeException("O Funcion�rio � obrigat�rio. Digite a matricula ou nome e efetue a pesquisa.");

		//validando ano inicial
		if ( entidade.getAnoinicial() == null || entidade.getAnoinicial().equals(0l))
			throw new SRHRuntimeException("O ano inicial � obrigat�rio.");

		//validando ano final
		if ( entidade.getAnofinal() == null || entidade.getAnofinal().equals(0l))
			throw new SRHRuntimeException("O ano final � obrigat�rio.");

		//validando a descricao
		if ( entidade.getDescricao() == null || entidade.getDescricao().equals("") )
			throw new SRHRuntimeException("A descri��o � obrigat�ria.");

	}


	/**
	 * Regra de Negocio:
	 * 
	 * O Per�odo Inicial e Final devem ser checados para saber se j� foi lan�ada alguma 
	 * licen�a epsecial para o servidor neste per�odo.
	 * 
	 * @param entidade
	 *
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificarLicencasAntigas(LicencaEspecial entidade) throws SRHRuntimeException {

		for (LicencaEspecial licencaAntigas : findByPessoal( entidade.getPessoal().getId() )) {

			if ( !licencaAntigas.getId().equals(entidade.getId()) ) {
				
				//FEITO PELA IVIA - PROBLEMA --> CONSIDERAVA OS ANOS EXTREMOS

//				if(licencaAntigas.getAnoinicial() >= entidade.getAnoinicial()
//						&& licencaAntigas.getAnoinicial() <= entidade.getAnofinal()) {
//					throw new SRHRuntimeException("J� existe uma licen�a especial com esse per�odo");
//				}
//
//				if(licencaAntigas.getAnofinal() >= entidade.getAnoinicial()
//						&& licencaAntigas.getAnofinal() <= entidade.getAnofinal()) {
//					throw new SRHRuntimeException("J� existe uma licen�a especial com esse per�odo");
//				}
				
				//Alterado 08/08/2013 - Zacarias e Andr�
				if(entidade.getAnoinicial()>=licencaAntigas.getAnoinicial()  
						&& entidade.getAnoinicial() < licencaAntigas.getAnofinal() ) {
					throw new SRHRuntimeException("J� existe uma licen�a especial com esse per�odo");
				}

				if(entidade.getAnofinal() > licencaAntigas.getAnoinicial()
						&& entidade.getAnofinal() <= licencaAntigas.getAnofinal()) {
					throw new SRHRuntimeException("J� existe uma licen�a especial com esse per�odo");
				}
				
				if(entidade.getAnoinicial() <= licencaAntigas.getAnoinicial()
						&& entidade.getAnofinal() >= licencaAntigas.getAnofinal()) {
					throw new SRHRuntimeException("J� existe uma licen�a especial com esse per�odo");
				}
				

			}

		}

	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Validar ano inicial e ano final.
	 * � O ano inicial n�o pode ser inferior a 1935.
	 * � O ano final n�o pode ser superior a 1999.
	 * 
	 * E calcular a quantidade de dias conforme o ano inicial e o final.
	 * 
	 * � Se for 10 anos a qtde m�xima � de 180 dias
	 * � Se for 5 anos a qtde m�xima � de 90 dias
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
	 *   Caso exista uma exclus�o de lan�amentos de licen�a, o sistema dever� creditar a qtde correspondente
	 *   na licen�a especial do funcion�rio.
	 * 
	 * Quando for REMOVER:
	 *   As licen�as especiais que forem lan�adas como usufru�das, dever�o ter seu saldo de dias de licen�a 
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
		 * Verificar se o saldo de dias nao eh negativo
		 */
		if (licencaEspecial.getSaldodias() != null && licencaEspecial.getSaldodias() < 0l)
			throw new SRHRuntimeException("O saldo de dias da Licen�a Especial n�o pode ser negativo. Opera��o cancelada.");

		dao.ajustarSaldo( idLicencaEspecial, licencaEspecial.getSaldodias() );

	}


	public void setDAO(LicencaEspecialDAO dao) {this.dao = dao;}

}
