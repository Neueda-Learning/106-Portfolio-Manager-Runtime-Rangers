package com.neueda.portfolio_manager.repository;

import com.neueda.portfolio_manager.entity.Holding;
import com.neueda.portfolio_manager.entity.HoldingAllocation;
import com.neueda.portfolio_manager.entity.SectorAllocation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class HoldingRepository {

    private final JdbcTemplate jdbcTemplate;

    public HoldingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Holding> holdingRowMapper = (rs, rowNum) -> {
        Holding holding = new Holding();
        holding.setId(rs.getInt("id"));
        holding.setMarketId(rs.getInt("market_id"));
        holding.setQuantity(rs.getInt("quantity"));
        holding.setPurchasePrice(rs.getDouble("purchase_price"));

        Date purchaseDate = rs.getDate("purchase_date");
        holding.setPurchaseDate(purchaseDate == null ? null : purchaseDate.toLocalDate());
        return holding;
    };

    public List<Holding> findAll() {
        String sql = "SELECT id, market_id, quantity, purchase_price, purchase_date FROM holding ORDER BY id";
        return jdbcTemplate.query(sql, holdingRowMapper);
    }

    public Optional<Holding> findById(int id) {
        String sql = "SELECT id, market_id, quantity, purchase_price, purchase_date FROM holding WHERE id = ?";
        List<Holding> holdings = jdbcTemplate.query(sql, holdingRowMapper, id);
        return holdings.stream().findFirst();
    }

    public Optional<Holding> findByMarketId(int marketId) {
        String sql = "SELECT id, market_id, quantity, purchase_price, purchase_date FROM holding WHERE market_id = ?";
        List<Holding> holdings = jdbcTemplate.query(sql, holdingRowMapper, marketId);
        return holdings.stream().findFirst();
    }

    public Holding save(Holding holding) {
        String sql = "INSERT INTO holding (market_id, quantity, purchase_price, purchase_date) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, holding.getMarketId());
            ps.setInt(2, holding.getQuantity());
            ps.setDouble(3, holding.getPurchasePrice());
            if (holding.getPurchaseDate() == null) {
                ps.setDate(4, null);
            } else {
                ps.setDate(4, Date.valueOf(holding.getPurchaseDate()));
            }
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            holding.setId(keyHolder.getKey().intValue());
        }
        return holding;
    }

    public boolean update(Holding holding) {
        String sql = "UPDATE holding SET market_id = ?, quantity = ?, purchase_price = ?, purchase_date = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql,
                holding.getMarketId(),
                holding.getQuantity(),
                holding.getPurchasePrice(),
                toSqlDate(holding.getPurchaseDate()),
                holding.getId());
        return rows > 0;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM holding WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean updateQuantityByMarketId(int marketId, int quantity) {
        String sql = "UPDATE holding SET quantity = ? WHERE market_id = ?";
        return jdbcTemplate.update(sql, quantity, marketId) > 0;
    }

    public List<HoldingAllocation> getPortfolioAllocation() {
        String sql = """
                SELECT h.id AS holding_id,
                       h.market_id,
                       m.symbol,
                       m.company_name,
                       m.sector,
                       h.quantity,
                       h.purchase_price,
                       m.current_price,
                       (h.quantity * h.purchase_price) AS invested_value,
                       (h.quantity * m.current_price) AS current_value,
                       ((h.quantity * m.current_price) - (h.quantity * h.purchase_price)) AS gain_loss
                FROM holding h
                JOIN market m ON m.id = h.market_id
                ORDER BY current_value DESC
                """;

        List<HoldingAllocation> allocations = jdbcTemplate.query(sql, (rs, rowNum) -> {
            HoldingAllocation allocation = new HoldingAllocation();
            allocation.setHoldingId(rs.getInt("holding_id"));
            allocation.setMarketId(rs.getInt("market_id"));
            allocation.setSymbol(rs.getString("symbol"));
            allocation.setCompanyName(rs.getString("company_name"));
            allocation.setSector(rs.getString("sector"));
            allocation.setQuantity(rs.getInt("quantity"));
            allocation.setPurchasePrice(rs.getDouble("purchase_price"));
            allocation.setCurrentPrice(rs.getDouble("current_price"));
            allocation.setInvestedValue(rs.getDouble("invested_value"));
            allocation.setCurrentValue(rs.getDouble("current_value"));
            allocation.setGainLoss(rs.getDouble("gain_loss"));
            return allocation;
        });

        double totalCurrentValue = allocations.stream()
                .mapToDouble(HoldingAllocation::getCurrentValue)
                .sum();

        for (HoldingAllocation allocation : allocations) {
            double percentage = totalCurrentValue == 0.0
                    ? 0.0
                    : (allocation.getCurrentValue() / totalCurrentValue) * 100.0;
            allocation.setAllocationPercentage(percentage);
        }

        return allocations;
    }

    public List<SectorAllocation> getSectorAllocation() {
        String sql = """
                SELECT m.sector,
                       SUM(h.quantity) AS total_quantity,
                       SUM(h.quantity * h.purchase_price) AS invested_value,
                       SUM(h.quantity * m.current_price) AS current_value
                FROM holding h
                JOIN market m ON m.id = h.market_id
                GROUP BY m.sector
                ORDER BY current_value DESC
                """;

        List<SectorAllocation> sectorAllocations = jdbcTemplate.query(sql, (rs, rowNum) -> {
            SectorAllocation sector = new SectorAllocation();
            sector.setSector(rs.getString("sector"));
            sector.setTotalQuantity(rs.getInt("total_quantity"));
            sector.setInvestedValue(rs.getDouble("invested_value"));
            sector.setCurrentValue(rs.getDouble("current_value"));
            return sector;
        });

        double totalCurrentValue = sectorAllocations.stream()
                .mapToDouble(SectorAllocation::getCurrentValue)
                .sum();

        for (SectorAllocation sectorAllocation : sectorAllocations) {
            double percentage = totalCurrentValue == 0.0
                    ? 0.0
                    : (sectorAllocation.getCurrentValue() / totalCurrentValue) * 100.0;
            sectorAllocation.setAllocationPercentage(percentage);
        }

        return sectorAllocations;
    }

    public double getTotalInvestedValue() {
        String sql = "SELECT COALESCE(SUM(quantity * purchase_price), 0) FROM holding";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result == null ? 0.0 : result;
    }

    public double getTotalCurrentValue() {
        String sql = """
                SELECT COALESCE(SUM(h.quantity * m.current_price), 0)
                FROM holding h
                JOIN market m ON m.id = h.market_id
                """;
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result == null ? 0.0 : result;
    }

    private Date toSqlDate(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }
}
