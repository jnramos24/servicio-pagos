package ar.edu.utn.diplomatura.clase16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Slide 16: busca las interfaces @FeignClient y les escribe la implementacion
public class ServicioPagosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioPagosApplication.class, args);
	}

}
