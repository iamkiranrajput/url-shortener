package com.guardians;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardians.dto.UrlRequest;
import com.guardians.dto.UrlResponse;
import com.guardians.model.UrlMapping;
import com.guardians.service.UrlShortenerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortnerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UrlShortenerServiceImpl urlShortenerService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
		// Ensure the Spring Boot application context loads properly
	}

	@Test
	void testCreateShortUrlIntegration() throws Exception {
		// Create a test URL request
		UrlRequest urlRequest = new UrlRequest();
		urlRequest.setOriginalUrl("https://www.github.com/iamkiranrajput");
		urlRequest.setExpiresAt(LocalDateTime.now().plusDays(1));
		urlRequest.setUsageLimit(10);

		// Perform an HTTP POST request to /api/url/shorten
		mockMvc.perform(post("/api/url/shorten")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(urlRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Shortened URL generated successfully."));
	}

	@Test
	void testRedirectToOriginalUrlIntegration() throws Exception {
		// Create a short URL
		UrlRequest urlRequest = new UrlRequest();
		urlRequest.setOriginalUrl("https://www.github.com/iamkiranrajput");
		urlRequest.setExpiresAt(LocalDateTime.now().plusDays(1));
		urlRequest.setUsageLimit(10);

		UrlResponse createdResponse = urlShortenerService.createShortUrl(urlRequest);
		UrlMapping createdMapping = (UrlMapping) createdResponse.getData();

		// Perform an HTTP GET request to /api/url/{shortUrl}
		mockMvc.perform(get("/api/url/" + createdMapping.getShortUrl()))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", createdMapping.getOriginalUrl()));
	}
}
