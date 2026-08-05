package com.neueda.portfolio_manager.controller;

import com.neueda.portfolio_manager.entity.Market;
import com.neueda.portfolio_manager.exception.GlobalExceptionHandler;
import com.neueda.portfolio_manager.service.MarketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MarketControllerTest {

	@Mock
	private MarketService marketService;

	@InjectMocks
	private MarketController marketController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(marketController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getAllMarketsReturnsMarketListWhenMarketsExist() throws Exception {
		when(marketService.getAllMarkets()).thenReturn(List.of(
				market(1, "AAPL", "Apple Inc.", "NASDAQ", "Technology", 210.25, 2.15),
				market(2, "MSFT", "Microsoft Corp.", "NASDAQ", "Technology", 432.10, 1.32)
		));

		mockMvc.perform(get("/api/market"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].symbol").value("AAPL"))
				.andExpect(jsonPath("$[0].companyName").value("Apple Inc."))
				.andExpect(jsonPath("$[0].currentPrice").value(210.25))
				.andExpect(jsonPath("$[1].symbol").value("MSFT"))
				.andExpect(jsonPath("$[1].exchange").value("NASDAQ"));
	}

	@Test
	void getAllMarketsReturnsEmptyArrayWhenNoMarketsExist() throws Exception {
		when(marketService.getAllMarkets()).thenReturn(List.of());

		mockMvc.perform(get("/api/market"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[]"));
	}

	@Test
	void getAllMarketsReturnsInternalServerErrorWhenServiceFails() throws Exception {
		when(marketService.getAllMarkets()).thenThrow(new RuntimeException("Market feed unavailable"));

		mockMvc.perform(get("/api/market"))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.error").value("Internal Server Error"))
				.andExpect(jsonPath("$.message").value("Unexpected error: Market feed unavailable"));
	}

	@Test
	void getTopGainersReturnsStocksWithPositiveChanges() throws Exception {
		when(marketService.getTopGainers()).thenReturn(List.of(
				market(3, "NVDA", "NVIDIA Corp.", "NASDAQ", "Technology", 980.55, 6.45),
				market(4, "AMZN", "Amazon.com Inc.", "NASDAQ", "Consumer Discretionary", 201.10, 3.12)
		));

		mockMvc.perform(get("/api/market/gainers"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].symbol").value("NVDA"))
				.andExpect(jsonPath("$[0].changePercent").value(6.45))
				.andExpect(jsonPath("$[1].symbol").value("AMZN"))
				.andExpect(jsonPath("$[1].sector").value("Consumer Discretionary"));
	}

	@Test
	void getTopGainersReturnsEmptyArrayWhenThereAreNoGainers() throws Exception {
		when(marketService.getTopGainers()).thenReturn(List.of());

		mockMvc.perform(get("/api/market/gainers"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[]"));
	}

	@Test
	void getTopLosersReturnsStocksWithNegativeChanges() throws Exception {
		when(marketService.getTopLosers()).thenReturn(List.of(
				market(5, "TSLA", "Tesla Inc.", "NASDAQ", "Automotive", 175.40, -4.75),
				market(6, "NFLX", "Netflix Inc.", "NASDAQ", "Communication Services", 598.80, -2.20)
		));

		mockMvc.perform(get("/api/market/losers"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].symbol").value("TSLA"))
				.andExpect(jsonPath("$[0].changePercent").value(-4.75))
				.andExpect(jsonPath("$[1].symbol").value("NFLX"))
				.andExpect(jsonPath("$[1].companyName").value("Netflix Inc."));
	}

	private Market market(int id, String symbol, String companyName, String exchange, String sector,
						  double currentPrice, double changePercent) {
		Market market = BeanUtils.instantiateClass(Market.class);
		market.setId(id);
		market.setSymbol(symbol);
		market.setCompanyName(companyName);
		market.setExchange(exchange);
		market.setSector(sector);
		market.setCurrentPrice(currentPrice);
		market.setChangePercent(changePercent);
		return market;
	}
}
