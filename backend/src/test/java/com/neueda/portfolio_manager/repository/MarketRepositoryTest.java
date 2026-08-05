package com.neueda.portfolio_manager.repository;

import com.neueda.portfolio_manager.entity.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketRepositoryTest {

	private JdbcTemplate jdbcTemplate;
	private MarketRepository marketRepository;

	@BeforeEach
	void setUp() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1");
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		jdbcTemplate = new JdbcTemplate(dataSource);
		marketRepository = new MarketRepository();
		ReflectionTestUtils.setField(marketRepository, "jdbcTemplate", jdbcTemplate);

		jdbcTemplate.execute("""
				CREATE TABLE market (
					id INT AUTO_INCREMENT PRIMARY KEY,
					symbol VARCHAR(10) NOT NULL UNIQUE,
					company_name VARCHAR(100) NOT NULL,
					exchange VARCHAR(50),
					sector VARCHAR(50),
					current_price DECIMAL(10,2),
					change_percent DECIMAL(6,2) DEFAULT 0
				)
				""");

		jdbcTemplate.batchUpdate(
				"INSERT INTO market (id, symbol, company_name, exchange, sector, current_price, change_percent) VALUES (?, ?, ?, ?, ?, ?, ?)",
				List.of(
						new Object[]{1, "AAPL", "Apple Inc", "NASDAQ", "Technology", 220.50, 1.25},
						new Object[]{2, "TSLA", "Tesla Inc", "NASDAQ", "Automotive", 350.20, -2.35},
						new Object[]{3, "MSFT", "Microsoft Corporation", "NASDAQ", "Technology", 420.75, 0.80},
						new Object[]{4, "AMZN", "Amazon Inc", "NASDAQ", "E-Commerce", 185.30, -0.55},
						new Object[]{5, "NVDA", "NVIDIA Corporation", "NASDAQ", "Technology", 980.55, 6.45},
						new Object[]{6, "NFLX", "Netflix Inc", "NASDAQ", "Communication Services", 598.80, -4.10}
				)
		);
	}

	@Test
	void getAllMarketsReturnsAllMarketsOrderedBySymbol() {
		List<Market> markets = marketRepository.getAllMarkets();

		assertEquals(6, markets.size());
		assertEquals("AAPL", markets.get(0).getSymbol());
		assertEquals("AMZN", markets.get(1).getSymbol());
		assertEquals("MSFT", markets.get(2).getSymbol());
		assertEquals("NFLX", markets.get(3).getSymbol());
		assertEquals("NVDA", markets.get(4).getSymbol());
		assertEquals("TSLA", markets.get(5).getSymbol());
	}

	@Test
	void getMarketBySymbolReturnsMarketWhenSymbolExists() {
		Optional<Market> market = marketRepository.getMarketBySymbol("MSFT");

		assertTrue(market.isPresent());
		assertEquals("MSFT", market.get().getSymbol());
		assertEquals("Microsoft Corporation", market.get().getCompanyName());
		assertEquals(420.75, market.get().getCurrentPrice(), 0.001);
	}

	@Test
	void getMarketBySymbolReturnsEmptyWhenSymbolDoesNotExist() {
		Optional<Market> market = marketRepository.getMarketBySymbol("META");

		assertTrue(market.isEmpty());
	}

	@Test
	void searchMarketsReturnsMatchesBySymbolAndCompanyName() {
		List<Market> bySymbol = marketRepository.searchMarkets("AAP");
		List<Market> byCompany = marketRepository.searchMarkets("Corporation");

		assertEquals(1, bySymbol.size());
		assertEquals("AAPL", bySymbol.get(0).getSymbol());

		assertEquals(2, byCompany.size());
		assertTrue(byCompany.stream().anyMatch(m -> "MSFT".equals(m.getSymbol())));
		assertTrue(byCompany.stream().anyMatch(m -> "NVDA".equals(m.getSymbol())));
	}

	@Test
	void searchMarketsReturnsAllMarketsWhenKeywordIsEmptyString() {
		List<Market> markets = marketRepository.searchMarkets("");

		assertEquals(6, markets.size());
	}

	@Test
	void getTopGainersReturnsAtMostFiveMarketsInDescendingChangeOrder() {
		List<Market> gainers = marketRepository.getTopGainers();

		assertEquals(5, gainers.size());
		assertEquals("NVDA", gainers.get(0).getSymbol());
		assertTrue(gainers.get(0).getChangePercent() >= gainers.get(1).getChangePercent());
		assertTrue(gainers.get(1).getChangePercent() >= gainers.get(2).getChangePercent());
		assertTrue(gainers.get(2).getChangePercent() >= gainers.get(3).getChangePercent());
		assertTrue(gainers.get(3).getChangePercent() >= gainers.get(4).getChangePercent());
	}

	@Test
	void getTopLosersReturnsAtMostFiveMarketsInAscendingChangeOrder() {
		List<Market> losers = marketRepository.getTopLosers();

		assertEquals(5, losers.size());
		assertEquals("NFLX", losers.get(0).getSymbol());
		assertTrue(losers.get(0).getChangePercent() <= losers.get(1).getChangePercent());
		assertTrue(losers.get(1).getChangePercent() <= losers.get(2).getChangePercent());
		assertTrue(losers.get(2).getChangePercent() <= losers.get(3).getChangePercent());
		assertTrue(losers.get(3).getChangePercent() <= losers.get(4).getChangePercent());
	}

	@Test
	void existsBySymbolReturnsTrueForExistingSymbolAndFalseForMissingSymbol() {
		assertTrue(marketRepository.existsBySymbol("TSLA"));
		assertFalse(marketRepository.existsBySymbol("META"));
	}

	@Test
	void saveInsertsMarketAndMakesItQueryable() {
		Market market = new Market(0, "ORCL", "Oracle Corporation", "NYSE", "Technology", 145.30, 0.42);

		int affectedRows = marketRepository.save(market);
		Optional<Market> savedMarket = marketRepository.getMarketBySymbol("ORCL");

		assertEquals(1, affectedRows);
		assertTrue(savedMarket.isPresent());
		assertEquals("Oracle Corporation", savedMarket.get().getCompanyName());
		assertEquals("NYSE", savedMarket.get().getExchange());
	}

	@Test
	void updatePriceUpdatesValuesForExistingSymbolAndReturnsZeroForMissingSymbol() {
		int updatedExisting = marketRepository.updatePrice("AAPL", 230.10, 2.75);
		int updatedMissing = marketRepository.updatePrice("META", 500.00, 1.00);
		Optional<Market> updatedMarket = marketRepository.getMarketBySymbol("AAPL");

		assertEquals(1, updatedExisting);
		assertEquals(0, updatedMissing);
		assertTrue(updatedMarket.isPresent());
		assertEquals(230.10, updatedMarket.get().getCurrentPrice(), 0.001);
		assertEquals(2.75, updatedMarket.get().getChangePercent(), 0.001);
	}

	@Test
	void deleteBySymbolRemovesMarketWhenItExistsAndReturnsZeroWhenMissing() {
		int deletedExisting = marketRepository.deleteBySymbol("AMZN");
		int deletedMissing = marketRepository.deleteBySymbol("META");

		assertEquals(1, deletedExisting);
		assertEquals(0, deletedMissing);
		assertTrue(marketRepository.getMarketBySymbol("AMZN").isEmpty());
	}
}
