package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.CarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.impl.CarritoServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PostVentaRepository postVentaRepository;
    @Mock
    private ProductoCarritoRepository productoCarritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Usuario usuario;
    private Carrito carrito;
    private PostVenta postVenta;

    @BeforeEach
    void setUp() {

        usuario = Usuario.builder().id(1L).nombre("Angel").build();

        carrito = Carrito.builder()
                .id(10L)
                .usuario(usuario)
                .coste(0.0)
                .productos(List.of())
                .build();

        postVenta = PostVenta.builder()
                .id(100L)
                .precio(30.0)
                .build();
    }

    @Test
    @DisplayName("Debe crear un carrito vacío para un usuario")
    void create_Success() {

        CarritoRequestDTO dto = new CarritoRequestDTO(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO result = carritoService.create(dto);

        assertNotNull(result);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    @DisplayName("Debe añadir un producto al carrito y actualizar el coste")
    void addProduct_Success() {

        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(carrito));
        when(postVentaRepository.findById(100L)).thenReturn(Optional.of(postVenta));
        when(productoCarritoRepository.existsByCarritoIdAndPostVentaId(10L, 100L)).thenReturn(false);

        CarritoResponseDTO result = carritoService.addProduct(100L, 1L);

        assertNotNull(result);
        assertEquals(30.0, carrito.getCoste());
        verify(productoCarritoRepository).save(any(ProductoCarrito.class));
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el producto ya está en el carrito")
    void addProduct_ThrowsBadRequest_WhenAlreadyInCart() {

        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(carrito));
        when(postVentaRepository.findById(100L)).thenReturn(Optional.of(postVenta));
        when(productoCarritoRepository.existsByCarritoIdAndPostVentaId(10L, 100L)).thenReturn(true);

        assertThrows(GSBadRequestException.class, () ->
                carritoService.addProduct(100L, 1L)
        );
        verify(carritoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un producto y restar su precio del coste total")
    void removeProduct_Success() {

        carrito.setCoste(50.0);
        ProductoCarrito pc = ProductoCarrito.builder()
                .id(500L)
                .carrito(carrito)
                .postVenta(postVenta)
                .build();

        when(productoCarritoRepository.findById(500L)).thenReturn(Optional.of(pc));

        CarritoResponseDTO result = carritoService.removeProduct(500L);

        assertNotNull(result);
        assertEquals(20.0, carrito.getCoste()); // 50.0 - 30.0
        verify(productoCarritoRepository).delete(pc);
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("El coste del carrito nunca debe ser inferior a 0")
    void removeProduct_CostNotNegative() {

        carrito.setCoste(10.0);
        ProductoCarrito pc = ProductoCarrito.builder()
                .id(500L)
                .carrito(carrito)
                .postVenta(postVenta)
                .build();

        when(productoCarritoRepository.findById(500L)).thenReturn(Optional.of(pc));

        carritoService.removeProduct(500L);

        assertEquals(0.0, carrito.getCoste());
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si el carrito no existe para el usuario")
    void findByUser_ThrowsNotFound() {

        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> carritoService.findByUser(1L));
    }
}