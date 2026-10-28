package ar.edu.utn.diplomatura.clase16.dto;

/**
 * Lo que devuelve POST /pagos.
 * estado: APROBADO o RECHAZADO. Si se rechazo, codigoOperacion y titular vienen en null.
 */
public record PagoResponse(String estado,
                           String codigoOperacion,
                           String titular,
                           double saldoNuevo,
                           String mensaje
) {
}
