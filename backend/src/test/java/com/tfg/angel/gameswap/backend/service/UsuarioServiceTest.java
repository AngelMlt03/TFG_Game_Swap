package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ChangePasswordRequest;
import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.*;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import com.tfg.angel.gameswap.backend.business.service.impl.UsuarioServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PostVentaService postVentaService;
    @Mock
    private PostIntercambioService postIntercambioService;
    @Mock
    private ProductoService productoService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRequestDTO usuarioDTO;
    private Usuario usuarioEntidad;

    @BeforeEach
    void setUp() {

        usuarioDTO = UsuarioRequestDTO.builder()
                .nombre("Angel")
                .nombreUsuario("angel_dev")
                .correo("angel@example.com")
                .fechaNacimiento(LocalDate.of(2003, 9, 2))
                .build();

        usuarioEntidad = Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .nombreUsuario("angel_dev")
                .correo("angel@example.com")
                .rol(Rol.CLIENTE)
                .build();
    }

    @Test
    @DisplayName("Debe crear un usuario correctamente si el correo y el nickname son únicos")
    void create_Success() {

        when(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())).thenReturn(false);
        when(usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        UsuarioResponseDTO response = usuarioService.create(usuarioDTO);

        assertNotNull(response);
        assertEquals("angel_dev", response.getNombreUsuario());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el correo ya existe")
    void create_ThrowsException_WhenEmailExists() {

        when(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())).thenReturn(true);

        GSBadRequestException exception = assertThrows(GSBadRequestException.class, () ->
                usuarioService.create(usuarioDTO)
        );

        assertEquals("El correo ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el nombre de usuario ya existe")
    void create_ThrowsException_WhenUsernameExists() {

        when(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())).thenReturn(false);
        when(usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())).thenReturn(true);

        assertThrows(GSBadRequestException.class, () -> usuarioService.create(usuarioDTO));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar los datos de un usuario existente")
    void update_Success() {

        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntidad));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        UsuarioResponseDTO response = usuarioService.update(id, usuarioDTO);

        assertNotNull(response);
        verify(usuarioRepository).save(usuarioEntidad);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar actualizar un usuario inexistente")
    void update_ThrowsNotFound() {

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> usuarioService.update(99L, usuarioDTO));
    }

    @Test
    @DisplayName("Debe eliminar un usuario si existe por ID")
    void delete_Success() {

        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.delete(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar eliminar un usuario que no existe")
    void delete_ThrowsNotFound() {

        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () -> usuarioService.delete(1L));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    private Usuario usuario() {
        return Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .nombreUsuario("angel")
                .correo("angel@test.com")
                .password("encoded")
                .saldo(100.0)
                .estrellas(4.5)
                .build();
    }

    private UsuarioRequestDTO usuarioDTO() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();

        dto.setNombre("Angel");
        dto.setNombreUsuario("angel");
        dto.setCorreo("angel@test.com");

        return dto;
    }

    @Test
    void create_ok() {

        when(usuarioRepository.existsByCorreo(anyString()))
                .thenReturn(false);

        when(usuarioRepository.existsByNombreUsuario(anyString()))
                .thenReturn(false);

        when(usuarioRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        UsuarioResponseDTO result =
                usuarioService.create(usuarioDTO());

        assertNotNull(result);
    }

    @Test
    void findById_ok() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario()));

        assertNotNull(usuarioService.findById(1L));
    }

    @Test
    void findById_notFound() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> usuarioService.findById(1L)
        );
    }

    @Test
    void findByUsername_ok() {

        when(usuarioRepository.findByNombreUsuario("angel"))
                .thenReturn(Optional.of(usuario()));

        assertNotNull(
                usuarioService.findByUsername("angel")
        );
    }

    @Test
    void findAll_ok() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario()));

        assertEquals(
                1,
                usuarioService.findAll().size()
        );
    }

    @Test
    void update_ok() {

        Usuario entity = usuario();

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(usuarioRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        UsuarioResponseDTO result =
                usuarioService.update(1L, usuarioDTO());

        assertNotNull(result);
    }

    @Test
    void delete_notFound() {

        when(usuarioRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> usuarioService.delete(1L)
        );
    }

    private Usuario crearUsuario() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Angel");
        u.setNombreUsuario("angel");
        u.setCorreo("angel@test.com");
        u.setPassword("oldPassword");
        u.setSaldo(100.0);
        u.setEstrellas(4.5);
        return u;
    }

    @Test
    void changePasswordCorrecto() {

        Usuario usuario = crearUsuario();

        ChangePasswordRequest request =
                new ChangePasswordRequest("actual", "nueva");

        when(usuarioRepository.findByNombreUsuario("angel"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("actual", "oldPassword"))
                .thenReturn(true);

        when(passwordEncoder.encode("nueva"))
                .thenReturn("encodedPassword");

        usuarioService.changePassword("angel", request);

        assertEquals("encodedPassword", usuario.getPassword());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void changePasswordIncorrecta() {

        Usuario usuario = crearUsuario();

        ChangePasswordRequest request =
                new ChangePasswordRequest("incorrecta", "nueva");

        when(usuarioRepository.findByNombreUsuario("angel"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("incorrecta", "oldPassword"))
                .thenReturn(false);

        assertThrows(
                GSBadRequestException.class,
                () -> usuarioService.changePassword("angel", request)
        );

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void addSaldo() {

        Usuario usuario = crearUsuario();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        ResponseEntity<Double> response =
                usuarioService.addSaldo(50.0);

        assertEquals(150.0, response.getBody());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void getPerfilPublico() {

        Usuario usuario = crearUsuario();

        when(usuarioRepository.findByNombreUsuarioIgnoreCase("angel"))
                .thenReturn(Optional.of(usuario));

        PerfilPublicoDTO dto =
                usuarioService.getPerfilPublico("angel");

        assertNotNull(dto);
        assertEquals("Angel", dto.getNombre());
        assertEquals("angel", dto.getNombreUsuario());
        assertEquals(4.5, dto.getEstrellas());
    }

    @Test
    void findVentasByUsuario() {

        PostVentaResponseDTO venta = PostVentaResponseDTO.builder()
                .id(10L)
                .idVendedor(1L)
                .idProducto(100L)
                .nombreProducto("FIFA")
                .plataforma("PS5")
                .precio(30.0)
                .nombreUsuario("angel")
                .descripcion("desc")
                .build();

        ProductoResponseDTO producto = ProductoResponseDTO.builder()
                .id(100L)
                .idAPI(500)
                .estado(EstadoProducto.NUEVO)
                .build();

        Usuario usuarioEntity = crearUsuario();

        when(usuarioRepository.findByNombreUsuario("angel"))
                .thenReturn(Optional.of(usuarioEntity));

        when(postVentaService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(venta));

        when(productoService.findById(100L))
                .thenReturn(producto);

        List<PostBusquedaDTO> resultado =
                usuarioService.findVentasByUsuario("angel");

        assertEquals(1, resultado.size());
        assertEquals("VENTA", resultado.getFirst().getTipo());
    }

    @Test
    void findIntercambiosByUsuario() {

        Usuario usuarioEntity = crearUsuario();

        PostIntercambioResponseDTO intercambio =
                PostIntercambioResponseDTO.builder()
                        .id(20L)
                        .idUsuario(1L)
                        .idProducto(100L)
                        .idProductoIntercambio(200L)
                        .nombreProducto("FIFA")
                        .nombreProductoIntercambio("COD")
                        .plataforma("PS5")
                        .plataformaIntercambio("XBOX")
                        .nombreUsuario("angel")
                        .descripcion("desc")
                        .build();

        ProductoResponseDTO producto1 =
                ProductoResponseDTO.builder()
                        .id(100L)
                        .idAPI(500)
                        .estado(EstadoProducto.NUEVO)
                        .build();

        ProductoResponseDTO producto2 =
                ProductoResponseDTO.builder()
                        .id(200L)
                        .idAPI(600)
                        .estado(EstadoProducto.USADO)
                        .build();

        when(usuarioRepository.findByNombreUsuario("angel"))
                .thenReturn(Optional.of(usuarioEntity));

        when(postIntercambioService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(intercambio));

        when(productoService.findById(100L))
                .thenReturn(producto1);

        when(productoService.findById(200L))
                .thenReturn(producto2);

        List<PostBusquedaDTO> resultado =
                usuarioService.findIntercambiosByUsuario("angel");

        assertEquals(1, resultado.size());
        assertEquals("INTERCAMBIO", resultado.getFirst().getTipo());
    }

}
