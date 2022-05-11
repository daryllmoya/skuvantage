package com.javaexam.skuvantage.controller;

import com.javaexam.skuvantage.service.JsonExtractService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MainController {

    private final JsonExtractService jsonExtractService;

	@GetMapping("/api/filtered-result")
	public ResponseEntity<?> getFilteredResult(@RequestParam String[] keys) {
		return ResponseEntity.ok().body(jsonExtractService.getKeyValues(keys));
	}

}
