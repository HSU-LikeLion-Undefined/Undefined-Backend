package com.likelion.RePlay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class RePlayApplicationTests {

	@Test
	void testPrintName() {
		assertDoesNotThrow(() -> {
			RePlayApplication.printName("RePlay");
		});
	}
}