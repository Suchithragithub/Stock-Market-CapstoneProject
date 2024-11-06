package com.capstone.tradingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.tradingservice.model.Portfolio;
import com.capstone.tradingservice.model.Stock;
import com.capstone.tradingservice.service.PortfolioService;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
	
	@Autowired
	private PortfolioService portfolioService;
	
	@PostMapping("/createPortfolio")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        Portfolio createdPortfolio = portfolioService.createPortfolio(portfolio);
        return ResponseEntity.ok(createdPortfolio);
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(portfolios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable String id) {
        return portfolioService.getPortfolioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable String id, @RequestBody Portfolio portfolio) {
        portfolio.setPortfolioId(id);
        Portfolio updatedPortfolio = portfolioService.updatePortfolio(portfolio);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable String id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }

    // Stock Endpoints

    @PostMapping("/{portfolioId}/stocks")
    public ResponseEntity<Stock> createStock(@PathVariable String portfolioId, @RequestBody Stock stock) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        portfolio.getStocks().add(stock);
        portfolioService.updatePortfolio(portfolio);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/{portfolioId}/stocks")
    public ResponseEntity<List<Stock>> getAllStocks(@PathVariable String portfolioId) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        return ResponseEntity.ok(portfolio.getStocks());
    }

    @DeleteMapping("/{portfolioId}/stocks/{stockId}")
    public ResponseEntity<Void> deleteStock(@PathVariable String portfolioId, @PathVariable String stockId) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        portfolio.getStocks().removeIf(stock -> stock.getStockId().equals(stockId));
        portfolioService.updatePortfolio(portfolio);
        return ResponseEntity.noContent().build();
    }
	
}