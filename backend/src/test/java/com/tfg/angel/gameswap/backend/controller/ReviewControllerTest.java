package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.ReviewController;
import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.service.ReviewService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Test
    void testAllMethods() {

        ReviewService service = mock(ReviewService.class);
        ReviewController controller = new ReviewController(service);

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();

        controller.findAll();
        controller.findById(1L);
        controller.findByReviewer(1L);
        controller.findByReviewed(1L);
        controller.create(reviewRequestDTO);
        controller.delete(1L);

        verify(service).findAll();
        verify(service).findById(1L);
        verify(service).findByReviewer(1L);
        verify(service).findByReviewed(1L);
        verify(service).create(reviewRequestDTO);
        verify(service).delete(1L);
    }
}
