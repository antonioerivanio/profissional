package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ComprovanteVinculoSocietarioDAO;
import br.gov.ce.tce.srh.domain.ComprovanteVinculoSocietario;

@Service
public class ComprovanteVinculoSocietarioService {

	@Autowired
	private ComprovanteVinculoSocietarioDAO comprovanteVinculoSocietarioDAO;
	
	@Transactional
	public void salvarComprovantes(List<ComprovanteVinculoSocietario> comprovanteList) {
		for(ComprovanteVinculoSocietario comprovante: comprovanteList) {
			this.comprovanteVinculoSocietarioDAO.salvar(comprovante);
		}  
	}	
	
	@Transactional
	public void excluir(ComprovanteVinculoSocietario comprovante) {
		this.comprovanteVinculoSocietarioDAO.excluir(comprovante);
	}

	public List<ComprovanteVinculoSocietario> getByPessoalId(Long id) {
		return this.comprovanteVinculoSocietarioDAO.getByPessoalId(id);
	}
}
