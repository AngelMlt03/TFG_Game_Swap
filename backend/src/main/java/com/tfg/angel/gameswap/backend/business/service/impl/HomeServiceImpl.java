package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.response.*;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.repository.*;

import com.tfg.angel.gameswap.backend.business.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final PostVentaRepository postventaRepository;
    private final PostIntercambioRepository postintercambioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<PostBusquedaDTO> ultimasVentas() {

        return postventaRepository.findTop4ByEstadoOrderByIdDesc(EstadoPost.ACTIVO)
                .stream()
                .map(v -> PostBusquedaDTO.builder()
                        .id(v.getId())
                        .tipo("VENTA")
                        .idApi(v.getProducto().getIdAPI().longValue())
                        .nombreProducto(v.getProducto().getNombre())
                        .plataforma(v.getPlataforma())
                        .estado(v.getProducto().getEstado().name())
                        .precio(v.getPrecio())
                        .nombreUsuario(v.getVendedor().getNombreUsuario())
                        .descripcion(v.getDescripcion())
                        .build())
                .toList();
    }

    @Override
    public List<PostBusquedaDTO> ultimosIntercambios() {

        return postintercambioRepository.findTop4ByEstadoOrderByIdDesc(EstadoPost.ACTIVO)
                .stream()
                .map(i -> PostBusquedaDTO.builder()
                        .id(i.getId())
                        .tipo("INTERCAMBIO")

                        .idApi(i.getProducto().getIdAPI().longValue())
                        .nombreProducto(i.getProducto().getNombre())
                        .plataforma(i.getPlataforma())
                        .estado(i.getProducto().getEstado().name())

                        .idApiIntercambio(i.getProductoCambio().getIdAPI().longValue())
                        .nombreProductoIntercambio(i.getProductoCambio().getNombre())
                        .plataformaIntercambio(i.getPlataformaCambio())
                        .estadoIntercambio(i.getProductoCambio().getEstado().name())

                        .nombreUsuario(i.getUsuario().getNombreUsuario())
                        .descripcion(i.getDescripcion())
                        .build())
                .toList();
    }

    @Override
    public HomeStatsDTO estadisticas() {

        return HomeStatsDTO.builder()
                .ventas((long) postventaRepository.findByEstado(EstadoPost.ACTIVO).size())
                .intercambios((long) postintercambioRepository.findByEstado(EstadoPost.ACTIVO).size())
                .usuarios(usuarioRepository.count())
                .reviews(reviewRepository.count())
                .build();
    }

    @Override
    public List<UsuarioRankingDTO> topUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Usuario::getEstrellas,
                                Comparator.nullsLast(Double::compareTo)
                        ).reversed()
                )
                .limit(6)
                .map(u -> UsuarioRankingDTO.builder()
                        .nombreUsuario(u.getNombreUsuario())
                        .estrellas(u.getEstrellas())
                        .build())
                .toList();
    }
}
