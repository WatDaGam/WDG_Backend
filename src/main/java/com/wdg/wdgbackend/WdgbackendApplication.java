package com.wdg.wdgbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@OpenAPIDefinition(servers = {
		@Server(url = "/", description = "https://api.watdagam.com")
})
@SpringBootApplication
public class WdgbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WdgbackendApplication.class, args);
	}

}
