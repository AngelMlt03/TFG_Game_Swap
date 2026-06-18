package com.tfg.angel.gameswap.backend.controller;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tfg.angel.gameswap.backend.business.controller.PagoController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoControllerTest {

    @Test
    void crearCheckout() throws Exception {

        PagoController controller = new PagoController();

        ReflectionTestUtils.setField(
                controller,
                "stripeSecretKey",
                "sk_test_fake"
        );

        ReflectionTestUtils.setField(
                controller,
                "frontendUrl",
                "http://localhost:4200"
        );

        Session session = mock(Session.class);

        when(session.getUrl())
                .thenReturn("https://checkout.stripe.com/test");

        try (MockedStatic<Session> mocked =
                     mockStatic(Session.class)) {

            mocked.when(() ->
                            Session.create(any(SessionCreateParams.class)))
                    .thenReturn(session);

            ResponseEntity<String> response =
                    controller.crearCheckout(10);

            assertEquals(
                    "https://checkout.stripe.com/test",
                    response.getBody()
            );
        }
    }
}
