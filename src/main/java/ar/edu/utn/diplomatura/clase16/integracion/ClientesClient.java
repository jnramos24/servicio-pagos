package ar.edu.utn.diplomatura.clase16.integracion;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.utn.diplomatura.clase16.dto.ClienteDTO;

/**
 * Cliente Feign del servicio de clientes (proyecto-clase05).
 *
 * Es solo una interfaz: la implementacion (abrir la conexion, mandar el pedido,
 * leer la respuesta, pasar el JSON a objetos) la escribe Feign al arrancar.
 *
 * name: identificador del servicio al que le hablamos.
 * url:  donde esta.
 *
 * Las anotaciones son las mismas que en un controller, pero leidas al reves:
 * en un controller dicen "atiendo este pedido"; aca dicen "mando este pedido".
 */
@FeignClient(name = "servicio-clientes")
public interface ClientesClient {

    // GET http://localhost:8080/clientes
    @GetMapping("/clientes")
    List<ClienteDTO> listar();

    // GET http://localhost:8080/clientes/1
    @GetMapping("/clientes/{id}")
    ClienteDTO obtener(@PathVariable Long id);

    // POST http://localhost:8080/clientes/1/acreditaciones?importe=15000
    @PostMapping("/clientes/{id}/acreditaciones")
    ClienteDTO acreditar(@PathVariable Long id, @RequestParam("importe") double importe);

    // GET http://localhost:8080/clientes?apellido=gomez
    @GetMapping("/clientes")
    List<ClienteDTO> buscarPorApellido(@RequestParam("apellido") String apellido);
}
