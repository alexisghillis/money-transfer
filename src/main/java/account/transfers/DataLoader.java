package account.transfers;

import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import account.transfers.util.Currency;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class DataLoader {

  public static final int MAX_ACCOUNTS = 100;

  public static void loadData(AccountRepository accountRepository) {

    IntStream.range(1, MAX_ACCOUNTS)
        .forEach(i -> {
          Account account = new Account();

          account.setAccountId(UUID.randomUUID());
          Currency[] availableCurrencies = Currency.values();
          account.setCurrency(availableCurrencies[new Random().nextInt(availableCurrencies.length)]);
          account.setAccountBalance(new BigDecimal(ThreadLocalRandom.current().nextInt(10, 2000)));

          accountRepository.save(account);
        });
  }
}
