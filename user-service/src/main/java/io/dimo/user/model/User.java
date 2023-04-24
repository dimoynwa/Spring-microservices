package io.dimo.user.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class User {
	
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String departamentId;
}