package vista.coreJuegoGUI;

import controlador.Observador;
import modelo.coreJuego.Banca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BancaGUI extends JPanel{
    private JPanel[] celdasBanca;
    private FichaClickeableGUI fichaDeLaBanca = null;
    private ArrayList<Observador> observadores;
    private TableroGUI tablero;
    private Banca bancaCore;

 public BancaGUI(){
     setLayout(new BorderLayout());
     this.bancaCore = new Banca();

     JPanel panelBanca = inicializarBanca();
     panelBanca.setOpaque(false);
     add(panelBanca, BorderLayout.SOUTH);
     this.setOpaque(false);
 }

    private JPanel inicializarBanca() {
        JPanel panelBanca = new JPanel();
        panelBanca.setLayout(new GridLayout(1, 9));
        panelBanca.setOpaque(false);
        celdasBanca = new JPanel[9];

        for (int i = 0; i < 9; i++) {
            JPanel celda = new JPanel();
            celda.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            celda.setPreferredSize(new Dimension(64, 72));
            celda.setOpaque(false);
            celdasBanca[i] = celda;

            final int index = i;
            celda.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (fichaDeLaBanca != null) {
                        moverFichaEnBanca(index);
                    } else if (celdasBanca[index].getComponentCount() > 0 && tablero.getFichaTablero() == null) {
                        seleccionarFicha((FichaClickeableGUI) celdasBanca[index].getComponent(0));
                    } else if(tablero.getFichaTablero() != null && fichaDeLaBanca == null){
                        moverFichaABancaDesdeTablero(tablero.getFichaTablero(),celdasBanca[index]);
                    }
                }
            });

            panelBanca.add(celda);
        }
        return panelBanca;
    }

    private void moverFichaEnBanca(int nuevaPosicion) {
        for (int i = 0; i < celdasBanca.length; i++) {
            if (celdasBanca[i].getComponentCount() > 0 && celdasBanca[i].getComponent(0) == fichaDeLaBanca) {
                JPanel celdaActual = celdasBanca[i];

                if (celdasBanca[nuevaPosicion].getComponentCount() > 0) {
                    FichaClickeableGUI fichaIntercambiada = (FichaClickeableGUI) celdasBanca[nuevaPosicion].getComponent(0);
                    celdasBanca[nuevaPosicion].remove(fichaIntercambiada);
                    celdaActual.remove(fichaDeLaBanca);
                    celdasBanca[nuevaPosicion].add(fichaDeLaBanca);
                    celdaActual.add(fichaIntercambiada);
                } else {
                    celdasBanca[nuevaPosicion].add(fichaDeLaBanca);
                    celdaActual.remove(fichaDeLaBanca);
                }

                actualizarCeldasBanca(celdaActual, celdasBanca[nuevaPosicion]);
                break;
            }
        }

        fichaDeLaBanca.setBorder(null);
        fichaDeLaBanca = null;
    }

    public void moverFichaABancaDesdeTablero(FichaClickeableGUI ficha, JPanel celdaDestino) {
        JPanel celdaOrigen = (JPanel) ficha.getParent();
        int filaOrigen = ficha.getFicha().getMovimiento().getFila();
        int columnaOrigen = ficha.getFicha().getMovimiento().getColumna();
        System.out.println("Movimiento ficha desde: (" + filaOrigen + ", " + columnaOrigen + ") a banca");
        if (celdaDestino.getComponentCount() == 0) {
            tablero.getTableroCore().eliminarFicha(filaOrigen, columnaOrigen);
            celdaDestino.add(ficha);
            celdaOrigen.remove(ficha);
            if (!tablero.fichaEstaEnTablero(ficha,false)) {
                tablero.removerRasgosFicha(ficha);
            }
            ficha.detenerAnimacion();
            ficha.iniciarAnimacion();
            fichaBancaSetNull();
            ficha.setBorder(null);
        } else {
            FichaClickeableGUI fichaEnBanca = (FichaClickeableGUI) celdaDestino.getComponent(0);
            tablero.getTableroCore().eliminarFicha(filaOrigen, columnaOrigen);
            tablero.getTableroCore().colocarFicha(tablero.getCoorCelda(celdaDestino, true), tablero.getCoorCelda(celdaDestino, false), fichaEnBanca.getFicha());
            celdaDestino.remove(fichaEnBanca);
            celdaDestino.add(ficha);
            celdaOrigen.remove(ficha);
            celdaOrigen.add(fichaEnBanca);
            ficha.detenerAnimacion();
            ficha.iniciarAnimacion();
            tablero.removerRasgosFicha(ficha);

            fichaEnBanca.detenerAnimacion();
            fichaEnBanca.animacionArriba();
            tablero.agregarRasgosFicha(fichaEnBanca);

            ficha.setBorder(null);
        }
        celdaOrigen.revalidate();
        celdaOrigen.repaint();
        celdaDestino.revalidate();
        celdaDestino.repaint();
        tablero.fichaTabSetNull();
        fichaBancaSetNull();
    }


    public FichaClickeableGUI getFichaDeLaBanca(){return fichaDeLaBanca;}

    public void fichaBancaSetNull(){this.fichaDeLaBanca = null;}

    public void setTablero(TableroGUI tab){this.tablero = tab;}

    public void eliminarFichaSeleccionada(FichaClickeableGUI ficha){
       try{
           Container parent1 = ficha.getParent();
           if (parent1 != null) {
               parent1.remove(ficha);
               parent1.revalidate();
               parent1.repaint();
               if(tablero.getFichaTablero() != null){
                   tablero.sacarFichaTablero(ficha.getFicha());
               }

           }
       }catch (Exception e){
           System.out.println("Error");
       }

    }

    public void seleccionarFicha(FichaClickeableGUI ficha) {
        if (tablero.getFichaTablero() == ficha) {
            ficha.setBorder(null);
            tablero.fichaTabSetNull();
            fichaDeLaBanca = null;
        } else {
            if (tablero.getFichaTablero() != null) {
                tablero.getFichaTablero().setBorder(null);
            }

            tablero.setFichaTablero(ficha);
            tablero.getFichaTablero().setBorder(BorderFactory.createLineBorder(Color.RED));

            // Verificar si la ficha seleccionada está en la banca antes de asignarla a fichaDeLaBanca
            Container parent = ficha.getParent();
            boolean estaEnBanca = false;

            // Verificar si el panel padre de la ficha pertenece a las celdas de la banca
            for (JPanel celdaBanca : celdasBanca) {
                if (parent == celdaBanca) {
                    estaEnBanca = true;
                    break;
                }
            }

            if (estaEnBanca) {
                fichaDeLaBanca = tablero.getFichaTablero();
            } else {
                fichaDeLaBanca = null;
            }
        }
    }

    private void actualizarCeldasBanca(JPanel celdaOriginal, JPanel celdaNueva) {
        celdaOriginal.revalidate();
        celdaOriginal.repaint();
        celdaNueva.revalidate();
        celdaNueva.repaint();
    }

    public void agregarFichaALaBanca(FichaClickeableGUI fichaClickeable) {
        for (int i = 0; i < celdasBanca.length; i++) {
            if (celdasBanca[i].getComponentCount() == 0) {
                fichaClickeable.iniciarAnimacion();
                //fichaClickeable.animacionArriba();
                fichaClickeable.setVerticalTextPosition(JLabel.BOTTOM);
                fichaClickeable.setHorizontalTextPosition(JLabel.CENTER);

                celdasBanca[i].add(fichaClickeable);

                //verificarFusion(fichaClickeable.getFicha(),indiceGuardadasBanca);
                verificarFusion();

                celdasBanca[i].revalidate();
                celdasBanca[i].repaint();
                break;
            }
        }
    }

    public boolean hayEspacioBanca(){
        for (int i = 0; i < celdasBanca.length; i++) {
            if (celdasBanca[i].getComponentCount() == 0) {
              return true;
            }
        }
        return false;
    }

    private void verificarFusion() {
        boolean fusionRealizada;

        do {
            fusionRealizada = false;
            Map<String, ArrayList<FichaClickeableGUI>> fichasPorNombreYEstrellas = new HashMap<>();

            for (int i = 0; i < celdasBanca.length; i++) {
                if (celdasBanca[i].getComponentCount() > 0) {
                    FichaClickeableGUI fichaEnBanca = (FichaClickeableGUI) celdasBanca[i].getComponent(0);
                    agregarFichaAMapa(fichasPorNombreYEstrellas, fichaEnBanca);
                }
            }

            for (int fila = 3; fila < 6; fila++) {
                for (int columna = 0; columna < 6; columna++) {
                    if (tablero.getTablero()[fila][columna].getComponentCount() > 0) {
                        FichaClickeableGUI fichaEnTablero = (FichaClickeableGUI) tablero.getTablero()[fila][columna].getComponent(0);
                        agregarFichaAMapa(fichasPorNombreYEstrellas, fichaEnTablero);
                    }
                }
            }

            // Verificar si hay 3 fichas iguales en algún grupo
            for (ArrayList<FichaClickeableGUI> grupoFichas : fichasPorNombreYEstrellas.values()) {
                if (grupoFichas.size() >= 3) {
                    // Subir de estrellas a una de las fichas
                    FichaClickeableGUI fichaParaSubir = grupoFichas.get(2);
                    fichaParaSubir.getFicha().subirEstrellas();
                    fichaParaSubir.getFicha().cambiarNombre();

                    //fichaParaSubir.iniciarAnimacion();
                    fichaParaSubir.animacionArriba();

                    // Eliminar las otras dos fichas
                    eliminarFichasFusionadas(grupoFichas.get(0), grupoFichas.get(1));

                    fusionRealizada = true;
                    break;
                }
            }
        } while (fusionRealizada);
    }

    private void agregarFichaAMapa(Map<String, ArrayList<FichaClickeableGUI>> fichasPorNombreYEstrellas, FichaClickeableGUI ficha) {
        String clave = ficha.getFicha().getNombre() + "-" + ficha.getFicha().getEstrellas();
        fichasPorNombreYEstrellas.putIfAbsent(clave, new ArrayList<>());
        fichasPorNombreYEstrellas.get(clave).add(ficha);
    }

    private void eliminarFichasFusionadas(FichaClickeableGUI ficha1, FichaClickeableGUI ficha2) {
        // Eliminar la primera ficha
        Container parent1 = ficha1.getParent();
        if (parent1 != null) {
            parent1.remove(ficha1);
            parent1.revalidate();
            parent1.repaint();
            tablero.sacarFichaTablero(ficha1.getFicha());
        }

        // Eliminar la segunda ficha
        Container parent2 = ficha2.getParent();
        if (parent2 != null) {
            parent2.remove(ficha2);
            parent2.revalidate();
            parent2.repaint();
            tablero.sacarFichaTablero(ficha2.getFicha());
        }
    }

    public Banca getBancaCore() {return bancaCore;}
}
