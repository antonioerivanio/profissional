function validarFerias()
{
	var dataPublicacao = document.getElementById('form:dataPublicacao').value;
	var dataDoAto = document.getElementById('form:dataDoAto').value;
	var resposta = true;
	if(dataPublicacao == "" || dataDoAto== ""){
		resposta = confirm("N�o foi preenchida a data de publica��o oficial e/ou a data do Ato. Deseja prosseguir a grava��o?");
	}
	return resposta;
}