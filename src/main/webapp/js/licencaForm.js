function validarLicenca() {
	pu = document.getElementById('form:publicacao').value;
	pro = document.getElementById('form:processo').value;
	var resposta = true;
	
	if(pu==""){
		if(pro==""){
			resposta = confirm(decodeURIComponent(escape("Não foi preenchida a Data de Publicação Oficial e o Número do Processo. Deseja prosseguir a gravação?")));
		}
	}

	if(pu==""){
		if(pro!=""){
			resposta = confirm(decodeURIComponent(escape("Não foi preenchida a Data de Publicação Oficial. Deseja prosseguir a gravação?")));
		}
	}

	if(pro==""){
		if(pu!=""){
			resposta = confirm(decodeURIComponent(escape("Não foi preenchida o Número do Processo. Deseja prosseguir a gravação?")));
		}
	}
	return resposta;	
}