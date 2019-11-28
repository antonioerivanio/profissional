var startValue = 30;
var timeout;
var funcaoTimeout;
var checkSessionId;
var segundos;
var minutos;
var renovaSessao = true;
           
function inicializaVariaveis() {
	startValue = 30;
	renovaSessao = true;
	funcaoTimeout = setTimeout("update()", 1000);	
}

function calcOffset() {
    var serverTime = getCookie('serverTime');
    serverTime = serverTime==null ? null : Math.abs(serverTime);
    var clientTimeOffset = getLocalTime() - serverTime;    
    setCookie('clientTimeOffset', clientTimeOffset);
        
    inicializaVariaveis();
    checkSession();
}

function checkSession() {
    if ( isSessaoValida() ) {    	
    	checkSessionId = setTimeout('checkSession()', 1000);
    	
        if ((getTempoRestanteDaSessao() <= tempoParaAlertarOFimDaSessao()) && renovaSessao) {
        	RichFaces.ui.PopupPanel.showPopupPanel('modalRenovaSessao');
        	timeout = setTimeout ("handleTimeout()", 1000);
        }
        
    } else {
    	clearTimeout(checkSessionId);
    	RichFaces.ui.PopupPanel.hidePopupPanel('modalRenovaSessao');
    	RichFaces.ui.PopupPanel.showPopupPanel('modalRedirecionaLogin');
    }
}

function update() {
	
	if ( isSessaoValida() ) {
				
		var tempoRestanteDaSessaoEmSegundos = parseInt(Math.abs(getTempoRestanteDaSessao()/1000));
		segundos = tempoRestanteDaSessaoEmSegundos % 60;
			
		var tempoRestanteDaSessaoEmMinutos = tempoRestanteDaSessaoEmSegundos / 60;
		minutos = parseInt(tempoRestanteDaSessaoEmMinutos % 60);		
		
		if (minutos < 10) minutos = '0' + minutos;
		document.getElementById('min').innerHTML = minutos;
		
		if (segundos < 10) segundos = '0' + segundos;
		document.getElementById('sec').innerHTML = segundos;
		
		funcaoTimeout = setTimeout("update()", 1000);
		
	} else {
		clearTimeout(funcaoTimeout);
		document.getElementById('min').innerHTML = '00';
		document.getElementById('sec').innerHTML = '00';			
	}
}

function isSessaoValida() {
	if ( getTempoRestanteDaSessao() >= 1000 ){
		return true;
	}
	return false;
}

function handleTimeout () {		
	if( isSessaoValida() ) {
		timeout = setTimeout("handleTimeout()", 1000);
	} else {
		clearTimeout(timeout);
		RichFaces.ui.PopupPanel.hidePopupPanel('modalRenovaSessao');
		RichFaces.ui.PopupPanel.showPopupPanel('modalRedirecionaLogin');	
	}
}

function keepSession() {
	console.log("Clicou para manter sessao");
	/* Faz uma requisiçao no servidor para renovar a sessao */	
	jQuery.ajax({
		url:'#{request.contextPath}/ping.html'			
	}).done(function(){
		console.log("Concluiu a requisicao para manter sessao");
		RichFaces.ui.PopupPanel.hidePopupPanel('modalRenovaSessao');
	});
}

function getLocalTime() {
	return (new Date()).getTime();
}

function dontKeepSession() {	
	renovaSessao = false;
	RichFaces.ui.PopupPanel.hidePopupPanel('modalRenovaSessao');
}

function setCookie(cname, cvalue) {
    var d = new Date();
    d.setTime(d.getTime() + (startValue*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

function getTempoQueASessaoVaiExpirar() {
	return Math.abs(getCookie('sessionExpiry'));
}

function getClientTimeOffset() {
	return getCookie('clientTimeOffset');
}

function getTempoRestanteDaSessao() {
	return getTempoQueASessaoVaiExpirar() - (getLocalTime() - getClientTimeOffset());
}

function tempoParaAlertarOFimDaSessao(){
	var tempoQueAJanelaApareceEmMilissegundos = 300000;
	return tempoQueAJanelaApareceEmMilissegundos;
}