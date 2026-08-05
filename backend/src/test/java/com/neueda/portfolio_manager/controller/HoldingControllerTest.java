package com.neueda.portfolio_manager.controller;

import com.neueda.portfolio_manager.entity.Holding;
import com.neueda.portfolio_manager.entity.HoldingAllocation;
import com.neueda.portfolio_manager.entity.SectorAllocation;
import com.neueda.portfolio_manager.exception.GlobalExceptionHandler;
import com.neueda.portfolio_manager.service.HoldingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HoldingControllerTest {

	@Mock
	private HoldingService holdingService;

	@InjectMocks
	private HoldingController holdingController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(holdingController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}


	@Test
	void createHoldingReturnsBadRequestWhenRequestBodyIsMalformed() throws Exception {
		mockMvc.perform(post("/api/holdings")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Invalid request body. Please check the input fields."));
	}

	@Test
	void deleteHoldingReturnsNoContentWhenHoldingExists() throws Exception {
		when(holdingService.deleteHolding(7)).thenReturn(true);

		mockMvc.perform(delete("/api/holdings/7"))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));
	}

	@Test
	void deleteHoldingReturnsBadRequestWhenHoldingDoesNotExist() throws Exception {
		doThrow(new IllegalArgumentException("Holding not found with id: 99"))
				.when(holdingService)
				.deleteHolding(99);

		mockMvc.perform(delete("/api/holdings/99"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Holding not found with id: 99"));
	}

	@Test
	void getPortfolioAllocationReturnsAllocationList() throws Exception {
		HoldingAllocation allocation = new HoldingAllocation();
		allocation.setHoldingId(1);
		allocation.setMarketId(101);
		allocation.setSymbol("AAPL");
		allocation.setCompanyName("Apple Inc.");
		allocation.setSector("Technology");
		allocation.setQuantity(10);
		allocation.setPurchasePrice(150.0);
		allocation.setCurrentPrice(180.0);
		allocation.setInvestedValue(1500.0);
		allocation.setCurrentValue(1800.0);
		allocation.setGainLoss(300.0);
		allocation.setAllocationPercentage(100.0);

		when(holdingService.getPortfolioAllocation()).thenReturn(List.of(allocation));

		mockMvc.perform(get("/api/portfolio/allocation"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].holdingId").value(1))
				.andExpect(jsonPath("$[0].symbol").value("AAPL"))
				.andExpect(jsonPath("$[0].companyName").value("Apple Inc."))
				.andExpect(jsonPath("$[0].currentValue").value(1800.0))
				.andExpect(jsonPath("$[0].allocationPercentage").value(100.0));
	}

	@Test
	void getSectorAllocationReturnsSectorAllocations() throws Exception {
		SectorAllocation sectorAllocation = new SectorAllocation();
		sectorAllocation.setSector("Technology");
		sectorAllocation.setTotalQuantity(15);
		sectorAllocation.setInvestedValue(3000.0);
		sectorAllocation.setCurrentValue(3450.0);
		sectorAllocation.setAllocationPercentage(72.5);

		when(holdingService.getSectorAllocation()).thenReturn(List.of(sectorAllocation));

		mockMvc.perform(get("/api/portfolio/sectors"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].sector").value("Technology"))
				.andExpect(jsonPath("$[0].totalQuantity").value(15))
				.andExpect(jsonPath("$[0].investedValue").value(3000.0))
				.andExpect(jsonPath("$[0].currentValue").value(3450.0))
				.andExpect(jsonPath("$[0].allocationPercentage").value(72.5));
	}

	@Test
	void getPortfolioSummaryReturnsSummaryWithGrowthPercentage() throws Exception {
		when(holdingService.getTotalInvestedValue()).thenReturn(2000.0);
		when(holdingService.getTotalCurrentValue()).thenReturn(2500.0);

		mockMvc.perform(get("/api/portfolio/summary"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.totalInvestedValue").value(2000.0))
				.andExpect(jsonPath("$.totalCurrentValue").value(2500.0))
				.andExpect(jsonPath("$.totalGainLoss").value(500.0))
				.andExpect(jsonPath("$.growthPercentage").value(25.0));
	}

	@Test
	void getPortfolioSummaryReturnsZeroGrowthPercentageWhenInvestedValueIsZero() throws Exception {
		when(holdingService.getTotalInvestedValue()).thenReturn(0.0);
		when(holdingService.getTotalCurrentValue()).thenReturn(250.0);

		mockMvc.perform(get("/api/portfolio/summary"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalInvestedValue").value(0.0))
				.andExpect(jsonPath("$.totalCurrentValue").value(250.0))
				.andExpect(jsonPath("$.totalGainLoss").value(250.0))
				.andExpect(jsonPath("$.growthPercentage").value(0.0));
	}
}
