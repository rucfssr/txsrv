package com.test.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.stereotype.Service;

import com.test.dto.Response;
import com.test.dto.ResponseStatus;
import com.test.dto.TransferRequest;
import com.test.repository.AccountRepository;
import com.test.repository.entity.Account;
import com.test.service.AccountService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author rucfssr
 *
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	/**
	 * Create account for the customer
	 * 
	 * @throws ValidationException
	 * 
	 */
	@Override
	public Response createAccount(Account account) throws ValidationException {
		if (accountRepository.findById(account.getAccountNumber()).orElse(null) != null) {
			throw new ValidationException("Account already exist.");
		} else if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Account can not be opened with negetive balance.");
		} else {
			accountRepository.save(account);
		}
		return new Response(ResponseStatus.SUCCESS, "Account created successfully.");
	}

	/**
	 * Initiate transfer from source account to destination account.
	 * @throws ValidationException 
	 */
	@Override
	@Transactional
	public Response transferAmount(TransferRequest transferRequest) throws ValidationException {
		synchronized (this) {
			Account sourceAccount = accountRepository.findById(transferRequest.getSourceAccountNumber()).orElse(null);
			Account destinationAccount = accountRepository.findById(transferRequest.getDestinationAccountNumber())
					.orElse(null);
			if (sourceAccount == null) {
				throw new ValidationException("Invalid source account.");
			} else if (destinationAccount == null) {
				throw new ValidationException("Invalid destination account.");
			} else if (sourceAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
				throw new ValidationException("Source account doesn't have sufficient funds.");
			}
			BigDecimal sourceAccBalance = sourceAccount.getBalance();
			BigDecimal destinationAccBalance = destinationAccount.getBalance();
			sourceAccBalance = sourceAccBalance.subtract(transferRequest.getAmount());
			destinationAccBalance = destinationAccBalance.add(transferRequest.getAmount());
			sourceAccount.setBalance(sourceAccBalance);
			destinationAccount.setBalance(destinationAccBalance);
			accountRepository.save(sourceAccount);
			accountRepository.save(destinationAccount);
			return new Response(ResponseStatus.SUCCESS, "Transfer completed.");
		}

	}

	/**
	 * List customer accounts
	 */
	@Override
	public Iterable<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

}
