package modelo.coreJuego;

import modelo.coreJuego.Jugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EstadoDelJuego implements Serializable {
    private ArrayList<Jugador> jugadores;
    private int rondaActual;

    public EstadoDelJuego(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.rondaActual = 1; // Comienza en la primera ronda
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public void avanzarRonda() {
        this.rondaActual++;
    }

    public void ordenarJugadoresPorVida() {
        Collections.sort(jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getVida(), j1.getVida());
            }
        });
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
}
