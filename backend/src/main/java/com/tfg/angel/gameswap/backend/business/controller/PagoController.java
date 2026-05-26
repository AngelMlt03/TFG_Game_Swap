package com.tfg.angel.gameswap.backend.business.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    @PostMapping("/checkout")
    public ResponseEntity<String> crearCheckout(@RequestParam int cantidad) throws GSBadRequestException, StripeException {

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()

                .setMode(SessionCreateParams.Mode.PAYMENT)

                .setSuccessUrl(frontendUrl + "/?pago=ok&cantidad=" + cantidad)

                .setCancelUrl(frontendUrl + "/saldo")

                .addLineItem(SessionCreateParams.LineItem.builder()

                        .setQuantity(1L)

                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()

                                .setCurrency("eur")

                                .setUnitAmount((long) cantidad * 100)

                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()

                                        .setName("Saldo GameSwap")

                                        .build())

                                .build())

                        .build())

                .build();

        Session session = Session.create(params);

        return ResponseEntity.ok(session.getUrl());
    }
}
