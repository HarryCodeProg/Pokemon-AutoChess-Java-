package modelo.coreJuego;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import vista.coreJuegoGUI.Banca;
import modelo.coreJuego.Jugador;
import vista.coreJuegoGUI.Tablero;

public class Juego {
    private int fase = 1;
    private int ronda = 0;
    private boolean esPVE;
    private Banca banca;
    private Tablero tablero;
    private Jugador jugador;
    private int[] xpNecesaria = new int[10];
    private JLabel labelXP;
    private JPanel panelXP;
    private ArrayList<Jugador> jugadores;
    private Tienda tienda;

    public Juego(Jugador jugador,Tienda tienda,Banca ban,Tablero tab) {
        this.jugador = jugador;
        this.tienda = tienda;
        this.banca = ban;
        this.tablero = tab;
        inicializarComponentes();
        setXpNecesaria();
    }

    public void iniciarRonda(){

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

        int numRectangulos = 10; // Supongamos que la barra se divide en 10 segmentos
        double ratioXP = (double) xpActual / xpNecesariaProximoNivel; // Proporción de XP acumulada

        for (int i = 0; i < numRectangulos; i++) {
            JPanel rectangulo = new JPanel();
            if (i < ratioXP * numRectangulos) {
                rectangulo.setBackground(Color.BLUE); // Los segmentos llenos se pintan de azul
            } else {
                rectangulo.setBackground(Color.LIGHT_GRAY); // Los vacíos de gris claro
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

    public void agregarJugador(Jugador jugador){
        jugadores.add(jugador);
    }

    public void empezarRondaPelea() {

    }

    public void rondasPve(){
        if (fase == 1){
            switch (ronda){
                case 1:{
                    agregarRattata(ronda);
                    break;
                }
                case 2:{
                    agregarRattata(ronda);
                    break;
                }
                case 3:{
                    agregarRattata(ronda);
                    break;
                }
            }
        }else if(fase <=3){
            switch (ronda){
                case 5: {
                    agregarRattata(ronda);
                    break;
                }
            }
        }else{
            switch (ronda){
                case 5: {
                    agregarRegigigas();
                    break;
                }
            }
        }

    }

    public void agregarRattata(int cantidad){
        for (int i = 0; i <cantidad;i++){
            tablero.añadirAlTableroPorCoor(1,i,tienda.getRattata());
        }
    }

    public void agregarRegigigas(){
        tablero.añadirAlTableroPorCoor(1,2,tienda.getRegigigas());
    }

    public void subirRonda(){
        if (ronda == 5){
            ronda = 1;
            fase++;
        }else{
          ronda++;
        }
        jugador.actualizarMonedasRonda();
        rondasPve();
    }

    public boolean todosListosParaNuevaRonda() {
        for (Jugador jugador : jugadores) {
            if (!jugador.estaListoParaRonda()) {
                return false;
            }
        }
        return true;
    }

    public void reiniciarEstadoDeRonda() {
        for (Jugador jugador : jugadores) {
            jugador.setListoParaRonda(false);
        }
    }

    public int getFase(){
        return this.fase;
    }

    public int getRonda(){
        return this.ronda;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public JLabel getLabelXP() {
        return labelXP;
    }

    public JPanel getPanelXP() {
        return panelXP;
    }
}
