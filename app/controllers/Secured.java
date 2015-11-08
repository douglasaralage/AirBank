package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

/**
 * Classe que estende Security.Authenticator
 * @author andre
 *
 */
public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("connected");
    }
    /**
     * Método invocado caso o usuario nao esteja autenticado
     */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
   
  
    
}