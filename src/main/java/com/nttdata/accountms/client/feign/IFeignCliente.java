package com.nttdata.accountms.client.feign;

import com.nttdata.accountms.dto.ClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "feignClient" , url = "http://localhost:8080/clientes/clientes/")
public interface IFeignCliente {

    @GetMapping(value = "/{id}")
    ClienteDto obtenerCliente(@PathVariable("id") Long id);
}
