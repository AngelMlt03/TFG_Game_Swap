package com.tfg.angel.gameswap.backend.igdb;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class IgdbService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${IGDB_CLIENT_ID}")
    private String clientId;

    @Value("${IGDB_CLIENT_SECRET}")
    private String clientSecret;

    private String accessToken;
    private long expiresAt;

    private String getToken() {

        if (accessToken == null || System.currentTimeMillis() >= expiresAt) {

            var response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("id.twitch.tv")
                            .path("/oauth2/token")
                            .queryParam("client_id", clientId)
                            .queryParam("client_secret", clientSecret)
                            .queryParam("grant_type", "client_credentials")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            assert response != null;
            accessToken = (String) response.get("access_token");
            Integer expiresIn = (Integer) response.get("expires_in");

            expiresAt = System.currentTimeMillis() + (expiresIn * 1000L);
        }

        return accessToken;
    }

    public String searchGames(String query) {

        String token = getToken();

        String body = """
        search "%s";
        fields name, cover.image_id;
        limit 8;
        """.formatted(query);

        return webClient.post()
                .uri("https://api.igdb.com/v4/games")
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String searchGames(String query, Long franchiseId) {

        String token = getToken();

        String body;

        if (franchiseId != null) {
            body = """
            search "%s";
            fields name, cover.image_id;
            where franchises = (%d);
            limit 8;
        """.formatted(query, franchiseId);
        } else {
            body = """
            search "%s";
            fields name, cover.image_id;
            limit 8;
        """.formatted(query);
        }

        return webClient.post()
                .uri("https://api.igdb.com/v4/games")
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String searchPlatforms(String query) {

        String token = getToken();

        String body = """
        search "%s";
        fields name, platform_logo.image_id;
        limit 6;
    """.formatted(query);

        return webClient.post()
                .uri("https://api.igdb.com/v4/platforms")
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getGameCover(Long igdbId) {

        String token = getToken();

        String body = "fields cover.image_id; where id = " + igdbId + ";";

        return webClient.post()
                .uri("https://api.igdb.com/v4/games")
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getGameDetails(Long id) {

        String token = getToken();

        String body = """
        fields
        name,
        summary,
        total_rating,
        first_release_date,
        genres.name,
        themes.name,
        game_modes.name,
        cover.image_id,
        screenshots.image_id,
        videos.video_id;

        where id = %d;
        """.formatted(id);

        return webClient.post()
                .uri("https://api.igdb.com/v4/games")
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}