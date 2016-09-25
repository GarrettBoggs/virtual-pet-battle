import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class PersonTest {

    @Rule
    public DatabaseRule database = new DatabaseRule();

    @Test
    public void person_instantiatesCorrestly_true(){
        Person testPerson = new Person("Henry", "henry@henry.com");
        assertEquals(true, testPerson instanceof Person);
    }
    @Test
    public void getName_personInstatiatesWithName_true(){
        Person testPerson = new Person("Henry", "henry@henry.com");
        assertEquals("Henry", testPerson.getName());
    }
    @Test
    public void getName_personInstatiatesWithEmail_true(){
        Person testPerson = new Person("Henry", "henry@henry.com");
        assertEquals("henry@henry.com", testPerson.getEmail());
    }

    @Test
    public void equals_returnsTrueIfNameAndEmailAreSame_true(){
        Person testPerson1 = new Person("Henry", "henry@henry.com");
        Person testPerson2 = new Person("Henry", "henry@henry.com");
        assertTrue(testPerson1.equals(testPerson2));
    }

    @Test
  public void save_assignsIdToObject() {
    Person testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
    Person savedPerson = Person.all().get(0);
    assertEquals(testPerson.getId(), savedPerson.getId());
  }

  @Test
  public void getMonsters_retrievesAllMonstersFromDatabase_monstersList() {
    Person testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
    Monster firstMonster = new Monster("Bubbles", testPerson.getId());
    firstMonster.save();
    Monster secondMonster = new Monster("Spud", testPerson.getId());
    secondMonster.save();
    Monster[] monsters = new Monster[] { firstMonster, secondMonster };
    assertTrue(testPerson.getMonsters().containsAll(Arrays.asList(monsters)));
  }

}
