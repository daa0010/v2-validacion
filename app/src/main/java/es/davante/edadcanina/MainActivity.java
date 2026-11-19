package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.controlador.EdadCaninaControlador;
import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;

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

        // Creo el controlador inyectándole el modelo para que gestione las acciones del usuario
        EdadCaninaControlador controlador = new EdadCaninaControlador(modelo);

        // Conecto tanto el modelo como el controlador con el binding para que los clics viajen desde el XML
        binding.setModelo(modelo);
        binding.setControlador(controlador);
    }
}