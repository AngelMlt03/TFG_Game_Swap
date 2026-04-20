package com.tfg.angel.gameswap.backend.security.records;

public record RegisterRequest(
        String name,
        String username,
        String password,
        String correo
) {}
