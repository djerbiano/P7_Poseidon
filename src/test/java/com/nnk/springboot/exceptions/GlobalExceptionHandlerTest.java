package com.nnk.springboot.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour {@link GlobalExceptionHandler}.
 * Vérifie que chaque gestionnaire d'exception produit le bon message
 * dans le modèle et renvoie vers la bonne vue d'erreur.
 */
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Vérifie que handleTypeMismatch() ajoute un message clair mentionnant
     * le nom du champ concerné, et retourne la vue "error/400".
     */
    @Test
    void handleTypeMismatch_shouldAddErrorMessageAndReturn400View() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("id");
        Model model = new ExtendedModelMap();

        String viewName = handler.handleTypeMismatch(ex, model);

        assertEquals("error/400", viewName);
        assertEquals("Invalid value for field 'id': a number is expected.", model.getAttribute("errorMsg"));
    }

    /**
     * Vérifie que handleResourceNotFound() ajoute le message de l'exception
     * dans le modèle, et retourne la vue "error/404".
     */
    @Test
    void handleResourceNotFound_shouldAddErrorMessageAndReturn404View() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Invalid curvePoint Id:99");
        Model model = new ExtendedModelMap();

        String viewName = handler.handleResourceNotFound(ex, model);

        assertEquals("error/404", viewName);
        assertEquals("Invalid curvePoint Id:99", model.getAttribute("errorMsg"));
    }
}