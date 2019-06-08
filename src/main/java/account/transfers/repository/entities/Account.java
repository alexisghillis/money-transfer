package account.transfers.repository.entities;

import account.transfers.util.Currency;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class Account {

  private UUID accountId;
  private Currency currency;
  private volatile BigDecimal accountBalance;
}
