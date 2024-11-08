package modelo.coreJuego;

import modelo.coreJuego.fichas.Ficha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Banca {
    private Ficha[] celdasBanca;
    private Ficha fichaSeleccionada = null;
    private Tablero tablero;

    public Banca() {
        celdasBanca = new Ficha[9];
    }

    public void moverFichaEnBanca(int nuevaPosicion) {
        for (int i = 0; i < celdasBanca.length; i++) {
            if (celdasBanca[i] == fichaSeleccionada) {
                if (celdasBanca[nuevaPosicion] != null) {
                    Ficha fichaIntercambiada = celdasBanca[nuevaPosicion];
                    celdasBanca[nuevaPosicion] = fichaSeleccionada;
                    celdasBanca[i] = fichaIntercambiada;
                } else {
                    celdasBanca[nuevaPosicion] = fichaSeleccionada;
                    celdasBanca[i] = null;
                }
                fichaSeleccionada = null;
                break;
            }
        }
    }

    public void moverFichaABancaDesdeTablero(Ficha ficha, int posicionDestino) {
        if (posicionDestino >= 0 && posicionDestino < celdasBanca.length) {
            if (celdasBanca[posicionDestino] == null) {
                celdasBanca[posicionDestino] = ficha;
                tablero.eliminarFicha(ficha.getMovimiento().getFila(),ficha.getMovimiento().getColumna());
            } else {
                // Intercambio de fichas entre el tablero y la banca
                Ficha fichaEnBanca = celdasBanca[posicionDestino];
                celdasBanca[posicionDestino] = ficha;
                tablero.colocarFicha(ficha.getMovimiento().getFila(),ficha.getMovimiento().getColumna(),fichaEnBanca);
                tablero.eliminarFicha(ficha.getMovimiento().getFila(),ficha.getMovimiento().getColumna());
            }
        }
    }

    public Ficha getFichaSeleccionada() {
        return fichaSeleccionada;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public void eliminarFicha(Ficha ficha) {
        for (int i = 0; i < celdasBanca.length; i++) {
            if (celdasBanca[i] == ficha) {
                celdasBanca[i] = null;
                break;
            }
        }
    }

    public void seleccionarFicha(Ficha ficha) {
        this.fichaSeleccionada = ficha;
    }

    public void agregarFichaALaBanca(Ficha ficha) {
        for (int i = 0; i < celdasBanca.length; i++) {
            if (celdasBanca[i] == null) {
                celdasBanca[i] = ficha;
                verificarFusion();
                break;
            }
        }
    }

    public boolean hayEspacioEnBanca() {
        for (Ficha ficha : celdasBanca) {
            if (ficha == null) {
                return true;
            }
        }
        return false;
    }

    private void verificarFusion() {
        boolean fusionRealizada;

        do {
            fusionRealizada = false;
            Map<String, ArrayList<Ficha>> fichasPorNombreYEstrellas = new HashMap<>();

            for (Ficha fichaEnBanca : celdasBanca) {
                if (fichaEnBanca != null) {
                    agregarFichaAMapa(fichasPorNombreYEstrellas, fichaEnBanca);
                }
            }

            for (Ficha fichaEnTablero : tablero.fichasEnTablero()) {
                agregarFichaAMapa(fichasPorNombreYEstrellas, fichaEnTablero);
            }

            for (ArrayList<Ficha> grupoFichas : fichasPorNombreYEstrellas.values()) {
                if (grupoFichas.size() >= 3) {

                    Ficha fichaParaSubir = grupoFichas.get(2);
                    fichaParaSubir.subirEstrellas();

                    eliminarFichasFusionadas(grupoFichas.get(0), grupoFichas.get(1));

                    fusionRealizada = true;
                    break;
                }
            }
        } while (fusionRealizada);
    }

    private void agregarFichaAMapa(Map<String, ArrayList<Ficha>> fichasPorNombreYEstrellas, Ficha ficha) {
        String clave = ficha.getNombre() + "-" + ficha.getEstrellas();
        fichasPorNombreYEstrellas.putIfAbsent(clave, new ArrayList<>());
        fichasPorNombreYEstrellas.get(clave).add(ficha);
    }

    private void eliminarFichasFusionadas(Ficha ficha1, Ficha ficha2) {
        eliminarFicha(ficha1);
        eliminarFicha(ficha2);
    }
}

