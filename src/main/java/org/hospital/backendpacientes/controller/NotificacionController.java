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

    // 1. OBTENER NOTIFICACIONES DE UN PACIENTE
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Notificacion>> obtenerPorPaciente(@PathVariable UUID id) {
        return ResponseEntity.ok(notificacionRepo.findByPacienteIdOrderByFechaDesc(id));
    }

    // 2. ENVIAR NOTIFICACIÓN (Para uso del sistema o admin)
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarNotificacion(@RequestParam UUID pacienteId, @RequestParam String mensaje) {
        if (!userRepo.existsById(pacienteId)) {
            return ResponseEntity.badRequest().body("Paciente no encontrado");
        }

        Notificacion noti = new Notificacion(pacienteId, mensaje);
        notificacionRepo.save(noti);

        return ResponseEntity.ok("Notificación enviada");
    }

    // 3. MARCAR COMO LEÍDAS (Opcional, para limpiar el icono rojo)
    @PutMapping("/leer/{id}")
    public ResponseEntity<?> marcarLeida(@PathVariable UUID id) {
        return notificacionRepo.findById(id).map(noti -> {
            noti.setLeida(true);
            notificacionRepo.save(noti);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}