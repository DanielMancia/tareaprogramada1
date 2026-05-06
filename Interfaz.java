import java.util.List;
 
public interface Interfaz {
    /** Muestra un mensaje al usuario */
    void mostrar(String mensaje);
 
    /** Pide una cadena de texto */
    String pedirTexto(String prompt);
 
    /** Pide un entero entre min y max */
    int pedirEntero(String prompt, int min, int max);
}
 
