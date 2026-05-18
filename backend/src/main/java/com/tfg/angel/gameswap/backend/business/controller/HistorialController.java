package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.impl.HistorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final HistorialService historialService;

    @GetMapping("/compras")
    public List<CompraVentaResponseDTO> historialCompras() {
        return historialService.getHistorialCompras();
    }

    @GetMapping("/ventas")
    public List<CompraVentaResponseDTO> historialVentas() {
        return historialService.getHistorialVentas();
    }

    @GetMapping("/intercambios")
    public List<IntercambioResponseDTO> historialIntercambios() {
        return historialService.getHistorialIntercambios();
    }
}
