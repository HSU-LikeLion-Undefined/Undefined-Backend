package com.likelion.RePlay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class RePlayApplicationTests {

	@Test
	void contextLoads() {
		// 애플리케이션 컨텍스트가 성공적으로 로드되는지
	}

	@Test
	void testPrintName() {
		// printName 메소드가 예외를 던지지 않고 실행되는지
		assertDoesNotThrow(() -> RePlayApplication.printName("RePlay"));
	}
}