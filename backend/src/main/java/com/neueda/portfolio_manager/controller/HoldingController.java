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

@RestController
@RequestMapping("/api")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

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
    }

    @PostMapping("/holdings")
    public ResponseEntity<Holding> createHolding(@RequestBody Holding holding) {
        Holding createdHolding = holdingService.createHolding(holding);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHolding);
    }

    @PutMapping("/holdings/{id}")
    public ResponseEntity<Void> updateHolding(@PathVariable int id, @RequestBody Holding holding) {
        boolean updated = holdingService.updateHolding(id, holding);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/holdings/{id}")
    public ResponseEntity<Void> deleteHolding(@PathVariable int id) {
        boolean deleted = holdingService.deleteHolding(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portfolio/allocation")
    public ResponseEntity<List<HoldingAllocation>> getPortfolioAllocation() {
        return ResponseEntity.ok(holdingService.getPortfolioAllocation());
    }

    @GetMapping("/portfolio/sectors")
    public ResponseEntity<List<SectorAllocation>> getSectorAllocation() {
        return ResponseEntity.ok(holdingService.getSectorAllocation());
    }

    @GetMapping("/portfolio/summary")
    public ResponseEntity<Map<String, Double>> getPortfolioSummary() {
        double investedValue = holdingService.getTotalInvestedValue();
        double currentValue = holdingService.getTotalCurrentValue();

        Map<String, Double> summary = new LinkedHashMap<>();
        summary.put("totalInvestedValue", investedValue);
        summary.put("totalCurrentValue", currentValue);
        summary.put("totalGainLoss", currentValue - investedValue);

        return ResponseEntity.ok(summary);
    }
}
