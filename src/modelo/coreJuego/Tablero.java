package modelo.coreJuego;

import modelo.coreJuego.fichas.Ficha;
import modelo.coreJuego.fichas.Rasgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tablero {
    private Ficha[][] celdasTablero;
    private Ficha fichaSeleccionada;
    private int cantidadMaximaTablero;
    private Map<Rasgo, Integer> contadorRasgos = new HashMap<>();
    private Banca banca;

    public Tablero() {celdasTablero = new Ficha[6][6];}

    public Ficha[][] getTablero() {return celdasTablero;}

    public void setBanca(Banca banca) {this.banca = banca;}

    public void colocarFicha(int x, int y, Ficha ficha) {
        int fichasActualesEnTablero = contarFichasEnTablero();

        if (fichasActualesEnTablero >= cantidadMaximaTablero) {
            System.out.println("No puedes colocar más fichas en el tableroCore");
            return;
        }

        if (celdasTablero[x][y] == null) {
            celdasTablero[x][y] = ficha;
            ficha.getMovimiento().setFila(x);
            ficha.getMovimiento().setColumna(y);
            agregarRasgosFicha(ficha);
            ordenarContadorRasgos();
            actualizarRasgos();
        }
    }

    public void moverFicha(int xOrigen, int yOrigen, int xDestino, int yDestino) {
        Ficha fichaOrigen = celdasTablero[xOrigen][yOrigen];
        Ficha fichaDestino = celdasTablero[xDestino][yDestino];
            if (fichaDestino == null) {
                celdasTablero[xOrigen][yOrigen] = null;
                celdasTablero[xDestino][yDestino] = fichaOrigen;
                fichaOrigen.getMovimiento().setFila(xDestino);
                fichaOrigen.getMovimiento().setColumna(yDestino);
            }else{
                celdasTablero[xOrigen][yOrigen] = fichaDestino;
                fichaDestino.getMovimiento().setFila(xOrigen);
                fichaDestino.getMovimiento().setColumna(yOrigen);
                celdasTablero[xDestino][yDestino] = fichaOrigen;
                fichaOrigen.getMovimiento().setFila(xDestino);
                fichaOrigen.getMovimiento().setColumna(yDestino);
            }
    }

    public void eliminarFicha(int x, int y) {
        Ficha ficha = celdasTablero[x][y];
        if (ficha != null) {
            removerRasgosFicha(ficha);
            celdasTablero[x][y] = null;
            actualizarRasgos();
        }
    }

    public void agregarRasgosFicha(Ficha ficha) {
        actualizarContadorRasgos(ficha.getRasgo1());
        actualizarContadorRasgos(ficha.getRasgo2());
        actualizarContadorRasgos(ficha.getRasgo3());
        actualizarRasgos();
    }

    public void removerRasgosFicha(Ficha ficha) {
        removerContadorRasgos(ficha.getRasgo1());
        removerContadorRasgos(ficha.getRasgo2());
        removerContadorRasgos(ficha.getRasgo3());
        actualizarRasgos();
    }

    public void actualizarContadorRasgos(Rasgo rasgo) {
        if (rasgo != null) {
            contadorRasgos.put(rasgo, contadorRasgos.getOrDefault(rasgo, 0) + 1);
        }
    }

    public void removerContadorRasgos(Rasgo rasgo) {
        if (rasgo != null) {
            contadorRasgos.put(rasgo, contadorRasgos.getOrDefault(rasgo, 0) - 1);
        }
    }

    public void ordenarContadorRasgos() {
        contadorRasgos = contadorRasgos.entrySet().stream()
                .sorted(Map.Entry.<Rasgo, Integer>comparingByValue().reversed())
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    public Map<Rasgo, Integer> getContadorRasgos() {return contadorRasgos;}

    public int contarFichasEnTablero() {
        int contador = 0;
        for (int fila = 0; fila < celdasTablero.length; fila++) {
            for (int columna = 0; columna < celdasTablero[fila].length; columna++) {
                if (celdasTablero[fila][columna] != null) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public Ficha[] fichasEnTablero(){
        List<Ficha> fichas = new ArrayList<>();
        for (int fila = 0; fila < celdasTablero.length; fila++) {
            for (int columna = 0; columna < celdasTablero[fila].length; columna++) {
                if (celdasTablero[fila][columna]!= null) {
                    fichas.add(celdasTablero[fila][columna]);
                }
            }
        }
        return fichas.toArray(new Ficha[fichas.size()]);
    }

    public boolean fichaEstaEnTablero(Ficha ficha) {
        for (int fila = 0; fila < celdasTablero.length; fila++) {
            for (int columna = 0; columna < celdasTablero[fila].length; columna++) {
                if (celdasTablero[fila][columna] != null && celdasTablero[fila][columna].equals(ficha)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean noTieneFichas() {
        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (celdasTablero[fila][columna] != null) {
                    return false;
                }
            }
        }
        return true;
    }



    public boolean estaCeldaOcupada(int x,int y){return celdasTablero[x][y] != null;}

    public Ficha getFichaEnCelda(int x, int y) {return celdasTablero[x][y];}

    public void setCantidadMaximaTablero(int cantidad) {this.cantidadMaximaTablero = cantidad;}

    public int getCantidadMaximaTablero() {return this.cantidadMaximaTablero;}

    public void actualizarRasgos() {//System.out.println("Rasgos actualizados: " + contadorRasgos);
        }

}

