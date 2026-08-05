package com.neueda.portfolio_manager.controller;

import com.neueda.portfolio_manager.entity.Holding;
import com.neueda.portfolio_manager.entity.HoldingAllocation;
import com.neueda.portfolio_manager.entity.SectorAllocation;
import com.neueda.portfolio_manager.service.HoldingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@Tag(
        name = "Holding APIs",
        description = "APIs for managing holdings and portfolio information"
)
@RestController
@RequestMapping("/api")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }
/*
    @GetMapping("/holdings")
    public ResponseEntity<List<Holding>> getAllHoldings() {
        return ResponseEntity.ok(holdingService.getAllHoldings());
    }

    @GetMapping("/holdings/{id}")
    public ResponseEntity<Holding> getHoldingById(@PathVariable int id) {
        return holdingService.getHoldingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/holdings/market/{marketId}")
    public ResponseEntity<Holding> getHoldingByMarketId(@PathVariable int marketId) {
        return holdingService.getHoldingByMarketId(marketId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }*/

    @Operation(
            summary = "Create a holding",
            description = "Creates a new holding in the portfolio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Holding created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })


    // POST /api/holdings
    @PostMapping("/holdings")
    public ResponseEntity<Holding> createHolding(@RequestBody Holding holding) {
        Holding createdHolding = holdingService.createHolding(holding);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHolding);
    }

    @Operation(
            summary = "Update an existing holding",
            description = "Updates the details of an existing holding using its ID. Used by the sell flow to update holding quantity instead of deleting the holding."
    )

    @PutMapping("/holdings/{id}")
    public ResponseEntity<Void> updateHolding(@PathVariable int id, @RequestBody Holding holding) {
        boolean updated = holdingService.updateHolding(id, holding);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a holding",
            description = "Deletes a holding using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Holding deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Holding not found")
    })


    // DELETE /api/holdings/{id}
    @DeleteMapping("/holdings/{id}")
    public ResponseEntity<Void> deleteHolding(@PathVariable int id) {

        holdingService.deleteHolding(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get portfolio allocation",
            description = "Returns allocation details for all holdings."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio allocation retrieved successfully")
    })
    // GET /api/portfolio/allocation
    @GetMapping("/portfolio/allocation")
    public ResponseEntity<List<HoldingAllocation>> getPortfolioAllocation() {
        return ResponseEntity.ok(holdingService.getPortfolioAllocation());
    }

    @Operation(
            summary = "Get sector allocation",
            description = "Returns portfolio allocation grouped by sector."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sector allocation retrieved successfully")
    })
    // GET /api/portfolio/sectors
    @GetMapping("/portfolio/sectors")
    public ResponseEntity<List<SectorAllocation>> getSectorAllocation() {
        return ResponseEntity.ok(holdingService.getSectorAllocation());
    }

    @Operation(
            summary = "Get portfolio summary",
            description = "Returns the total invested value, current value, and overall gain or loss."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio summary retrieved successfully")
    })
    // GET /api/portfolio/summary
    @GetMapping("/portfolio/summary")
    public ResponseEntity<Map<String, Double>> getPortfolioSummary() {
        double investedValue = holdingService.getTotalInvestedValue();
        double currentValue = holdingService.getTotalCurrentValue();

        Map<String, Double> summary = new LinkedHashMap<>();
        double gainLoss = currentValue - investedValue;

        summary.put("totalInvestedValue", investedValue);
        summary.put("totalCurrentValue", currentValue);
        summary.put("totalGainLoss", gainLoss);
        summary.put(
                "growthPercentage",
                investedValue == 0 ? 0.0 : (gainLoss / investedValue) * 100
        );

        return ResponseEntity.ok(summary);
    }
}
