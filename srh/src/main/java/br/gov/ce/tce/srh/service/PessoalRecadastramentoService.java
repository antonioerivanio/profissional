package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PessoalRecadastramentoDAO;
import br.gov.ce.tce.srh.domain.PessoalRecadastramento;

@Service
public class PessoalRecadastramentoService {

	@Autowired
	private PessoalRecadastramentoDAO dao;

	@Transactional
	public PessoalRecadastramento salvar(PessoalRecadastramento entidade) {
		return dao.salvar(entidade);
	}
	
	public PessoalRecadastramento findById(Long id) {
		return dao.findById(id);
	}
		
	public PessoalRecadastramento findByPessoalAndRecadastramento(Long idPessoal, Long idRecadastramento) {
		return dao.findByPessoalAndRecadastramento(idPessoal, idRecadastramento);
	}

	public void setDAO(PessoalRecadastramentoDAO pessoalRecadastramentoDAO) {this.dao = pessoalRecadastramentoDAO;}	

}
