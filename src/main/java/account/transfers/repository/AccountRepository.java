package account.transfers.repository;

import account.transfers.repository.entities.Account;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface AccountRepository {

  Stream<Account> getAllAccounts();

  Optional<Account> findByAccountId(UUID accountId);

  Account save(Account account);

  void delete(UUID accountId);

}
