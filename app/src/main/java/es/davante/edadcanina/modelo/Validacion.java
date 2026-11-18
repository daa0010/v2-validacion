package es.davante.edadcanina.modelo;

// Defino un enum con los estados posibles para separar las reglas de negocio de los mensajes visuales
public enum Validacion {
    // Indico que aún no se ha pulsado calcular o que el texto ha cambiado desde el último cálculo
    SIN_CALCULAR,
    // Represento que la entrada es válida y el cálculo se ha realizado con éxito
    OK,
    // Señalo que el campo está vacío o solo contiene espacios
    VACIO,
    // Indico que el texto no se puede convertir a un número entero válido
    NO_NUMERO,
    // Señalo que la edad introducida queda fuera de los límites permitidos
    FUERA_RANGO
}
