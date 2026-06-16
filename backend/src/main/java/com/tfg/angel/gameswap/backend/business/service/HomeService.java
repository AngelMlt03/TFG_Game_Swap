package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.response.HomeStatsDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioRankingDTO;

import java.util.List;

public interface HomeService {

    List<PostBusquedaDTO> ultimasVentas();

    List<PostBusquedaDTO> ultimosIntercambios();

    HomeStatsDTO estadisticas();

    List<UsuarioRankingDTO> topUsuarios();
}
