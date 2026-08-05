package com.neueda.portfolio_manager.service;

import com.neueda.portfolio_manager.entity.Holding;
import com.neueda.portfolio_manager.entity.HoldingAllocation;
import com.neueda.portfolio_manager.entity.SectorAllocation;
import com.neueda.portfolio_manager.repository.HoldingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HoldingServiceTest {

	@Mock
	private HoldingRepository holdingRepository;

	@InjectMocks
	private HoldingService holdingService;

	@Test
	void getAllHoldingsReturnsRepositoryResults() {
		List<Holding> expected = List.of(
				new Holding(1, 10, 3, 150.0, LocalDate.of(2026, 1, 1)),
				new Holding(2, 11, 5, 220.0, LocalDate.of(2026, 1, 2))
		);
		when(holdingRepository.findAll()).thenReturn(expected);

		List<Holding> actual = holdingService.getAllHoldings();

		assertSame(expected, actual);
	}

	@Test
	void getHoldingByIdReturnsMatchingHoldingWhenPresent() {
		Holding expected = new Holding(7, 22, 4, 180.0, LocalDate.of(2026, 2, 14));
		when(holdingRepository.findById(7)).thenReturn(Optional.of(expected));

		Optional<Holding> actual = holdingService.getHoldingById(7);

		assertTrue(actual.isPresent());
		assertSame(expected, actual.get());
	}

	@Test
	void getHoldingByMarketIdReturnsEmptyWhenNoHoldingExists() {
		when(holdingRepository.findByMarketId(999)).thenReturn(Optional.empty());

		Optional<Holding> actual = holdingService.getHoldingByMarketId(999);

		assertTrue(actual.isEmpty());
	}

	@Test
	void createHoldingPersistsAndReturnsHoldingWhenQuantityIsValid() {
		Holding request = new Holding(0, 5, 8, 125.5, LocalDate.of(2026, 3, 10));
		Holding saved = new Holding(42, 5, 8, 125.5, LocalDate.of(2026, 3, 10));
		when(holdingRepository.save(request)).thenReturn(saved);

		Holding actual = holdingService.createHolding(request);

		assertSame(saved, actual);
		verify(holdingRepository).save(request);
	}

	@Test
	void createHoldingThrowsWhenQuantityIsZero() {
		Holding request = new Holding(0, 5, 0, 125.5, LocalDate.of(2026, 3, 10));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> holdingService.createHolding(request)
		);

		assertEquals("Quantity must be greater than zero", exception.getMessage());
		verify(holdingRepository, never()).save(any(Holding.class));
	}

	@Test
	void createHoldingThrowsWhenQuantityIsNegative() {
		Holding request = new Holding(0, 5, -4, 125.5, LocalDate.of(2026, 3, 10));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> holdingService.createHolding(request)
		);

		assertEquals("Quantity must be greater than zero", exception.getMessage());
		verify(holdingRepository, never()).save(any(Holding.class));
	}

	@Test
	void updateHoldingAppliesPathIdAndReturnsTrueWhenRepositoryUpdates() {
		Holding request = new Holding(0, 17, 12, 333.0, LocalDate.of(2026, 4, 1));
		when(holdingRepository.update(any(Holding.class))).thenReturn(true);

		boolean updated = holdingService.updateHolding(9, request);

		ArgumentCaptor<Holding> captor = ArgumentCaptor.forClass(Holding.class);
		verify(holdingRepository).update(captor.capture());
		assertTrue(updated);
		assertEquals(9, captor.getValue().getId());
		assertEquals(9, request.getId());
	}

	@Test
	void deleteHoldingReturnsTrueWhenRepositoryDeletesRow() {
		when(holdingRepository.deleteById(3)).thenReturn(true);

		boolean deleted = holdingService.deleteHolding(3);

		assertTrue(deleted);
	}

	@Test
	void deleteHoldingThrowsWhenHoldingDoesNotExist() {
		when(holdingRepository.deleteById(77)).thenReturn(false);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> holdingService.deleteHolding(77)
		);

		assertEquals("Holding not found with id: 77", exception.getMessage());
	}

	@Test
	void getPortfolioAllocationReturnsRepositoryAllocationList() {
		HoldingAllocation allocation = new HoldingAllocation();
		allocation.setHoldingId(1);
		allocation.setCurrentValue(1000.0);
		List<HoldingAllocation> expected = List.of(allocation);
		when(holdingRepository.getPortfolioAllocation()).thenReturn(expected);

		List<HoldingAllocation> actual = holdingService.getPortfolioAllocation();

		assertSame(expected, actual);
	}

	@Test
	void getSectorAllocationReturnsRepositorySectorAllocationList() {
		SectorAllocation sectorAllocation = new SectorAllocation();
		sectorAllocation.setSector("Technology");
		List<SectorAllocation> expected = List.of(sectorAllocation);
		when(holdingRepository.getSectorAllocation()).thenReturn(expected);

		List<SectorAllocation> actual = holdingService.getSectorAllocation();

		assertSame(expected, actual);
	}

	@Test
	void getTotalValuesReturnRepositoryTotals() {
		when(holdingRepository.getTotalInvestedValue()).thenReturn(1234.5);
		when(holdingRepository.getTotalCurrentValue()).thenReturn(1567.8);

		double invested = holdingService.getTotalInvestedValue();
		double current = holdingService.getTotalCurrentValue();

		assertEquals(1234.5, invested, 0.001);
		assertEquals(1567.8, current, 0.001);
	}
}
