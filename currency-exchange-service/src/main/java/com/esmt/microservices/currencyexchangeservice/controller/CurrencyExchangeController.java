package com.esmt.microservices.currencyexchangeservice.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.esmt.microservices.currencyexchangeservice.bean.CurrencyExchange;
import com.esmt.microservices.currencyexchangeservice.repository.CurrencyExchangeRepository;

@RestController
public class CurrencyExchangeController {
	@Autowired
	CurrencyExchangeRepository repository;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange getCurrencyExchenge( 
			@PathVariable String from, @PathVariable String to) {
			CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);
			if(currencyExchange==null)
				throw new RuntimeException("Taux de change non trouvé pour " + from + " -> " + to);
			
			return currencyExchange;
	}
	}
			
	


