package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // Instancio el modelo para delegar en él los datos, la validación y el cálculo
    private EdadCaninaModelo modelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        modelo = new EdadCaninaModelo();

        binding.btnCalc.setOnClickListener(v -> calcularEdad());
    }

    private void calcularEdad() {
        // Paso el texto actual al modelo y le pido que evalúe las reglas de negocio
        modelo.setEdadTexto(binding.ageInput.getText().toString());
        Validacion estado = modelo.calcular();

        // Traduzco el estado devuelto por el modelo a elementos visuales según el idioma del dispositivo
        switch (estado) {
            case OK:
                binding.ageInput.setError(null);
                binding.resultText.setText(getString(R.string.tu_edad_format, modelo.getEdadHumana()));
                break;
            case VACIO:
                mostrarError(getString(R.string.error_vacio));
                break;
            case NO_NUMERO:
                mostrarError(getString(R.string.error_no_numero));
                break;
            case FUERA_RANGO:
                mostrarError(getString(
                        R.string.error_rango,
                        EdadCaninaModelo.EDAD_MINIMA,
                        EdadCaninaModelo.EDAD_MAXIMA
                ));
                break;
            default:
                break;
        }
    }

    // Centralizo la limpieza y muestra de errores visuales para no repetir código en cada rama del switch
    private void mostrarError(String mensaje) {
        binding.ageInput.setError(mensaje);
        binding.ageInput.requestFocus();
        binding.resultText.setText("");
    }
}