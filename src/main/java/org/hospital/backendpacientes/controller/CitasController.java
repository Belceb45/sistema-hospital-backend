package org.hospital.backendpacientes.controller;

import org.hospital.backendpacientes.model.Cita;
import org.hospital.backendpacientes.model.Doctor;
import org.hospital.backendpacientes.model.User;
import org.hospital.backendpacientes.repository.CitasRepo;
import org.hospital.backendpacientes.repository.DoctorRepo;
import org.hospital.backendpacientes.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitasController {

    @Autowired
    private CitasRepo citasRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    // ---------------------------------------------------------
    // 1. OBTENER CITAS DISPONIBLES
    @GetMapping("/disponibles")
    public ResponseEntity<?> citasDisponibles(@RequestParam UUID pacienteId) {


        User paciente = userRepo.findById(pacienteId).orElse(null);

        if (paciente == null) {
            return ResponseEntity.badRequest().body("Paciente no encontrado");
        }

        if (paciente.getDoctorId() == null) {
            return ResponseEntity.badRequest().body("El paciente no tiene un doctor asignado. Vaya a 'Perfil' o 'Cambiar Doctor'.");
        }

        UUID doctorId = paciente.getDoctorId();


        List<Cita> disponibles = citasRepo.findByEstadoAndDoctorId("disponible", doctorId);

        return ResponseEntity.ok(disponibles);
    }

    // ---------------------------------------------------------
    // 2. OBTENER HISTORIAL COMPLETO
    // ---------------------------------------------------------
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Cita>> citasPorPaciente(@PathVariable UUID pacienteId) {

        List<Cita> citas = citasRepo.findByPacienteId(pacienteId);
        return ResponseEntity.ok(citas);
    }

    // ---------------------------------------------------------
    // 3. AGENDAR CITA
    // ---------------------------------------------------------
    @PostMapping("/agendar/{idCita}")
    public ResponseEntity<?> agendarCita(
            @PathVariable UUID idCita,
            @RequestParam UUID pacienteId
    ) {
        Optional<Cita> citaOpt = citasRepo.findById(idCita);

        if (citaOpt.isEmpty()) return ResponseEntity.badRequest().body("La cita no existe.");

        Cita cita = citaOpt.get();

        if (!cita.getEstado().equalsIgnoreCase("disponible"))
            return ResponseEntity.badRequest().body("La cita ya no está disponible.");

        cita.setPacienteId(pacienteId);
        cita.setEstado("agendada");

        citasRepo.save(cita);

        return ResponseEntity.ok().body("{\"message\": \"Cita agendada correctamente\"}");
    }

    // ---------------------------------------------------------
    // 4. CANCELAR CITA
    // ---------------------------------------------------------
    @PostMapping("/cancelar/{idCita}")
    public ResponseEntity<?> cancelarCita(@PathVariable UUID idCita) {

        Optional<Cita> citaOpt = citasRepo.findById(idCita);
        if (citaOpt.isEmpty()) return ResponseEntity.badRequest().body("La cita no existe.");

        Cita cita = citaOpt.get();


        cita.setEstado("disponible");
        cita.setPacienteId(null);

        citasRepo.save(cita);

        return ResponseEntity.ok().body("{\"message\": \"Cita cancelada correctamente\"}");
    }

    // ---------------------------------------------------------
    // 5. REAGENDAR CITA
    // ---------------------------------------------------------
    @PostMapping("/reagendar")
    public ResponseEntity<?> reagendarCita(
            @RequestParam UUID idCitaActual,
            @RequestParam UUID idNuevaCita,
            @RequestParam UUID pacienteId
    ) {

        Optional<Cita> citaActualOpt = citasRepo.findById(idCitaActual);
        Optional<Cita> nuevaCitaOpt = citasRepo.findById(idNuevaCita);

        if (citaActualOpt.isEmpty() || nuevaCitaOpt.isEmpty())
            return ResponseEntity.badRequest().body("Una de las citas no existe.");

        Cita citaActual = citaActualOpt.get();
        Cita nuevaCita = nuevaCitaOpt.get();

        // Validaciones
        if (!citaActual.getPacienteId().equals(pacienteId))
            return ResponseEntity.badRequest().body("La cita actual no pertenece a este paciente.");

        if (!nuevaCita.getEstado().equalsIgnoreCase("disponible"))
            return ResponseEntity.badRequest().body("La nueva cita NO está disponible.");

        // Liberar la cita actual
        citaActual.setEstado("disponible");
        citaActual.setPacienteId(null);

        // Ocupar la nueva cita
        nuevaCita.setEstado("agendada");
        nuevaCita.setPacienteId(pacienteId);

        citasRepo.save(citaActual);
        citasRepo.save(nuevaCita);

        return ResponseEntity.ok().body("{\"message\": \"Cita reagendada correctamente\"}");
    }

    // ---------------------------------------------------------
    // 6. GENERAR CITAS AUTOMÁTICAS (Utilidad)
    // ---------------------------------------------------------
    @PostMapping("/generar")
    public ResponseEntity<?> generarCitas() {

        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusMonths(2);

        // Asegúrate de tener este método en tu CitasRepo
        List<Object[]> ultimasFechas = citasRepo.findUltimaFechaPorDoctor();

        for (Object[] fila : ultimasFechas) {
            UUID doctorId = (UUID) fila[0];
            java.sql.Date sqlDate = (java.sql.Date) fila[1]; // PA devuelve sql.Date
            LocalDate fechaInicio = (sqlDate != null) ? sqlDate.toLocalDate() : hoy;

            LocalDate fecha = fechaInicio.plusDays(1);

            while (!fecha.isAfter(limite)) {
                LocalTime hora = LocalTime.of(10, 0);

                while (hora.isBefore(LocalTime.of(17, 0))) {
                    boolean existe = citasRepo.existsByDoctorIdAndFechaAndHora(doctorId, fecha, hora);

                    if (!existe) {
                        Cita nueva = new Cita();
                        nueva.setDoctor(doctorId);
                        nueva.setFecha(fecha);
                        nueva.setHora(hora);
                        nueva.setEstado("disponible");
                        nueva.setPacienteId(null);
                        citasRepo.save(nueva);
                    }
                    hora = hora.plusMinutes(60);
                }
                fecha = fecha.plusDays(1);
            }
        }
        return ResponseEntity.ok().body("{\"message\": \"Horarios generados correctamente\"}");
    }

    // ---------------------------------------------------------
    // 7. GET INDIVIDUAL
    // ---------------------------------------------------------
    @GetMapping("/{citaId}")
    public ResponseEntity<Cita> getCitaDetalle(@PathVariable UUID citaId) {
        return citasRepo.findById(citaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------
    // 8. NUEVO: DERIVAR CON ESPECIALISTA (Generar cita automática)
    // ---------------------------------------------------------
    @PostMapping("/derivacion")
    public ResponseEntity<?> generarCitaEspecialista(
            @RequestParam UUID pacienteId,
            @RequestParam String especialidad
    ) {
        // Validar paciente
        if (!userRepo.existsById(pacienteId)) {
            return ResponseEntity.badRequest().body("Paciente no encontrado.");
        }

        // Buscar doctores de esa especialidad
        List<Doctor> especialistas = doctorRepo.findByEspecialidad(especialidad);

        if (especialistas.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay doctores registrados con la especialidad: " + especialidad);
        }

        // Buscar la primera cita disponible entre los especialistas encontrados
        Cita citaEncontrada = null;

        for (Doctor doc : especialistas) {
            // próxima cita libre
            Optional<Cita> posibleCita = citasRepo.findFirstByDoctorIdAndEstadoOrderByFechaAscHoraAsc(
                    doc.getId(), "disponible"
            );

            if (posibleCita.isPresent()) {
                citaEncontrada = posibleCita.get();
                break;
            }
        }

        if (citaEncontrada == null) {
            return ResponseEntity.badRequest().body("No hay horarios disponibles próximamente para " + especialidad);
        }

        // Asignar la cita al paciente
        citaEncontrada.setPacienteId(pacienteId);
        citaEncontrada.setEstado("agendada");

        citasRepo.save(citaEncontrada);

        return ResponseEntity.ok("Cita de especialidad (" + especialidad + ") generada exitosamente para el: " + citaEncontrada.getFecha() + " a las " + citaEncontrada.getHora());
    }
}