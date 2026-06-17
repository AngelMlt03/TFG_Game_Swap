package com.tfg.angel.gameswap.backend.business.dto.request;

import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    private String nombre;
    private String nombreUsuario;
    private LocalDate fechaNacimiento;
    private String correo;
    private Rol rol;
}