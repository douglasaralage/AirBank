package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

//import com.ning.http.client.FilePart;
//import akka.io.Tcp.Event;

//models do programa
import models.User;
import models.Event;
import models.Noticias;
import play.*;

import play.data.Form;
import play.mvc.*;

import views.html.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

public class Application extends Controller {

//formulario para fazer login
	static Form<Login> formLogin = Form.form(Login.class);
//formulario para cadastrar
	static Form<User> formUser = Form.form(User.class);
//formulario para cadastrar evento
	static Form<Event> formEvent = Form.form(Event.class);
//formulario para cadastro de noticias
	static Form<Noticias> formNoticias = Form.form(Noticias.class);
//formulario para atualização do usuario
	static Form<User> formup = Form.form(User.class);
	
//adicionando usuario
	public static Result addUser() {
		Form<User> formulario = formUser.bindFromRequest();
		if (formulario.hasErrors()){
			flash("Erro","Preencha todos os campos corretamentes");
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
/*Pagina de Cadastro de Evento Form*/	
	public static Result jogo(){
		List<Event> nome2 = Event.findAll();
		return ok(jogo.render("jogo", nome2));
	}
	public static Result info(){
		User usuario = User.findByEmail(session("connected"));
		return ok(info.render("Info", usuario));
	}
	public static Result principalNoticia(Long id) {
		Noticias noticia = Noticias.findById(id);
		return ok (principalNoticia.render(noticia));
	}
	public static Result principalEvento(Long id) {
		Event evento = Event.findById(id);
		return ok (principalEvento.render(evento));
	}
	public static Result upinfo(Long id){
		Form<User> formalterar = formUser.bindFromRequest();
		Logger.info(formalterar.toString());
		User user = formalterar.get();
		user.password=BCrypt.hashpw(user.password, BCrypt.gensalt());
		user.update(id);
		flash("Certo","Dados Alterado com sucesso");
		return ok(info.render("Info", user));
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
	public static Result adicionarNoticias() {
		return ok(adicionarNoticias.render(formNoticias));
	}
	public static Result addNoticia() throws IOException, FileNotFoundException {
		Form<Noticias> novoform = formNoticias.bindFromRequest();
		Logger.info(novoform.errors().toString());
		if(novoform.hasErrors()){
			flash("Erro","Preencha todos os campos corretamentes");
			return ok(adicionarNoticias.render(formNoticias));
		}else{
		Noticias novaNoticias = novoform.get();
		Logger.info(novoform.toString());
		if(novaNoticias.imgNoticias != null) {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("picture");
		novaNoticias.imgNoticias = IOUtils.toByteArray(new FileInputStream(picture.getFile()));
		}
		novaNoticias.save();
		flash("Success","Noticia cadastrada com sucesso.");
		return ok(adicionarNoticias.render(formNoticias));}
	}
//get imagem
	public static Result getImageNoticia(Long idNoticias){
		Noticias noticia = Noticias.findById(idNoticias);
		response().setContentType("image/jpeg");
		return ok(noticia.imgNoticias);
	}
	public static Result getImageEvento(Long idEvent){
		Event evento = Event.findById(idEvent);
		response().setContentType("image/jpeg");
		return ok(evento.imgEvent);
	}
//addeventos
	public static Result adicionarEvento() {
		return ok(adicionarEvento.render(formEvent));
	}
	public static Result addEvento() throws IOException, FileNotFoundException {
		Form<Event> novoform = formEvent.bindFromRequest();
		Logger.info(novoform.errors().toString());
		if(novoform.hasErrors()){
			flash("Errorr","Preencha todos os campos corretamentes");
			return ok(adicionarEvento.render(formEvent));
		}else{
		Event novoEvent = novoform.get();
		if(novoEvent.imgEvent != null) {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("picture");
		novoEvent.imgEvent = IOUtils.toByteArray(new FileInputStream(picture.getFile()));
		}
		novoEvent.save();
		flash("Succes","Evento cadastrada com sucesso.");
		return ok(adicionarEvento.render(formEvent));}
	}
}
