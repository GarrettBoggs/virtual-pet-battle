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
      Person playerOne = new Person("Jeff","jeff@gmail.com");
      playerOne.save();
      Person playerTwo = new Person("Bob","bob@gmail.com");
      playerTwo.save();
      FireMonster playerOneMonster = new FireMonster("Ash",playerOne.getId());
      playerOneMonster.save();
      WaterMonster playerTwoMonster = new WaterMonster("Bubbles",playerTwo.getId());
      playerTwoMonster.save();
      newGame.setPlayers(Person.find(playerOne.getId()), Person.find(playerTwo.getId()));
      // newGame.setFirstMove();

      model.put("game", newGame);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    get("/battle", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      if(newGame.isStillPlaying()){
        newGame.getDefender().getMonsters().get(0).takeDamage(newGame.getAttacker().getMonsters().get(0).melee());
        if(newGame.isDead()){
          return;
        }

        newGame.changeMove();
      }
      model.put("game", newGame);
      model.put("template", "templates/battle.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
