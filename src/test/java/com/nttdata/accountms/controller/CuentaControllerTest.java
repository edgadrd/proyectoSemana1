package com.nttdata.accountms.controller;

import com.nttdata.accountms.dto.CuentaDto;
import com.nttdata.accountms.service.ICuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

public class CuentaControllerTest {

    @Mock
    private ICuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cuentaController).build();
    }

    @Test
    public void testListarCuentass() throws Exception {
        List<CuentaDto> cuentaList = Arrays.asList(new CuentaDto(), new CuentaDto());
        when(cuentaService.findAllAccount()).thenReturn(cuentaList);

        mockMvc.perform(get("/cuenta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testObtenerCuenta() throws Exception {
        CuentaDto cuentaDto = new CuentaDto();
        when(cuentaService.getAccountById(anyLong())).thenReturn(cuentaDto);

        mockMvc.perform(get("/cuenta/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testCrearCuenta() throws Exception {
        CuentaDto cuentaDto = new CuentaDto();
        when(cuentaService.createAccount(any(CuentaDto.class))).thenReturn(cuentaDto);

        mockMvc.perform(post("/cuenta")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"field\":\"value\"}")) // Replace with actual JSON content
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testDepositar() throws Exception {
        CuentaDto cuentaDto = new CuentaDto();
        when(cuentaService.depositAccount(anyLong(), any(Double.class))).thenReturn(cuentaDto);

        mockMvc.perform(put("/cuenta/{id}/depositar", 1L)
                .param("monto", "100.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testRetirar() throws Exception {
        CuentaDto cuentaDto = new CuentaDto();
        when(cuentaService.withdrawAccount(anyLong(), any(Double.class))).thenReturn(cuentaDto);

        mockMvc.perform(put("/cuenta/{id}/retirar", 1L)
                .param("monto", "50.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testEliminarCuenta() throws Exception {
        mockMvc.perform(delete("/cuenta/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testObtenerCliente() throws Exception {
        when(cuentaService.clientWhitAccount(anyLong())).thenReturn(true);

        mockMvc.perform(get("/cuenta/clienteCuenta/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testExisteCuenta() throws Exception {
        when(cuentaService.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(get("/cuenta/existeCuenta/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
