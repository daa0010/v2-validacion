package es.davante.edadcanina.vista;

import android.content.Context;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import es.davante.edadcanina.R;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

// Declaro la clase final porque solo contiene métodos estáticos de utilidad para la capa de vista
public final class AdaptadoresVista {

    // Defino un constructor privado para evitar que se instancie una clase que solo agrupa adaptadores estáticos
    private AdaptadoresVista() {
    }

    // Creo el atributo personalizado app:errorValidacion; debe ser public static para que DataBinding pueda invocarlo desde el XML
    @BindingAdapter("errorValidacion")
    public static void mostrarErrorValidacion(EditText campo, Validacion validacion) {
        // Obtengo el Context desde la propia vista para poder resolver los textos traducidos de strings.xml
        String mensaje = mensajeDeError(campo.getContext(), validacion);
        campo.setError(mensaje);

        // Solo pido el foco cuando existe un error real para guiar al usuario hacia el campo que debe corregir
        if (mensaje != null) {
            campo.requestFocus();
        }
    }

    // Traduzco el estado semántico del modelo a un mensaje legible según el idioma del dispositivo
    private static String mensajeDeError(Context context, Validacion validacion) {
        if (validacion == null) {
            return null;
        }
        switch (validacion) {
            case VACIO:
                return context.getString(R.string.error_vacio);
            case NO_NUMERO:
                return context.getString(R.string.error_no_numero);
            case FUERA_RANGO:
                return context.getString(
                        R.string.error_rango,
                        EdadCaninaModelo.EDAD_MINIMA,
                        EdadCaninaModelo.EDAD_MAXIMA
                );
            default:
                // Devuelvo null en OK o SIN_CALCULAR porque setError(null) limpia cualquier aviso previo en el EditText
                return null;
        }
    }
}