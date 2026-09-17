package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid bidList Id:" + id));
    }

    public BidList save(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    public void deleteById(Integer id) {
        BidList bidList = findById(id);
        bidListRepository.delete(bidList);
    }
}
