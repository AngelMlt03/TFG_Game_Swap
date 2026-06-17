package com.tfg.angel.gameswap.backend.business.dto.request;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {}
