package account.transfers.service;

import account.transfers.util.Currency;
import java.math.BigDecimal;

public interface CurrencyRatesService {

  BigDecimal fetchCurrencyRate(Currency from, Currency to);
}
