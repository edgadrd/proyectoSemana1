package com.nttdata.accountms.mapper.impl;

import com.nttdata.accountms.domain.Cuenta;
import com.nttdata.accountms.dto.CuentaDto;
import com.nttdata.accountms.mapper.CuentaMapper;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapperImpl implements CuentaMapper {

    @Override
    public Cuenta cuentaDtoToCuenta(CuentaDto cuentaDto) {
        return Cuenta.builder()
                .id(cuentaDto.getId())
                .numeroCuenta(cuentaDto.getNumeroCuenta())
                .saldo(cuentaDto.getSaldo())
                .tipoCuenta(cuentaDto.getTipoCuenta())
                .clienteId(cuentaDto.getClienteId())
                .build();
    }

    @Override
    public CuentaDto cuentaToCuentaDto(Cuenta cuenta) {
        return CuentaDto.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .saldo(cuenta.getSaldo())
                .tipoCuenta(cuenta.getTipoCuenta())
                .clienteId(cuenta.getClienteId())
                .build();
    }
}
