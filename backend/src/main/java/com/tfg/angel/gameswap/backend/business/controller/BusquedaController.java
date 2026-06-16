package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.service.impl.BusquedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/busqueda")
@RequiredArgsConstructor
public class BusquedaController {

    private final BusquedaService busquedaService;

    @GetMapping
    public List<PostBusquedaDTO> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String plataforma,
            @RequestParam(required = false) String estado) {
        return busquedaService.buscar(nombre, tipo, plataforma, estado);
    }

}
