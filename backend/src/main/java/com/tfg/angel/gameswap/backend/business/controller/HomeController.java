package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.response.HomeStatsDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioRankingDTO;
import com.tfg.angel.gameswap.backend.business.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/ultimas-ventas")
    public List<PostBusquedaDTO> ultimasVentas() {
        return homeService.ultimasVentas();
    }

    @GetMapping("/ultimos-intercambios")
    public List<PostBusquedaDTO> ultimosIntercambios() {
        return homeService.ultimosIntercambios();
    }

    @GetMapping("/estadisticas")
    public HomeStatsDTO estadisticas() {
        return homeService.estadisticas();
    }

    @GetMapping("/top-usuarios")
    public List<UsuarioRankingDTO> topUsuarios() {
        return homeService.topUsuarios();
    }
}
