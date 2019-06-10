package account.transfers.service;

import account.transfers.dto.AccountDTO;
import account.transfers.repository.entities.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.jooby.mvc.Body;

public interface AccountService {

  void transfer(UUID accountIdFrom, UUID accountIdTo, BigDecimal amount);

  List<AccountDTO> getAllAccounts();

  AccountDTO getAccount(UUID accountId);

  Account addAccount(@Body AccountDTO accountDTO);

  void deleteAccount(UUID accountId);
}
