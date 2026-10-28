package ar.edu.utn.diplomatura.clase16.servicios;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ar.edu.utn.diplomatura.clase16.dto.ClienteDTO;
import ar.edu.utn.diplomatura.clase16.dto.PagoRequest;
import ar.edu.utn.diplomatura.clase16.dto.PagoResponse;
import ar.edu.utn.diplomatura.clase16.integracion.ClientesClient;

/**
 * Slide 22. Las reglas del pago. Todo lo que es "datos del cliente" se lo pregunta
 * al servicio de clientes con el ClientesClient.
 */
@Service
public class PagosService {

    private final ClientesClient clientesClient;

    // Inyeccion por constructor
    // Spring nos pasa la implementacion que escribio Feign.
    public PagosService(ClientesClient clientesClient) {
        this.clientesClient = clientesClient;
    }

    /**
     * Acredita el importe en la cuenta del cliente, si se puede.
     * Si el cliente no existe, Feign lanza FeignException.NotFound (la atrapa el controller).
     */
    public PagoResponse registrar(PagoRequest pedido) {
        if (pedido.importe() <= 0) {
            return rechazado("El importe tiene que ser mayor a cero");
        }

        // 1er llamado HTTP al otro servicio: GET /clientes/{id}
        ClienteDTO cliente = clientesClient.obtener(pedido.clienteId());

        if (!cliente.activo()) {
            return rechazado("El cliente " + cliente.id() + " esta inactivo");
        }

        // 2do llamado HTTP: POST /clientes/{id}/acreditaciones?importe=...
        ClienteDTO actualizado = clientesClient.acreditar(cliente.id(), pedido.importe());

        String codigo = "OP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PagoResponse("APROBADO",
                codigo,
                actualizado.nombre() + " " + actualizado.apellido(),
                actualizado.saldo(),
                "Acreditado: " + pedido.concepto());
    }

    private PagoResponse rechazado(String motivo) {
        return new PagoResponse("RECHAZADO", null, null, 0, motivo);
    }
}
