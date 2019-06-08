package account.transfers.controller;

import account.transfers.converter.AccountDTOToAccountConverter;
import account.transfers.converter.AccountToAccountDTOConverter;
import account.transfers.dto.AccountDTO;
import account.transfers.dto.AccountTransferRequestDTO;
import account.transfers.repository.AccountRepository;
import account.transfers.repository.entities.Account;
import account.transfers.service.AccountTransactionService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jooby.Err;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.Body;
import org.jooby.mvc.DELETE;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.PUT;
import org.jooby.mvc.Path;

@Path("/monetary-transfer/accounts")
public class AccountController {

  @Inject
  public AccountController(AccountRepository accountRepository,
      AccountToAccountDTOConverter accountToAccountDTOConverter,
      AccountDTOToAccountConverter accountDTOToAccountConverter,
      AccountTransactionService accountTransactionService) {
    this.accountRepository = accountRepository;
    this.accountToAccountDTOConverter = accountToAccountDTOConverter;
    this.accountDTOToAccountConverter = accountDTOToAccountConverter;
    this.accountTransactionService = accountTransactionService;
  }

  private final AccountRepository accountRepository;
  private final AccountToAccountDTOConverter accountToAccountDTOConverter;
  private final AccountDTOToAccountConverter accountDTOToAccountConverter;
  private final AccountTransactionService accountTransactionService;

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

  @PUT
  @Path("/account")
  public Account addAccount(@Body AccountDTO accountDTO) {
    return accountRepository.save(accountDTOToAccountConverter.convert(accountDTO));
  }

  @DELETE
  @Path("/{accountId}")
  public void deleteAccount(UUID accountId) {
    accountRepository.delete(accountId);
  }
}
