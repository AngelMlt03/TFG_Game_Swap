package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    private String nombre;
    private String nUsuario;
    private LocalDate fechaNacimiento;
    private String correo;
}