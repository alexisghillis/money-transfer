package account.transfers.repository.entities;

import account.transfers.util.Currency;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

  private UUID accountId;
  private Currency currency;
  private volatile BigDecimal accountBalance;
}
