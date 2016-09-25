import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.sql2o.*;

public class Monster {
    public int id;
    private String name;
    private int personId;

    public Monster(String name, int personId){
        this.name = name;
        this.personId = personId;
    }
    // Getters
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getPersonId(){
        return personId;
    }

    @Override
    public boolean equals(Object otherMonster){
        if (!(otherMonster instanceof Monster)) {
            return false;
        } else {
            Monster newMonster = (Monster) otherMonster;
            return this.getName().equals(newMonster.getName()) &&
            this.getPersonId() == newMonster.getPersonId();
        }
    }
    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO monsters (name, personid) VALUES (:name, :personId)";
            this.id = (int) con.createQuery(sql, true)
            .addParameter("name", this.name)
            .addParameter("personId", this.personId)
            .executeUpdate()
            .getKey();
        }
    }
    public static List<Monster> all() {
        String sql = "SELECT * FROM monsters";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Monster.class);
        }
    }
    public static Monster find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM monsters where id=:id";
            Monster monster = con.createQuery(sql)
            .addParameter("id", id)
            .executeAndFetchFirst(Monster.class);
            return monster;
        }
    }
}
