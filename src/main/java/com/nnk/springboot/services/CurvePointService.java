package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointService {

    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid curvePoint Id:" + id));
    }

    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public void deleteById(Integer id) {
        CurvePoint curvePoint = findById(id);
        curvePointRepository.delete(curvePoint);
    }
}
