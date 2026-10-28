package ar.edu.utn.diplomatura.clase16.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.diplomatura.clase16.dto.ClienteDTO;
import ar.edu.utn.diplomatura.clase16.dto.PagoRequest;
import ar.edu.utn.diplomatura.clase16.dto.PagoResponse;
import ar.edu.utn.diplomatura.clase16.integracion.ClientesClient;
import ar.edu.utn.diplomatura.clase16.servicios.PagosService;
import feign.FeignException;

/**
 * La API del servicio de pagos, en el puerto 8081.
 */
@RestController
@RequestMapping("/pagos")
public class PagosController {

    private final ClientesClient clientesClient;
    private final PagosService pagosService;

    public PagosController(ClientesClient clientesClient, PagosService pagosService) {
        this.clientesClient = clientesClient;
        this.pagosService = pagosService;
    }

    /** GET /pagos/clientes: le pide la lista al servicio de clientes y la devuelve tal cual. */
    @GetMapping("/clientes")
    public List<ClienteDTO> listarClientes(@RequestParam(required = false) String apellido) {
        if (apellido == null || apellido.isBlank()) {
            return clientesClient.listar();
        }
        return clientesClient.buscarPorApellido(apellido);
    }

    /** GET /pagos/clientes/1 */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clientesClient.obtener(id));
        } catch (FeignException.NotFound e) {
            // El servicio de clientes contesto 404 y Feign lo convirtio en esta excepcion.
            return ResponseEntity.notFound().build();
        }
    }

    /** POST /pagos  con { "clienteId": 1, "importe": 15000, "concepto": "Sueldo octubre" } */
    @PostMapping
    public ResponseEntity<PagoResponse> pagar(@RequestBody PagoRequest pedido) {
        try {
            PagoResponse respuesta = pagosService.registrar(pedido);
            if (respuesta.estado().equals("RECHAZADO")) {
                return ResponseEntity.unprocessableEntity().body(respuesta); // 422
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta); // 201
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}
