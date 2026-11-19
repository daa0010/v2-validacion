package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.controlador.EdadCaninaControlador;
import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

public class MainActivity extends AppCompatActivity {

    // Defino constantes para las claves del Bundle y así evitar errores tipográficos al guardar y restaurar
    private static final String CLAVE_EDAD_TEXTO = "edad_texto";
    private static final String CLAVE_CALCULADO = "calculado";
    private ActivityMainBinding binding;
    // Guardo el modelo como atributo para poder consultar su estado dentro de onSaveInstanceState
    private EdadCaninaModelo modelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        modelo = new EdadCaninaModelo();

        // Restauro el estado previo antes de entregar el modelo al binding para que la pantalla nazca ya sincronizada
        if (savedInstanceState != null) {
            restaurarEstado(savedInstanceState);
        }

        // Creo el controlador inyectándole el modelo para que gestione las acciones del usuario
        EdadCaninaControlador controlador = new EdadCaninaControlador(modelo);

        // Conecto tanto el modelo como el controlador con el binding para que los clics viajen desde el XML
        binding.setModelo(modelo);
        binding.setControlador(controlador);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardo solo lo mínimo necesario antes de que Android destruya la Activity: el texto y si hubo cálculo
        outState.putString(CLAVE_EDAD_TEXTO, modelo.getEdadTexto());
        outState.putBoolean(CLAVE_CALCULADO, modelo.getValidacion() != Validacion.SIN_CALCULAR);
    }

    // Reconstruyo el estado del modelo; como el cálculo es determinista, basta con repetirlo si estaba calculado
    private void restaurarEstado(Bundle savedInstanceState) {
        String edadGuardada = savedInstanceState.getString(CLAVE_EDAD_TEXTO, "");
        boolean estabaCalculado = savedInstanceState.getBoolean(CLAVE_CALCULADO, false);

        modelo.setEdadTexto(edadGuardada);
        if (estabaCalculado) {
            modelo.calcular();
        }
    }
}