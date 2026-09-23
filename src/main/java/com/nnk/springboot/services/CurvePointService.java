package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link CurvePoint}.
 * Il reçoit les données des formulaires sous forme de {@link CurvePointDto}
 * et gère automatiquement les dates {@code creationDate} et {@code asOfDate}.
 */
@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    /**
     * Récupère l'ensemble des CurvePoint enregistrés.
     *
     * @return la liste de tous les CurvePoint
     */
    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    /**
     * Récupère un CurvePoint à partir de son identifiant.
     *
     * @param id l'identifiant du CurvePoint recherché
     * @return le CurvePoint correspondant
     * @throws ResourceNotFoundException si aucun CurvePoint n'existe pour cet identifiant
     */
    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid curvePoint Id:" + id));
    }

    /**
     * Crée un nouveau CurvePoint à partir des champs saisis dans le
     * formulaire. {@code creationDate} et {@code asOfDate} sont fixées à
     * l'instant courant. L'identifiant éventuellement présent dans le DTO
     * est ignoré : il est généré par la base de données.
     *
     * @param dto les données soumises depuis le formulaire d'ajout
     */
    public void create(CurvePointDto dto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        CurvePoint curvePoint = new CurvePoint(dto.getCurveId(), dto.getTerm(), dto.getValue());
        curvePoint.setCreationDate(now);
        curvePoint.setAsOfDate(now);
        curvePointRepository.save(curvePoint);
    }

    /**
     * Met à jour un CurvePoint existant. Seuls les champs du formulaire sont
     * modifiés, {@code asOfDate} est fixée à l'instant courant, et la
     * {@code creationDate} d'origine est conservée.
     *
     * @param id  l'identifiant du CurvePoint à modifier
     * @param dto les nouvelles valeurs saisies dans le formulaire de modification
     * @throws ResourceNotFoundException si aucun CurvePoint n'existe pour cet identifiant
     */
    public void update(Integer id, CurvePointDto dto) {
        CurvePoint curvePoint = findById(id);
        curvePoint.setCurveId(dto.getCurveId());
        curvePoint.setTerm(dto.getTerm());
        curvePoint.setValue(dto.getValue());
        curvePoint.setAsOfDate(new Timestamp(System.currentTimeMillis()));
        curvePointRepository.save(curvePoint);
    }

    /**
     * Supprime un CurvePoint à partir de son identifiant.
     *
     * @param id l'identifiant du CurvePoint à supprimer
     * @throws ResourceNotFoundException si aucun CurvePoint n'existe pour cet identifiant
     */
    public void deleteById(Integer id) {
        CurvePoint curvePoint = findById(id);
        curvePointRepository.delete(curvePoint);
    }
}