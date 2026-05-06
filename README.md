# tareaprogramada1

Daniel Mancia C34504

Instrucciones de compilación

Compilar desde línea de comandos

Desde la carpeta raíz del proyecto:

```bash
javac *.java
```

Todos los archivos `.class` quedarán en el mismo directorio.

Instrucciones de ejecución

Modo consola 

```bash
java Main
```

Modo interfaz gráfica 

```bash
java Main -gui
```

Instrucciones de uso

Al iniciar el programa se muestra el menú principal con tres opciones:

1. Ver historial — muestra el mejor marcador guardado de partidas anteriores.
2. Jugar partida — inicia una nueva partida solicitando el nombre del equipo y de los tres jugadores.
3. Salir — termina la ejecución del programa.

Durante una partida

- Se ingresa el nombre del equipo y los tres jugadores.
- El equipo enfrenta tres oponentes en orden creciente de dificultad (intensidades 20%, 30%, 40%).
- En cada turno se puede realizar un combo o abandonar la partida.
- Al realizar un combo, se asigna una carta de un jugador activo a cada carta del oponente. Cada jugador activo debe aportar al menos una carta.
- Se elige la cantidad de ataques (1–10) y se resuelven los enfrentamientos.
- La energía del equipo disminuye con cada combo según la intensidad del oponente.

Reglas de afectación elemental

| Atacante | Afecta a |
|----------|----------|
| AIRE     | TIERRA   |
| TIERRA   | AGUA     |
| AGUA     | AIRE     |
| Mismo tipo | Ambas cartas se afectan mutuamente |

Condiciones de fin de partida

- El equipo gana si derrota los tres oponentes (todas sus cartas llegan a 0).
- El equipo pierde si todos sus jugadores son derrotados o la energía llega a 0.
- El usuario puede abandonar en cualquier momento.

Marcador

Al finalizar cada partida se compara el resultado con el mejor marcador previo. Los criterios de comparación (en orden de prioridad) son:

1. Mayor número de oponentes derrotados.
2. Mayor número de jugadores activos al terminar.
3. Menor número de combos utilizados.

El mejor marcador se guarda automáticamente en el archivo `record.dat`.



Reporte de errores y mejoras pendientes

Errores conocidos

1. Conteo de combos incorrecto al acumular total**: el método `getCombosUsados()` retorna `combosEnPartida`, variable que se reinicia a 0 al inicio de cada batalla contra un oponente. Como consecuencia, `totalCombos` en el `Marcador` final siempre almacena únicamente los combos del último oponente enfrentado, y no el acumulado real de toda la partida.

2. `Marcador` no declara `serialVersionUID`: la clase `Marcador` implementa `Serializable` pero no define `serialVersionUID`. Si el código se modifica y se recompila, archivos `record.dat` generados con versiones anteriores pueden volverse incompatibles, lanzando `InvalidClassException` al intentar cargarlos.

Mejoras pendientes

1. Reducción de intensidad al derrotar un jugador: las reglas del juego establecen que cuando un jugador es derrotado, la intensidad del oponente actual debería reducirse a la mitad. Esta lógica está indicada en comentarios del código pero no se implementó.

2. Tipos de cartas del oponente ocultos: el UI informa al usuario que los tipos del oponente son "ocultos", pero la lógica interna los asigna desde el inicio. Podría añadirse un mecanismo real de revelación progresiva de tipos para mayor profundidad estratégica.

3. Validación de nombre vacío: no se valida que el nombre del equipo o de los jugadores sean cadenas no vacías, lo que podría generar marcadores con nombres en blanco.

4. Energía negativa no limitada: la energía puede quedar en valores negativos (por debajo de 0) ya que se verifica la condición `<= 0` solo al inicio del siguiente turno, no inmediatamente después de consumirla.
