package modelo.coreJuego;

import modelo.coreJuego.fichas.Ficha;

public class Combate {
    private final Tablero tableroJugador;
    private final Tablero tableroEnemigo;

    public Combate(Tablero tableroJugador, Tablero tableroEnemigo) {
        this.tableroJugador = tableroJugador;
        this.tableroEnemigo = tableroEnemigo;
    }

    public void iniciarCombate() {
        // Iterar sobre las fichas del jugador para atacar o moverse hacia fichas enemigas
        for (int fila = 3; fila <= 5; fila++) {
            for (int columna = 0; columna <= 5; columna++) {
                if (tableroJugador.estaCeldaOcupada(fila, columna)) {
                    Ficha fichaJugador = tableroJugador.getFichaEnCelda(fila, columna);
                    manejarAccionDeFicha(fichaJugador, tableroEnemigo);
                }
            }
        }

        // Iterar sobre las fichas del enemigo para atacar fichas aliadas
        for (int fila = 0; fila <= 2; fila++) {
            for (int columna = 0; columna <= 5; columna++) {
                if (tableroEnemigo.estaCeldaOcupada(fila, columna)) {
                    Ficha fichaEnemiga = tableroEnemigo.getFichaEnCelda(fila, columna);
                    manejarAccionDeFicha(fichaEnemiga, tableroJugador);
                }
            }
        }

        verificarFinDePelea();
    }

    private void manejarAccionDeFicha(Ficha ficha, Tablero tableroOponente) {
        Ficha enemigo = ficha.getMovimiento().buscarEnemigoCercano(tableroOponente.getTablero(), ficha.getMovimiento().getFila(), ficha.getMovimiento().getColumna());

        if (enemigo != null) {
            if (ficha.getMovimiento().estaEnAlcance(enemigo)) {
                ficha.atacar(enemigo);
                if (!enemigo.estaViva()) {
                    tableroOponente.eliminarFicha(enemigo.getMovimiento().getFila(), enemigo.getMovimiento().getColumna());
                }
            } else {
                ficha.getMovimiento().moverFichaHaciaEnemigo(tableroOponente.getTablero(), ficha.getMovimiento().getFila(), ficha.getMovimiento().getColumna());
            }
        }
    }

    private void verificarFinDePelea() {
        if (tableroJugador.noTieneFichas()) {
            System.out.println("El jugador ha perdido la pelea.");
        } else if (tableroEnemigo.noTieneFichas()) {
            System.out.println("El enemigo ha perdido la pelea.");
        }
    }
}
