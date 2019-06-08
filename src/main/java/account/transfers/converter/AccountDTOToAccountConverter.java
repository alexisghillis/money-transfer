package account.transfers.converter;

import account.transfers.dto.AccountDTO;
import account.transfers.repository.entities.Account;
import account.transfers.util.Currency;

public class AccountDTOToAccountConverter {

  public Account convert(AccountDTO source) {
    if (source == null) {
      return null;
    }

    Account account = new Account();

    account.setAccountId(source.getAccountId());
    account.setCurrency(Currency.valueOf(source.getCurrency()));
    account.setAccountBalance(source.getAccountBalance());

    return account;
  }
}
