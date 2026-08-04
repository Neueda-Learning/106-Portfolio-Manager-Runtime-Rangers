package com.neueda.portfolio_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.neueda.portfolio_manager.entity.Market;

@Repository
public class MarketRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // GET /api/market -> complete market list
    public List<Market> getAllMarkets() {
        String sql = "SELECT * FROM market ORDER BY symbol ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Market.class));
    }

    // GET /api/market/{symbol} -> Stock Details Card

    public Optional<Market> getMarketBySymbol(String symbol) {
        String sql = "SELECT * FROM market WHERE symbol = ?";
        List<Market> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Market.class), symbol);
        return results.stream().findFirst();
    }

    // GET /api/market/search?symbol=AAPL -> Search Bar
    public List<Market> searchMarkets(String keyword) {
        String sql = "SELECT * FROM market WHERE symbol LIKE ? OR company_name LIKE ?";
        String pattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Market.class), pattern, pattern);
    }

    // GET /api/market/gainers -> Top Gainers widget
    public List<Market> getTopGainers() {
        String sql = "SELECT * FROM market ORDER BY change_percent DESC LIMIT 5";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Market.class));
    }

    // GET /api/market/losers -> Top Losers widget
    public List<Market> getTopLosers() {
        String sql = "SELECT * FROM market ORDER BY change_percent ASC LIMIT 5";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Market.class));
    }

    public boolean existsBySymbol(String symbol) {
        String sql = "SELECT COUNT(*) FROM market WHERE symbol = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, symbol);
        return count != null && count > 0;
    }

    public int save(Market market) {
        String sql = "INSERT INTO market (symbol, company_name, exchange, sector, current_price, change_percent) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                market.getSymbol(),
                market.getCompanyName(),
                market.getExchange(),
                market.getSector(),
                market.getCurrentPrice(),
                market.getChangePercent());
    }

    // used by Buy/Sell flow to refresh price after a transaction
    public int updatePrice(String symbol, double currentPrice, double changePercent) {
        String sql = "UPDATE market SET current_price = ?, change_percent = ? WHERE symbol = ?";
        return jdbcTemplate.update(sql, currentPrice, changePercent, symbol);
    }

    public int deleteBySymbol(String symbol) {
        String sql = "DELETE FROM market WHERE symbol = ?";
        return jdbcTemplate.update(sql, symbol);
    }
}
