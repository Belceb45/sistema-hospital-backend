package org.hospital.backendpacientes.controller;

import org.hospital.backendpacientes.model.Doctor;
import org.hospital.backendpacientes.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctores")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorRepo doctorRepo;

    @GetMapping
    public List<Doctor> listarDoctores() {
        return doctorRepo.findAll();
    }

    @GetMapping("/{id}")
    public Doctor obtenerDoctor(@PathVariable UUID id) {
        return doctorRepo.findById(id).orElse(null);
    }
}
