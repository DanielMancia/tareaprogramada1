public class Main {
    public static void main(String[] args) {
        boolean useGui = false;
        for (String arg : args) {
            if (arg.equals("-gui")) useGui = true;
        }
        Interfaz interfaz = useGui ? new InterfazGUI() : new InterfazConsola();
        Juego juego = new Juego(interfaz);
        juego.iniciar();
    }
}
 
