package com.tfg.angel.gameswap.backend.igdb;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/igdb")
@RequiredArgsConstructor
public class IgdbController {

    private final IgdbService igdbService;

    @GetMapping("/games")
    public String searchGames(@RequestParam String query, @RequestParam(required = false) Long franchiseId) {
        return igdbService.searchGames(query, franchiseId);
    }

    @GetMapping("/franchises")
    public String searchFranchises(@RequestParam String query) {
        return igdbService.searchFranchises(query);
    }

    @GetMapping("/platforms")
    public String searchPlatforms(@RequestParam String query) {
        return igdbService.searchPlatforms(query);
    }

    @GetMapping("/cover")
    public String getCover(@RequestParam Long id) {
        return igdbService.getGameCover(id);
    }
}
