package pe.edu.cibertec.patitas_frontend_wc_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.LogRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.LogResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.LoginResponseDTO;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5174")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAutenticacion;

    @PostMapping("/autenticar-async")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {

        // Validar campos de entrada
        if (loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().isEmpty() ||
                loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().isEmpty() ||
                loginRequestDTO.password() == null || loginRequestDTO.password().trim().isEmpty()) {

            return  Mono.just(new LoginResponseDTO("01", "Error: Debe completar correctamente sus credenciales", "", ""));

        }

        try {


            return webClientAutenticacion.post()
                    .uri("http://localhost:8095/autenticacion/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class)
                    .flatMap(response -> {
                        if(response.codigo().equals("00")) {
                            return Mono.just(new LoginResponseDTO("00", "", response.nombreUsuario(), ""));
                        } else {
                            return Mono.just(new LoginResponseDTO("02", "Error: Credenciales incorrectas", "", ""));
                        }
                    });

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return Mono.just(new LoginResponseDTO("99", "Error: Ocurrió un problema en al autenticación.", "", ""));

        }

    }

    @PostMapping("/cerrar-sesion-async")
    public Mono<LogResponseDTO> cerrarSesion(@RequestBody LogRequestDTO logoutRequestDTO) {

        if (logoutRequestDTO.tipoDocumento() == null || logoutRequestDTO.tipoDocumento().trim().isEmpty() ||
                logoutRequestDTO.numeroDocumento() == null || logoutRequestDTO.numeroDocumento().trim().isEmpty()) {

            return  Mono.just(new LogResponseDTO("01", "Debe completar correctamente sus credenciales"));

        }

        try {


            return webClientAutenticacion.post()
                    .uri("http://localhost:8095/autenticacion/log")
                    .body(Mono.just(logoutRequestDTO), LogRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LogResponseDTO.class)
                    .flatMap(response -> {
                        if(response.codigo().equals("00")) {
                            return Mono.just(new LogResponseDTO("00", ""));
                        } else {
                            return Mono.just(new LogResponseDTO("02", "Error: No se puedo cerrar sesión"));
                        }
                    });

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return Mono.just(new LogResponseDTO("99", "Error: Ocurrió un problema en al cerrar sesión."));

        }

    }


}