package com.example.demo;

import com.github.jnidzwetzki.bitfinex.v2.entity.currency.BitfinexCurrencyPair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2

public class DemoApplication {

	public  static void main(String[] args) throws IOException {
		BitfinexCurrencyPair.registerDefaults();
		ConfigurableApplicationContext applicationContext=SpringApplication.run(DemoApplication.class, args);
//		BitfinexTra bitfinex=applicationContext.getBean(BitfinexTra.class);
	}
//	@Scheduled(cron ="*/6 * * * * ?")
//	public void sayHello() {
//		System.out.println("hello");
//	}

}
