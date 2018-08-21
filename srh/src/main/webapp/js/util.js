/**
 * Validação de somente número no campo de matrícula
 * 
 * @param  Evento realizado pelo usuário
 * @return Se for backspace, enter ou número retorna true
 */
function numeric(e) {
	var code;
	var keychar;
	var numcheck;

	if (e.keyCode)
		code = e.keyCode; // IE
	else if (e.which)
		code = e.which; // Netscape 4.*
	else if (e.charCode)
		code = e.charCode; // Mozilla

	keychar = String.fromCharCode(code);
	numcheck = /\d/;
	
	
	return (code == 8) || numcheck.test(keychar);
}

// Só digitar numeros
function SoNumeros(obj) {

	chvalidos = '0123456789';
	var i = 0;
	var valid = true;

	while ((i < obj.value.length) && (valid)) {
		valid = false;
		for(j = 0; j < chvalidos.length;j++) {
			if (obj.value.charAt(i) == chvalidos.charAt(j)) {
				valid = true;
			}
		}
		i++;
	}

	if (!valid) {
		obj.focus();
		obj.value="";
		return(false);
	}
	return(true);
}

function SoNumeros(obj) {

	chvalidos = '0123456789';
	var i = 0;
	var valid = true;

	while ((i < obj.value.length) && (valid)) {
		valid = false;
		for(j = 0; j < chvalidos.length;j++) {
			if (obj.value.charAt(i) == chvalidos.charAt(j)) {
				valid = true;
			}
		}
		i++;
	}

	if (!valid) {
		obj.focus();
		obj.value="";
		return(false);
	}
	return(true);
}

function mantemSomenteNumeros(obj) {

	obj.value = obj.value.replace(/\D+/g, '');	
	
}

function validarPessoa() {
	cpf = document.getElementById('form:cpf').value;
	if(cpf==""){
		alert("Antes de preencher este campo, informe o nome e clique em pesquisar para preencher os dados pessoais.");
	}
}