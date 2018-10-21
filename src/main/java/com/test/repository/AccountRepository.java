package com.test.repository;

import org.springframework.data.repository.CrudRepository;

import com.test.repository.entity.Account;

/**
 * Repository for Account
 * 
 * @author rucfssr
 *
 */
public interface AccountRepository extends CrudRepository<Account, String> {
}
