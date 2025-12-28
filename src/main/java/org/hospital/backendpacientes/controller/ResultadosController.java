package org.hospital.backendpacientes.controller;

import org.hospital.backendpacientes.model.ResultadosLaboratorio;
import org.hospital.backendpacientes.repository.ResultadosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resultados")
@CrossOrigin(origins = "*")
public class ResultadosController {

    @Autowired
    private ResultadosRepo resultadoRepo;

    // 1. OBTENER RESULTADOS DE UN PACIENTE (Lectura)
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ResultadosLaboratorio>> obtenerPorPaciente(@PathVariable UUID id) {
        return ResponseEntity.ok(resultadoRepo.findByPacienteIdOrderByFechaRealizacionDesc(id));
    }

    // 2. CARGAR RESULTADO (Para uso de Administrativos/Laboratoristas - Pruebas)
    @PostMapping("/cargar")
    public ResponseEntity<?> cargarResultado(@RequestBody ResultadosLaboratorio resultado) {
        // Aquí podrías validar que el paciente exista
        resultadoRepo.save(resultado);
        return ResponseEntity.ok("Resultado cargado exitosamente");
    }
}