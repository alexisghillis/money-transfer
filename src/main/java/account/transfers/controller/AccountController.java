package account.transfers.controller;

import account.transfers.converter.AccountDTOToAccountConverter;
import account.transfers.converter.AccountToAccountDTOConverter;
import account.transfers.dto.AccountDTO;
import account.transfers.dto.AccountTransferRequestDTO;
import account.transfers.repository.entities.Account;
import account.transfers.service.AccountService;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
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
  public AccountController(AccountToAccountDTOConverter accountToAccountDTOConverter,
      AccountDTOToAccountConverter accountDTOToAccountConverter,
      AccountService accountService) {
    this.accountToAccountDTOConverter = accountToAccountDTOConverter;
    this.accountDTOToAccountConverter = accountDTOToAccountConverter;
    this.accountService = accountService;
  }

  private final AccountToAccountDTOConverter accountToAccountDTOConverter;
  private final AccountDTOToAccountConverter accountDTOToAccountConverter;
  private final AccountService accountService;

  @GET
  public List<AccountDTO> getAllAccounts() {
    return accountService.getAllAccounts();
  }

  @GET
  @Path("/{accountId}")
  public AccountDTO getAccount(UUID accountId) {
    return accountService.getAccount(accountId);
  }

  @POST
  @Path("/transfer")
  public Result monetaryTransfer(@Body AccountTransferRequestDTO accountTransferRequestDTO) {
    accountService.transfer(accountTransferRequestDTO.getAccountIdFrom(),
        accountTransferRequestDTO.getAccountIdTo(), accountTransferRequestDTO.getAmount());

    return Results.accepted();
  }

  @PUT
  @Path("/account")
  public Account addAccount(@Body AccountDTO accountDTO) {
    return accountService.addAccount(accountDTO);
  }

  @DELETE
  @Path("/{accountId}")
  public void deleteAccount(UUID accountId) {
    accountService.deleteAccount(accountId);
  }
}
