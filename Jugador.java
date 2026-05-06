import java.util.ArrayList;
import java.util.List;
 
public class Jugador {
    private String nombre;
    private Carta cartaAire;
    private Carta cartaTierra;
    private Carta cartaAgua;
    private boolean derrotado;
 
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.cartaAire   = new Carta(Carta.Tipo.AIRE,   this);
        this.cartaTierra = new Carta(Carta.Tipo.TIERRA, this);
        this.cartaAgua   = new Carta(Carta.Tipo.AGUA,   this);
        this.derrotado = false;
    }
 
    public void verificarDerrota() {
        if (!cartaAire.tienePoder() && !cartaTierra.tienePoder() && !cartaAgua.tienePoder()) {
            derrotado = true;
        }
    }
 
    public boolean isDerrotado() { return derrotado; }
    public String getNombre() { return nombre; }
 
    public Carta getCarta(Carta.Tipo tipo) {
        switch (tipo) {
            case AIRE:   return cartaAire;
            case TIERRA: return cartaTierra;
            case AGUA:   return cartaAgua;
        }
        return null;
    }
 
    public List<Carta> getCartas() {
        List<Carta> lista = new ArrayList<>();
        lista.add(cartaAire);
        lista.add(cartaTierra);
        lista.add(cartaAgua);
        return lista;
    }
}
