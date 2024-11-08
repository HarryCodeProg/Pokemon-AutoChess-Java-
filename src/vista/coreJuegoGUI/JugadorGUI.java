package vista.coreJuegoGUI;

import modelo.coreJuego.Jugador;

import javax.swing.*;
import java.awt.*;

public class JugadorGUI {
    private int[] xpNecesaria = new int[10];
    private JLabel labelXP;
    private JPanel panelXP;
    private Jugador jugador;

    public JugadorGUI(Jugador jugador){
        this.jugador = jugador;
        inicializarComponentes();
        setXpNecesaria();
    }

    public void inicializarComponentes() {
        labelXP = new JLabel("XP: 0 / 2");
        panelXP = new JPanel();
        panelXP.setLayout(new FlowLayout(FlowLayout.LEFT));

        actualizarExperienciaVisual();
    }

    public void setXpNecesaria() {
        xpNecesaria[0] = 4;
        xpNecesaria[1] = 4;
        xpNecesaria[2] = 4;
        xpNecesaria[3] = 6;
        xpNecesaria[4] = 10;
        xpNecesaria[5] = 20;
        xpNecesaria[6] = 36;
        xpNecesaria[7] = 46;
        xpNecesaria[8] = 72;
        xpNecesaria[9] = 84;
    }

    public void actualizarExperienciaVisual() {
        if(jugador.getNivel()<10){
            int nivelActual = jugador.getNivel();

            int xpActual = jugador.getXpActual();
            int xpNecesariaProximoNivel = xpNecesaria[nivelActual - 1]; // Restamos 1 al nivel para acceder al índice correcto

            labelXP.setText("XP: " + xpActual + " / " + xpNecesariaProximoNivel);
            labelXP.setForeground(new Color(255, 255, 255, 220));
            labelXP.setBackground(new Color(0, 0, 0, 160));
            labelXP.setOpaque(true);

            panelXP.removeAll();

            int numRectangulos = 10; // la barra se divide en 10 segmentos
            double ratioXP = (double) xpActual / xpNecesariaProximoNivel; // Proporción de XP acumulada

            for (int i = 0; i < numRectangulos; i++) {
                JPanel rectangulo = new JPanel();
                if (i < ratioXP * numRectangulos) {
                    rectangulo.setBackground(Color.BLUE); // Los segmentos llenos se pintan de azul
                } else {
                    rectangulo.setBackground(Color.LIGHT_GRAY);
                }
                rectangulo.setPreferredSize(new Dimension(15, 10)); // Dimensión de cada rectángulo
                panelXP.add(rectangulo);
            }
        }else{
            panelXP.removeAll();
            labelXP.removeAll();
        }
        panelXP.revalidate();
        panelXP.repaint();
    }

    public void comprarXP(int xp) {
        jugador.sumarXp(xp);

        // Revisar si se alcanza el siguiente nivel
        while (jugador.getXpActual() >= xpNecesaria[jugador.getNivel() - 1]) { // Restamos 1 al nivel para acceder al índice correcto
            jugador.restarXp(xpNecesaria[jugador.getNivel() - 1]); // Restamos 1 al nivel para acceder al índice correcto
            jugador.subirNivel();
        }
        if (xp == 4){
            jugador.restarMonedas(4);
        }

        actualizarExperienciaVisual();
    }

    public JLabel getLabelXP() {return labelXP;}

    public JPanel getPanelXP() {return panelXP;}

}
