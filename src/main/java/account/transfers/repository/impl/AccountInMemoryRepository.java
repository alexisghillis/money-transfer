package account.transfers.repository.impl;

import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class AccountInMemoryRepository implements AccountRepository {

  private Map<UUID, Account> accountsPersistence = new ConcurrentHashMap<>();

  @Override
  public Stream<Account> getAllAccounts() {
    return accountsPersistence.values().parallelStream();
  }

  @Override
  public Optional<Account> findByAccountId(UUID accountId) {
    return Optional.ofNullable(accountsPersistence.get(accountId));
  }

  @Override
  public Account save(Account account) {
    return accountsPersistence.put(account.getAccountId(), account);
  }

  @Override
  public void delete(Account account) {
    accountsPersistence.remove(account.getAccountId());
  }
}
