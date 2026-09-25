import javax.swing.*;

public class Main{
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JuegoEscritura ventana = new JuegoEscritura();
            ventana.setVisible(true);
        });
    }
}