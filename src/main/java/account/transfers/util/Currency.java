package account.transfers.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {

  EURO("EUR"),
  USA_DOLLAR("USD"),
  JAPAN_YEN("JPY"),
  GREAT_BRITAIN_POUND("GBP"),
  POLAND_ZLOTY("PLN");

  private String currencyAbbr;

}
