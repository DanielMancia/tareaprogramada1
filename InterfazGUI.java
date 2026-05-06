import javax.swing.JOptionPane;
 
public class InterfazGUI implements Interfaz {
 
    @Override
    public void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Dojo de Supervivencia", JOptionPane.INFORMATION_MESSAGE);
    }
 
    @Override
    public String pedirTexto(String prompt) {
        String resultado = JOptionPane.showInputDialog(null, prompt, "Dojo de Supervivencia", JOptionPane.QUESTION_MESSAGE);
        return resultado == null ? "" : resultado.trim();
    }
 
    @Override
    public int pedirEntero(String prompt, int min, int max) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, prompt + " (" + min + "-" + max + ")",
                    "Dojo de Supervivencia", JOptionPane.QUESTION_MESSAGE);
            if (input == null) continue;
            try {
                int val = Integer.parseInt(input.trim());
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            JOptionPane.showMessageDialog(null, "Por favor ingrese un número entre " + min + " y " + max + ".");
        }
    }
}
