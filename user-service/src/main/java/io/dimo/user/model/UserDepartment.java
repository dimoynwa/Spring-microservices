package io.dimo.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDepartment {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private Department departament;
	
}