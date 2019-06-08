package account.transfers.converter;

import account.transfers.dto.AccountDTO;
import account.transfers.repository.entities.Account;

public class AccountToAccountDTOConverter {

  public AccountDTO convert(Account source) {
    if (source == null) {
      return null;
    }

    AccountDTO accountDTO = new AccountDTO();

    accountDTO.setAccountId(source.getAccountId());
    accountDTO.setCurrency(source.getCurrency().getCurrencyAbbr());
    accountDTO.setAccountBalance(source.getAccountBalance());

    return accountDTO;
  }

}
