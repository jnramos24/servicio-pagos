package ar.edu.utn.diplomatura.clase16.dto;

/**
 * Lo que este servicio necesita saber de un cliente.
 *
 * Tiene la forma del JSON que devuelve el servicio de clientes, pero solo con los
 * campos que usamos aca: el cuit y el email que vienen en ese JSON se ignoran.
 * No es una entidad (no lleva @Entity): este servicio no tiene base de datos.
 */
public record ClienteDTO(

        Long id,
        String nombre,
        String apellido,
        double saldo,
        boolean activo
) {
}
