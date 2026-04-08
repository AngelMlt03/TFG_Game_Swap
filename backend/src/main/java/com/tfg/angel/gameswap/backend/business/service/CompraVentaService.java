package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;

import java.util.List;

public interface CompraVentaService {

    CompraVentaResponseDTO create(Long idPostVenta, Long idUsuario);

    CompraVentaResponseDTO findById(Long id);

    List<CompraVentaResponseDTO> findAll();

    List<CompraVentaResponseDTO> findByComprador(Long idComprador);

    List<CompraVentaResponseDTO> findByVendedor(Long idVendedor);

    void delete(Long id);
}
