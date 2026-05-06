import java.io.*;
 
public class Marcador implements Serializable {
    private String nombreEquipo;
    private int oponentesDerotados;
    private int jugadoresActivos;
    private int totalCombos;
 
    public Marcador(String nombreEquipo, int oponentesDerrotados, int jugadoresActivos, int totalCombos) {
        this.nombreEquipo = nombreEquipo;
        this.oponentesDerotados = oponentesDerrotados;
        this.jugadoresActivos = jugadoresActivos;
        this.totalCombos = totalCombos;
    }
 
    /**
     * Retorna true si este marcador es mejor que 'otro'.
     * Criterios (en orden):
     * 1. Mayor cantidad de oponentes derrotados
     * 2. Mayor cantidad de jugadores activos
     * 3. Menor cantidad de combos utilizados
     */
    public boolean esMejorQue(Marcador otro) {
        if (this.oponentesDerotados != otro.oponentesDerotados)
            return this.oponentesDerotados > otro.oponentesDerotados;
        if (this.jugadoresActivos != otro.jugadoresActivos)
            return this.jugadoresActivos > otro.jugadoresActivos;
        return this.totalCombos < otro.totalCombos;
    }
 
    public String getNombreEquipo() { return nombreEquipo; }
    public int getOponentesDerrotados() { return oponentesDerotados; }
    public int getJugadoresActivos() { return jugadoresActivos; }
    public int getTotalCombos() { return totalCombos; }
 
    @Override
    public String toString() {
        return String.format("Equipo: %s | Oponentes derrotados: %d | Jugadores activos: %d | Combos usados: %d",
                nombreEquipo, oponentesDerotados, jugadoresActivos, totalCombos);
    }
 
    // Persistencia simple en archivo
    private static final String ARCHIVO = "record.dat";
 
    public static void guardar(Marcador m) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(m);
        } catch (Exception e) { /* ignorar */ }
    }
 
    public static Marcador cargar() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (Marcador) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
 
