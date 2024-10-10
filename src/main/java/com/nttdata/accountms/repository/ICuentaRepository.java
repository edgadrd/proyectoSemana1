package com.nttdata.accountms.repository;

import com.nttdata.accountms.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICuentaRepository extends JpaRepository<Cuenta, Long> {

    boolean existsByClienteId(Long clienteId);
}
