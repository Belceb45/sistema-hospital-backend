package org.hospital.backendpacientes.controller;

import org.hospital.backendpacientes.model.User;
import org.hospital.backendpacientes.register.RegisterRequest;
import org.hospital.backendpacientes.register.UpdateRequest;
import org.hospital.backendpacientes.repository.UserRepo;
import org.hospital.backendpacientes.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    // ==========================================
    // MÉTODO AUXILIAR: GENERAR EXPEDIENTE ÚNICO
    // ==========================================
    private String generarExpedienteUnico() {
        String expediente;
        boolean existe;

        do {
            int anio = Year.now().getValue();
            int randomNum = new Random().nextInt(90000) + 10000;
            expediente = anio + "-" + randomNum;
            existe = userRepo.findByNumExpediente(expediente).isPresent();
        } while (existe);

        return expediente;
    }

    // ============================
    // REGISTRO
    // ============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if ((request.getNumExpediente() == null || request.getNumExpediente().isEmpty()) &&
                (request.getCurp() == null || request.getCurp().isEmpty())) {
            return ResponseEntity.badRequest().body("Debes enviar expediente (si ya tienes) O CURP (para nuevo registro).");
        }

        if (request.getNumExpediente() != null && !request.getNumExpediente().isEmpty() &&
                request.getCurp() != null && !request.getCurp().isEmpty()) {
            return ResponseEntity.badRequest().body("Solo envía expediente o CURP, no ambos.");
        }

        String numExpedienteFinal = request.getNumExpediente();

        if (numExpedienteFinal == null || numExpedienteFinal.isEmpty()) {
            numExpedienteFinal = generarExpedienteUnico();
        } else {
            if (userRepo.findByNumExpediente(numExpedienteFinal).isPresent()) {
                return ResponseEntity.badRequest().body("El expediente proporcionado ya está registrado.");
            }
        }

        if (request.getCurp() != null && !request.getCurp().isEmpty() &&
                userRepo.findByCurp(request.getCurp()).isPresent()) {
            return ResponseEntity.badRequest().body("La CURP ya está registrada.");
        }

        if (request.getNombreCompleto() != null &&
                userRepo.findByNombreCompleto(request.getNombreCompleto()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya está registrado.");
        }

        List<UUID> idsDeDoctores = doctorRepo.findAllDoctorIds();

        if (idsDeDoctores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No hay doctores registrados en el sistema.");
        }

        UUID doctorAsignado = idsDeDoctores.get(new Random().nextInt(idsDeDoctores.size()));

        User user = new User();
        user.setNombreCompleto(request.getNombreCompleto());
        user.setCurp(request.getCurp());
        user.setTelefono(request.getTelefono());
        user.setTelefono2(request.getTelefono2());
        user.setTelefono3(request.getTelefono3());
        user.setRFC(request.getRfc());
        user.setDireccion(request.getDireccion());
        user.setFechaNacimiento(request.getFechaNacimiento());
        user.setNumExpediente(numExpedienteFinal);
        user.setDoctorId(doctorAsignado);

        userRepo.save(user);

        return ResponseEntity.ok(user);
    }

    // ============================
    // LOGIN (CORREGIDO: BÚSQUEDA INTELIGENTE)
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest loginRequest) {

        String identificador = null;

        // 1. Extraer el dato, venga donde venga (CURP o Expediente)
        if (loginRequest.getCurp() != null && !loginRequest.getCurp().isEmpty()) {
            identificador = loginRequest.getCurp();
        } else if (loginRequest.getNumExpediente() != null && !loginRequest.getNumExpediente().isEmpty()) {
            identificador = loginRequest.getNumExpediente();
        } else {
            return ResponseEntity.badRequest().body("Debes enviar CURP o número de expediente");
        }

        // 2. Intentar buscar por CURP primero
        Optional<User> userOpt = userRepo.findByCurp(identificador);

        // 3. Si no se encontró por CURP, intentar buscar por Expediente
        if (userOpt.isEmpty()) {
            userOpt = userRepo.findByNumExpediente(identificador);
        }

        // 4. Verificar resultado final
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado (Verifique sus credenciales)");
        }

        return ResponseEntity.ok(userOpt.get());
    }


    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateRequest request
    ) {

        Optional<User> userOpt = userRepo.findById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        User user = userOpt.get();

        if (request.getTelefono() != null) user.setTelefono(request.getTelefono());
        if (request.getTelefono2() != null) user.setTelefono2(request.getTelefono2());
        if (request.getTelefono3() != null) user.setTelefono3(request.getTelefono3());
        if (request.getDireccion() != null) user.setDireccion(request.getDireccion());
        if (request.getFechaNacimiento() != null) user.setFechaNacimiento(request.getFechaNacimiento());
        if (request.getNumExpediente() != null) user.setNumExpediente(request.getNumExpediente());

        userRepo.save(user);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/actualizar/doctor/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id){

        Optional<User> userOpt = userRepo.findById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        User user = userOpt.get();

        List<UUID> idsDeDoctores = doctorRepo.findAllDoctorIds();
        List<UUID> idsDeDoctores2 = idsDeDoctores.stream()
                .filter(idN->!idN.equals(user.getDoctorId()))
                .toList();

        if (idsDeDoctores2.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No hay mas doctores disponibles");
        }

        UUID doctorAsignado = idsDeDoctores2.get(new Random().nextInt(idsDeDoctores2.size()));

        user.setDoctorId(doctorAsignado);
        userRepo.save(user);

        return ResponseEntity.ok(user);
    }
}