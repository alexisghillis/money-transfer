package account.transfers;

import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import account.transfers.service.AccountTransactionService;
import account.transfers.service.CurrencyRatesService;
import account.transfers.service.impl.AccountTransactionServiceImpl;
import account.transfers.util.Currency;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;

public class UnitTests {

  @Mock
  private AccountRepository accountRepository;
  @Mock
  private CurrencyRatesService currencyRatesService;
  private AccountTransactionService accountTransactionService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    accountTransactionService = new AccountTransactionServiceImpl(accountRepository, currencyRatesService);
    Mockito.when(currencyRatesService
        .fetchCurrencyRate(Currency.EURO, Currency.GREAT_BRITAIN_POUND))
        .thenReturn(new BigDecimal("0.89"));
  }

  @Test
  public void makeTransaction() {

    UUID accountIDSender = UUID.randomUUID();
    UUID accountIdDestination = UUID.randomUUID();

    Account accountSender = new Account(accountIDSender, Currency.EURO, new BigDecimal("1400"));
    Account accountDestination = new Account(accountIdDestination, Currency.GREAT_BRITAIN_POUND, new BigDecimal("1000"
    ));

    Mockito.when(accountRepository.findByAccountId(accountIDSender))
        .thenReturn(Optional.of(accountSender));
    Mockito.when(accountRepository.findByAccountId(accountIdDestination))
        .thenReturn(Optional.of(accountDestination));

    accountTransactionService.transfer(accountIDSender, accountIdDestination, new BigDecimal("10"));

    assertTrue(accountSender.getAccountBalance().equals(BigDecimal.valueOf(1390)));
    assertTrue(accountDestination.getAccountBalance().toString().equals("1008.90"));

  }
}
