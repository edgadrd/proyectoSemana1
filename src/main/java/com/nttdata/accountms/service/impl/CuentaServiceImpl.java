package com.nttdata.accountms.service.impl;

import com.nttdata.accountms.client.feign.IFeignCliente;
import com.nttdata.accountms.domain.Cuenta;
import com.nttdata.accountms.domain.TipoCuenta;
import com.nttdata.accountms.dto.ClienteDto;
import com.nttdata.accountms.dto.CuentaDto;
import com.nttdata.accountms.mapper.CuentaMapper;
import com.nttdata.accountms.repository.ICuentaRepository;
import com.nttdata.accountms.service.ICuentaService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CuentaServiceImpl implements ICuentaService {

    private final ICuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;
    private final IFeignCliente feignCliente;

    @Override
    public CuentaDto createAccount(CuentaDto cuentaDto) {
        try {
            ClienteDto clienteDto = feignCliente.obtenerCliente(cuentaDto.getClienteId());
            if (clienteDto == null) {
                throw new RuntimeException("Cliente no existe");
            }
            if (cuentaDto.getSaldo()<= 0) {
                throw new IllegalArgumentException("El saldo inicial debe ser mayor a 0");
            }
            Cuenta nuevaCuenta = new Cuenta();
            nuevaCuenta.setNumeroCuenta(generarNumeroCuenta());
            nuevaCuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
            nuevaCuenta.setSaldo(cuentaDto.getSaldo());
            nuevaCuenta.setSaldo(cuentaDto.getSaldo());
            nuevaCuenta.setClienteId(cuentaDto.getClienteId());
            return cuentaMapper.cuentaToCuentaDto(cuentaRepository.save(nuevaCuenta));

        }catch (FeignException e){
            throw new RuntimeException("Error al comunicarse con el servicio de cuentas: " + e.getMessage());
        }

    }

    @Override
    public List<CuentaDto> findAllAccount() {
        return cuentaRepository.findAll().stream()
                .map(cuentaMapper::cuentaToCuentaDto)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDto getAccountById(Long id) {
        return cuentaMapper.cuentaToCuentaDto(this.cuentaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cuenta no existente")
        ));
    }

    @Override
    public CuentaDto depositAccount( Long id, Double amount) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        cuenta.setSaldo(cuenta.getSaldo() + amount);
       return cuentaMapper.cuentaToCuentaDto(cuentaRepository.save(cuenta));
    }

    @Override
    public CuentaDto withdrawAccount(Long id , Double amount) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS && cuenta.getSaldo() - amount < 0) {
            throw new IllegalArgumentException("RETIRO NO PERMITIDO");
        }

        if (cuenta.getTipoCuenta() == TipoCuenta.CORRIENTE && cuenta.getSaldo() - amount < -500) {
            throw new IllegalArgumentException("RETIRO NO PERMITIDO");
        }

        cuenta.setSaldo(cuenta.getSaldo() - amount);
        return cuentaMapper.cuentaToCuentaDto(cuentaRepository.save(cuenta));
    }

    @Override
    public void deleteAccount(Long id) {
        boolean exists = cuentaRepository.existsById(id);
        if (!exists) {
            throw new RuntimeException("Cuenta no encontrada");
        }
        cuentaRepository.deleteById(id);

    }

    @Override
    public boolean clientWhitAccount(Long clienteId) {
        return cuentaRepository.existsByClienteId(clienteId);
    }

    @Override
    public boolean existsById(Long id) {
        return cuentaRepository.existsById(id);
    }


    private String generarNumeroCuenta() {
        Random random = new Random();
        long numeroCuenta = 1000000000000000L + (long) (random.nextDouble() * 9000000000000000L);
        return String.valueOf(numeroCuenta);
    }


}
