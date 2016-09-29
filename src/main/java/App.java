import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    Game newGame = new Game();

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/pre-battle", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Monster playerOneMonster;
      Monster playerTwoMonster;

      String pOneTeamName = request.queryParams("pOne-team-name");
      String pTwoTeamName = request.queryParams("pTwo-team-name");
      String pOneMonster = request.queryParams("pOne-type");
      String pTwoMonster = request.queryParams("pTwo-type");
      Person playerOne = new Person(pOneTeamName,"jeff@gmail.com");
      playerOne.save();
      Person playerTwo = new Person(pTwoTeamName,"bob@gmail.com");
      playerTwo.save();

      if(pOneMonster.equals("water")){
        playerOneMonster = new WaterMonster("Bubbles", playerOne.getId());
      }else{
        playerOneMonster = new FireMonster("Ash",playerOne.getId());
      }
      playerOneMonster.save();

      if(pTwoMonster.equals("water")){
        playerTwoMonster = new WaterMonster("Spigot", playerTwo.getId());
      }else{
        playerTwoMonster = new FireMonster("Coal",playerTwo.getId());
      }
      playerTwoMonster.save();

      newGame.setPlayers(Person.find(playerOne.getId()), Person.find(playerTwo.getId()));
      newGame.setFirstMove();

      model.put("game", newGame);
      model.put("template", "templates/battle.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/battle-melee", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      // if(newGame.isStillPlaying()){


      int dmg = newGame.getAttacker().getMonsters().get(0).melee();
      // System.out.println("*******DAMAGE DONE*******" + dmg);
      newGame.getDefender().getMonsters().get(0).takeDamage(dmg);
      // System.out.println("*******HEALTH*******" + newGame.getDefender().getMonsters().get(0).getCurrentHealth());
      // System.out.println("*******HEALTH*******" + newGame.getDefender().getMonsters().get(0).isDead());
      newGame.isDead();
      newGame.changeMove();

      model.put("game", newGame);
      model.put("template", "templates/battle.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/battle-heal", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      newGame.getAttacker().getMonsters().get(0).heal();
      newGame.changeMove();
      model.put("game", newGame);
      model.put("template", "templates/battle.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
