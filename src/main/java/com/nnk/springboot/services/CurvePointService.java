package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link CurvePoint}.
 * Il gère automatiquement les dates {@code creationDate} et {@code asOfDate}
 * lors de l'enregistrement.
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
     * Enregistre un CurvePoint en gérant automatiquement ses dates.
     * <ul>
     *   <li>Création (identifiant nul) : {@code creationDate} est fixée à l'instant courant.</li>
     *   <li>Mise à jour : la {@code creationDate} d'origine est conservée.</li>
     *   <li>Dans les deux cas, {@code asOfDate} est fixée à l'instant courant.</li>
     * </ul>
     *
     * @param curvePoint le CurvePoint à enregistrer
     * @return le CurvePoint enregistré
     * @throws ResourceNotFoundException si l'identifiant fourni ne correspond à aucun CurvePoint existant
     */
    public CurvePoint save(CurvePoint curvePoint) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (curvePoint.getId() == null) {
            curvePoint.setCreationDate(now);
        } else {
            CurvePoint existing = findById(curvePoint.getId());
            curvePoint.setCreationDate(existing.getCreationDate());
        }
        curvePoint.setAsOfDate(now);
        return curvePointRepository.save(curvePoint);
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
