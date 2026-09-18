package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid trade Id:" + id));
    }

    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    public void deleteById(Integer id) {
        Trade trade = findById(id);
        tradeRepository.delete(trade);
    }
}
