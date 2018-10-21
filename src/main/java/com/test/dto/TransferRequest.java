package com.test.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author rucfssr
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

	@NotNull
	private String sourceAccountNumber;

	@NotNull
	private String destinationAccountNumber;

	@NotNull
	private BigDecimal amount;
}
