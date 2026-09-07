package cl.duoc.pedidos360.carrito.controller;

import cl.duoc.pedidos360.carrito.dto.CarritoResponse;
import cl.duoc.pedidos360.carrito.service.CarritoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Slice test de la capa web: los filtros de seguridad (JWT, scopes) se
// desactivan aqui a proposito para probar solo la logica del controller.
@WebMvcTest(CarritoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;

    @Test
    void obtenerCarritoActual_debeRetornar200() throws Exception {
        CarritoResponse response = CarritoResponse.builder()
                .id(1L)
                .usuarioId("usuario-demo")
                .estado("ABIERTO")
                .items(List.of())
                .total(BigDecimal.ZERO)
                .build();

        when(carritoService.obtenerCarritoActual(any())).thenReturn(response);

        mockMvc.perform(get("/api/carrito"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ABIERTO"));
    }
}
