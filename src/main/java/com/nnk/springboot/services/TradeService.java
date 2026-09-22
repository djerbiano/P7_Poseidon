package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link Trade}.
 * Il s'appuie sur {@link TradeRepository} pour l'accès aux données.
 */
@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    /**
     * Récupère l'ensemble des Trade enregistrés.
     *
     * @return la liste de tous les Trade
     */
    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    /**
     * Récupère un Trade à partir de son identifiant.
     *
     * @param id l'identifiant du Trade recherché
     * @return le Trade correspondant
     * @throws ResourceNotFoundException si aucun Trade n'existe pour cet identifiant
     */
    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid trade Id:" + id));
    }

    /**
     * Enregistre un Trade : création s'il n'a pas d'identifiant,
     * mise à jour sinon.
     *
     * @param trade le Trade à enregistrer
     * @return le Trade enregistré, avec son identifiant généré le cas échéant
     */
    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    /**
     * Supprime un Trade à partir de son identifiant.
     *
     * @param id l'identifiant du Trade à supprimer
     * @throws ResourceNotFoundException si aucun Trade n'existe pour cet identifiant
     */
    public void deleteById(Integer id) {
        Trade trade = findById(id);
        tradeRepository.delete(trade);
    }
}
