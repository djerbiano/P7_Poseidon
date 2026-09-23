package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service gérant la logique métier des BidList. Reçoit les données des
 * formulaires sous forme de {@link BidListDto} et les applique sur les
 * entités {@link BidList}.
 */
@Service
public class BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    /**
     * Retourne toutes les BidList.
     *
     * @return la liste des BidList
     */
    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    /**
     * Retourne une BidList à partir de son identifiant.
     *
     * @param id l'identifiant recherché
     * @return la BidList trouvée
     * @throws ResourceNotFoundException si aucune BidList ne correspond
     */
    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BidList not found with id: " + id));
    }

    /**
     * Crée une nouvelle BidList à partir des champs du formulaire.
     *
     * @param dto les données soumises
     */
    public void create(BidListDto dto) {
        BidList bidList = new BidList(dto.getAccount(), dto.getType(), dto.getBidQuantity());
        bidListRepository.save(bidList);
    }

    /**
     * Met à jour une BidList existante. Seuls les champs du formulaire sont
     * modifiés ; les autres colonnes de l'entité sont conservées.
     *
     * @param id  l'identifiant de la BidList à modifier
     * @param dto les nouvelles valeurs des champs du formulaire
     * @throws ResourceNotFoundException si aucune BidList ne correspond
     */
    public void update(Integer id, BidListDto dto) {
        BidList bidList = findById(id);
        bidList.setAccount(dto.getAccount());
        bidList.setType(dto.getType());
        bidList.setBidQuantity(dto.getBidQuantity());
        bidListRepository.save(bidList);
    }

    /**
     * Supprime une BidList à partir de son identifiant.
     *
     * @param id l'identifiant de la BidList à supprimer
     * @throws ResourceNotFoundException si aucune BidList ne correspond
     */
    public void deleteById(Integer id) {
        bidListRepository.delete(findById(id));
    }
}