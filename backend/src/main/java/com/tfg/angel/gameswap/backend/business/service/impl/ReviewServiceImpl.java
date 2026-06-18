package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.ReviewMapper;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;
import com.tfg.angel.gameswap.backend.business.model.Intercambio;
import com.tfg.angel.gameswap.backend.business.model.Review;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.CompraVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.IntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.ReviewRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.ReviewService;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final UsuarioDetailsService usuarioDetailsService;

    private final CompraVentaRepository compraVentaRepository;
    private final IntercambioRepository intercambioRepository;

    @Override
    public ReviewResponseDTO create(ReviewRequestDTO dto) {

        Usuario reviewer = usuarioDetailsService.obtenerUsuarioActual();

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
                .tipoReview(dto.getTipoReview())
                .build();

        if (dto.getTipoReview().equals("VENTA")) {
            Optional<CompraVenta> historial = compraVentaRepository.findById(dto.getIdHistorial());
            if (historial.isPresent())
                entity.setCompraVenta(historial.get());
        }
        if (dto.getTipoReview().equals("INTERCAMBIO")) {
            Optional<Intercambio> historial = intercambioRepository.findById(dto.getIdHistorial());
            if (historial.isPresent())
                entity.setIntercambio(historial.get());
        }

        entity = reviewRepository.save(entity);
        actualizarMediaReviews(reviewed);

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
    public List<ReviewResponseDTO> getReviewsRecibidasToUsuario(String nombreUsuario) {

        UsuarioResponseDTO usuario = usuarioService.findByUsername(nombreUsuario);

        return reviewRepository.findByReviewedId(usuario.getId())
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    public List<ReviewResponseDTO>  getReviewsEnviadas() {
        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        return reviewRepository.findByReviewerId(usuario.getId())
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewResponseDTO> getByUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow();

        return reviewRepository.findByReviewedId(usuario.getId())
                    .stream()
                    .map(ReviewMapper::toDTO)
                    .toList();
    }

    @Override
    public void actualizarMediaReviews(Usuario usuario) {
        List<Review> reviews = reviewRepository.findByReviewedId(usuario.getId());

        double media = reviews.stream()
                .mapToDouble(Review::getEstrellas)
                .average()
                .orElse(0);

        usuario.setEstrellas(media);

        usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Long id) {
        
        if (!reviewRepository.existsById(id)) {
            throw new GSNotFoundException("Review no encontrada");
        }

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        Review review = reviewRepository.findById(id).orElseThrow();

        if (!review.getReviewer().getId() .equals(usuario.getId())) {
            throw new GSBadRequestException( "No puedes eliminar esta review" );
        }

        Usuario reviewed = review.getReviewed();

        reviewRepository.delete(review);
        actualizarMediaReviews(reviewed);
    }
}
