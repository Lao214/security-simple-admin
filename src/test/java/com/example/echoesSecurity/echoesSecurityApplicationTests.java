package com.example.echoesSecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest
class echoesSecurityApplicationTests {


 	@Test
	void lambdaFor() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		System.out.println(passwordEncoder.encode("hfe1234"));
	}



}
