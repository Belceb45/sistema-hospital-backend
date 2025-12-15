package org.hospital.backendpacientes.services;

import org.hospital.backendpacientes.model.Cita;
import org.hospital.backendpacientes.repository.CitasRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class CitasService {

    @Autowired
    private CitasRepo citasRepo;

    private static final LocalTime HORA_INICIO = LocalTime.of(10, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(17, 0);
    private static final int DURACION_MIN = 90;

    public int generarCitasDesdeExistentes() {

        int totalCreadas = 0;

        List<Object[]> datos = citasRepo.findUltimaFechaPorDoctor();

        for (Object[] fila : datos) {

            UUID doctorId = (UUID) fila[0];
            LocalDate ultimaFecha = (LocalDate) fila[1];

            LocalDate inicio = ultimaFecha.plusDays(1);
            LocalDate fin = ultimaFecha.plusMonths(2);

            totalCreadas += generarCitasDoctor(doctorId, inicio, fin);
        }

        return totalCreadas;
    }

    private int generarCitasDoctor(
            UUID doctorId,
            LocalDate inicio,
            LocalDate fin
    ) {
        int creadas = 0;

        for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {

            LocalTime hora = HORA_INICIO;

            while (hora.plusMinutes(DURACION_MIN).isBefore(HORA_FIN.plusSeconds(1))) {

                if (!citasRepo.existsByDoctorIdAndFechaAndHora(
                        doctorId, fecha, hora)) {

                    Cita cita = new Cita();
                    cita.setDoctor(doctorId);
                    cita.setFecha(fecha);
                    cita.setHora(hora);
                    cita.setEstado("DISPONIBLE");

                    citasRepo.save(cita);
                    creadas++;
                }

                hora = hora.plusMinutes(DURACION_MIN);
            }
        }

        return creadas;
    }
}
