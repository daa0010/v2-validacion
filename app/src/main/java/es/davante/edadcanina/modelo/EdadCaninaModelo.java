package es.davante.edadcanina.modelo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Objects;

import es.davante.edadcanina.BR;

// Heredo de BaseObservable para que el modelo pueda avisar a la vista cada vez que cambie una propiedad
public class EdadCaninaModelo extends BaseObservable {

    public static final int FACTOR_CANINO = 7;
    public static final int EDAD_MINIMA = 1;
    public static final int EDAD_MAXIMA = 30;

    private String edadTexto = "";
    private int edadHumana = 0;
    private Validacion validacion = Validacion.SIN_CALCULAR;

    // Anoto los tres getters con @Bindable para que DataBinding genere sus identificadores en la clase BR
    @Bindable
    public String getEdadTexto() {
        return edadTexto;
    }

    @Bindable
    public int getEdadHumana() {
        return edadHumana;
    }

    @Bindable
    public Validacion getValidacion() {
        return validacion;
    }

    public void setEdadTexto(String nuevoTexto) {
        if (nuevoTexto == null) {
            nuevoTexto = "";
        }
        // Corto el aviso si el texto es idéntico para evitar un bucle infinito con el enlace bidireccional @={}
        if (Objects.equals(this.edadTexto, nuevoTexto)) {
            return;
        }
        this.edadTexto = nuevoTexto;
        notifyPropertyChanged(BR.edadTexto);

        // Aplico la regla de negocio: si cambia la entrada, invalido el resultado anterior y aviso a la vista
        setEdadHumana(0);
        setValidacion(Validacion.SIN_CALCULAR);
    }

    // Hago privado este setter para que solo calcular() y setEdadTexto() puedan modificar el resultado
    private void setEdadHumana(int nuevaEdadHumana) {
        if (this.edadHumana == nuevaEdadHumana) {
            return;
        }
        this.edadHumana = nuevaEdadHumana;
        notifyPropertyChanged(BR.edadHumana);
    }

    // Hago privado este setter para encapsular los cambios de estado dentro de la lógica del modelo
    private void setValidacion(Validacion nuevaValidacion) {
        if (this.validacion == nuevaValidacion) {
            return;
        }
        this.validacion = nuevaValidacion;
        notifyPropertyChanged(BR.validacion);
    }

    public Validacion calcular() {
        // Uso trim() para ignorar espacios en blanco antes o después del número
        String textoLimpio = edadTexto.trim();

        if (textoLimpio.isEmpty()) {
            setEdadHumana(0);
            setValidacion(Validacion.VACIO);
            return validacion;
        }

        int edad;
        try {
            // Capturo NumberFormatException por si llega texto no numérico o un entero que desborde el límite
            edad = Integer.parseInt(textoLimpio);
        } catch (NumberFormatException e) {
            setEdadHumana(0);
            setValidacion(Validacion.NO_NUMERO);
            return validacion;
        }

        // Compruebo el rango válido para evitar edades irreales y prevenir desbordamientos al multiplicar
        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            setEdadHumana(0);
            setValidacion(Validacion.FUERA_RANGO);
            return validacion;
        }

        // Actualizo el resultado y el estado mediante sus setters privados para que DataBinding repinte la vista
        setEdadHumana(edad * FACTOR_CANINO);
        setValidacion(Validacion.OK);
        return validacion;
    }

    // Añado la operación de reinicio en el modelo porque limpiar el estado forma parte de las reglas de la app
    public void limpiar() {
        setEdadTexto("");
        // Aseguro que se limpie cualquier error previo incluso si el campo ya estaba vacío al pulsar Limpiar
        setEdadHumana(0);
        setValidacion(Validacion.SIN_CALCULAR);
    }
}