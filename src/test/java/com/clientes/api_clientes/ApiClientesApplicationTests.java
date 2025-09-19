package com.clientes.api_clientes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiClientesApplicationTests {

	@Value("${app.analytics.eventhub.connection-string}")
	private String connectionString;

	@Value("${app.analytics.eventhub.name}")
	private String eventHubName;


	@Test
	void contextLoads() {
//levanta los tests
	}

}
