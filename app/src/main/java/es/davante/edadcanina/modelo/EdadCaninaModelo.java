package es.davante.edadcanina.modelo;

import java.util.Objects;

public class EdadCaninaModelo {

    // Defino las reglas de negocio como constantes para evitar números mágicos y centralizar los límites
    public static final int FACTOR_CANINO = 7;
    public static final int EDAD_MINIMA = 1;
    public static final int EDAD_MAXIMA = 30;

    // Guardo el estado actual del modelo sin depender de ninguna clase de Android
    private String edadTexto = "";
    private int edadHumana = 0;
    private Validacion validacion = Validacion.SIN_CALCULAR;

    public String getEdadTexto() {
        return edadTexto;
    }

    public int getEdadHumana() {
        return edadHumana;
    }

    public Validacion getValidacion() {
        return validacion;
    }

    public void setEdadTexto(String nuevoTexto) {
        if (nuevoTexto == null) {
            nuevoTexto = "";
        }
        // Aplico la regla de negocio: si el texto cambia, el resultado y la validación anteriores dejan de valer
        if (!Objects.equals(this.edadTexto, nuevoTexto)) {
            this.edadTexto = nuevoTexto;
            this.edadHumana = 0;
            this.validacion = Validacion.SIN_CALCULAR;
        }
    }

    // Ejecuto la validación y el cálculo devolviendo un estado semántico sin textos ligados al idioma
    public Validacion calcular() {
        // Uso trim() para no dar por válidas entradas compuestas únicamente por espacios en blanco
        String textoLimpio = edadTexto.trim();

        if (textoLimpio.isEmpty()) {
            edadHumana = 0;
            validacion = Validacion.VACIO;
            return validacion;
        }

        int edad;
        try {
            // Capturo NumberFormatException por si llega texto no numérico o un número que desborda un int
            edad = Integer.parseInt(textoLimpio);
        } catch (NumberFormatException e) {
            edadHumana = 0;
            validacion = Validacion.NO_NUMERO;
            return validacion;
        }

        // Compruebo los límites para evitar edades irreales y asegurar que multiplicar por 7 nunca desborde
        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            edadHumana = 0;
            validacion = Validacion.FUERA_RANGO;
            return validacion;
        }

        // Calculo la edad humana y marco la validación como correcta
        edadHumana = edad * FACTOR_CANINO;
        validacion = Validacion.OK;
        return validacion;
    }
}