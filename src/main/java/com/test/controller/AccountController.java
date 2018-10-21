package com.test.controller;

import javax.security.auth.login.AccountException;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.dto.Response;
import com.test.dto.TransferRequest;
import com.test.repository.entity.Account;
import com.test.service.AccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;

/***
 * REST api for the account
 * 
 * @author rucfssr
 *
 */
@RestController
@RequestMapping("accounts")
@SwaggerDefinition(tags = { @Tag(name = "Account", description = "Rest api for account") })
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@ApiOperation(value = "REST api to create account", response = Response.class)
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response createAccount(@Valid @RequestBody Account account) throws AccountException {
		return accountService.createAccount(account);

	}

	@ApiOperation(value = "REST api to transfer amount from one account to another", response = Response.class)
	@RequestMapping(value = "/transfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response transferAmount(@Valid @RequestBody TransferRequest transferRequest) throws AccountException {
		return accountService.transferAmount(transferRequest);
	}
	
	@ApiOperation(value = "REST api to get the list of all accounts", response = Iterable.class)
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Account> getAllAccounts() {
		return accountService.getAllAccounts();
	}

}
