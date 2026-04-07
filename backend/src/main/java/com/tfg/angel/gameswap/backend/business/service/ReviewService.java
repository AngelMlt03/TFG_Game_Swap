package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {

    ReviewResponseDTO create(ReviewRequestDTO dto);

    ReviewResponseDTO findById(Long id);

    List<ReviewResponseDTO> findAll();

    List<ReviewResponseDTO> findByReviewed(Long idReviewed);

    List<ReviewResponseDTO> findByReviewer(Long idReviewer);

    void delete(Long id);
}
