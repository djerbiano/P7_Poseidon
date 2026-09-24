package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link RuleNameService}.
 * Les dépendances (RuleNameRepository) sont simulées avec Mockito
 * pour tester la logique du service de manière isolée, sans base de données réelle.
 */
@ExtendWith(MockitoExtension.class)
public class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    private RuleName ruleName;
    private RuleNameDto dto;

    @BeforeEach
    void setUp() {
        ruleName = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        ruleName.setId(1);

        dto = new RuleNameDto();
        dto.setName("New Name");
        dto.setDescription("New Description");
        dto.setJson("New Json");
        dto.setTemplate("New Template");
        dto.setSqlStr("New SQL");
        dto.setSqlPart("New SQL Part");
    }

    /**
     * Vérifie que findAll() retourne bien la liste fournie par le repository.
     */
    @Test
    void findAll_shouldReturnListOfRuleNames() {
        when(ruleNameRepository.findAll()).thenReturn(List.of(ruleName));

        List<RuleName> result = ruleNameService.findAll();

        assertEquals(1, result.size());
        verify(ruleNameRepository, times(1)).findAll();
    }

    /**
     * Vérifie que findById() retourne le bon RuleName quand l'id existe.
     */
    @Test
    void findById_shouldReturnRuleName_whenIdExists() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        RuleName result = ruleNameService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Vérifie que findById() lève ResourceNotFoundException quand l'id n'existe pas.
     */
    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ruleNameService.findById(99));
    }

    /**
     * Vérifie que create() enregistre un nouveau RuleName contenant les champs
     * du formulaire, en ignorant tout identifiant fourni par l'utilisateur.
     */
    @Test
    void create_shouldSaveNewEntity_andIgnoreIdFromDto() {
        dto.setId(42);

        ruleNameService.create(dto);

        verify(ruleNameRepository, times(1)).save(
                argThat((RuleName saved) ->
                        saved.getId() == null
                                && "New Name".equals(saved.getName())
                                && "New Description".equals(saved.getDescription())
                                && "New Json".equals(saved.getJson())
                                && "New Template".equals(saved.getTemplate())
                                && "New SQL".equals(saved.getSqlStr())
                                && "New SQL Part".equals(saved.getSqlPart())
                )
        );
    }

    /**
     * Vérifie que update() modifie les champs du formulaire sur l'entité
     * existante, en conservant son identifiant.
     */
    @Test
    void update_shouldOverwriteFormFields_andKeepId() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        ruleNameService.update(1, dto);

        verify(ruleNameRepository, times(1)).save(ruleName);
        assertEquals(1, ruleName.getId());
        assertEquals("New Name", ruleName.getName());
        assertEquals("New Description", ruleName.getDescription());
        assertEquals("New Json", ruleName.getJson());
        assertEquals("New Template", ruleName.getTemplate());
        assertEquals("New SQL", ruleName.getSqlStr());
        assertEquals("New SQL Part", ruleName.getSqlPart());
    }

    /**
     * Vérifie que update() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler save().
     */
    @Test
    void update_shouldThrowException_whenIdDoesNotExist() {
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ruleNameService.update(99, dto));
        verify(ruleNameRepository, never()).save(any());
    }

    /**
     * Vérifie que deleteById() trouve l'entité puis la supprime via le repository.
     */
    @Test
    void deleteById_shouldCallRepositoryDelete_whenIdExists() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        ruleNameService.deleteById(1);

        verify(ruleNameRepository, times(1)).delete(ruleName);
    }

    /**
     * Vérifie que deleteById() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler delete().
     */
    @Test
    void deleteById_shouldThrowException_whenIdDoesNotExist() {
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ruleNameService.deleteById(99));
        verify(ruleNameRepository, never()).delete(any());
    }
}