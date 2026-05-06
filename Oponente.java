import java.util.ArrayList;
import java.util.List;
 
public class Oponente {
    private int numero;
    private double intensidad;
    private List<Carta> cartas;
 
    public Oponente(int numero, double intensidad) {
        this.numero = numero;
        this.intensidad = intensidad;
        this.cartas = new ArrayList<>();
        // 3 cartas aleatorias (tipos pueden repetirse)
        Carta.Tipo[] tipos = Carta.Tipo.values();
        for (int i = 0; i < 3; i++) {
            Carta.Tipo tipo = tipos[(int)(Math.random() * tipos.length)];
            cartas.add(new Carta(tipo));
        }
    }
 
    public boolean isDerrotado() {
        for (Carta c : cartas) {
            if (c.tienePoder()) return false;
        }
        return true;
    }
 
    public int getNumero() { return numero; }
    public double getIntensidad() { return intensidad; }
    public List<Carta> getCartas() { return cartas; }
    public Carta getCarta(int index) { return cartas.get(index); }
}
