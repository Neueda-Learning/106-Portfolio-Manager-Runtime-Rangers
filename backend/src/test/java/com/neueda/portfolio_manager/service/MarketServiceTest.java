package com.neueda.portfolio_manager.service;

import com.neueda.portfolio_manager.entity.Market;
import com.neueda.portfolio_manager.repository.MarketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketServiceTest {

	@Mock
	private MarketRepository marketRepository;

	@InjectMocks
	private MarketService marketService;

	@Test
	void getAllMarketsReturnsRepositoryResults() {
		List<Market> expected = List.of(
				new Market(1, "AAPL", "Apple Inc", "NASDAQ", "Technology", 220.5, 1.25),
				new Market(2, "MSFT", "Microsoft Corporation", "NASDAQ", "Technology", 420.75, 0.80)
		);
		when(marketRepository.getAllMarkets()).thenReturn(expected);

		List<Market> actual = marketService.getAllMarkets();

		assertSame(expected, actual);
	}

	@Test
	void getMarketBySymbolReturnsMarketWhenFound() {
		Market expected = new Market(3, "TSLA", "Tesla Inc", "NASDAQ", "Automotive", 350.2, -2.35);
		when(marketRepository.getMarketBySymbol("TSLA")).thenReturn(Optional.of(expected));

		Market actual = marketService.getMarketBySymbol("TSLA");

		assertSame(expected, actual);
	}

	@Test
	void getMarketBySymbolThrowsWhenSymbolDoesNotExist() {
		when(marketRepository.getMarketBySymbol("META")).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> marketService.getMarketBySymbol("META")
		);

		assertEquals("Market not found for symbol: META", exception.getMessage());
	}

	@Test
	void searchMarketsReturnsResultsForTrimmedKeyword() {
		List<Market> expected = List.of(
				new Market(1, "AAPL", "Apple Inc", "NASDAQ", "Technology", 220.5, 1.25)
		);
		when(marketRepository.searchMarkets("AAPL")).thenReturn(expected);

		List<Market> actual = marketService.searchMarkets("  AAPL  ");

		assertSame(expected, actual);
		verify(marketRepository).searchMarkets("AAPL");
	}

	@Test
	void searchMarketsThrowsWhenKeywordIsNull() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> marketService.searchMarkets(null)
		);

		assertEquals("Search keyword must not be empty", exception.getMessage());
		verify(marketRepository, never()).searchMarkets(org.mockito.ArgumentMatchers.anyString());
	}

	@Test
	void searchMarketsThrowsWhenKeywordIsBlank() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> marketService.searchMarkets("   ")
		);

		assertEquals("Search keyword must not be empty", exception.getMessage());
		verify(marketRepository, never()).searchMarkets(org.mockito.ArgumentMatchers.anyString());
	}

	@Test
	void getTopGainersReturnsRepositoryResults() {
		List<Market> expected = List.of(
				new Market(5, "NVDA", "NVIDIA Corporation", "NASDAQ", "Technology", 980.55, 6.45)
		);
		when(marketRepository.getTopGainers()).thenReturn(expected);

		List<Market> actual = marketService.getTopGainers();

		assertSame(expected, actual);
	}

	@Test
	void getTopLosersReturnsRepositoryResults() {
		List<Market> expected = List.of(
				new Market(6, "NFLX", "Netflix Inc", "NASDAQ", "Communication Services", 598.80, -4.10)
		);
		when(marketRepository.getTopLosers()).thenReturn(expected);

		List<Market> actual = marketService.getTopLosers();

		assertSame(expected, actual);
	}

	@Test
	void marketExistsReturnsRepositoryResult() {
		when(marketRepository.existsBySymbol("AAPL")).thenReturn(true);
		when(marketRepository.existsBySymbol("META")).thenReturn(false);

		assertTrue(marketService.marketExists("AAPL"));
		assertFalse(marketService.marketExists("META"));
	}

	@Test
	void addMarketSavesAndReturnsMarketWhenSymbolDoesNotExist() {
		Market market = new Market(0, "ORCL", "Oracle Corporation", "NYSE", "Technology", 145.3, 0.42);
		when(marketRepository.existsBySymbol("ORCL")).thenReturn(false);

		Market saved = marketService.addMarket(market);

		assertSame(market, saved);
		verify(marketRepository).save(market);
	}

	@Test
	void addMarketThrowsWhenSymbolAlreadyExists() {
		Market market = new Market(0, "AAPL", "Apple Inc", "NASDAQ", "Technology", 220.5, 1.25);
		when(marketRepository.existsBySymbol("AAPL")).thenReturn(true);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> marketService.addMarket(market)
		);

		assertEquals("Market already exists for symbol: AAPL", exception.getMessage());
		verify(marketRepository, never()).save(market);
	}

	@Test
	void updatePriceUpdatesRepositoryWhenMarketExists() {
		when(marketRepository.existsBySymbol("AAPL")).thenReturn(true);

		marketService.updatePrice("AAPL", 230.1, 2.75);

		verify(marketRepository).updatePrice("AAPL", 230.1, 2.75);
	}

	@Test
	void updatePriceThrowsWhenMarketDoesNotExist() {
		when(marketRepository.existsBySymbol("META")).thenReturn(false);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> marketService.updatePrice("META", 500.0, 1.0)
		);

		assertEquals("Market not found for symbol: META", exception.getMessage());
		verify(marketRepository, never()).updatePrice("META", 500.0, 1.0);
	}

	@Test
	void deleteMarketDeletesWhenMarketExists() {
		when(marketRepository.existsBySymbol("AMZN")).thenReturn(true);

		marketService.deleteMarket("AMZN");

		verify(marketRepository).deleteBySymbol("AMZN");
	}

	@Test
	void deleteMarketThrowsWhenMarketDoesNotExist() {
		when(marketRepository.existsBySymbol("META")).thenReturn(false);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> marketService.deleteMarket("META")
		);

		assertEquals("Market not found for symbol: META", exception.getMessage());
		verify(marketRepository, never()).deleteBySymbol("META");
	}
}
