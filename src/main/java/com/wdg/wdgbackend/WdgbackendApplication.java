package com.wdg.wdgbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@OpenAPIDefinition(servers = {
		@Server(url = "/", description = "https://api.watdagam.com")
})
@SpringBootApplication
public class WdgbackendApplication {
//	@Value("${apple.team.id}") private static String APPLE_TEAM_ID;
//	@Value("${apple.client.id}") private static String APPLE_CLIENT_ID;
//	@Value("${apple.redirect.url}")	private static String APPLE_REDIRECT_URL;
//	@Value("${apple.key.id}") private static String APPLE_KEY_ID;
//	@Value("${apple.auth.key.path}") private static String APPLE_AUTH_KEY_PATH;
//	@Value("${spring.datasource.url}") private static String DATABASE_URL;

	public static void main(String[] args) {

//		System.out.println("APPLE_TEAM_ID = " + APPLE_TEAM_ID);
//		System.out.println("APPLE_CLIENT_ID = " + APPLE_CLIENT_ID);
//		System.out.println("APPLE_REDIRECT_URL = " + APPLE_REDIRECT_URL);
//		System.out.println("APPLE_KEY_ID = " + APPLE_KEY_ID);
//		System.out.println("APPLE_AUTH_KEY_PATH = " + APPLE_AUTH_KEY_PATH);
//		System.out.println("DATABASE_URL = " + DATABASE_URL);
		SpringApplication.run(WdgbackendApplication.class, args);
	}

}
