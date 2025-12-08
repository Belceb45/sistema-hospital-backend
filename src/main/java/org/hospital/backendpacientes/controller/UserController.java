package org.hospital.backendpacientes.controller;
import org.hospital.backendpacientes.model.User;
import org.hospital.backendpacientes.register.RegisterRequest;
import org.hospital.backendpacientes.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepo UserRepo;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // Validar que solo envíe uno
        if ((request.getNumExpediente() == null || request.getNumExpediente().isEmpty()) &&
                (request.getCurp() == null || request.getCurp().isEmpty())) {

            return ResponseEntity.badRequest().body("Debes enviar expediente O CURP.");
        }

        if (request.getNumExpediente() != null && !request.getNumExpediente().isEmpty() &&
                request.getCurp() != null && !request.getCurp().isEmpty()) {

            return ResponseEntity.badRequest().body("Solo envía expediente o CURP, no ambos.");
        }

        // Validar duplicados por expediente
        if (request.getNumExpediente() != null && !request.getNumExpediente().isEmpty()) {
            if (UserRepo.findByNumExpediente(request.getNumExpediente()).isPresent()) {
                return ResponseEntity.badRequest().body("El expediente ya está registrado.");
            }
        }

        // Validar duplicados por CURP
        if (request.getCurp() != null && !request.getCurp().isEmpty()) {
            if (UserRepo.findByCurp(request.getCurp()).isPresent()) {
                return ResponseEntity.badRequest().body("La CURP ya está registrada.");
            }
        }

        //Validar duplicados de nombre
        if (request.getNombreCompleto() != null && !request.getNombreCompleto().isEmpty()) {
            if (UserRepo.findByNombreCompleto(request.getNombreCompleto()).isPresent()) {
                return ResponseEntity.badRequest().body("El usuario ya está registrado.");
            }
        }
        User user = new User(
                request.getNombreCompleto(),
                request.getCurp(),
                request.getTelefono(),
                request.getTelefono2(),
                request.getTelefono3(),
                request.getRfc(),
                request.getDireccion()
        );

        UserRepo.save(user);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest loginRequest) {

        Optional<User> userOpt = Optional.empty();

        // Buscar por CURP si se envía
        if (loginRequest.getCurp() != null && !loginRequest.getCurp().isEmpty()) {
            userOpt = userRepo.findByCurp(loginRequest.getCurp());
        }
        // Si no se envió CURP, buscar por número de expediente
        else if (loginRequest.getNumExpediente() != null && !loginRequest.getNumExpediente().isEmpty()) {
            userOpt = userRepo.findByNumExpediente(loginRequest.getNumExpediente());
        }
        else {
            return ResponseEntity.badRequest().body("Debes enviar CURP o número de expediente");
        }

        // Si no existe el usuario
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = userOpt.get();

        // Retornar un JSON con los datos del usuario
        return ResponseEntity.ok(user);
    }

}
