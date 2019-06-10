package account.transfers;

import account.transfers.dto.AccountTransferRequestDTO;
import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import account.transfers.util.Currency;
import java.math.BigDecimal;
import java.util.UUID;
import org.jooby.test.JoobyRule;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class IntegrationTests {

  private App app = new App();

  @Rule
  public JoobyRule req = new JoobyRule(app);

  @Test
  public void healthCheck() {
    get("/swagger")
        .then()
        .assertThat()
        .body(notNullValue());
  }

  @Test
  public void notFoundCheck() {

    when()
        .get("/drunkcheck")
        .then()
        .statusCode(404);
  }

  @Test
  public void getAllAccounts() {
    when().get("/monetary-transfer/accounts")
        .then()
        .body("$", hasSize((DataLoader.MAX_ACCOUNTS) - 1));
  }

  @Test
  public void getAccount() {

    UUID accountID = UUID.randomUUID();

    createAccount(accountID, Currency.EURO, new BigDecimal("1400"));

    when().get(String.format("/monetary-transfer/accounts/%s", accountID))
        .then()
        .body("accountId", is(accountID.toString()))
        .body("accountBalance", comparesEqualTo(1400));
  }

  @Test
  public void getMissingAccount() {
    when().get(String.format("/monetary-transfer/accounts/%s", UUID.randomUUID()))
        .then()
        .statusCode(404);
  }


  @Test
  public void deleteAccount() {
    UUID accountID = UUID.randomUUID();
    createAccount(accountID, Currency.EURO, new BigDecimal("1400"));

    when().get(String.format("/monetary-transfer/accounts/%s", accountID))
        .then()
        .body("accountId", is(accountID.toString()))
        .body("accountBalance", comparesEqualTo(1400));

    when().delete(String.format("/monetary-transfer/accounts/%s", accountID))
        .then()
        .statusCode(204);


    when().get(String.format("/monetary-transfer/accounts/%s", accountID))
        .then()
        .statusCode(404);
  }

  @Test
  public void validTransfer() {
    UUID accountIDSender = UUID.randomUUID();
    UUID accountIdDestination = UUID.randomUUID();

    createAccount(accountIDSender, Currency.EURO, new BigDecimal("1400"));
    createAccount(accountIdDestination, Currency.GREAT_BRITAIN_POUND, new BigDecimal("1000"));

    given().when().body(new AccountTransferRequestDTO(accountIDSender, accountIdDestination, new BigDecimal("10")))
        .contentType("application/json")
        .post("/monetary-transfer/accounts/transfer")
        .then()
        .statusCode(202);

    when()
        .get(String.format("/monetary-transfer/accounts/%s", accountIDSender))
        .then()
        .body("accountBalance", comparesEqualTo(1390));

    when()
        .get(String.format("/monetary-transfer/accounts/%s", accountIdDestination))
        .then()
        .body("accountBalance", comparesEqualTo(new Float("1008.9")));
  }

  @Test
  public void transferDestinationAccountDoesNotExist() {

    UUID accountIDSender = UUID.randomUUID();
    UUID accountIdDestination = UUID.randomUUID();

    createAccount(accountIDSender, Currency.EURO, new BigDecimal("1400"));

    given().when().body(new AccountTransferRequestDTO(accountIDSender, accountIdDestination, new BigDecimal("10")))
        .contentType("application/json")
        .post("/monetary-transfer/accounts/transfer")
        .then()
        .statusCode(500)
        .body(containsString(String.format("Account receiver with id %s not found", accountIdDestination)));
  }

  @Test
  public void transferSenderAccountInsufficientFounds() {
    UUID accountIDSender = UUID.randomUUID();
    UUID accountIdDestination = UUID.randomUUID();

    createAccount(accountIDSender, Currency.EURO, new BigDecimal("1400"));
    createAccount(accountIdDestination, Currency.GREAT_BRITAIN_POUND, new BigDecimal("1000"));

    given().when().body(new AccountTransferRequestDTO(accountIDSender, accountIdDestination, new BigDecimal("1900")))
        .contentType("application/json")
        .post("/monetary-transfer/accounts/transfer")
        .then()
        .statusCode(500)
        .body(containsString(String.format("Account with accountId %s cannot send 1691.00 GREAT_BRITAIN_POUND. "
                + "Insufficient funds!",
            accountIDSender)));

    when()
        .get(String.format("/monetary-transfer/accounts/%s", accountIDSender))
        .then()
        .body("accountBalance", comparesEqualTo(1400));
  }

  //util
  private void createAccount(UUID accountId, Currency currency, BigDecimal balance) {
    app.require(AccountRepository.class).save(new Account(accountId, currency, balance));
  }
}
