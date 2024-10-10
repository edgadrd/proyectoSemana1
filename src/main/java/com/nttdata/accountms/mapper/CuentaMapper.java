package com.nttdata.accountms.mapper;

import com.nttdata.accountms.domain.Cuenta;
import com.nttdata.accountms.dto.CuentaDto;



//@Mapper(componentModel = "spring")
public interface CuentaMapper {

    Cuenta cuentaDtoToCuenta(CuentaDto cuentaDto);

    CuentaDto cuentaToCuentaDto(Cuenta cuenta);
}
