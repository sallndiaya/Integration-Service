package com.esmt.m223isi.microservices.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.config.environment.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.esmt.m223isi.microservices.currencyconversionservice.bean.CurrencyConversion;

import com.esmt.m223isi.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;

@RestController
public class CurrencyConversionController {
  @Autowired
  private CurrencyExchangeProxy proxy;
  
  /*@Autowired 
  private Environment environment;
  String port = environment.getProperty("local.server.port");*/
  
  @GetMapping ("/currency-conversion/from/{from}/to/{to}/amount/{amount}")
  CurrencyConversion getCurrencyConversion(@PathVariable String from, @PathVariable String to,@PathVariable BigDecimal amount)
  {
    HashMap<String, String> uriVariables = new HashMap<>();
    uriVariables.put("from", from);
    uriVariables.put("to", to);

    ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().
      getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
        CurrencyConversion.class, uriVariables);
    CurrencyConversion currencyConversion = responseEntity.getBody();
    String source = "Rest Template";
    String environment="8100";
    currencyConversion.setSource(source);
    return new CurrencyConversion(currencyConversion.getId(), from, to, amount,currencyConversion.getRateExchange(),amount.multiply(currencyConversion.getRateExchange()),currencyConversion.getSource(),currencyConversion.getEnvironment());
  }
  
  @GetMapping ("/currency-conversion-feign/from/{from}/to/{to}/amount/{amount}")
  CurrencyConversion getCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,@PathVariable BigDecimal amount)
  {
    
    CurrencyConversion currencyConversion = proxy.getCurrencyExchange(from,to);
    String source = "Feign Rest Client";
    String environment="8100";
    currencyConversion.setSource(source);
    
    return new CurrencyConversion(currencyConversion.getId(), from, to, amount,currencyConversion.getRateExchange(),amount.multiply(currencyConversion.getRateExchange()),currencyConversion.getSource(),currencyConversion.getEnvironment());
  }
  

}