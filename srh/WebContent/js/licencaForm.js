function validarLicenca()
{
	pu = document.getElementById('form:publicacao').value;
	pro = document.getElementById('form:processo').value;
	var resposta = true;
	
	if(pu==""){
		if(pro==""){
			resposta = confirm("N�o foi preenchida a Data de Publica��o Oficial e o N�mero do Processo. Deseja prosseguir a grava��o?");
		}
	}

	if(pu==""){
		if(pro!=""){
			resposta = confirm("N�o foi preenchida a Data de Publica��o Oficial. Deseja prosseguir a grava��o?");
		}
	}

	if(pro==""){
		if(pu!=""){
			resposta = confirm("N�o foi preenchida o N�mero do Processo. Deseja prosseguir a grava��o?");
		}
	}
	return resposta;	
}