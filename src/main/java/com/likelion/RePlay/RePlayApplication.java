package com.likelion.RePlay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RePlayApplication {

	public static void main(String[] args) {
		printName("RePlay");
	}

	public static void printName(String name) {
		System.out.println("Hello, " + name + "!");
	}
}
