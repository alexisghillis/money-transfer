package account.transfers.controller;

import account.transfers.converter.AccountToAccountDTOConverter;
import account.transfers.dto.AccountDTO;
import account.transfers.dto.AccountTransferRequestDTO;
import account.transfers.repository.AccountRepository;
import account.transfers.service.AccountTransactionService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooby.Err;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.Body;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

@Path("/monetary-transfer/accounts")
public class AccountController {

  private AccountTransactionService accountTransactionService;
  private AccountRepository accountRepository;
  private AccountToAccountDTOConverter accountToAccountDTOConverter;

  @GET
  public List<AccountDTO> getAllAccounts() {
    return accountRepository.getAllAccounts()
        .map(it -> accountToAccountDTOConverter.convert(it))
        .collect(Collectors.toList());
  }

  @GET
  @Path("/{accountId}")
  public AccountDTO getAccount(UUID accountId) {
    return accountRepository.findByAccountId(accountId)
        .map(it -> accountToAccountDTOConverter.convert(it))
        .orElseThrow(() -> new Err(404, String.format("The following accountId: %s was not found in repository",
            accountId.toString())));
  }

  @POST
  @Path("/transfer")
  public Result monetaryTransfer(@Body AccountTransferRequestDTO accountTransferRequestDTO) {
    accountTransactionService.transfer(accountTransferRequestDTO.getAccountIdFrom(),
        accountTransferRequestDTO.getAccountIdTo(), accountTransferRequestDTO.getAmount());

    return Results.accepted();
  }
}
