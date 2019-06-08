package account.transfers.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountTransactionService {

  void transfer(UUID accountIdFrom, UUID accountIdTo, BigDecimal amount);
}
