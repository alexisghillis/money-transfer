package account.transfers.service.impl;

import account.transfers.service.CurrencyRatesService;
import account.transfers.util.Currency;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;

@Singleton
public class CurrencyRatesServiceImpl implements CurrencyRatesService {

  private Map<AvailableTransformations, BigDecimal> availableTransformationRates =
      new HashMap<AvailableTransformations, BigDecimal>() {{
        put(new AvailableTransformations(Currency.EURO, Currency.GREAT_BRITAIN_POUND), new BigDecimal("0.89"));
        put(new AvailableTransformations(Currency.EURO, Currency.POLAND_ZLOTY), new BigDecimal("4.26"));
        put(new AvailableTransformations(Currency.EURO, Currency.JAPAN_YEN), new BigDecimal("122.70"));
        put(new AvailableTransformations(Currency.USA_DOLLAR, Currency.GREAT_BRITAIN_POUND), new BigDecimal("0.78"));
        put(new AvailableTransformations(Currency.USA_DOLLAR, Currency.POLAND_ZLOTY), new BigDecimal("3.76"));
      }};


  @Override
  public BigDecimal fetchCurrencyRate(Currency from, Currency to) {

    if (from.equals(to)) {
      return BigDecimal.ONE;
    }

    Optional<BigDecimal> rate =
        Optional.ofNullable(availableTransformationRates.get(new AvailableTransformations(from, to)));

    return rate.orElseThrow(() -> new IllegalStateException(String.format("No rate found for conversion %s to %s",
        from.getCurrencyAbbr(), to.getCurrencyAbbr())));
  }

  @AllArgsConstructor
  @Data
  private class AvailableTransformations {
    private Currency currencyFrom;
    private Currency currencyTo;
  }
}
