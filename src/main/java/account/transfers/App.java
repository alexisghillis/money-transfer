package account.transfers;

import account.transfers.controller.AccountController;
import account.transfers.repository.AccountRepository;
import account.transfers.repository.impl.AccountInMemoryRepository;
import account.transfers.service.AccountTransactionService;
import account.transfers.service.CurrencyRatesService;
import account.transfers.service.impl.AccountTransactionServiceImpl;
import account.transfers.service.impl.CurrencyRatesServiceImpl;
import org.jooby.Jooby;
import org.jooby.apitool.ApiTool;
import org.jooby.json.Jackson;

/**
 * Hello starter project.
 */
public class App extends Jooby {

  {
    onStart(registry -> DataLoader.loadData(registry.require(AccountRepository.class)));

    use(new Jackson());

    use(new ApiTool().swagger());

    use(AccountController.class);

    use((environment, configuration, binder) -> {
      binder.bind(AccountTransactionService.class).to(AccountTransactionServiceImpl.class);
      binder.bind(CurrencyRatesService.class).to(CurrencyRatesServiceImpl.class);
      binder.bind(AccountRepository.class).to(AccountInMemoryRepository.class);
    });

  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
