package modelo.coreJuego.fichas;

import modelo.coreJuego.Tablero;

import java.util.*;

public class FichaMovimiento {
    private int filaOriginal;
    private int columnaOriginal;
    private int fila;
    private int columna;
    private int alcance;
    private final Set<String> celdasEnMovimiento = Collections.synchronizedSet(new HashSet<>());

    public FichaMovimiento(int alcance) {
        this.alcance = alcance;
    }

    public void moverHaciaConBFS(Tablero tablero, Ficha ficha) {
        int filas = tablero.getTablero().length;
        int columnas = tablero.getTablero()[0].length;

        // direcciones (arriba, derecha, abajo, izquierda)
        int[] dFila = {-1, 0, 1, 0};
        int[] dColumna = {0, 1, 0, -1};

        Queue<int[]> cola = new LinkedList<>();
        cola.add(new int[]{fila, columna}); // posicion inicial

        boolean[][] visitado = new boolean[filas][columnas];
        visitado[fila][columna] = true;

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int f = actual[0];
            int c = actual[1];

            // si encontramos un enemigo
            if (tablero.getFichaEnCelda(f, c) != null && tablero.getFichaEnCelda(f, c).getEquipo() != ficha.getEquipo()) {
                moverUnPasoHacia(f, c, tablero);
                return;
            }

            // Explorar celdas adyacentes
            for (int i = 0; i < 4; i++) {
                int nuevaFila = f + dFila[i];
                int nuevaColumna = c + dColumna[i];

                if (nuevaFila >= 0 && nuevaFila < filas && nuevaColumna >= 0 && nuevaColumna < columnas &&
                        !visitado[nuevaFila][nuevaColumna] && tablero.estaCeldaLibre(nuevaFila, nuevaColumna)) {
                    visitado[nuevaFila][nuevaColumna] = true;
                    cola.add(new int[]{nuevaFila, nuevaColumna});
                }
            }
        }
    }

   /* private void moverUnPasoHacia(int filaObjetivo, int columnaObjetivo, Tablero tablero) {
        int deltaFila = Integer.compare(filaObjetivo, fila);
        int deltaColumna = Integer.compare(columnaObjetivo, columna);

        int nuevaFila = fila + deltaFila;
        int nuevaColumna = columna + deltaColumna;

        if (tablero.estaCeldaLibre(nuevaFila, nuevaColumna)) {
            tablero.moverFichaCombate(fila, columna, nuevaFila, nuevaColumna);
            fila = nuevaFila;
            columna = nuevaColumna;
        }
    }*/

    private void moverUnPasoHacia(int filaObjetivo, int columnaObjetivo, Tablero tablero) {
        String destino = filaObjetivo + "," + columnaObjetivo;

        synchronized (celdasEnMovimiento) {
            if (!celdasEnMovimiento.contains(destino) && tablero.estaCeldaLibre(filaObjetivo, columnaObjetivo)) {
                celdasEnMovimiento.add(destino);
                tablero.moverFichaCombate(fila, columna, filaObjetivo, columnaObjetivo);
                fila = filaObjetivo;
                columna = columnaObjetivo;
                celdasEnMovimiento.remove(destino);
            }
        }
    }

    public void moverHacia(Tablero tablero, Ficha ficha) {
        Ficha enemigo = buscarEnemigoCercano(tablero.getTablero(), getFila(),getColumna(),ficha);
        if (enemigo != null) {
            int filaEnemigo = enemigo.getMovimiento().fila;
            int colEnemigo = enemigo.getMovimiento().columna;

            int nuevaFila = fila + Integer.compare(filaEnemigo, fila);
            int nuevaColumna = columna + Integer.compare(colEnemigo, columna);

            if (tablero.estaCeldaLibre(nuevaFila, nuevaColumna)) {
                tablero.moverFichaCombate(fila, columna, nuevaFila, nuevaColumna);
                fila = nuevaFila;
                columna = nuevaColumna;
            }
        }
    }

    public boolean estaEnAlcance(Ficha enemigo) {
        int distanciaFila = Math.abs(this.fila - enemigo.getMovimiento().getFila());
        int distanciaColumna = Math.abs(this.columna - enemigo.getMovimiento().getColumna());
        return distanciaFila <= alcance && distanciaColumna <= alcance;
    }

    public Ficha buscarEnemigoCercano(Ficha[][] tablero, int filaActual, int colActual,Ficha ficha) {
        for (int i = 1; i <= alcance; i++) {
            int filaEnemigo = filaActual - i;
            if (filaEnemigo >= 0) {
                for (int col = 0; col < tablero[0].length; col++) {
                    if (tablero[filaEnemigo][col] != null && tablero[filaEnemigo][col].getEquipo() != ficha.getEquipo()) {
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

    public int getFilaOriginal() {
        return filaOriginal;
    }

    public int getColumnaOriginal() {
        return columnaOriginal;
    }

    public int getFila() {return fila;}

    public int getColumna() {return columna;}

    public void cambiarFila(int x){this.fila=x;}

    public void cambiarColumna(int y){this.columna=y;}
}
