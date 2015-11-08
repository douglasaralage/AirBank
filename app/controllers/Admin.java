package controllers;


import java.util.List;


import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;


//a anotation a seguir informa que todas as actions deste controllers
//devem ser feitas apenas por usuarios autenticados
/**
 * Esta classe é um controller no qual todas as suas actions sao 
 * permitidas apenas para usuarios autenticados.
 * 
 * Isto é feito com uso da anotation abaixo
 */
@Security.Authenticated(Secured.class)
public class Admin extends Controller {
	
    //CONTROLLERS PRECEDIDOS DE LOGIN
  
   
   
    
}
