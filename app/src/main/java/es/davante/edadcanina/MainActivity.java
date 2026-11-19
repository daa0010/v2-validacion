package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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

        // Conecto el modelo con el binding para que se suscriba a sus avisos y funcione el enlace @={}
        binding.setModelo(modelo);
        // Delego directamente el clic en el modelo porque la vista ya reacciona automáticamente a los cambios de estado
        binding.btnCalc.setOnClickListener(v -> modelo.calcular());
    }
}