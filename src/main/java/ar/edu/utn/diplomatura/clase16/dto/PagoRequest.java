package ar.edu.utn.diplomatura.clase16.dto;

/**
 * Lo que llega en el cuerpo de POST /pagos.
 * Ejemplo: { "clienteId": 1, "importe": 15000, "concepto": "Sueldo octubre" }
 */
public record PagoRequest(
        Long clienteId,
        double importe,
        String concepto
) {
}
