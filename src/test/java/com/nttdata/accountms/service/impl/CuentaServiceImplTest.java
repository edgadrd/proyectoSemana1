package com.nttdata.accountms.service.impl;

import com.nttdata.accountms.client.feign.IFeignCliente;
import com.nttdata.accountms.domain.Cuenta;
import com.nttdata.accountms.domain.TipoCuenta;
import com.nttdata.accountms.dto.ClienteDto;
import com.nttdata.accountms.dto.CuentaDto;
import com.nttdata.accountms.mapper.CuentaMapper;
import com.nttdata.accountms.mapper.impl.CuentaMapperImpl;
import com.nttdata.accountms.repository.ICuentaRepository;
import com.nttdata.accountms.util.ClienteJsonReader;
import com.nttdata.accountms.util.CuentaJsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    @Mock
    private ICuentaRepository cuentaRepository;

    @Mock
    private CuentaMapper cuentaMapper;

    @Mock
    private IFeignCliente feignCliente;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private List<CuentaDto> cuentaDtoList;

    private List<Cuenta> cuentaEntityList;

    private List<ClienteDto> clienteDtoList;

    @BeforeEach
    void setUp() throws IOException {
        cuentaMapper = new CuentaMapperImpl();
        cuentaService = new CuentaServiceImpl(cuentaRepository, cuentaMapper, feignCliente);
        cuentaDtoList =       CuentaJsonReader.readCuentaDtoFromJson("src/test/java/com/nttdata/accountms/resources/CuentaDto.json");
        cuentaEntityList = CuentaJsonReader.readCuentaEntityFromJson("src/test/java/com/nttdata/accountms/resources/CuentaEntity.json");
        clienteDtoList =   ClienteJsonReader.readClientesDtoFromJson("src/test/java/com/nttdata/accountms/resources/ClienteDto.json");
    }

    @Test
    void createAccount_Success() {
        
        when(feignCliente.obtenerCliente(anyLong())).thenReturn(clienteDtoList.get(0));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaEntityList.get(0));

        CuentaDto result = cuentaService.createAccount(cuentaDtoList.get(0));

        assertNotNull(result);

    }

    @Test
    void createAccount_ClientNotFound() {
        
        when(feignCliente.obtenerCliente(anyLong())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.createAccount(cuentaDtoList.get(0));
        });

        assertEquals("Cliente no existe", exception.getMessage());
    }

    @Test
    void createAccount_InvalidInitialBalance() {
        
        when(feignCliente.obtenerCliente(anyLong())).thenReturn(clienteDtoList.get(0));

        cuentaDtoList.get(0).setSaldo(0.0); // saldo inicial 0
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.createAccount(cuentaDtoList.get(0));
        });

        assertEquals("El saldo inicial debe ser mayor a 0", exception.getMessage());
    }

    @Test
    void findAllAccount() {
        
        when(cuentaRepository.findAll()).thenReturn(cuentaEntityList);

        List<CuentaDto> result = cuentaService.findAllAccount();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAccountById_Success() {
        
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntityList.get(0)));
        //when(cuentaMapper.cuentaToCuentaDto(cuentaEntityList.get(0))).thenReturn(cuentaDtoList.get(0));

        CuentaDto result = cuentaService.getAccountById(1L);

        assertNotNull(result);
    }

    @Test
    void getAccountById_NotFound() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.getAccountById(1L);
        });

        assertEquals("Cuenta no existente", exception.getMessage());
    }

    @Test
    void depositAccount_Success() {
        
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntityList.get(0)));
        when(cuentaRepository.save(cuentaEntityList.get(0))).thenReturn(cuentaEntityList.get(0));

        CuentaDto result = cuentaService.depositAccount(1L, 50.0);

        assertNotNull(result);
        assertEquals(1050.0, result.getSaldo());
    }

    @Test
    void withdrawAccount_Success() {
        
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntityList.get(0)));
        when(cuentaRepository.save(cuentaEntityList.get(0))).thenReturn(cuentaEntityList.get(0));
        

        CuentaDto result = cuentaService.withdrawAccount(1L, 50.0);

        assertNotNull(result);
        assertEquals(950.0, result.getSaldo());
    }

    @Test
    void withdrawAccount_InsufficientFunds() {
        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(100.0);
        cuenta.setTipoCuenta(TipoCuenta.AHORROS);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.withdrawAccount(1L, 150.0);
        });

        assertEquals("RETIRO NO PERMITIDO", exception.getMessage());
    }

    @Test
    void deleteAccount_Success() {
        when(cuentaRepository.existsById(1L)).thenReturn(true);

        cuentaService.deleteAccount(1L);

        verify(cuentaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAccount_NotFound() {
        when(cuentaRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.deleteAccount(1L);
        });

        assertEquals("Cuenta no encontrada", exception.getMessage());
    }

    @Test
    void clientWhitAccount() {
        when(cuentaRepository.existsByClienteId(1L)).thenReturn(true);

        boolean result = cuentaService.clientWhitAccount(1L);

        assertTrue(result);
    }

    @Test
    void existsById() {
        when(cuentaRepository.existsById(1L)).thenReturn(true);

        boolean result = cuentaService.existsById(1L);

        assertTrue(result);
    }
}