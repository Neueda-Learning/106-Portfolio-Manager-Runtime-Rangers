package com.neueda.portfolio_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neueda.portfolio_manager.entity.Market;
import com.neueda.portfolio_manager.repository.MarketRepository;

@Service
public class MarketService {

    @Autowired
    private MarketRepository marketRepository;

    // GET /api/market -> complete market list
    public List<Market> getAllMarkets() {
        return marketRepository.getAllMarkets();
    }

    // GET /api/market/{symbol} -> Stock Details Card
    public Market getMarketBySymbol(String symbol) {
        Optional<Market> market = marketRepository.getMarketBySymbol(symbol);
        return market.orElseThrow(() -> new IllegalArgumentException("Market not found for symbol: " + symbol));
    }

    // GET /api/market/search?symbol=AAPL -> Search Bar
    public List<Market> searchMarkets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword must not be empty");
        }
        return marketRepository.searchMarkets(keyword.trim());
    }

    // GET /api/market/gainers -> Top Gainers widget
    public List<Market> getTopGainers() {
        return marketRepository.getTopGainers();
    }

    // GET /api/market/losers -> Top Losers widget
    public List<Market> getTopLosers() {
        return marketRepository.getTopLosers();
    }

    public boolean marketExists(String symbol) {
        return marketRepository.existsBySymbol(symbol);
    }

    public Market addMarket(Market market) {
        if (marketRepository.existsBySymbol(market.getSymbol())) {
            throw new IllegalArgumentException("Market already exists for symbol: " + market.getSymbol());
        }
        marketRepository.save(market);
        return market;
    }

    // used by Buy/Sell flow (Transaction Service) to refresh price after a transaction
    public void updatePrice(String symbol, double currentPrice, double changePercent) {
        if (!marketRepository.existsBySymbol(symbol)) {
            throw new IllegalArgumentException("Market not found for symbol: " + symbol);
        }
        marketRepository.updatePrice(symbol, currentPrice, changePercent);
    }

    public void deleteMarket(String symbol) {
        if (!marketRepository.existsBySymbol(symbol)) {
            throw new IllegalArgumentException("Market not found for symbol: " + symbol);
        }
        marketRepository.deleteBySymbol(symbol);
    }
}
