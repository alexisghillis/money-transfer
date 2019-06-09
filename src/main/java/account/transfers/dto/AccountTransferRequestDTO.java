package account.transfers.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferRequestDTO {

  private UUID accountIdFrom;
  private UUID accountIdTo;
  private BigDecimal amount;
}
