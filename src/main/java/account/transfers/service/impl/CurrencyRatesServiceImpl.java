package account.transfers.service.impl;

import account.transfers.service.CurrencyRatesService;
import account.transfers.util.Currency;
import java.math.BigDecimal;

public class CurrencyRatesServiceImpl implements CurrencyRatesService {
  @Override
  public BigDecimal fetchCurrencyRate(Currency from, Currency to) {
    return null;
  }
}
