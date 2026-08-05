package com.neueda.portfolio_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neueda.portfolio_manager.entity.Market;
import com.neueda.portfolio_manager.service.MarketService;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    @Autowired
    private MarketService marketService;

    // GET /api/market -> Complete Market List
    @GetMapping
    public ResponseEntity<List<Market>> getAllMarkets() {
        return ResponseEntity.ok(marketService.getAllMarkets());
    }

    // GET /api/market/search?symbol=AAPL -> Search Bar
    @GetMapping("/search")
    public ResponseEntity<List<Market>> searchMarkets(@RequestParam String symbol) {
        return ResponseEntity.ok(marketService.searchMarkets(symbol));
    }

    // GET /api/market/gainers -> Top Gainers widget
    @GetMapping("/gainers")
    public ResponseEntity<List<Market>> getTopGainers() {
        return ResponseEntity.ok(marketService.getTopGainers());
    }

    // GET /api/market/losers -> Top Losers widget
    @GetMapping("/losers")
    public ResponseEntity<List<Market>> getTopLosers() {
        return ResponseEntity.ok(marketService.getTopLosers());
    }

    // GET /api/market/{symbol} -> Stock Details Card
    @GetMapping("/{symbol}")
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
    }
}
