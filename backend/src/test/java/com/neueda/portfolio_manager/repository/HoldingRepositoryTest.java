package com.neueda.portfolio_manager.repository;

import com.neueda.portfolio_manager.entity.Holding;
import com.neueda.portfolio_manager.entity.HoldingAllocation;
import com.neueda.portfolio_manager.entity.SectorAllocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HoldingRepositoryTest {

	private JdbcTemplate jdbcTemplate;
	private HoldingRepository holdingRepository;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new DriverManagerDataSource(
				"jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1",
				"sa",
				""
		);

		jdbcTemplate = new JdbcTemplate(dataSource);
		holdingRepository = new HoldingRepository(jdbcTemplate);

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

		jdbcTemplate.execute("""
				CREATE TABLE holding (
					id INT AUTO_INCREMENT PRIMARY KEY,
					market_id INT NOT NULL,
					quantity INT NOT NULL,
					purchase_price DECIMAL(10,2),
					purchase_date DATE,
					CONSTRAINT fk_holding_market FOREIGN KEY (market_id) REFERENCES market(id)
				)
				""");

		jdbcTemplate.batchUpdate(
				"INSERT INTO market (id, symbol, company_name, exchange, sector, current_price, change_percent) VALUES (?, ?, ?, ?, ?, ?, ?)",
				List.of(
						new Object[]{1, "AAPL", "Apple Inc", "NASDAQ", "Technology", 220.50, 1.25},
						new Object[]{2, "TSLA", "Tesla Inc", "NASDAQ", "Automotive", 350.20, -2.35},
						new Object[]{3, "MSFT", "Microsoft Corporation", "NASDAQ", "Technology", 420.75, 0.80},
						new Object[]{4, "AMZN", "Amazon Inc", "NASDAQ", "E-Commerce", 185.30, -0.55}
				)
		);

		jdbcTemplate.batchUpdate(
				"INSERT INTO holding (id, market_id, quantity, purchase_price, purchase_date) VALUES (?, ?, ?, ?, ?)",
				List.of(
						new Object[]{1, 1, 10, 180.00, java.sql.Date.valueOf("2026-01-15")},
						new Object[]{2, 2, 5, 300.00, java.sql.Date.valueOf("2026-02-20")},
						new Object[]{3, 3, 8, 390.50, java.sql.Date.valueOf("2026-03-10")}
				)
		);
	}

	@Test
	void findAllReturnsHoldingsOrderedById() {
		List<Holding> holdings = holdingRepository.findAll();

		assertEquals(3, holdings.size());
		assertEquals(1, holdings.get(0).getId());
		assertEquals(2, holdings.get(1).getId());
		assertEquals(3, holdings.get(2).getId());
		assertEquals(LocalDate.of(2026, 1, 15), holdings.get(0).getPurchaseDate());
	}

	@Test
	void findByIdReturnsHoldingWhenItExists() {
		Optional<Holding> holding = holdingRepository.findById(2);

		assertTrue(holding.isPresent());
		assertEquals(2, holding.get().getId());
		assertEquals(2, holding.get().getMarketId());
		assertEquals(5, holding.get().getQuantity());
		assertEquals(300.0, holding.get().getPurchasePrice(), 0.001);
	}

	@Test
	void findByIdReturnsEmptyWhenHoldingDoesNotExist() {
		Optional<Holding> holding = holdingRepository.findById(99);

		assertTrue(holding.isEmpty());
	}

	@Test
	void findByMarketIdReturnsMatchingHoldingWhenItExists() {
		Optional<Holding> holding = holdingRepository.findByMarketId(3);

		assertTrue(holding.isPresent());
		assertEquals(3, holding.get().getId());
		assertEquals(8, holding.get().getQuantity());
		assertEquals(LocalDate.of(2026, 3, 10), holding.get().getPurchaseDate());
	}

	@Test
	void savePersistsHoldingAndAssignsGeneratedIdWhenPurchaseDateIsNull() {
		Holding holding = new Holding(0, 4, 6, 175.25, null);

		Holding savedHolding = holdingRepository.save(holding);
		Optional<Holding> reloadedHolding = holdingRepository.findById(savedHolding.getId());

		assertTrue(savedHolding.getId() > 0);
		assertTrue(reloadedHolding.isPresent());
		assertEquals(4, reloadedHolding.get().getMarketId());
		assertEquals(6, reloadedHolding.get().getQuantity());
		assertEquals(175.25, reloadedHolding.get().getPurchasePrice(), 0.001);
		assertNull(reloadedHolding.get().getPurchaseDate());
	}

	@Test
	void updateReturnsTrueAndPersistsChangesWhenHoldingExists() {
		Holding updatedHolding = new Holding(2, 2, 9, 315.75, LocalDate.of(2026, 4, 1));

		boolean updated = holdingRepository.update(updatedHolding);
		Optional<Holding> reloadedHolding = holdingRepository.findById(2);

		assertTrue(updated);
		assertTrue(reloadedHolding.isPresent());
		assertEquals(9, reloadedHolding.get().getQuantity());
		assertEquals(315.75, reloadedHolding.get().getPurchasePrice(), 0.001);
		assertEquals(LocalDate.of(2026, 4, 1), reloadedHolding.get().getPurchaseDate());
	}

	@Test
	void updateReturnsFalseWhenHoldingDoesNotExist() {
		Holding missingHolding = new Holding(99, 1, 3, 125.0, LocalDate.of(2026, 5, 5));

		boolean updated = holdingRepository.update(missingHolding);

		assertFalse(updated);
	}

	@Test
	void deleteByIdReturnsTrueAndRemovesHoldingWhenItExists() {
		boolean deleted = holdingRepository.deleteById(1);

		assertTrue(deleted);
		assertTrue(holdingRepository.findById(1).isEmpty());
	}

	@Test
	void deleteByIdReturnsFalseWhenHoldingDoesNotExist() {
		boolean deleted = holdingRepository.deleteById(77);

		assertFalse(deleted);
	}

	@Test
	void updateQuantityByMarketIdReturnsTrueAndChangesQuantityWhenHoldingExists() {
		boolean updated = holdingRepository.updateQuantityByMarketId(1, 14);
		Optional<Holding> reloadedHolding = holdingRepository.findByMarketId(1);

		assertTrue(updated);
		assertTrue(reloadedHolding.isPresent());
		assertEquals(14, reloadedHolding.get().getQuantity());
	}

	@Test
	void updateQuantityByMarketIdReturnsFalseWhenHoldingDoesNotExist() {
		boolean updated = holdingRepository.updateQuantityByMarketId(88, 14);

		assertFalse(updated);
	}


	@Test
	void getSectorAllocationReturnsAggregatedValuesAndPercentages() {
		List<SectorAllocation> allocations = holdingRepository.getSectorAllocation();

		assertEquals(2, allocations.size());

		SectorAllocation technology = allocations.get(0);
		assertEquals("Technology", technology.getSector());
		assertEquals(18, technology.getTotalQuantity());
		assertEquals(4924.0, technology.getInvestedValue(), 0.001);
		assertEquals(5571.0, technology.getCurrentValue(), 0.001);
		assertEquals(76.0858, technology.getAllocationPercentage(), 0.001);

		double percentageSum = allocations.stream()
				.mapToDouble(SectorAllocation::getAllocationPercentage)
				.sum();
		assertEquals(100.0, percentageSum, 0.001);
	}

	@Test
	void totalValueQueriesReturnZeroWhenNoHoldingsRemain() {
		jdbcTemplate.update("DELETE FROM holding");

		assertEquals(0.0, holdingRepository.getTotalInvestedValue(), 0.001);
		assertEquals(0.0, holdingRepository.getTotalCurrentValue(), 0.001);
		assertTrue(holdingRepository.getPortfolioAllocation().isEmpty());
		assertTrue(holdingRepository.getSectorAllocation().isEmpty());
	}
}
