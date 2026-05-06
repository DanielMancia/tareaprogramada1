import java.util.ArrayList;
import java.util.List;
 
public class Equipo {
    private String nombre;
    private List<Jugador> jugadores;
    private double energia; // se reinicia con cada oponente
 
    public Equipo(String nombre, String nombre1, String nombre2, String nombre3) {
        this.nombre = nombre;
        jugadores = new ArrayList<>();
        jugadores.add(new Jugador(nombre1));
        jugadores.add(new Jugador(nombre2));
        jugadores.add(new Jugador(nombre3));
        this.energia = 1.0;
    }
 
    public void reiniciarEnergia() {
        this.energia = 1.0;
    }
 
    public void consumirEnergia(double intensidad) {
        this.energia -= intensidad;
    }
 
    public List<Jugador> getJugadoresActivos() {
        List<Jugador> activos = new ArrayList<>();
        for (Jugador j : jugadores) {
            if (!j.isDerrotado()) activos.add(j);
        }
        return activos;
    }
 
    public boolean sinJugadoresActivos() {
        return getJugadoresActivos().isEmpty();
    }
 
    public String getNombre() { return nombre; }
    public List<Jugador> getJugadores() { return jugadores; }
    public double getEnergia() { return energia; }
}
