package com.neueda.portfolio_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neueda.portfolio_manager.entity.Market;
import com.neueda.portfolio_manager.service.MarketService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(
        name = "Market APIs",
        description = "APIs for retrieving and managing market information"
)
@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://10.9.77.127:8085"})
@RequestMapping("/api/market")
public class MarketController {
    @Autowired
    private MarketService marketService;

    // GET /api/market -> Complete Market List
    @Operation(
            summary = "Get all market data",
            description = "Retrieves the complete list of market stocks."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Market list retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<Market>> getAllMarkets() {
        return ResponseEntity.ok(marketService.getAllMarkets());
    }

    // GET /api/market/search?symbol=AAPL -> Search Bar
    /*@GetMapping("/search")
    public ResponseEntity<List<Market>> searchMarkets(@RequestParam String symbol) {
        return ResponseEntity.ok(marketService.searchMarkets(symbol));
    }*/

    // GET /api/market/gainers -> Top Gainers widget
    @Operation(
            summary = "Get top gainers",
            description = "Returns the list of stocks with the highest positive price change."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Top gainers retrieved successfully")
    })
    @GetMapping("/gainers")    public ResponseEntity<List<Market>> getTopGainers() {
        return ResponseEntity.ok(marketService.getTopGainers());
    }

    // GET /api/market/losers -> Top Losers widget
    @Operation(
            summary = "Get top losers",
            description = "Returns the list of stocks with the highest negative price change."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Top losers retrieved successfully")
    })
    @GetMapping("/losers")    public ResponseEntity<List<Market>> getTopLosers() {
        return ResponseEntity.ok(marketService.getTopLosers());
    }

    // GET /api/market/{symbol} -> Stock Details Card
   /* @GetMapping("/{symbol}")
    public ResponseEntity<Market> getMarketBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(marketService.getMarketBySymbol(symbol));
    }

    // POST /api/market -> add a new market entry
    @PostMapping
    public ResponseEntity<Market> addMarket(@RequestBody Market market) {
        Market saved = marketService.addMarket(market);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/market/{symbol}/price -> refresh price (used internally by Buy/Sell flow)
    @PutMapping("/{symbol}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable String symbol,
                                             @RequestParam double currentPrice,
                                             @RequestParam double changePercent) {
        marketService.updatePrice(symbol, currentPrice, changePercent);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/market/{symbol} -> remove a market entry
    @DeleteMapping("/{symbol}")
    public ResponseEntity<Void> deleteMarket(@PathVariable String symbol) {
        marketService.deleteMarket(symbol);
        return ResponseEntity.noContent().build();
    }*/
}
