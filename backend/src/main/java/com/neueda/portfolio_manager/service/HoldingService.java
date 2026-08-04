package com.neueda.portfolio_manager.service;

import com.neueda.portfolio_manager.entity.Holding;
import com.neueda.portfolio_manager.entity.HoldingAllocation;
import com.neueda.portfolio_manager.entity.SectorAllocation;
import com.neueda.portfolio_manager.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoldingService {

    private final HoldingRepository holdingRepository;

    public HoldingService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    public List<Holding> getAllHoldings() {
        return holdingRepository.findAll();
    }

    public Optional<Holding> getHoldingById(int id) {
        return holdingRepository.findById(id);
    }

    public Optional<Holding> getHoldingByMarketId(int marketId) {
        return holdingRepository.findByMarketId(marketId);
    }

    public Holding createHolding(Holding holding) {
        return holdingRepository.save(holding);
    }

    public boolean updateHolding(int id, Holding holding) {
        holding.setId(id);
        return holdingRepository.update(holding);
    }

    public boolean deleteHolding(int id) {
        return holdingRepository.deleteById(id);
    }

    public List<HoldingAllocation> getPortfolioAllocation() {
        return holdingRepository.getPortfolioAllocation();
    }

    public List<SectorAllocation> getSectorAllocation() {
        return holdingRepository.getSectorAllocation();
    }

    public double getTotalInvestedValue() {
        return holdingRepository.getTotalInvestedValue();
    }

    public double getTotalCurrentValue() {
        return holdingRepository.getTotalCurrentValue();
    }
}
