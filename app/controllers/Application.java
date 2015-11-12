package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
//models do programa
import models.User;
import models.Event;
import models.Noticias;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;


public class Application extends Controller {

//formulario para fazer login
	static Form<Login> formLogin = Form.form(Login.class);
//formulario para cadastrar
	static Form<User> formUser = Form.form(User.class);

	
//adicionando usuario
	public static Result addUser() {
		Form<User> formulario = formUser.bindFromRequest();
		if (formulario.hasErrors()){
			flash("Error","Preencha todos os campos corretamentes");
			return ok(cadastro.render(formUser));
		}else{
		User user = formulario.get();
		user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
		Logger.info("Criando usuario:"+user.email+"com senha:"+user.password);
		user.save();
		flash("success","Usuario Cadastrado com sucesso!");
		return login();}
	}
	
	/*
	 * ACTIONS DO CONTROLLERS
	 */
/*Pagina de Localização*/
	public static Result localizacao() {
		return ok(localizacao.render("Localizacao Air"));
	}
/*Pagina de Cadastro*/	
	public static Result cadastro() {
		return ok(cadastro.render(formUser));
	}
/*Pagina Sobre*/	
	public static Result sobre() {
		return ok(sobre.render("Sobre Air"));
	}
/*Pagina de Noticias*/		
	public static Result noticias(){
		List<Noticias> nome1 = Noticias.findAll();
		return ok(noticias.render("Noticias Air", nome1));
	}
/*Index*/	
	public static Result index() {
		return ok(index.render("Home Air"));
	}

	public static Result principalNoticia(Long id) {
		Noticias noticia = Noticias.findById(id);
		return ok (principalNoticia.render(noticia));
	}
	
	/*
	 * METODOS E CLASSES PARA LOGIN
	 */
	// -- Classe para autenticacao
	public static class Login {

		// dados necessarios para login
		public String email;
		public String password;

		// AQUI ESTA O TRUQUE, COMO VALIDAR UM FORMULARIO
		// NA HORA EM QUE O FORMULARIO É VALIDADO, EXECUTAMOS O LOGIN!
		// O metodo validade é invocado quando o framework play valida o
		// formulario de login
		//Observe que ele invoca o metodo authenticate do model User
		//e nao o authenticate que encontra-se neste controller
		public String validate() {
			if (User.authenticate(email, password) == null) {
				return "Usuario ou senha inválidos";
			}
			return null;
		}

	} // -- fim da classe login

	/**
	 * Login page.
	 */
	public static Result login() {
		return ok(login.render(formLogin));
	}

	/**
	 * Atua na verificacao do formulario do login
	 */
	public static Result authenticate() {
		
		//recebe formulario de login
		Form<Login> loginForm = formLogin.bindFromRequest();
		//valida o formulario e, ao mesmo tempo, se o usuario conseguiu se autenticar
		if (loginForm.hasErrors()) {
			flash("error", "Login ou senha inválida. Tente novamente");
			//caso nao, envia novamente para o usuario
			return badRequest(login.render(loginForm));
		} else {
			//usuario autenticado
			//posso inserir dados na sessão
	
			session("connected", loginForm.get().email);
			
				User user = User.findByEmail(loginForm.get().email);
			if(user.tipo.equals(User.Tipo.Professor)) 
				session("professor","professor");
			else
				session("aluno","aluno");
			//rediciono ele para a pagina inicial
			return redirect(routes.Application.index());
		}
	}

	/**
	 * Logout and clean the session.
	 */
	public static Result logout() {
		session().clear();
		flash("success", "Sessão finalizada com sucesso.");
		return redirect(routes.Application.index());
	}
	
	public static Result upinfo(Long id){
		Form<User> formalterar = formUser.bindFromRequest();
		Logger.info(formalterar.toString());
		User user = formalterar.get();
		user.password=BCrypt.hashpw(user.password, BCrypt.gensalt());
		user.update(id);
		flash("success","Dados Alterado com sucesso");
		return ok(info.render("Info", user));
		}
	
	
//get imagem
	public static Result getImageNoticia(Long idNoticias){
		Noticias noticia = Noticias.find.byId(idNoticias);
		response().setContentType("image/jpeg");
		return ok(noticia.imgNoticias);
	}
	
}
