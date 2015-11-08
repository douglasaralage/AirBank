package models;

import java.util.*;
import javax.persistence.*;
import javax.validation.Constraint;

import play.Logger;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import com.avaje.ebean.annotation.EnumMapping;
import com.avaje.ebean.annotation.Formula;

import controllers.BCrypt;

/**
 * Model de Usuario do Sistema
 * Para AUTENTICACAO
 * As senhas ja sao armazenadas criptografadas no BD com uso do BCRYPT
 */
@Entity 
@Table(name="account")
public class User extends Model {

    @Id
    public Long id;
    
    @Constraints.Required
    @Formats.NonEmpty
    public String email;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    public String password;
    
    
    @EnumMapping(nameValuePairs="Professor = P, Aluno = A")
    public enum Tipo {
    	Professor, Aluno;
    }
    @Enumerated(EnumType.STRING)
    public Tipo tipo;
    // --Crio um objeto da classe Finder, para realizar queries no banco de dados
   
    public int quantidadehoras = 0;
    
    public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);
    
    /**
     * Obtenho todos os usuarios
     */
    public static List<User> findAll() {
        return find.all();
    }

    /**
     * Busca um usuario pelo email
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
    
    /**
     * Autenticando um usuario
     * @param email o email do usuario a ser autenticado
     * @param password a senha informada pelo usuario a ser autenticado
     */
    public static User authenticate(String email, String password) {
    	
    	User loginUser = findByEmail(email);
    	List<User> users = find.all();
    	//Logger.info("TODOS OS USUARIOS " + users);
    	//Logger.info("Verificando usuario! " + loginUser);
    	
    	//devo verificar se é null antes, pois pode ser que o email enviado para login nem exista
    	if(loginUser != null && BCrypt.checkpw(password, loginUser.password))
    		return loginUser; //caso exista e a senha esta ok, entao o usuario é retornado
    	else
    		return null;// caso contrario retorno NULL
    	
    	//Comentario: BCrypt;.checkpw(password, hash), tenta verificar se a hash informada foi gerada a partir da passowrd
    		
    
    	/*
    	Forma origina, sem BCrypt
    	
        return find.where()
            .eq("email", email)
            .eq("password", password)
            .findUnique();
            */
    }
    
    
    
    public String toString() {
        return "User(" + email + ")";
    }

}