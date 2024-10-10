package com.nttdata.accountms.controller;

import com.nttdata.accountms.dto.CuentaDto;
import com.nttdata.accountms.service.ICuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("cuenta")
public class CuentaController{

    private final ICuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaDto>> listarCuentass() {
        return new ResponseEntity<>(cuentaService.findAllAccount(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuentaDto> obtenerCuenta(@PathVariable ("id") Long id) {
        return new ResponseEntity<>(cuentaService.getAccountById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CuentaDto> crearCuenta(@RequestBody CuentaDto cuentaDto) {
        return new ResponseEntity<>(cuentaService.createAccount(cuentaDto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}/depositar")
    public ResponseEntity<CuentaDto> depositar(@PathVariable ("id") Long id ,@RequestParam Double monto) {
        return new ResponseEntity<>(cuentaService.depositAccount(id , monto), HttpStatus.OK);
    }

    @PutMapping("/{id}/retirar")
    public ResponseEntity<CuentaDto> retirar(@PathVariable ("id") Long id ,@RequestParam Double monto) {
        return new ResponseEntity<>(cuentaService.withdrawAccount(id , monto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {
        cuentaService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/clienteCuenta/{id}")
    public ResponseEntity<Boolean> obtenerCliente(@PathVariable ("id") Long id) {
        return new ResponseEntity<>(cuentaService.clientWhitAccount(id), HttpStatus.OK);
    }

    @GetMapping(value = "/existeCuenta/{id}")
    public ResponseEntity<Boolean> existeCuenta(@PathVariable ("id") Long id) {
        return new ResponseEntity<>(cuentaService.existsById(id), HttpStatus.OK);
    }

}
