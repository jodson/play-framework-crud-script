package models;

import javax.persistence.Entity;
import java.util.List;
@Entity
public class Jodson extends Timestampable {

    public String nome;
  public String nani;


  public static Finder<Long,Jodson> find = new Finder(Long.class, Jodson.class);

  public static List<Jodson> all() {
    return find.all();
  }
}
