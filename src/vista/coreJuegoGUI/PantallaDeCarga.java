package vista.coreJuegoGUI;

import javax.swing.*;
import java.awt.*;

public class PantallaDeCarga extends JFrame {
    private JProgressBar progressBar;

    public PantallaDeCarga() {
        setTitle("Cargando...");
        setSize(400, 200); setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JLabel etiqueta = new JLabel("Cargando, por favor espere...", SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 18)); add(etiqueta, BorderLayout.NORTH);
        progressBar = new JProgressBar(); progressBar.setMinimum(0); progressBar.setMaximum(100);
        progressBar.setStringPainted(true); add(progressBar, BorderLayout.CENTER); }

    public void actualizarProgreso(int valor) { progressBar.setValue(valor); }
    public static void main(String[] args) {
        PantallaDeCarga pantallaDeCarga = new PantallaDeCarga();
        pantallaDeCarga.setVisible(true);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i++) { Thread.sleep(50);
                    pantallaDeCarga.actualizarProgreso(i);
                }
                return null;
            } @Override protected void done() {
                pantallaDeCarga.dispose();
                JOptionPane.showMessageDialog(null, "Carga completa!"); }
           };
                worker.execute();
             }
             }
            
