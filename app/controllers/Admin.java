package controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import models.Event;
import models.Noticias;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.adicionarEvento;
import views.html.adicionarNoticias;
import views.html.info;
import views.html.jogo;
import views.html.principalEvento;


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
	
	
	
	//formulario para cadastrar evento
		static Form<Event> formEvent = Form.form(Event.class);
	//formulario para cadastro de noticias
		static Form<Noticias> formNoticias = Form.form(Noticias.class);
	//formulario para atualização do usuario
		static Form<User> formup = Form.form(User.class);
   
		/*Pagina de Cadastro de Evento Form*/	
		public static Result jogo(){
			List<Event> nome2 = Event.findAll();
			return ok(jogo.render("jogo", nome2));
		}
		public static Result info(){
			User usuario = User.findByEmail(session("connected"));
			return ok(info.render("Info", usuario));
		}
		public static Result principalEvento(Long id) {
			Event evento = Event.findById(id);
			return ok (principalEvento.render(evento));
		}
		
		public static Result adicionarNoticias() {
			return ok(adicionarNoticias.render(formNoticias));
		}
		public static Result addNoticia() throws FileNotFoundException, IOException{
			//Erro ao cadastrar imagem
			//play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
			//play.mvc.Http.MultipartFormData.FilePart picture1 = body.getFile("picture1");
			Form<Noticias> form=formNoticias.bindFromRequest();
			if(form.hasErrors()){
				flash("error","Preencha todos os campos!");
				return badRequest(adicionarNoticias.render(formNoticias));
			}else{
			Noticias newSystem=form.get();
			//Erro ao cadastrar imagem
			//if	(picture != null){
			//String fileName= picture1.getContentType();
			//String contentType = picture1.getContentType();
			//File file = picture1.getFile();
			//newSystem.imgNoticias = IOUtils.toByteArray(new FileInputStream(file));
			flash("success","Noticia cadastrada!");
			newSystem.save();
			return ok (adicionarNoticias.render(formNoticias));
			//}else{
			//return badRequest(adicionarNoticias.render(formNoticias));
			//}
		}}
		
		public static Result getImageEvento(Long idEvent){
			Event evento = Event.find.byId(idEvent);
			response().setContentType("image/jpeg");
			return ok(evento.imgEvent);
		}
	//addeventos
		public static Result adicionarEvento() {
			return ok(adicionarEvento.render(formEvent));
		}
		
		public static Result addEvento() throws FileNotFoundException, IOException{
			play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
			play.mvc.Http.MultipartFormData.FilePart picture = body.getFile("picture");
			Form<Event> form=formEvent.bindFromRequest();
			Logger.info(form.toString());
			if (form.hasErrors()){
				flash("error","Preencha todos os campos!");
				return badRequest(adicionarEvento.render(formEvent));
			}else{
			Event newSystem=form.get();
		if	(picture != null){
			String fileName = picture.getFilename();
			String contentType = picture.getContentType();
			File file = picture.getFile();
			newSystem.imgEvent = IOUtils.toByteArray(new FileInputStream(file));
			flash("success","Atividade cadastrada");
			newSystem.save();
			return ok (adicionarEvento.render(formEvent));
		}else{
			flash("error","Verifique se voce preencheu corretamente o formulario");
			return badRequest(adicionarEvento.render(formEvent));
		}	} }
}

