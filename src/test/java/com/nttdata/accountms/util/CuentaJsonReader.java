package com.nttdata.accountms.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.accountms.domain.Cuenta;
import com.nttdata.accountms.dto.CuentaDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CuentaJsonReader {
    public static List<Cuenta> readCuentaEntityFromJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), new TypeReference<List<Cuenta>>() {});
    }

    public static List<CuentaDto> readCuentaDtoFromJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), new TypeReference<List<CuentaDto>>() {});
    }
}
