package modelo.coreJuego.fichas;

public class FichaMovimiento {
    private int fila;
    private int columna;
    private int alcance;

    public FichaMovimiento(int alcance) {
        this.alcance = alcance;
    }

    public void moverFichaHaciaEnemigo(Ficha[][] tablero, int filaActual, int colActual) {
        Ficha enemigoCercano = buscarEnemigoCercano(tablero, filaActual, colActual);
        if (enemigoCercano != null) {
            int filaEnemigo = enemigoCercano.getMovimiento().getFila();
            int colEnemigo = enemigoCercano.getMovimiento().getColumna();

            if (filaActual > filaEnemigo) {
                moverAdelante();
            } else if (colActual > colEnemigo) {
                moverIzquierda();
            } else if (colActual < colEnemigo) {
                moverDerecha();
            }
        }
    }

    public Ficha buscarEnemigoCercano(Ficha[][] tablero, int filaActual, int colActual) {
        for (int i = 1; i <= getAlcance(); i++) {
            int filaEnemigo = filaActual - i;
            if (filaEnemigo >= 0) {
                for (int col = 0; col < tablero[0].length; col++) {
                    if (tablero[filaEnemigo][col] != null && tablero[filaEnemigo][col].getFichaEnemiga()) {
                        return tablero[filaEnemigo][col];
                    }
                }
            }
        }
        return null; // No se encontró un enemigo dentro del alcance
    }

    public void setFila(int y) {this.alcance = y;}

    public void setColumna(int x) {this.columna = x;}

    public int getAlcance() {
        return alcance;
    }

    public void moverAdelante(){
        this.fila -= 1;
    }

    public void moverAtras(){
        this.fila += 1;
    }

    public void moverIzquierda(){
        this.columna -= 1;
    }

    public void moverDerecha(){
        this.columna += 1;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
