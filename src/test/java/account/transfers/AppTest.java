package account.transfers;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

import org.jooby.test.JoobyRule;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

public class AppTest {

  /**
   * One app/server for all the test of this class. If you want to start/stop a new server per test,
   * remove the static modifier and replace the {@link ClassRule} annotation with {@link Rule}.
   */
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());

  @Ignore
  @Test
  public void integrationTest() {
    get("/")
        .then()
        .assertThat()
        .body(equalTo("{\"message\":\"Hello Jooby!\"}"))
        .statusCode(200)
        .contentType("application/json;charset=UTF-8");
  }
}
