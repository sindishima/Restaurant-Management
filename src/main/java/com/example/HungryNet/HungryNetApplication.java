package com.example.HungryNet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HungryNetApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(HungryNetApplication.class, args);
//		for(String s : applicationContext.getBeanDefinitionNames()){   //print all created beans
//			System.out.println(s);
//		}
	}

}
