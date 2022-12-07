package com.viamedsalud.gvp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Bcrypt {

	public Bcrypt() {
	}

	public String hashear(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(password);
		return encodedPassword;
	}

}
