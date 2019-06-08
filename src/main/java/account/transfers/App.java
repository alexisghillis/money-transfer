package account.transfers;

import org.jooby.Jooby;
import org.jooby.json.Jackson;

/**
 * Hello starter project.
 */
public class App extends Jooby {

  {
    /** Render JSON: */
    use(new Jackson());

    /**
     * Say hello:
     */
    get(req -> {
      String name = req.param("name").value("Jooby");
      return new Message("Hello " + name + "!");
    });

  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
