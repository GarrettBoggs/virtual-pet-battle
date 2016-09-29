import java.util.Random;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Game{
  private Person playerOne;
  private Person playerTwo;
  private Person attacker;
  private Person defender;
  private boolean stillPlaying = true;

  public void setPlayers(Person playerOne, Person playerTwo){
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
    attacker = playerOne;
    defender = playerTwo;
  }
  public int randomInt(int min, int max){
    Random rand = new Random();
    return rand.nextInt(max-min) + min;
  }
  public Person getPlayerOne(){
    return playerOne;
  }
  public Person getPlayerTwo(){
    return playerTwo;
  }
  public Person getAttacker(){
    return attacker;
  }
  public Person getDefender(){
    return defender;
  }
  public boolean isStillPlaying(){
    return stillPlaying;
  }
  public String setFirstMove(){
    if(randomInt(1,2) == 1){
      attacker = playerOne;
      defender = playerTwo;
    }else{
      attacker = playerTwo;
      defender = playerOne;
    }
    return (attacker.getName() + " is attacking!");
  }
  public String changeMove(){
    if(attacker == playerOne){
      attacker = playerTwo;
      defender = playerOne;
    }else{
      attacker = playerOne;
      defender = playerTwo;
    }
    return (attacker.getName() + " is attacking!");
  }

  public boolean isDead(){
    if(playerOne.getMonsters().get(0).getCurrentHealth() <= 0){
      stillPlaying = false;
      return true;
    }
    return false;
  }
}
