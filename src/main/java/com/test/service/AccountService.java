package com.test.service;

import javax.validation.ValidationException;

import com.test.dto.Response;
import com.test.dto.TransferRequest;
import com.test.repository.entity.Account;

/***
 * 
 * @author rucfssr
 *
 */
public interface AccountService {

	public Response createAccount(Account account) throws ValidationException;

	public Response transferAmount(TransferRequest transferRequest) throws ValidationException;

	public Iterable<Account> getAllAccounts();

}
