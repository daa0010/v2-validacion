package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int FACTOR_CANINO = 7;
    private static final int EDAD_MINIMA = 1;
    private static final int EDAD_MAXIMA = 30;

    // Guardo el binding como atributo para acceder a todas las vistas con id sin usar findViewById
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflo el layout con DataBindingUtil para obtener la instancia generada de ActivityMainBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Uso el campo generado btnCalc en camelCase para detectar cualquier fallo de id al compilar
        binding.btnCalc.setOnClickListener(v -> calcularEdad());
    }

    private void calcularEdad() {
        // Leo el texto desde el binding y aplico trim() para que los espacios en blanco no cuenten como entrada válida
        String ageString = binding.ageInput.getText().toString().trim();

        // Compruebo primero si está vacío para no intentar convertir una cadena sin contenido
        if (ageString.isEmpty()) {
            mostrarError(getString(R.string.error_vacio));
            return;
        }

        // Capturo NumberFormatException porque el usuario podría pegar texto no numérico o provocar desbordamiento
        int age;
        try {
            age = Integer.parseInt(ageString);
        } catch (NumberFormatException e) {
            mostrarError(getString(R.string.error_no_numero));
            return;
        }

        // Acoto entre 1 y 30 años para evitar edades imposibles y garantizar que la multiplicación nunca desborde un int
        if (age < EDAD_MINIMA || age > EDAD_MAXIMA) {
            mostrarError(getString(R.string.error_rango, EDAD_MINIMA, EDAD_MAXIMA));
            return;
        }

        // Limpio cualquier aviso previo antes de pintar el resultado final en el TextView
        binding.ageInput.setError(null);
        int resultado = age * FACTOR_CANINO;
        binding.resultText.setText(getString(R.string.tu_edad_format, resultado));
    }

    // Centralizo la gestión de errores para no duplicar código y asegurar que siempre se borre un resultado anterior
    private void mostrarError(String mensaje) {
        binding.ageInput.setError(mensaje);
        binding.ageInput.requestFocus();
        binding.resultText.setText("");
    }
}