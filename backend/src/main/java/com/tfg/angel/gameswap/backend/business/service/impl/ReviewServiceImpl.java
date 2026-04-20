package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.ReviewMapper;
import com.tfg.angel.gameswap.backend.business.model.Review;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.ReviewRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.ReviewService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ReviewResponseDTO create(ReviewRequestDTO dto) {

        Usuario reviewer = usuarioRepository.findById(dto.getIdReviewer())
                .orElseThrow(() -> new GSNotFoundException("Reviewer no encontrado"));

        Usuario reviewed = usuarioRepository.findById(dto.getIdReviewed())
                .orElseThrow(() -> new GSNotFoundException("Reviewed no encontrado"));

        if (reviewer.getId().equals(reviewed.getId())) {
            throw new GSBadRequestException("Un usuario no puede valorarse a sí mismo");
        }

        if (dto.getEstrellas() < 0 || dto.getEstrellas() > 5) {
            throw new GSBadRequestException("Las estrellas deben estar entre 0 y 5");
        }

        Review entity = Review.builder()
                .reviewer(reviewer)
                .reviewed(reviewed)
                .contenido(dto.getContenido())
                .estrellas(dto.getEstrellas())
                .build();

        entity = reviewRepository.save(entity);

        return ReviewMapper.toDTO(entity);
    }

    @Override
    public ReviewResponseDTO findById(Long id) {
        Review entity = reviewRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Review no encontrada"));

        return ReviewMapper.toDTO(entity);
    }

    @Override
    public List<ReviewResponseDTO> findAll() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewResponseDTO> findByReviewed(Long idReviewed) {
        return reviewRepository.findByReviewedId(idReviewed)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewResponseDTO> findByReviewer(Long idReviewer) {
        return reviewRepository.findByReviewerId(idReviewer)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new GSNotFoundException("Review no encontrada");
        }

        reviewRepository.deleteById(id);
    }
}
