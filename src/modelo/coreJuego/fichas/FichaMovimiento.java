package modelo.coreJuego.fichas;

public class FichaMovimiento {
    private int filaOriginal;
    private int columnaOriginal;
    private int fila;
    private int columna;
    private int alcance;

    public FichaMovimiento(int alcance) {
        this.alcance = alcance;
    }

    public boolean estaEnAlcance(Ficha enemigo) {
        int distanciaFila = Math.abs(this.fila - enemigo.getMovimiento().getFila());
        int distanciaColumna = Math.abs(this.columna - enemigo.getMovimiento().getColumna());
        return distanciaFila <= alcance && distanciaColumna <= alcance;
    }

    public void moverFichaHaciaEnemigo(Ficha[][] tablero, int filaActual, int colActual) {
        Ficha enemigoCercano = buscarEnemigoCercano(tablero, filaActual, colActual);
        if (enemigoCercano != null) {
            int filaEnemigo = enemigoCercano.getMovimiento().getFila();
            int colEnemigo = enemigoCercano.getMovimiento().getColumna();

            // Intentar moverse hacia el enemigo evitando bloqueos
            if (filaActual > filaEnemigo && puedeMoverAdelante(tablero)) {
                moverAdelante();
            } else if (colActual > colEnemigo && puedeMoverIzquierda(tablero)) {
                moverIzquierda();
            } else if (colActual < colEnemigo && puedeMoverDerecha(tablero)) {
                moverDerecha();
            } else if (filaActual < filaEnemigo && puedeMoverAtras(tablero)) {
                moverAtras();
            }
        }
    }

    public Ficha buscarEnemigoCercano(Ficha[][] tablero, int filaActual, int colActual) {
        for (int i = 1; i <= alcance; i++) {
            int filaEnemigo = filaActual - i;
            if (filaEnemigo >= 0) {
                for (int col = 0; col < tablero[0].length; col++) {
                    if (tablero[filaEnemigo][col] != null && tablero[filaEnemigo][col].getFichaEnemiga()) {
                        return tablero[filaEnemigo][col];
                    }
                }
            }
        }
        return null;
    }


    private boolean puedeMoverAdelante(Ficha[][] tablero) {return fila > 0 && tablero[fila - 1][columna] == null;}

    private boolean puedeMoverIzquierda(Ficha[][] tablero) {return columna > 0 && tablero[fila][columna - 1] == null;}

    private boolean puedeMoverDerecha(Ficha[][] tablero) {return columna < tablero[0].length - 1 && tablero[fila][columna + 1] == null;}

    private boolean puedeMoverAtras(Ficha[][] tablero) {return fila < tablero.length - 1 && tablero[fila + 1][columna] == null;}

    public void setFila(int x) {
        this.filaOriginal= x;
        this.fila = x;
    }

    public void setColumna(int y) {
        this.columnaOriginal = y;
        this.columna = y;
    }

    public int getAlcance() {
        return alcance;
    }

    public void moverAdelante(){
        this.fila -= 1;
    }

    public void moverAtras(){
        this.fila += 1;
    }

    public void moverIzquierda(){this.columna -= 1;}

    public void moverDerecha(){this.columna += 1;}

    public int getFilaOriginal() {
        return filaOriginal;
    }

    public int getColumnaOriginal() {
        return columnaOriginal;
    }

    public int getFila() {return fila;}

    public int getColumna() {return columna;}
}
