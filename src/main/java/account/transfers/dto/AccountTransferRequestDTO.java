package account.transfers.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class AccountTransferRequestDTO {

  private UUID accountIdFrom;
  private UUID accountIdTo;
  private BigDecimal amount;
}
