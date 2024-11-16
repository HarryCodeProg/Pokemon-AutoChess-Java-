package modelo.coreJuego;

import modelo.coreJuego.Tablero;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Combate {
    private Tablero tableroJugador;
    private Tablero tableroEnemigo;
    private boolean combateTerminado = false;

    private ExecutorService poolDeFichas; // maneja los hilos de las fichas

    public Combate(Tablero tableroJugador, Tablero tableroEnemigo) {
        this.tableroJugador = tableroJugador;
        this.tableroEnemigo = tableroEnemigo;
        this.poolDeFichas = Executors.newCachedThreadPool();
    }

    public void iniciarCombate() {
        // iniciar el combate en ambos tableros
        tableroJugador.iniciarCombate(poolDeFichas);
        tableroEnemigo.iniciarCombate(poolDeFichas);

        // monitorear el estado del combate
        new Thread(this::monitorearCombate).start();
    }

    private void monitorearCombate() {
        while (!combateTerminado) {
            // verificar si queda un solo equipo con fichas vivas
            if (tableroJugador.equipoEliminado() || tableroEnemigo.equipoEliminado()) {
                combateTerminado = true;
                poolDeFichas.shutdownNow(); // detener todos los hilos
                anunciarGanador();
            }

            try {
                Thread.sleep(500); // reducir la carga del monitoreo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void anunciarGanador() {
        if (tableroJugador.equipoEliminado()) {
            System.out.println("¡El enemigo ganó el combate!");
        } else if (tableroEnemigo.equipoEliminado()) {
            System.out.println("¡El jugador ganó el combate!");
        }
    }
}
