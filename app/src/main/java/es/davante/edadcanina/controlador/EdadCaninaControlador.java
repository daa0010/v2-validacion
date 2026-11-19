package es.davante.edadcanina.controlador;

import es.davante.edadcanina.modelo.EdadCaninaModelo;

// Creo el controlador para traducir las acciones del usuario en órdenes directas hacia el modelo
public class EdadCaninaControlador {

    // Guardo una referencia al modelo para delegar en él todas las operaciones sin tocar la vista
    private final EdadCaninaModelo modelo;

    // Recibo el modelo por constructor para desacoplar el controlador de la creación de datos
    public EdadCaninaControlador(EdadCaninaModelo modelo) {
        this.modelo = modelo;
    }

    // Traduzco el clic del botón Calcular en una orden al modelo para que valide y calcule
    public void onCalcularClick() {
        modelo.calcular();
    }

    // Traduzco el clic del botón Limpiar en una orden al modelo para que reinicie su estado
    public void onLimpiarClick() {
        modelo.limpiar();
    }
}