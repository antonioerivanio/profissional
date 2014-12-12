function validarFerias()
{
	var dataPublicacao = document.getElementById('form:dataPublicacao').value;
	var dataDoAto = document.getElementById('form:dataDoAto').value;
	var resposta = true;
	if(dataPublicacao == "" || dataDoAto== ""){
		resposta = confirm("Não foi preenchida a data de publicação oficial e/ou a data do Ato. Deseja prosseguir a gravação?");
	}
	return resposta;
}