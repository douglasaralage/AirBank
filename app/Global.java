
import play.*;

import play.libs.*;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import com.avaje.ebean.Ebean;
import models.*;

import java.util.*;
import play.Logger;


public class Global extends GlobalSettings {
  
    public void onStart(Application app) {
        // Check if the database is empty
    	//Add admin user
        if (User.find.findRowCount() == 0) {
         Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
         	Logger.info("[ADMIN] realizado carregamento inicial de usuario");
            Ebean.save(all.get("users"));
        }
    }
    
    
}
