package account.transfers.service.impl;

import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import account.transfers.service.AccountTransactionService;
import account.transfers.service.CurrencyRatesService;
import java.math.BigDecimal;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccountTransactionServiceImpl implements AccountTransactionService {

  private final AccountRepository accountRepository;
  private final CurrencyRatesService currencyRatesService;

  @Inject
  public AccountTransactionServiceImpl(AccountRepository accountRepository,
      CurrencyRatesService currencyRatesService) {
    this.accountRepository = accountRepository;
    this.currencyRatesService = currencyRatesService;
  }

  @Override
  public void transfer(UUID accountIdFrom, UUID accountIdTo, BigDecimal amount) {

    Account accountFrom =
        accountRepository.findByAccountId(accountIdFrom).orElseThrow(() -> new IllegalStateException(String.format(
            "Account sender with id %s not found", accountIdFrom.toString())));

    Account accountTo =
        accountRepository.findByAccountId(accountIdTo).orElseThrow(() -> new IllegalStateException(String.format(
            "Account receiver with id %s not found", accountIdTo.toString())));

    BigDecimal rate = currencyRatesService.fetchCurrencyRate(accountFrom.getCurrency(), accountTo.getCurrency());

    BigDecimal convertedAmountFrom = amount.multiply(rate);

    if (accountFrom.getAccountBalance().compareTo(convertedAmountFrom) < 0) {
      throw new IllegalStateException(String.format("Account with accountId %s cannot send %s %s. Insufficient "
          + "funds!", accountFrom.getAccountId(), convertedAmountFrom, accountTo.getCurrency()));
    }

    accountFrom.setAccountBalance(accountFrom.getAccountBalance().subtract(convertedAmountFrom));
    accountTo.setAccountBalance(accountTo.getAccountBalance().add(convertedAmountFrom));
  }
}
