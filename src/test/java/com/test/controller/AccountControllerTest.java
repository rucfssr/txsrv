package com.test.controller;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.Application;
import com.test.dto.Response;
import com.test.dto.ResponseStatus;
import com.test.dto.TransferRequest;
import com.test.repository.entity.Account;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private HttpHeaders headers = new HttpHeaders();
	private String ACCOUNT_URL = null;

	@Before
	public void setUp() throws Exception {
		ACCOUNT_URL = "http://localhost:" + port + "/accounts";
	}

	/**
	 * Test get operation should return list of all the accounts
	 */
	@Test
	public void getAccountsShouldReturnOneAccount() {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Iterable<Account>> response = restTemplate.exchange(ACCOUNT_URL + "/list", HttpMethod.GET,
				entity, new ParameterizedTypeReference<Iterable<Account>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * Test account creation operation should create account successfully
	 */
	@Test
	public void createAccountShouldCreateAccountSuccessfully() {
		Account account = new Account("NL87654321", new BigDecimal("50.50"));
		HttpEntity<Account> entity = new HttpEntity<Account>(account, headers);
		System.out.println(ACCOUNT_URL);
		ResponseEntity<Response> response = restTemplate.exchange(ACCOUNT_URL + "/create", HttpMethod.POST, entity,
				Response.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody().getStatus(), ResponseStatus.SUCCESS);
		assertEquals(response.getBody().getMessage(), "Account created successfully.");
	}

	/**
	 * Test account creation operation should return error when same account number
	 * is post again
	 */
	@Test
	public void createAccountShouldNotCreateAccount() {
		Account account = new Account("NL12345678", new BigDecimal("100.05"));
		HttpEntity<Account> entity = new HttpEntity<Account>(account, headers);
		ResponseEntity<Response> response = restTemplate.exchange(ACCOUNT_URL + "/create", HttpMethod.POST, entity,
				Response.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(response.getBody().getStatus(), ResponseStatus.ERROR);
		assertEquals(response.getBody().getMessage(), "Account already exist.");
	}

	/**
	 * Test account creation operation should return error when account is opened
	 * with negative amount
	 */
	@Test
	public void createAccountShouldNotCreateAccountWithNegativeAmount() {
		Account account = new Account("NL555666999", new BigDecimal("-100.05"));
		HttpEntity<Account> entity = new HttpEntity<Account>(account, headers);
		ResponseEntity<Response> response = restTemplate.exchange(ACCOUNT_URL + "/create", HttpMethod.POST, entity,
				Response.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(response.getBody().getStatus(), ResponseStatus.ERROR);
		assertEquals(response.getBody().getMessage(), "Account can not be opened with negetive balance.");
	}

	/**
	 * Transfer amount from source account to destination should result in valid
	 * transfer
	 */
	@Test
	public void transferFromSourceToDestinationAccountShouldResultInValidTransfer() {
		TransferRequest request = new TransferRequest("NL12345678", "GB12345678", new BigDecimal("10.0"));
		HttpEntity<TransferRequest> entity = new HttpEntity<TransferRequest>(request, headers);
		ResponseEntity<Response> response = restTemplate.exchange(ACCOUNT_URL + "/transfer", HttpMethod.POST, entity,
				Response.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody().getStatus(), ResponseStatus.SUCCESS);
		assertEquals(response.getBody().getMessage(), "Transfer completed.");
	}

	/**
	 * Transfer amount from source account to invalid destination account should
	 * result in error
	 */
	@Test
	public void transferFromSourceToInvalidDestinationAccountShouldResultInError() {
		TransferRequest request = new TransferRequest("NL12345678", "GB1234", new BigDecimal("10.0"));
		HttpEntity<TransferRequest> entity = new HttpEntity<TransferRequest>(request, headers);
		ResponseEntity<Response> response = restTemplate.exchange(ACCOUNT_URL + "/transfer", HttpMethod.POST, entity,
				Response.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(response.getBody().getStatus(), ResponseStatus.ERROR);
		assertEquals(response.getBody().getMessage(), "Invalid destination account.");
	}

	/**
	 * Transfer amount from invalid source account to valid destination should
	 * result in error
	 */
	@Test
	public void transferFromInvalidSourceToValidDestinationAccountShouldResultInError() {
		TransferRequest request = new TransferRequest("NL1234", "GB12345678", new BigDecimal("10.0"));
		HttpEntity<TransferRequest> entity = new HttpEntity<TransferRequest>(request, headers);
		ResponseEntity<Response> response = restTemplate.exchange(ACCOUNT_URL + "/transfer", HttpMethod.POST, entity,
				Response.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(response.getBody().getStatus(), ResponseStatus.ERROR);
		assertEquals(response.getBody().getMessage(), "Invalid source account.");
	}

	/**
	 * Transfer amount more than source account available balance to destination
	 * should result in error
	 */
	@Test
	public void transferMoreAmountFromSourceToDestinationAccountShouldResultInError() {
		TransferRequest request = new TransferRequest("NL12345678", "GB12345678", new BigDecimal("1000.0"));
		HttpEntity<TransferRequest> entity = new HttpEntity<TransferRequest>(request, headers);
		ResponseEntity<Response> response = restTemplate.exchange(ACCOUNT_URL + "/transfer", HttpMethod.POST, entity,
				Response.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(response.getBody().getStatus(), ResponseStatus.ERROR);
		assertEquals(response.getBody().getMessage(), "Source account doesn't have sufficient funds.");
	}

}
