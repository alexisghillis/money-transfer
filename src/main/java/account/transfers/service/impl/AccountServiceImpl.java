package account.transfers.service.impl;

import account.transfers.converter.AccountDTOToAccountConverter;
import account.transfers.converter.AccountToAccountDTOConverter;
import account.transfers.dto.AccountDTO;
import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import account.transfers.service.AccountService;
import account.transfers.service.CurrencyRatesService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jooby.Err;
import org.jooby.mvc.Body;

@Singleton
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final CurrencyRatesService currencyRatesService;
  private final AccountToAccountDTOConverter accountToAccountDTOConverter;
  private final AccountDTOToAccountConverter accountDTOToAccountConverter;

  @Inject
  public AccountServiceImpl(AccountRepository accountRepository,
      CurrencyRatesService currencyRatesService,
      AccountToAccountDTOConverter accountToAccountDTOConverter,
      AccountDTOToAccountConverter accountDTOToAccountConverter) {
    this.accountRepository = accountRepository;
    this.currencyRatesService = currencyRatesService;
    this.accountToAccountDTOConverter = accountToAccountDTOConverter;
    this.accountDTOToAccountConverter = accountDTOToAccountConverter;
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

    accountFrom.setAccountBalance(accountFrom.getAccountBalance().subtract(amount));
    accountTo.setAccountBalance(accountTo.getAccountBalance().add(convertedAmountFrom));
  }

  @Override
  public List<AccountDTO> getAllAccounts() {
    return accountRepository.getAllAccounts()
        .map(it -> accountToAccountDTOConverter.convert(it))
        .collect(Collectors.toList());
  }

  @Override
  public AccountDTO getAccount(UUID accountId) {
    return accountRepository.findByAccountId(accountId)
        .map(it -> accountToAccountDTOConverter.convert(it))
        .orElseThrow(() -> new Err(404, String.format("The following accountId: %s was not found in repository",
            accountId.toString())));
  }

  @Override
  public Account addAccount(@Body AccountDTO accountDTO) {
    return accountRepository.save(accountDTOToAccountConverter.convert(accountDTO));
  }

  @Override
  public void deleteAccount(UUID accountId) {
    accountRepository.delete(accountId);
  }
}
