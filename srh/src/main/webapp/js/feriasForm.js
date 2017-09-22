function validarFerias()
{
	var dataPublicacao = document.getElementById('form:dataPublicacao').value;
	var dataDoAto = document.getElementById('form:dataDoAto').value;
	var resposta = true;
	
	var texto;

	try{	    
		texto = decodeURIComponent(escape("Não foi preenchida a data de publicação oficial e/ou a data do Ato. Deseja prosseguir a gravação?"));
	}catch(e){	    
		texto = "Não foi preenchida a data de publicação oficial e/ou a data do Ato. Deseja prosseguir a gravação?";
	}
	if(dataPublicacao == "" || dataDoAto== ""){
		resposta = confirm(texto);		
	}
	return resposta;
}