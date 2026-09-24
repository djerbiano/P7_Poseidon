package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link Trade}.
 * Il s'appuie sur {@link TradeRepository} pour l'accès aux données et
 * reçoit les données des formulaires sous forme de {@link TradeDto}.
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
     * Crée un nouveau Trade à partir des champs saisis dans le formulaire.
     * L'identifiant éventuellement présent dans le DTO est ignoré : il est
     * généré par la base de données.
     *
     * @param dto les données soumises depuis le formulaire d'ajout
     */
    public void create(TradeDto dto) {
        Trade trade = new Trade(dto.getAccount(), dto.getType());
        trade.setBuyQuantity(dto.getBuyQuantity());
        tradeRepository.save(trade);
    }

    /**
     * Met à jour un Trade existant. Seuls les champs du formulaire sont
     * modifiés ; les autres colonnes de l'entité (trader, book, prix,
     * dates, etc.) sont conservées.
     *
     * @param id  l'identifiant du Trade à modifier
     * @param dto les nouvelles valeurs saisies dans le formulaire de modification
     * @throws ResourceNotFoundException si aucun Trade n'existe pour cet identifiant
     */
    public void update(Integer id, TradeDto dto) {
        Trade trade = findById(id);
        trade.setAccount(dto.getAccount());
        trade.setType(dto.getType());
        trade.setBuyQuantity(dto.getBuyQuantity());
        tradeRepository.save(trade);
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