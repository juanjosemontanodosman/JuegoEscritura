import javax.swing.*;
import java.awt.*;

public class JuegoEscritura extends JFrame{
    private JLabel etiquetaPalabra;
    private JTextField campoTexto;
    private JLabel etiquetaNivel;
    private JLabel etiquetaTiempo;
    private JProgressBar barraTiempo;

    public JuegoEscritura() {
        setTitle("Escritura rapida");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Panel superior
        etiquetaNivel = new JLabel("Nivel 1", SwingConstants.CENTER);
        etiquetaNivel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(etiquetaNivel, BorderLayout.NORTH);

        // Panel central
        etiquetaPalabra = new JLabel("palabra", SwingConstants.CENTER);
        etiquetaPalabra.setFont(new Font("Times New Roman", Font.BOLD, 36));
        etiquetaPalabra.setForeground(Color.BLUE);
        add(etiquetaPalabra, BorderLayout.CENTER);

        //campoTexto.set...()....
    }
}
