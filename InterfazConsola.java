import java.util.Scanner;
 
public class InterfazConsola implements Interfaz {
    private Scanner sc = new Scanner(System.in);
 
    @Override
    public void mostrar(String mensaje) {
        System.out.println(mensaje);
    }
 
    @Override
    public String pedirTexto(String prompt) {
        System.out.print(prompt + " ");
        return sc.nextLine().trim();
    }
 
    @Override
    public int pedirEntero(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " [" + min + "-" + max + "]: ");
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            System.out.println("Por favor ingrese un número entre " + min + " y " + max + ".");
        }
    }
}
 
