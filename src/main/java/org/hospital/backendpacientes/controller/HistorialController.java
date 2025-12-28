package org.hospital.backendpacientes.controller;

import org.hospital.backendpacientes.model.HistorialMedico;
import org.hospital.backendpacientes.repository.HistorialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/historial")
@CrossOrigin(origins = "*") // Permite conexión desde React
public class HistorialController {

    @Autowired
    private HistorialRepo historialRepo;

    // OBTENER MI HISTORIAL
    @GetMapping("/{pacienteId}")
    public ResponseEntity<?> obtenerHistorial(@PathVariable UUID pacienteId) {
        Optional<HistorialMedico> historial = historialRepo.findByPacienteId(pacienteId);

        if (historial.isPresent()) {
            return ResponseEntity.ok(historial.get());
        } else {
            // Retorna vacío si no tiene historial (el front lo manejará limpiando los campos)
            return ResponseEntity.noContent().build();
        }
    }

    // GUARDAR O ACTUALIZAR
    @PostMapping("/guardar")
    public ResponseEntity<?> guardarHistorial(@RequestBody HistorialMedico datosEntrantes) {

        // Buscamos si ya existe historial para este paciente
        Optional<HistorialMedico> existente = historialRepo.findByPacienteId(datosEntrantes.getPacienteId());

        if (existente.isPresent()) {
            // --- MODO ACTUALIZACIÓN ---
            HistorialMedico historial = existente.get();

            // Actualizamos campo por campo
            historial.setDiabetes(datosEntrantes.isDiabetes());
            historial.setHipertension(datosEntrantes.isHipertension());
            historial.setAsma(datosEntrantes.isAsma());
            historial.setEnfermedadesCardiacas(datosEntrantes.isEnfermedadesCardiacas());
            historial.setObesidad(datosEntrantes.isObesidad());

            historial.setAlergias(datosEntrantes.getAlergias());
            historial.setCirugiasPrevias(datosEntrantes.getCirugiasPrevias());
            historial.setAntecedentesFamiliares(datosEntrantes.getAntecedentesFamiliares());
            historial.setTipoSangre(datosEntrantes.getTipoSangre());

            historialRepo.save(historial);
            return ResponseEntity.ok(historial);

        } else {
            // --- MODO CREACIÓN (Primera vez) ---
            historialRepo.save(datosEntrantes);
            return ResponseEntity.ok(datosEntrantes);
        }
    }
}