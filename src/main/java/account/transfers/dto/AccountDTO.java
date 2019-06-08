package account.transfers.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class AccountDTO {

  private UUID accountId;
  private String currency;
  private BigDecimal accountBalance;
}
