package org.hospital.backendpacientes.controller;

import org.hospital.backendpacientes.model.Notificacion;
import org.hospital.backendpacientes.repository.NotificacionRepo;
import org.hospital.backendpacientes.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    @Autowired
    private NotificacionRepo notificacionRepo;

    @Autowired
    private UserRepo userRepo;

    // OBTENER NOTIFICACIONES DE UN PACIENTE
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Notificacion>> obtenerPorPaciente(@PathVariable UUID id) {
        return ResponseEntity.ok(notificacionRepo.findByPacienteIdOrderByFechaDesc(id));
    }

    // ENVIAR NOTIFICACIÓN
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarNotificacion(@RequestParam UUID pacienteId, @RequestParam String mensaje) {
        if (!userRepo.existsById(pacienteId)) {
            return ResponseEntity.badRequest().body("Paciente no encontrado");
        }

        Notificacion noti = new Notificacion(pacienteId, mensaje);
        notificacionRepo.save(noti);

        return ResponseEntity.ok("Notificación enviada");
    }

    // MARCAR COMO LEÍDAS
    @PutMapping("/leer/{id}")
    public ResponseEntity<?> marcarLeida(@PathVariable UUID id) {
        return notificacionRepo.findById(id).map(noti -> {
            noti.setLeida(true);
            notificacionRepo.save(noti);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}