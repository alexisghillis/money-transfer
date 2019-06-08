package account.transfers.repository.impl;

import account.transfers.converter.AccountDTOToAccountConverter;
import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.inject.Singleton;

@Singleton
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
    accountsPersistence.put(account.getAccountId(), account);
    return account;
  }

  @Override
  public void delete(UUID accountId) {
    accountsPersistence.remove(accountId);
  }
}
