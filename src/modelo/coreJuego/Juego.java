package modelo.coreJuego;

import controlador.Observador;
import vista.coreJuegoGUI.FichaClickeableGUI;
import vista.coreJuegoGUI.TableroGUI;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Juego {
    private int fase = 1;
    private int ronda = 0;
    private boolean esPVE;
    private ArrayList<Jugador> jugadores;
    private Tienda tienda;
    private ArrayList<Jugador> ordenDeRivales;
    private int indiceRival;
    private EstadosJuego estadoActual = EstadosJuego.INICIO_PARTIDA;
    private Timer timer;
    private ArrayList<Observador> observadores = new ArrayList<>();

    public Juego(Tienda tienda) {
        this.jugadores = new ArrayList<>();
        this.tienda = tienda;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {this.jugadores = jugadores;}

    public void setOrdenDeRivales(){
        this.ordenDeRivales = new ArrayList<>(jugadores);
        //this.ordenDeRivales.remove(jugador);
        Collections.shuffle(this.ordenDeRivales);
        this.indiceRival = 0;
    }

    public void iniciarPreparacion() {
        estadoActual = EstadosJuego.PREPARACION;
        notificarObservadores();
        iniciarTemporizador();
    }

    private void iniciarTemporizador() {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = 15;
            @Override public void actionPerformed(ActionEvent e) {
                tiempoRestante--;
                notificarTiempoRestante(tiempoRestante);
                if (tiempoRestante <= 0) {
                    ((Timer)e.getSource()).stop();
                    comenzarPelea();
                } } }
        );
        timer.start();
    }

    public void comenzarPelea() {
        estadoActual = EstadosJuego.PELEA;
        notificarObservadores();
        rondas();
    }

    public Jugador elegirSiguienteRival() {
        Jugador rival = ordenDeRivales.get(indiceRival);
        indiceRival = (indiceRival + 1) % ordenDeRivales.size();
        return rival;
    }

    public void agregarJugador(Jugador jugador){jugadores.add(jugador);}

    public void sacarJugador(Jugador jugador){jugadores.remove(jugador);}

    public void agregarRattata(int cantidad,Jugador jugador){
        for (int i = 0; i <cantidad;i++){
                jugador.getTablero().añadirAlTableroPorCoor(1,i+1,tienda.getRattata());
        }
    }

    public void agregarRegigigas(Jugador jugador){jugador.getTablero().añadirAlTableroPorCoor(1,2,tienda.getRegigigas());}

    public void subirRonda(){
        if (ronda == 5){
            ronda = 1;
            fase++;
        }else{
          ronda++;
        }
        estadoActual = EstadosJuego.FIN_DE_RONDA;
        //notificar();
        notificarObservadores();
        for (Jugador jugador : jugadores){
            jugador.actualizarMonedasRonda();
        }
        //rondas();
        iniciarPreparacion();
    }

    public boolean todosListosParaNuevaRonda() {
        for (Jugador jugador : jugadores) {
            if (!jugador.estaListoParaRonda()) {
                return false;
            }
        }
        return true;
    }

    public void cambiarListosNull(){
        for (Jugador jugador : jugadores) {
            jugador.setListoParaRonda(false);
        }
    }

    public void reiniciarEstadoDeRonda() {
        for (Jugador jugador : jugadores) {
            jugador.setListoParaRonda(false);
        }
    }

    public void iniciarRondaPvp() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador1 = jugadores.get(i);
            Jugador jugador2 = jugadores.get((i + 1) % jugadores.size());

            transferirFichasPvp(jugador1, jugador2);

            Combate combate = new Combate(jugador1.getTablero().getTableroCore(), jugador2.getTablero().getTableroCore());
            combate.iniciarCombate();
        }
    }

    private void transferirFichasPvp(Jugador jugador1, Jugador jugador2) {
        TableroGUI tableroDestino = jugador1.getTablero();
        TableroGUI tableroOrigen = jugador2.getTablero();
        tableroDestino.limpiarTableroEnemigo();

        for (int fila = 3; fila <= 5; fila++) {
            for (int columna = 0; columna <= 5; columna++) {
                if (tableroOrigen.estaCeldaOcupada(fila, columna)) {
                    FichaClickeableGUI ficha = tableroOrigen.getFichaEnCelda(fila, columna);
                    ficha.getFicha().esFichaEnemiga();
                    int filaOpuesta = 5 - fila;
                    int columnaOpuesta = 5 - columna;
                    tableroDestino.añadirAlTableroPorCoor(filaOpuesta, columnaOpuesta, ficha.getFicha());
                }
            }
        }
    }

    public int getFase(){return this.fase;}

    public int getRonda(){return this.ronda;}

    public EstadosJuego getEstadoActual(){return this.estadoActual;}

    public void iniciarRonda() {iniciarPreparacion();}

    public void finDeRonda() {
        estadoActual = EstadosJuego.FIN_DE_RONDA;
        notificarObservadores();
        if (verificarFinPartida()) {
            finDePartida();
        } else {
            subirRonda();
            iniciarPreparacion();
        }
    }

    public void finDePartida() {
        estadoActual = EstadosJuego.FIN_PARTIDA;
        notificarObservadores();
    }

    private boolean verificarFinPartida() {
        int jugadoresConVida = 0;
        for (Jugador jugador : jugadores) {
                jugadoresConVida++;
        }
        return jugadoresConVida <= 1;
    }

    public void rondas(){
        if (fase == 1){
            switch (ronda){
                case 1:
                case 2:
                case 3:{
                    for (Jugador jugador : jugadores){
                        agregarRattata(ronda,jugador);
                    }
                    break;
                }
                case 4:
                case 5:{
                    iniciarRondaPvp();
                    break;
                }
            }
        }else if(fase <=3){
            switch (ronda){
                case 1:
                case 2:
                case 3:
                case 4:{
                    iniciarRondaPvp();
                    break;
                }
                case 5: {
                    for (Jugador jugador : jugadores){
                        agregarRattata(ronda,jugador);
                    }
                    break;
                }
            }
        }else{
            switch (ronda){
                case 1:
                case 2:
                case 3:
                case 4:{
                    iniciarRondaPvp();
                    break;
                }
                case 5: {
                    for (Jugador jugador : jugadores){
                        agregarRegigigas(jugador);
                    }
                    break;
                }
            }
        }

    }

    public void agregarObservador(Observador observador) { observadores.add(observador); }
    public void eliminarObservador(Observador observador) { observadores.remove(observador); }
    public void notificarObservadores() {
        for (Observador observador : observadores) {
                        observador.actualizarRasgos();
                        observador.notificarRonda();
        }
    }


    public void notificarRonda() {
        for (Observador observador : observadores) {
            observador.notificarRonda();
        }
    }

    public void notificarMensaje(String mensaje) {
        for (Observador observador : observadores) {
            observador.notificarMensaje(mensaje);
        }
    }

    public void notificarError(String mensaje) {
        for (Observador observador : observadores) {
            observador.notificarError(mensaje);
        }
    }

    public void notificarTiempoRestante(int segundos) {
        for (Observador observador : observadores) {
            observador.actualizarTiempo(segundos);
        }
    }

}

