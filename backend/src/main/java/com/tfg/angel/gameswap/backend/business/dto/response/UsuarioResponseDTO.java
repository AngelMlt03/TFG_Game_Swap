package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String nUsuario;
    private LocalDate fechaNacimiento;
    private Double saldo;
    private String correo;
    private Double estrellas;
}
