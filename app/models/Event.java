package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import play.data.format.*;


@Entity
public class Event extends Model {
	
	@Id
	public Long id;
	
	@Required
	public String nomeEvento;
	
	@Required
	public String horasEventos;
	
	@Required
	public String sobreEvento;
	
	@Required
	public String localEvento;
	
	@Required
	public String dataEvento;
	
	@Column(columnDefinition="LONGBLOB")
	public byte[] imgEvent;
	
	public static Model.Finder<Long, Event> find = new Model.Finder<>(Long.class, Event.class);
	
	public static List<Event> findAll(){
		return find.all();
	}
	public static Event findById(Long id){
		return find.where().eq("id", id).findUnique();
	}
}