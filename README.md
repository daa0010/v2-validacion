# 🐶 EdadCanina · v1

Aplicación Android (Java) que calcula la edad "humana" de un perro multiplicando su edad por 7.

Esta es la **versión 1**: funciona, pero no comprueba lo que escribe el usuario. Tu tarea es
convertirla en la **versión 2** (rama `v2-validacion`), que valida la entrada y controla los errores
sin que la app se cierre.

---

## Objetivos

Al terminar sabrás:

- Detectar por qué una app se cierra (`NumberFormatException`) y evitarlo con `try/catch`.
- Validar datos de entrada: campo vacío, texto no numérico y rango de valores.
- Mostrar errores al usuario con `EditText.setError()`.
- Sustituir "números mágicos" por constantes.
- Llevar los textos a `strings.xml` y usar formatos con varios parámetros.
- Trabajar en una rama de Git distinta de `main`.

---

## Paso 0 · Prepara el proyecto

1. Haz un **fork** de este repositorio (botón *Fork*) y clona tu fork:
   ```bash
   git clone [https://github.com/TU_USUARIO/edadCaninav1.git](https://github.com/TU_USUARIO/edadCaninav1.git)
   ```
2. Ábrelo en Android Studio y ejecútalo en el emulador.

## Paso 1 · Rompe la app (a propósito)

Antes de arreglar nada, comprueba qué falla. Prueba estas entradas y anota qué pasa:

| Entrada         | ¿Qué ocurre?                                                                                                                                  |
|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| *(campo vacío)* | La app se cierra por `java.lang.NumberFormatException: For input string: ""`.                                                                 |
| `abc`           | La app se cierra por `java.lang.NumberFormatException: For input string: "abc"`.                                                              |
| `3.5`           | La app se cierra por `java.lang.NumberFormatException` al no admitir decimales.                                                               |
| `0`             | No se cierra, pero muestra un resultado ilógico: "Si tu perro fuera humano tendría 0 años".                                                   |
| `-4`            | No se cierra, pero calcula una edad negativa imposible: "Si tu perro fuera humano tendría -28 años".                                          |
| `99999999999`   | La app se cierra por `java.lang.NumberFormatException` al superar el límite de un `int` de 32 bits (`2147483647`).                            |
| `400000000`     | No se cierra al convertir el número, pero desborda al multiplicar por 7 (*integer overflow*) y muestra un resultado negativo (`-1494967296`). |

> 💡 **Cuando la app se cierre, abre el Logcat y busca la excepción. ¿Qué método la lanza? ¿Por qué
el último caso no cierra la app pero da un resultado absurdo?**
> - **¿Qué método la lanza?**: El método `Integer.parseInt(String s)`.
> - **¿Por qué el último caso no cierra la app pero da un resultado absurdo?**: Porque `400000000`
    cabe dentro del rango de un entero de 32 bits (`Integer.MAX_VALUE = 2.147.483.647`), por lo que
    `Integer.parseInt()` funciona sin lanzar excepción. Sin embargo, al multiplicarlo por 7 el
    resultado (`2.800.000.000`) supera el límite máximo permitido, produciendo un desbordamiento
    aritmético (*overflow*) en binario que da la vuelta hacia los números negativos.

## Paso 2 · Crea la rama de trabajo

```bash
git checkout -b v2-validacion
```

Todo lo que hagas a partir de aquí irá en esta rama; `main` se queda como está.

## Paso 3 · Corrige los fallos que ya trae la v1

Antes de añadir nada nuevo, limpia lo que hay:

1. **`activity_main.xml`**: busca la etiqueta de cierre `</TextView>>` y quita el `>` que sobra.
2. **`strings.xml`**: en `tu_edad_format` hay una falta de ortografía ("fuero"). Corrígela.
3. **Texto escrito "a fuego"**: el `android:hint="Ejemplo: 6"` del `EditText` está directamente en
   el layout. Llévalo a `strings.xml` con el nombre `hint_edad` y úsalo con `@string/hint_edad`.
   > 💡 Android Studio marca este aviso en amarillo (*Hardcoded string*). Prueba `Alt + Enter`
   encima.
4. **`MainActivity.java`**: hay tres `import` que no se usan (`Insets`, `ViewCompat`,
   `WindowInsetsCompat`). Elimínalos (`Ctrl + Alt + O`).

## Paso 4 · Añade los mensajes de error

En `res/values/strings.xml` añade tres textos nuevos:

```xml

<string name="error_vacio">Introduce la edad de tu perro</string><string name="error_no_numero">
Introduce un número entero válido
</string><string name="error_rango">La edad debe estar entre %1$d y %2$d años</string>
```

> ❓ **¿Qué significan `%1$d` y `%2$d`? ¿En qué orden se rellenan?**
> - Son especificadores de formato posicionales: `%` indica el inicio del marcador, `1$` y `2$`
    señalan la posición del primer y segundo argumento respectivamente, y `d` indica que el valor a
    formatear es un número entero decimal.
> - Se rellenan en el orden en que se pasan los parámetros al llamar a
    `getString(R.string.error_rango, EDAD_MINIMA, EDAD_MAXIMA)`: `EDAD_MINIMA` sustituye a `%1$d` y
    `EDAD_MAXIMA` sustituye a `%2$d`.

## Paso 5 · Restringe la entrada desde el layout

En el `EditText` (`@+id/age_input`) añade:

```xml
android:inputType="number"android:maxLength="2"android:gravity="center"android:importantForAutofill="no"
```

| Atributo                    | Para qué sirve                                                         |
|-----------------------------|------------------------------------------------------------------------|
| `inputType="number"`        | Muestra el teclado numérico: sin letras, sin signo menos ni decimales. |
| `maxLength="2"`             | No deja escribir más de 2 cifras.                                      |
| `gravity="center"`          | Centra el número en el campo.                                          |
| `importantForAutofill="no"` | Evita que el sistema ofrezca autocompletar.                            |

> ⚠️ Esto **no sustituye** a la validación en Java: el campo puede quedarse vacío y nunca conviene
> fiarse solo de la interfaz. Por eso seguimos con el código.

## Paso 6 · Sustituye los números mágicos por constantes

Al principio de la clase `MainActivity` declara:

```java
private static final int FACTOR_CANINO = 7;
private static final int EDAD_MINIMA = 1;
private static final int EDAD_MAXIMA = 30; // ningún perro supera esta edad
```

y usa `FACTOR_CANINO` en lugar del `7`.

## Paso 7 · Convierte las vistas en atributos de la clase

Ahora `ageEdit` y `resultText` son variables locales de `onCreate()`. Como vas a usarlas desde otros
métodos, decláralas como atributos:

```java
private EditText ageEdit;
private TextView resultText;
```

y en `onCreate()` asígnalas **sin volver a escribir el tipo**:

```java
ageEdit =

findViewById(R.id.age_input);

resultText =

findViewById(R.id.result_text);
```

> ❓ **¿Qué pasaría si dejaras `EditText ageEdit = findViewById(...)` dentro de `onCreate()`?**
> - Se crearía una variable local llamada `ageEdit` que solo existiría dentro de `onCreate()`,
    dejando el atributo de la clase con valor `null`. Al pulsar el botón e intentar usar `ageEdit`
    desde `calcularEdad()`, la app se cerraría lanzando un `NullPointerException`.

## Paso 8 · Saca la lógica del botón a un método

El `setOnClickListener` debe quedar así de corto:

```java
button1.setOnClickListener(view ->

calcularEdad());
```

Toda la lógica pasa a un método nuevo: `private void calcularEdad()`.

## Paso 9 · Crea un método para mostrar errores

```java
private void mostrarError(String mensaje) {
    // 1. Mostrar el mensaje en el EditText (setError)
    // 2. Poner el foco en el EditText (requestFocus)
    // 3. Borrar el resultado anterior del TextView
}
```

## Paso 10 · Valida la entrada en `calcularEdad()`

Implementa el método siguiendo este orden. En cuanto algo falle, llama a `mostrarError(...)` y sal
con `return`:

1. **Leer** el texto del `EditText` quitando espacios (`trim()`).
2. **¿Está vacío?** → `error_vacio`. Recuerda que `Integer.parseInt("")` lanza
   `NumberFormatException`.
3. **Convertir a `int`** dentro de un `try/catch (NumberFormatException e)` → si falla,
   `error_no_numero`.
4. **¿Está en rango?** Si `age < EDAD_MINIMA || age > EDAD_MAXIMA` → `error_rango`. Para rellenar
   `%1$d` y `%2$d`:
   ```java
   getString(R.string.error_rango, EDAD_MINIMA, EDAD_MAXIMA)
   ```
5. **Todo correcto**: quita el error del campo (`setError(null)`), calcula `age * FACTOR_CANINO` y
   muestra el resultado con `tu_edad_format`.

> ❓ **¿Por qué limitar la edad a 30 evita también el desbordamiento que viste en el Paso 1?**
> - Porque el cálculo máximo posible pasa a ser `30 * 7 = 210`, una cifra muy por debajo del límite
    máximo que soporta un entero de 32 bits en Java (`2.147.483.647`), haciendo imposible que se
    produzca un *integer overflow*.

## Paso 11 · Vuelve a probar

Comprueba que **la app nunca se cierra**:

| Entrada   | Resultado esperado                            |
|-----------|-----------------------------------------------|
| *(vacío)* | Error: "Introduce la edad de tu perro"        |
| `0`       | Error: "La edad debe estar entre 1 y 30 años" |
| `31`      | Error de rango                                |
| `1`       | "Si tu perro fuera humano tendría 7 años"     |
| `30`      | "Si tu perro fuera humano tendría 210 años"   |

> 💡 **Con el teclado numérico no puedes escribir `abc` ni `-4`. ¿Cómo comprobarías que
el `try/catch` funciona? (Pista: quita `inputType` un momento.)**
> - Quitando temporalmente el atributo `android:inputType="number"` del `EditText` en
    `activity_main.xml` para poder usar el teclado alfanumérico completo, o copiando texto como
    `"abc"` o `"-4"` desde otra app y pegándolo directamente en el cuadro de texto.

## Paso 12 · Guarda y sube tu trabajo

```bash
git add .
git commit -m "Validación de la entrada y control de errores aritméticos"
git push -u origin v2-validacion
```

Comprueba en GitHub que tu fork tiene ahora dos ramas: `main` y `v2-validacion`.

---

## ✅ Lista de comprobación

- [x] La app no se cierra con ninguna entrada.
- [x] Todos los textos están en `strings.xml` (ningún aviso *Hardcoded string*).
- [x] No quedan números mágicos en `MainActivity`.
- [x] `onCreate()` solo prepara la pantalla; la lógica está en `calcularEdad()`.
- [x] El trabajo está en la rama `v2-validacion`, no en `main`.

---

## EdadCaninaMVC

Refactorización de la aplicación **EdadCanina** al patrón **MVC (Modelo-Vista-Controlador)**
utilizando **Android DataBinding**, separación estricta de responsabilidades, pruebas unitarias con
**JUnit** y conservación del estado ante cambios de configuración.

### Arquitectura del proyecto

* **`modelo` (`EdadCaninaModelo`, `Validacion`):** Contiene las constantes (`FACTOR_CANINO`,
  `EDAD_MINIMA`, `EDAD_MAXIMA`), el estado observable mediante `BaseObservable` y `@Bindable`, las
  reglas de validación y el cálculo. No depende de vistas ni de textos de `strings.xml`.
* **`vista` (`activity_main.xml`, `AdaptadoresVista`):** Muestra los datos enlazados directamente al
  modelo, reacciona automáticamente a los cambios de estado y define adaptadores personalizados (
  `@BindingAdapter`) para pintar los errores traducidos y gestionar la acción del teclado.
* **`controlador` (`EdadCaninaControlador`):** Recibe las acciones del usuario desde el XML (
  `Calcular`, `Limpiar` y tecla `Hecho`) y las traduce en órdenes hacia el modelo, sin ninguna
  referencia a vistas de Android.
* **`MainActivity`:** Se encarga únicamente de ensamblar las piezas (`binding`, `modelo` y
  `controlador`) y de conservar y restaurar el estado mínimo mediante `onSaveInstanceState` al girar
  la pantalla o cambiar el tema.

---

### Explícalo con tus palabras

#### 1. ¿Cuál es la diferencia entre `@{...}` y `@={...}`?

* **`@{...}` (enlace en un sentido):** Los datos solo viajan del **modelo hacia la vista
  **. Lo utilizo en el `TextView` del resultado (`android:text`, `android:visibility`) y en
  `app:errorValidacion`: cuando el modelo cambia y notifica con `notifyPropertyChanged`, la pantalla
  se actualiza sola, pero la vista no modifica el modelo.
* **`@={...}` (enlace en dos sentidos):** Los datos viajan en **ambas direcciones**. Lo
  utilizo en `android:text="@={modelo.edadTexto}"` del `EditText` para que, cuando escribo una
  cifra, se invoque automáticamente `setEdadTexto(...)` en el modelo y, a la inversa, cuando el
  modelo vacía el texto al pulsar **Limpiar**, el `EditText` también se borre solo.

#### 2. Recorrido paso a paso desde que escribo `6` y pulso Calcular hasta que aparece `42`

1. Al escribir `6` en el `EditText`, el enlace bidireccional `@={modelo.edadTexto}` llama a
   `modelo.setEdadTexto("6")`, guarda el texto, invalida cualquier cálculo previo y notifica
   `BR.edadTexto`.
2. Al pulsar el botón **Calcular**, el atributo
   `android:onClick="@{() -> controlador.onCalcularClick()}"` del XML invoca al controlador.
3. `EdadCaninaControlador.onCalcularClick()` llama a `modelo.calcular()`.
4. `EdadCaninaModelo.calcular()` limpia espacios, comprueba que `"6"` es un entero dentro del rango
   `[1, 30]`, calcula `6 * 7 = 42` y actualiza `edadHumana` y `validacion` (`Validacion.OK`)
   mediante sus setters privados.
5. Esos setters llaman a `notifyPropertyChanged(BR.edadHumana)` y
   `notifyPropertyChanged(BR.validacion)`.
6. `ActivityMainBinding`, que está suscrito al modelo, recibe los avisos y reevalúa las expresiones
   del XML:
    * En `result_text`, `android:text` formatea `tu_edad_format` con `42` y `android:visibility`
      cambia a `View.VISIBLE`.
    * En `age_input`, `app:errorValidacion` ejecuta `AdaptadoresVista.mostrarErrorValidacion(...)`
      con `Validacion.OK`, que devuelve `null` y limpia cualquier error anterior.

#### 3. ¿Qué cambiaría si mañana el factor canino fuera `6` en vez de `7`?

* **En el código de producción:** Solo tendría que modificar una única línea en
  `EdadCaninaModelo.java`: cambiar `public static final int FACTOR_CANINO = 7;` por `6`. No habría
  que tocar ni `MainActivity`, ni el controlador, ni los adaptadores, ni el XML.
* **En las pruebas unitarias:** Actualizaría los valores esperados en `EdadCaninaModeloTest` y
  `EdadCaninaControladorTest` (por ejemplo, esperar `36` en lugar de `42` para una entrada de `6`).

---

### Pruebas unitarias (JUnit)

Las pruebas se ejecutan en local sobre la JVM sin necesidad de abrir el emulador:

* **`EdadCaninaModeloTest` (7 pruebas):** cálculo válido, entrada vacía/espacios, texto no numérico,
  límite inferior (`0`), límite superior (`31`), invalidación del resultado al editar el texto y
  notificación de cambios de propiedades (`BaseObservable`).
* **`EdadCaninaControladorTest` (2 pruebas):** ejecución de cálculo desde `onCalcularClick()` y
  reinicio completo del estado desde `onLimpiarClick()`.

---

### Reto implementado: Teclado (`Hecho`)

He añadido soporte para calcular la edad directamente desde la tecla **Hecho** del teclado virtual (
o `Enter` en teclado físico) sin añadir lógica en `MainActivity`:

* En `activity_main.xml`, he configurado `android:imeOptions="actionDone"` y el atributo
  personalizado `app:accionHecho="@{controlador}"` en el `EditText`.
* En `AdaptadoresVista.java`, he creado un segundo `@BindingAdapter("accionHecho")` que captura
  `EditorInfo.IME_ACTION_DONE` y `KeyEvent.KEYCODE_ENTER` y delega la acción en
  `controlador.onCalcularClick()`.