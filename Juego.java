import java.util.ArrayList;
import java.util.List;
 
public class Juego {
    private Interfaz ui;
    private Marcador record;
 
    // Intensidades de los 3 oponentes
    private static final double[] INTENSIDADES = {0.2, 0.3, 0.4};
 
    public Juego(Interfaz ui) {
        this.ui = ui;
        this.record = Marcador.cargar();
    }
 
    public void iniciar() {
        while (true) {
            mostrarMenuPrincipal();
            int opcion = ui.pedirEntero("Seleccione una opción:", 1, 3);
            switch (opcion) {
                case 1:
                    mostrarHistorial();
                    break;
                case 2:
                    jugarPartida();
                    break;
                case 3:
                    ui.mostrar("¡Hasta luego!");
                    return;
            }
        }
    }
 
    private void mostrarMenuPrincipal() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Bienvenido al Dojo de Supervivencia ===\n\n");
        sb.append("1. Ver el historial del mejor juego hasta el momento.\n");
        sb.append("2. Enfrentar al oponente 1 para iniciar su juego.\n");
        sb.append("3. Salir y abandonar el juego.\n");
        ui.mostrar(sb.toString());
    }
 
    private void mostrarHistorial() {
        if (record == null) {
            ui.mostrar("No hay ningún récord registrado todavía.");
        } else {
            ui.mostrar("=== Mejor Marcador ===\n" + record.toString());
        }
    }
 
    private void jugarPartida() {
        // Registrar equipo
        String nombreEquipo = ui.pedirTexto("Nombre del equipo:");
        String nombre1 = ui.pedirTexto("Nombre del Jugador 1:");
        String nombre2 = ui.pedirTexto("Nombre del Jugador 2:");
        String nombre3 = ui.pedirTexto("Nombre del Jugador 3:");
 
        Equipo equipo = new Equipo(nombreEquipo, nombre1, nombre2, nombre3);
 
        // Crear 3 oponentes
        List<Oponente> oponentes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            oponentes.add(new Oponente(i + 1, INTENSIDADES[i]));
        }
 
        int oponentesDerrotados = 0;
        int totalCombos = 0;
        boolean abandonado = false;
 
        for (int i = 0; i < 3; i++) {
            Oponente oponente = oponentes.get(i);
            equipo.reiniciarEnergia();
 
            ui.mostrar("=== ¡Enfrentando al Oponente " + oponente.getNumero() + "! ===");
 
            // Batalla contra este oponente
            boolean resultado = batallarContraOponente(equipo, oponente);
            if (resultado == false) {
                // Usuario eligió abandonar
                abandonado = true;
                break;
            }
 
            totalCombos += getCombosUsados(equipo, oponente);
 
            if (oponente.isDerrotado()) {
                oponentesDerrotados++;
                ui.mostrar("¡Han derrotado al oponente " + oponente.getNumero() + "!");
                if (i < 2) {
                    // Pasar al siguiente oponente
                } else {
                    ui.mostrar("¡Han derrotado a todos los oponentes! ¡El equipo gana!");
                }
            }
 
            // Verificar si quedaron jugadores activos
            if (equipo.sinJugadoresActivos()) {
                ui.mostrar("Todos los jugadores han sido derrotados. El equipo pierde.");
                break;
            }
 
            // Si la energía llegó a 0 o menos Y no hay oponente derrotado a tiempo
            // (la energía se evalúa por oponente, ya fue evaluada dentro de la batalla)
        }
 
        // Mostrar información completa al final
        mostrarInfoFinal(equipo);
 
        // Guardar marcador si aplica
        int jugadoresActivos = equipo.getJugadoresActivos().size();
        Marcador nuevoMarcador = new Marcador(nombreEquipo, oponentesDerrotados, jugadoresActivos, totalCombos);
 
        if (record == null || nuevoMarcador.esMejorQue(record)) {
            record = nuevoMarcador;
            Marcador.guardar(record);
            ui.mostrar("¡Nuevo récord registrado!\n" + record.toString());
        } else {
            ui.mostrar("Partida finalizada.\n" + nuevoMarcador.toString());
            ui.mostrar("Récord actual:\n" + record.toString());
        }
    }
 
    // Lleva el conteo de combos por partida de forma simple
    private int combosEnPartida = 0;
 
    private int getCombosUsados(Equipo equipo, Oponente oponente) {
        return combosEnPartida; // retornado al final de cada batalla
    }
 
    /**
     * Batalla del equipo contra un oponente.
     * Retorna true si terminó normalmente (victoria o derrota por energía/jugadores),
     * false si el usuario abandonó.
     */
    private boolean batallarContraOponente(Equipo equipo, Oponente oponente) {
        combosEnPartida = 0;
 
        while (!oponente.isDerrotado()) {
            // Verificar condición de fin
            if (equipo.sinJugadoresActivos()) {
                ui.mostrar("Todos los jugadores han sido derrotados.");
                return true;
            }
            if (equipo.getEnergia() <= 0) {
                ui.mostrar("Se acabó la energía del equipo.");
                // Reducir intensidad del oponente a la mitad si un jugador fue derrotado
                // (ver reglas: si energía llega a 0 por derrota de jugador, intensidad baja a la mitad)
                return true;
            }
 
            mostrarEstado(equipo, oponente, combosEnPartida);
 
            int opcion = ui.pedirEntero("Seleccione:", 1, 2);
            if (opcion == 2) {
                // Abandonar
                return false;
            }
 
            // Realizar combo
            boolean comboRealizado = realizarCombo(equipo, oponente);
            if (comboRealizado) {
                combosEnPartida++;
                equipo.consumirEnergia(oponente.getIntensidad());
 
                // Verificar jugadores derrotados
                for (Jugador j : equipo.getJugadores()) {
                    j.verificarDerrota();
                }
 
                // Si un jugador fue derrotado, reducir intensidad del oponente a la mitad
                // (según las reglas, se menciona cuando la energía llega a 0 por derrota de jugador)
            }
        }
        return true;
    }
 
    private void mostrarEstado(Equipo equipo, Oponente oponente, int combosRealizados) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Oponente: %d | Intensidad: %.0f%% | Energía: %.0f%%\n",
                oponente.getNumero(),
                oponente.getIntensidad() * 100,
                equipo.getEnergia() * 100));
        sb.append("Combos realizados: ").append(combosRealizados).append("\n");
        sb.append("Equipo: ").append(equipo.getNombre()).append("\n\n");
 
        for (Jugador j : equipo.getJugadores()) {
            sb.append(j.getNombre()).append(" - Estado: ")
              .append(j.isDerrotado() ? "Derrotado" : "Activo").append("\n");
            sb.append(String.format("  Vida cartas: Aire %.0f%% | Tierra %.0f%% | Agua %.0f%%\n",
                    j.getCarta(Carta.Tipo.AIRE).getVida() * 100,
                    j.getCarta(Carta.Tipo.TIERRA).getVida() * 100,
                    j.getCarta(Carta.Tipo.AGUA).getVida() * 100));
        }
 
        sb.append("\n1. Realizar un combo\n2. Abandonar el juego");
        ui.mostrar(sb.toString());
    }
 
    /**
     * Realiza un combo: el usuario selecciona para cada carta del oponente
     * una carta de un jugador activo (cada jugador activo debe aportar al menos una).
     */
    private boolean realizarCombo(Equipo equipo, Oponente oponente) {
        List<Jugador> activos = equipo.getJugadoresActivos();
        List<Carta> cartasOponente = oponente.getCartas();
 
        ui.mostrar("=== Definir Combo ===");
        ui.mostrar("El oponente tiene 3 cartas (tipos ocultos). Debe asignar una carta de jugador a cada carta del oponente.");
        ui.mostrar("Regla: cada jugador activo debe aportar AL MENOS una carta.");
 
        // Asignar carta de jugador a cada carta del oponente
        // Índices: carta 1, 2, 3 del oponente
        Carta[] asignaciones = new Carta[3]; // una por carta del oponente
        int[] jugadorAsignado = new int[3];   // qué jugador (índice en activos) aporta cada carta
 
        for (int i = 0; i < 3; i++) {
            // Pedir al usuario qué jugador activo y qué tipo de carta usa contra la carta i+1 del oponente
            StringBuilder prompt = new StringBuilder();
            prompt.append("Contra la carta ").append(i + 1).append(" del oponente, ¿qué jugador activo aporta la carta?\n");
            for (int j = 0; j < activos.size(); j++) {
                prompt.append((j + 1)).append(". ").append(activos.get(j).getNombre()).append("\n");
            }
            int jugIdx = ui.pedirEntero(prompt.toString(), 1, activos.size()) - 1;
 
            // Pedir tipo de carta
            Jugador jugSelec = activos.get(jugIdx);
            StringBuilder promptTipo = new StringBuilder();
            promptTipo.append("¿Qué tipo de carta usa ").append(jugSelec.getNombre()).append("?\n");
            promptTipo.append("1. AIRE (vida: ").append(String.format("%.0f%%", jugSelec.getCarta(Carta.Tipo.AIRE).getVida() * 100)).append(")\n");
            promptTipo.append("2. TIERRA (vida: ").append(String.format("%.0f%%", jugSelec.getCarta(Carta.Tipo.TIERRA).getVida() * 100)).append(")\n");
            promptTipo.append("3. AGUA (vida: ").append(String.format("%.0f%%", jugSelec.getCarta(Carta.Tipo.AGUA).getVida() * 100)).append(")");
            int tipoIdx = ui.pedirEntero(promptTipo.toString(), 1, 3);
            Carta.Tipo tipo = tipoIdx == 1 ? Carta.Tipo.AIRE : tipoIdx == 2 ? Carta.Tipo.TIERRA : Carta.Tipo.AGUA;
 
            asignaciones[i] = jugSelec.getCarta(tipo);
            jugadorAsignado[i] = jugIdx;
        }
 
        // Verificar que todos los jugadores activos aporten al menos una carta
        boolean[] jugadorAporto = new boolean[activos.size()];
        for (int i = 0; i < 3; i++) {
            jugadorAporto[jugadorAsignado[i]] = true;
        }
        for (int j = 0; j < activos.size(); j++) {
            if (!jugadorAporto[j]) {
                ui.mostrar("Error: el jugador " + activos.get(j).getNombre() + " no aportó ninguna carta. Combo inválido.");
                return false;
            }
        }
 
        // Pedir cantidad de ataques
        int numAtaques = ui.pedirEntero("¿Cuántos ataques tendrá el combo?", 1, 10);
 
        // Ejecutar combo: por cada ataque, cada par de cartas se enfrenta
        StringBuilder resumen = new StringBuilder("=== Resultado del Combo ===\n");
        for (int ataque = 0; ataque < numAtaques; ataque++) {
            for (int i = 0; i < 3; i++) {
                Carta cartaJugador = asignaciones[i];
                Carta cartaOponente = cartasOponente.get(i);
 
                if (!cartaJugador.tienePoder() || !cartaOponente.tienePoder()) continue;
 
                // Carta jugador afecta a carta oponente
                cartaOponente.recibirAtaque(cartaJugador);
 
                // Si son del mismo tipo, es recíproco (carta oponente también afecta al jugador)
                if (cartaJugador.getTipo() == cartaOponente.getTipo()) {
                    cartaJugador.recibirAtaque(cartaOponente);
                }
                // Si carta oponente afecta a carta jugador (relación normal)
                else if (Carta.afecta(cartaOponente.getTipo(), cartaJugador.getTipo())) {
                    cartaJugador.recibirAtaque(cartaOponente);
                }
            }
        }
 
        // Mostrar vida resultante
        for (int i = 0; i < 3; i++) {
            resumen.append(String.format("Carta %d oponente: %.0f%% vida | Carta jugador: %s (%.0f%% vida)\n",
                    i + 1,
                    cartasOponente.get(i).getVida() * 100,
                    asignaciones[i].getTipo().name(),
                    asignaciones[i].getVida() * 100));
        }
        ui.mostrar(resumen.toString());
        return true;
    }
 
    private void mostrarInfoFinal(Equipo equipo) {
        StringBuilder sb = new StringBuilder("=== Información Final de Cartas ===\n");
        for (Jugador j : equipo.getJugadores()) {
            sb.append("\n").append(j.getNombre()).append(":\n");
            for (Carta c : j.getCartas()) {
                sb.append(String.format("  %s - Vida: %.0f%% | Ataque: %.2f | Defensa: %.2f\n",
                        c.getTipo().name(),
                        c.getVida() * 100,
                        c.getAtaque(),
                        c.getDefensa()));
            }
        }
        ui.mostrar(sb.toString());
    }
}
