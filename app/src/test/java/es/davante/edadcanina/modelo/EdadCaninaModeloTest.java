package es.davante.edadcanina.modelo;

import org.junit.Before;
import org.junit.Test;

import androidx.databinding.Observable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EdadCaninaModeloTest {

    private EdadCaninaModelo modelo;

    @Before
    public void setUp() {
        // Creo una instancia limpia antes de cada test para que ninguna prueba influya en las demás
        modelo = new EdadCaninaModelo();
    }

    @Test
    public void edadValida_calculaPorSiete() {
        // Verifico que una edad dentro del rango devuelve OK y multiplica correctamente por el factor canino
        modelo.setEdadTexto("6");
        assertEquals(Validacion.OK, modelo.calcular());
        assertEquals(42, modelo.getEdadHumana());
    }

    @Test
    public void entradaVacia_devuelveErrorVacio() {
        // Compruebo que una cadena vacía o con solo espacios se detecta como VACIO y deja el resultado a cero
        modelo.setEdadTexto("   ");
        assertEquals(Validacion.VACIO, modelo.calcular());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void entradaNoNumerica_devuelveErrorNoNumero() {
        // Verifico que una entrada no numérica se captura sin lanzar excepción y devuelve NO_NUMERO
        modelo.setEdadTexto("abc");
        assertEquals(Validacion.NO_NUMERO, modelo.calcular());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void edadMenorQueMinimo_devuelveErrorFueraRango() {
        // Compruebo que el límite inferior (0) se rechaza como FUERA_RANGO
        modelo.setEdadTexto("0");
        assertEquals(Validacion.FUERA_RANGO, modelo.calcular());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void edadMayorQueMaximo_devuelveErrorFueraRango() {
        // Compruebo que superar el límite superior (31) se rechaza como FUERA_RANGO
        modelo.setEdadTexto("31");
        assertEquals(Validacion.FUERA_RANGO, modelo.calcular());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void cambiarTextoDespuesDeCalcular_invalidaResultadoAnterior() {
        // Verifico la regla de negocio: al modificar el texto tras un cálculo, el estado anterior se invalida
        modelo.setEdadTexto("6");
        modelo.calcular();

        modelo.setEdadTexto("7");
        assertEquals(Validacion.SIN_CALCULAR, modelo.getValidacion());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void calcular_notificaCambiosDePropiedades() {
        // Registro un observador para comprobar que el modelo avisa a DataBinding al calcular
        List<Integer> propiedadesNotificadas = new ArrayList<>();
        modelo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                propiedadesNotificadas.add(propertyId);
            }
        });

        modelo.setEdadTexto("6");
        propiedadesNotificadas.clear();

        modelo.calcular();

        // Verifico que se disparen exactamente dos avisos (uno por edadHumana y otro por validacion)
        assertEquals(2, propiedadesNotificadas.size());
    }
}