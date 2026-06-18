package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.service.impl.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaccion")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping("/comprar/{idPostVenta}")
    public Double comprar(@PathVariable Long idPostVenta) {
        return transaccionService.comprar(idPostVenta);
    }

    @PostMapping("/intercambiar/{idPostIntercambio}")
    public void intercambiar(@PathVariable Long idPostIntercambio) {
        transaccionService.intercambiar(idPostIntercambio);
    }

}