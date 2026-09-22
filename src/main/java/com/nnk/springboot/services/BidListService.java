package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link BidList}.
 * Il s'appuie sur {@link BidListRepository} pour l'accès aux données.
 */
@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    /**
     * Récupère l'ensemble des BidList enregistrées.
     *
     * @return la liste de toutes les BidList
     */
    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    /**
     * Récupère une BidList à partir de son identifiant.
     *
     * @param id l'identifiant de la BidList recherchée
     * @return la BidList correspondante
     * @throws ResourceNotFoundException si aucune BidList n'existe pour cet identifiant
     */
    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid bidList Id:" + id));
    }

    /**
     * Enregistre une BidList : création si elle n'a pas d'identifiant,
     * mise à jour sinon.
     *
     * @param bidList la BidList à enregistrer
     * @return la BidList enregistrée, avec son identifiant généré le cas échéant
     */
    public BidList save(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    /**
     * Supprime une BidList à partir de son identifiant.
     *
     * @param id l'identifiant de la BidList à supprimer
     * @throws ResourceNotFoundException si aucune BidList n'existe pour cet identifiant
     */
    public void deleteById(Integer id) {
        BidList bidList = findById(id);
        bidListRepository.delete(bidList);
    }
}
