package ar.edu.utn.diplomatura.clase16.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.diplomatura.clase16.dto.ClienteDTO;
import ar.edu.utn.diplomatura.clase16.dto.PagoRequest;
import ar.edu.utn.diplomatura.clase16.dto.PagoResponse;
import ar.edu.utn.diplomatura.clase16.integracion.ClientesClient;

/**
 * Clase 16. El ClientesClient es una interfaz: en el test se reemplaza por un mock,
 * igual que el repositorio en ClienteServiceTest. No hace falta levantar el otro servicio.
 */
@ExtendWith(MockitoExtension.class)
class PagosServiceTest {

    @Mock
    private ClientesClient clientesClient;

    @InjectMocks
    private PagosService pagosService;

    @Test
    @DisplayName("Un cliente activo recibe la acreditacion")
    void pagoAprobado() {
        // given
        when(clientesClient.obtener(1L))
                .thenReturn(new ClienteDTO(1L, "Ana", "Gomez", 1000.0, true));
        when(clientesClient.acreditar(1L, 500.0))
                .thenReturn(new ClienteDTO(1L, "Ana", "Gomez", 1500.0, true));

        // when
        PagoResponse respuesta = pagosService.registrar(new PagoRequest(1L, 500.0, "Prueba"));

        // then
        assertEquals("APROBADO", respuesta.estado());
        assertEquals(1500.0, respuesta.saldoNuevo());
        assertNotNull(respuesta.codigoOperacion());
    }

    @Test
    @DisplayName("A un cliente inactivo no se le acredita nada")
    void clienteInactivoRechazado() {
        // given
        when(clientesClient.obtener(6L))
                .thenReturn(new ClienteDTO(6L, "Federico", "Luna", 500.0, false));

        // when
        PagoResponse respuesta = pagosService.registrar(new PagoRequest(6L, 500.0, "Prueba"));

        // then
        assertEquals("RECHAZADO", respuesta.estado());
        verify(clientesClient, never()).acreditar(anyLong(), anyDouble());
    }
}
