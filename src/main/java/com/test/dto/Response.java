package com.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author rucfssr
 *
 */
@Data
@AllArgsConstructor
public class Response {

	private ResponseStatus status;
	private String message;

}
