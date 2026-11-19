package es.davante.edadcanina.controlador;

import org.junit.Before;
import org.junit.Test;

import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

import static org.junit.Assert.assertEquals;

public class EdadCaninaControladorTest {

    private EdadCaninaModelo modelo;
    private EdadCaninaControlador controlador;

    @Before
    public void setUp() {
        // Instancio un modelo y un controlador limpios antes de cada prueba para aislar los casos
        modelo = new EdadCaninaModelo();
        controlador = new EdadCaninaControlador(modelo);
    }

    @Test
    public void onCalcularClick_ordenaCalcularAlModelo() {
        // Simulo que el usuario escribe una edad y pulsa Calcular para comprobar que el modelo se actualiza
        modelo.setEdadTexto("6");
        controlador.onCalcularClick();

        assertEquals(Validacion.OK, modelo.getValidacion());
        assertEquals(42, modelo.getEdadHumana());
    }

    @Test
    public void onLimpiarClick_reiniciaElEstadoDelModelo() {
        // Calculo primero un resultado válido y luego simulo pulsar Limpiar para verificar el reinicio completo
        modelo.setEdadTexto("6");
        controlador.onCalcularClick();

        controlador.onLimpiarClick();

        assertEquals("", modelo.getEdadTexto());
        assertEquals(0, modelo.getEdadHumana());
        assertEquals(Validacion.SIN_CALCULAR, modelo.getValidacion());
    }
}