import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;


public abstract class Monster {
  public int id;
  public String name;
  public int personId;
  public int foodLevel;
  public int sleepLevel;
  public int playLevel;
  public int heal_counter;
  public Timestamp birthday;
  public Timestamp lastSlept;
  public Timestamp lastAte;
  public Timestamp lastPlayed;
  public Timer timer;
  public String type;
  public int current_health;

  public static final int MAX_FOOD_LEVEL = 3;
  public static final int MAX_SLEEP_LEVEL = 8;
  public static final int MAX_PLAY_LEVEL = 12;
  public static final int MIN_ALL_LEVELS = 0;
  public static final int MAX_HEALTH = 100;
  public static final int MAX_HEALS = 1;

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
  public int getPlayLevel(){
    return playLevel;
  }
  public int getSleepLevel(){
    return sleepLevel;
  }
  public int getFoodLevel(){
    return foodLevel;
  }
  public Timestamp getBirthday(){
    return birthday;
  }
  public Timestamp getLastSlept(){
    return lastSlept;
  }
  public Timestamp getLastAte(){
    return lastAte;
  }
  public Timestamp getLastPlayed(){
    return lastPlayed;
  }
  public int getCurrentHealth(){
    return current_health;
  }
  public String getType(){
    return type;
  }
  public int getHealCount(){
    return heal_counter;
  }

  public int randomInt(int min, int max){
    Random rand = new Random();
    return rand.nextInt(max-min) + min;
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

  public boolean isAlive() {
    if (foodLevel <= MIN_ALL_LEVELS ||
    playLevel <= MIN_ALL_LEVELS ||
    sleepLevel <= MIN_ALL_LEVELS) {
      return false;
    }
    return true;
  }

  public void depleteLevels(){
    if (isAlive()){
      playLevel--;
      foodLevel--;
      sleepLevel--;
    }
  }

  public void play(){
    if (playLevel >= MAX_PLAY_LEVEL){
      throw new UnsupportedOperationException("You cannot play with monster anymore!");
    }
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE monsters SET lastplayed = now() WHERE id = :id";
      con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
    playLevel++;
  }

  public void sleep(){
    if (sleepLevel >= MAX_SLEEP_LEVEL){
      throw new UnsupportedOperationException("You cannot make your monster sleep anymore!");
    }
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE monsters SET lastslept = now() WHERE id = :id";
      con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
    sleepLevel++;
  }

  public void feed(){
    if (foodLevel >= MAX_FOOD_LEVEL){
      throw new UnsupportedOperationException("You cannot feed your monster anymore!");
    }
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE monsters SET lastate = now() WHERE id = :id";
      con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
    foodLevel++;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO monsters (name, personId, birthday, type, current_health, heal_counter) VALUES (:name, :personId, now(), :type, :current_health, :heal_counter)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("personId", this.personId)
      .addParameter("type", this.type)
      .addParameter("current_health", this.current_health)
      .addParameter("heal_counter", this.heal_counter)
      .executeUpdate()
      .getKey();
    }
  }


  public void startTimer(){
    Monster currentMonster = this;
    TimerTask timerTask = new TimerTask(){
      @Override
      public void run() {
        if (currentMonster.isAlive() == false){
          cancel();
        }
        depleteLevels();
      }
    };
    this.timer.schedule(timerTask, 0, 600);
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM monsters WHERE id = :id;";
    con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public void setHealth(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE monsters SET current_health = :current_health WHERE id = :id";
      con.createQuery(sql)
      .addParameter("id",id)
      .addParameter("current_health",this.current_health)
      .executeUpdate();
    }
  }

  public void setHealCounter(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE monsters SET heal_counter = :heal_counter WHERE id = :id";
      con.createQuery(sql)
      .addParameter("id",id)
      .addParameter("heal_counter",this.heal_counter)
      .executeUpdate();
    }
  }

  public void takeDamage(int damage){
    current_health -= damage;
    this.setHealth();
  }

  public void heal(){
    // System.out.println("*** hc1: ***" + heal_counter);
    current_health += 20;
    if(current_health > MAX_HEALTH){
      current_health = MAX_HEALTH;
    }
    this.setHealth();
    heal_counter -= 1;
    this.setHealCounter();
      // System.out.println("*** hc2: ***" + heal_counter);
  }

  public int melee(){
      int hit = randomInt(0, 10);
      if (hit <= 2){
          return 0;
      }
      else if (hit > 2 && hit <= 8){
          return (randomInt(8, 12));
      }
      return (randomInt(15, 20));
  }

}
