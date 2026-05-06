public class Carta {
    public enum Tipo { AIRE, TIERRA, AGUA }
 
    private Tipo tipo;
    private Jugador jugador;
    private double vida;
    private double ataque;
    private double defensa;
 
    public Carta(Tipo tipo, Jugador jugador) {
        this.tipo = tipo;
        this.jugador = jugador;
        this.vida = 1.0;
        this.ataque = 0.6 + Math.random() * 0.4; // 0.6 a 1.0
        this.defensa = Math.random() * 0.5;       // 0.0 a 0.5
    }
 
    // Constructor para cartas de oponentes (ataque/defensa aleatorio igual)
    public Carta(Tipo tipo) {
        this(tipo, null);
    }
 
    public boolean tienePoder() {
        return vida > 0;
    }
 
    /**
     * Esta carta es afectada por 'atacante'.
     * vida -= vida * (ataque_atacante - defensa_esta)
     * Solo ocurre si hay relación de afectación entre tipos.
     */
    public void recibirAtaque(Carta atacante) {
        if (!atacante.tienePoder()) return;
        if (!afecta(atacante.tipo, this.tipo)) return;
        double dano = this.vida * (atacante.ataque - this.defensa);
        this.vida -= dano;
        if (this.vida < 0) this.vida = 0;
    }
 
    /**
     * Reglas de afectación:
     * AIRE -> TIERRA
     * TIERRA -> AGUA
     * AGUA -> AIRE
     * mismo tipo -> se afectan mutuamente (recíproco, manejado desde fuera)
     */
    public static boolean afecta(Tipo atacante, Tipo defensor) {
        if (atacante == defensor) return true;
        if (atacante == Tipo.AIRE && defensor == Tipo.TIERRA) return true;
        if (atacante == Tipo.TIERRA && defensor == Tipo.AGUA) return true;
        if (atacante == Tipo.AGUA && defensor == Tipo.AIRE) return true;
        return false;
    }
 
    public Tipo getTipo() { return tipo; }
    public Jugador getJugador() { return jugador; }
    public double getVida() { return vida; }
    public double getAtaque() { return ataque; }
    public double getDefensa() { return defensa; }
 
    @Override
    public String toString() {
        return tipo.name() + String.format("(vida=%.0f%%)", vida * 100);
    }
}
