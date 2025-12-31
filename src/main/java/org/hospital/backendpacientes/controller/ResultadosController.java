package org.hospital.backendpacientes.controller;

import org.hospital.backendpacientes.model.ResultadosLaboratorio;
import org.hospital.backendpacientes.repository.ResultadosRepo;
import org.hospital.backendpacientes.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resultados")
@CrossOrigin(origins = "*")
public class ResultadosController {

    @Autowired
    private ResultadosRepo resultadoRepo;
    @Autowired
    private UserRepo userRepo;
    // OBTENER RESULTADOS DE UN PACIENTE
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ResultadosLaboratorio>> obtenerPorPaciente(@PathVariable UUID id) {
        return ResponseEntity.ok(resultadoRepo.findByPacienteIdOrderByFechaRealizacionDesc(id));
    }

    // CARGAR RESULTADO
    @PostMapping("/cargar")
    public ResponseEntity<?> cargarResultado(@RequestBody ResultadosLaboratorio resultado) {

        try {

            if (!userRepo.existsById(resultado.getPacienteId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: El paciente no existe.");
            }


            ResultadosLaboratorio nuevoResultado = resultadoRepo.save(resultado);


            return new ResponseEntity<>(nuevoResultado, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al cargar el estudio: " + e.getMessage());
        }
    }
}