package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import play.data.format.Formats;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Noticias extends Model {
	
	@Id
	public Long id;
	
	@Required
	public String nomeNoticias;
	
	@Required
	public String sobreNoticias;
	
	
	@Required
	public String dataNoticias;
	
	@Column(columnDefinition="LONGBLOB")
	public byte[] imgNoticias;
	
	
	public static Model.Finder<Long, Noticias> find = new Model.Finder<>(Long.class, Noticias.class);
	
	public static List<Noticias> findAll(){
		return find.all();
	}
	public static Noticias findById(Long id){
		return find.where().eq("id", id).findUnique();
	}
}