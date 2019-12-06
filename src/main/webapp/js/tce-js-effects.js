jQuery.noConflict();
jQuery(document).ready(function($) {
    
    //  INICIO EFEITOS MENU PRINCIPAL
    
	var idMenuSelecionado;
	
    $(".menu-principal li a").click(function(){ 
    	
    	if( $(this).parent().find(".sub-menu").hasClass("menu-aberto")) {
    		
    		idMenuSelecionado = undefined;
            
            $(this).parent().animate({
                height: 42
            }, 450,function(){                    
                $(this).parent().find(".sub-menu").slideUp();
                $(this).parent().find(".sub-menu").removeClass("menu-aberto");
            });
            
        }
        else{
            
            if($(this).parent().hasClass("third-menu")) {
                
                $(this).parent().find(".sub-sub-menu").slideToggle();                
                $(this).toggleClass("sub-menu-aberto");
                
            }else {
            	
            	var element = $(this);
            	
            	element.parent().css("height","auto");
            	element.parent().find(".sub-menu").slideDown();
            	element.parent().find(".sub-menu").addClass("menu-aberto");
            	
            	if(idMenuSelecionado || idMenuSelecionado != "#" + element.parent().attr('id')){
            		 $(idMenuSelecionado).animate({
                         height: 42
                     }, 450,function(){                    
                         $(idMenuSelecionado).find(".sub-menu").slideUp();
                         $(idMenuSelecionado).find(".sub-menu").removeClass("menu-aberto");                         
                     });   
            	}
            	
            	setTimeout(function(){
            		idMenuSelecionado = "#" + element.parent().attr('id');
            	}, 500);
                
            } 
            
        }
        
    });
    
    //  FIM EFEITOS MENU PRINCIPAL    
   
    
    // INICIO MASCARAS

    $(".maskData").mask("99/99/9999",{placeholder:" "});  // DATA
    $(".maskVigencia").mask("99/9999",{placeholder:" "});  // VIGÊNCIA ESOCIAL
    $(".maskCpf").mask("999.999.999-99",{placeholder:" "});  // CPF
    $(".maskPasep").mask("999.99999.99/9",{placeholder:" "});  // PASEP
    $(".maskAgencia").mask("9999-9",{placeholder:" "});  // AGENCIA
    $(".maskConta").mask("9999999-9",{placeholder:" "});  // CONTA
    $(".maskCep").mask("99999-999",{placeholder:" "});  // CEP    
	$(".maskTelefone").mask("(99)99999999?9",{placeholder:" "}); // TELEFONE    
    $(".maskCelular").mask("(99)99999-9999",{placeholder:" "});  // CELULAR
    $(".maskMatricula").mask("9999-9",{placeholder:" "});  // MATRICULA
    $(".maskMotorVeiculo").mask("9.9",{placeholder:" "});  // MOTOR VEICULO
    $(".maskPlacaVeiculo").mask("aaa-9999",{placeholder:" "});  // PLACA VEICULO
    $(".maskProcesso").mask("99999/9999-9",{placeholder:" "});  // NUMERO DO PROCESSO
    $(".maskQtdQuintos").mask("9",{placeholder:" "});  // QTD QUINTOS
    $(".maskSalario").maskMoney({
    	symbol:'R$ ',
    	showSymbol:true,
    	symbolStay: true,
    	decimal:',', 
    	thousands:'.',
    	precision: 2});  // SALARIO
    $(".maskFap").maskMoney({
    	decimal:',', 
    	precision: 4});
    $(".maskNoDocumento").mask("9999/9999",{placeholder:" "});  // NUMERO DO DOCUMENTO
    $(".maskHora").mask("99:99", {placeholder:" "}); // HORA

    // FIM MASCARAS
    
});