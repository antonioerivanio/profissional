function alerta(mensagem){
	Swal.fire({	  
	  title: mensagem,
	  icon: 'warning',
	  confirmButtonColor: '#195297'
	});
}

function alertaExclusao() {	
	Swal.fire({
	  title: 'Deseja realmente excluir o registro?',
	  icon: 'warning',
	  confirmButtonText: 'Confirmar',
	  cancelButtonText: 'Cancelar',
	  showCancelButton: true,
	  reverseButtons: true,
	  focusConfirm: false,
	  confirmButtonColor: '#195297'
	}).then((result) => {
		if(result.value){
			excluir();			
		} 
	});
}

//usado em telas com mais de uma chamada a função excluir
function alertaExclusao2() {	
	Swal.fire({
	  title: 'Deseja realmente excluir o registro?',
	  icon: 'warning',
	  confirmButtonText: 'Confirmar',
	  cancelButtonText: 'Cancelar',
	  showCancelButton: true,
	  confirmButtonColor: '#195297'
	}).then((result) => {
		if(result.value){
			excluir2();			
		} 
	});
}

function modalInstrucoesVigencia() {
	Swal.fire({
		title: 'Informações sobre o período de vigência',
		icon: 'info',
		confirmButtonColor: '#195297',
		customClass: {
			content: 'modalInstrucoesVigencia'
		  },
		html: '<p>Para enviar estes dados ao eSocial, preencha obrigatoriamente a data de <b>INÍCIO</b> do Período de Validade.</p>' +
		'<p>Caso este cadastro não deva ser enviado ao eSocial, basta não preencher as datas de validade.</p>' +
		'<p>Para mais informações sobre a exclusão e a utilização da data fim, veja o <a href="http://portal.esocial.gov.br/institucional/documentacao-tecnica" target="_blank"> Manual de Orientação do eSocial</a>.</p>'
	});
}

function modalRenovaSessao() {
	Swal.fire({
		icon: 'warning',
		title: 'Renovação de sessão',
		text: 'Sua sessão irá expirar em alguns instantes. Clique em OK para renová-la.',
		timer: 299000,
		cancelButtonText: 'Cancelar',
		showCancelButton: true,
		allowOutsideClick: false,
		reverseButtons: true,
		confirmButtonColor: '#195297'
	}).then((result) => {
		if(result.value){			
			keepSession();	
		} else {
			dontKeepSession();
		}
	});
}

function modalRedirecionaLogin() {
	Swal.fire({
		icon: 'info',
		title: 'Fazer novo login',
		text: 'Sua sessão expirou. Clique em OK para fazer um novo login.',
		confirmButtonColor: '#195297'
	}).then((result) => {
		if(result.value){
			window.location.href = "/srh/login.faces";		
		} 
	});
}

function finalizaRecadastramento() {
	Swal.fire({
		title: 'Informações sobre o período de vigência',
		icon: 'info',
		confirmButtonColor: '#195297',
		showCancelButton: true,
		allowOutsideClick: false,
		reverseButtons: true,
		width: 700,
		customClass: {
			content: 'modalInstrucoesVigencia'
		  },
		html: '<span>'
			+ '<b>ATENÇÃO!</b> Esclarecemos que ao finalizar o recadastramento, você não poderá editar seus dados por meio do SRH.' 
			+ 'Você pode utilizar indistintamente a opção salvar, e só partir para a ação de finalizar, quando efetivamente' 
			+ 'tiver encerrado todas as suas informações do recadastramento.' 
			+ 'Caso esteja seguro que efetivamente finalizou o cadastro, leia o texto abaixo e clique na opção [Confirmar].'
			+ '<br /><br />'
			+ '<b>DECLARO</b> serem verdadeiras as informações constantes do presente' 
			+ 'formulário, comprometendo-me a comunicar ao Tribunal de Contas do Estado Ceará qualquer alteração ocorrida' 
			+ 'após a finalização do recadastramento e apresentar documentos complementares solicitados' 
			+ 'pela Gerência de Atos Funcionais.'					
		+ '</span>'
	}).then((result) => {
		if(result.value){
			finalizarRecadastramento();			
		} 
	});
}

function validarFerias() {
	
	var dataPublicacao = document.getElementById('form:dataPublicacao').value;
	var dataDoAto = document.getElementById('form:dataDoAto').value;
	
	if(dataPublicacao == "" || dataDoAto == ""){
		Swal.fire({
			title: 'Atenção!',
			icon: 'warning',
			text: 'Não foi preenchida a data de publicação oficial e/ou a data do ato. Deseja prosseguir a gravação?',
			confirmButtonText: 'Confirmar',
			cancelButtonText: 'Cancelar',
			showCancelButton: true,
			reverseButtons: true,
			focusConfirm: false,
			confirmButtonColor: '#195297'
		}).then((result) => {
			if(result.value){
				salvar();			
			} 
		});		
	} else {
		salvar();
	}
}