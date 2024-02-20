package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingleController {

	@GetMapping("getdata")
	public String get() {
		System.out.print("####");
		return "fisaData";
	}	

}
