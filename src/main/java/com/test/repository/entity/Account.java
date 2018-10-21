package com.test.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity class for account.
 * 
 * @author rucfssr
 *
 */
@Data
@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Id
	@NotNull
	private String accountNumber;

	@Column
	@NotNull
	private BigDecimal balance;

}
