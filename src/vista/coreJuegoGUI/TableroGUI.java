package vista.coreJuegoGUI;

import modelo.coreJuego.Tablero;
import modelo.coreJuego.fichas.Ficha;
import modelo.coreJuego.fichas.Rasgo;
import modelo.coreJuego.fichas.Rasgos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableroGUI extends JPanel{
    private JPanel[][] celdasTablero;
    private FichaClickeableGUI fichaTablero = null;
    private JPanel panelRasgos;
    private BancaGUI banca;
    private Tablero tableroCore;

    public TableroGUI(Tablero tableroC){
        this.tableroCore = tableroC;
        setLayout(new BorderLayout());
        JPanel tablero = inicializarTablero();
        tablero.setOpaque(false);
        panelRasgos = new JPanel();
        panelRasgos.setOpaque(false);
        add(tablero, BorderLayout.CENTER);
        this.setOpaque(false);
    }

    public JPanel inicializarTablero() {
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(6, 6));
        panelTablero.setOpaque(false);
        celdasTablero = new JPanel[6][6];

        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna <6; columna++) {
                JPanel celda = new JPanel();
                celda.setBorder(BorderFactory.createLineBorder(fila < 3 ? Color.BLACK : Color.BLUE));
                celda.setPreferredSize(new Dimension(64, 72));
                celda.setOpaque(false);

                final int filaFinal = fila; // Necesario para uso en clase anonima
                final int columnaFinal = columna;

                celda.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (filaFinal < 3) {
                            System.out.println("fila incorrecta");
                        } else {
                            if (banca.getFichaDeLaBanca() != null) {
                                colocarFichaEnTablero(celda, tableroCore.getCantidadMaximaTablero());
                            }else if (celdasTablero[filaFinal][columnaFinal].getComponentCount() > 0 && fichaTablero == null) {
                                banca.seleccionarFicha((FichaClickeableGUI) celdasTablero[filaFinal][columnaFinal].getComponent(0));
                            } else if (fichaTablero != null) {
                                moverFichaDentroDelTablero(celda);
                            }
                        }
                    }
                });

                celdasTablero[fila][columna] = celda;
                panelTablero.add(celda);
            }
        }
        return panelTablero;
    }

    public JPanel[][] getTablero(){
        return this.celdasTablero;
    }

    public void setBanca(BancaGUI ba){
        this.banca = ba;
    }

    public boolean estaCeldaOcupada(int fila, int columna) { return celdasTablero[fila][columna].getComponentCount() > 0; }

    public void añadirAlTableroPorCoor(int x, int y, Ficha ficha){
        if (!estaCeldaOcupada(x, y)){
            FichaClickeableGUI fichagui = new FichaClickeableGUI(ficha);
            fichagui.setBanca(banca);
            fichagui.getFicha().getMovimiento().setFila(x);
            fichagui.getFicha().getMovimiento().setColumna(x);
            this.celdasTablero[x][y].add(fichagui);
            fichagui.iniciarAnimacion();
        }
    }

    public FichaClickeableGUI getFichaTablero(){
        return this.fichaTablero;
    }

    public void fichaTabSetNull(){
        this.fichaTablero = null;
    }

    public void setFichaTablero(FichaClickeableGUI ficha){
        this.fichaTablero = ficha;
    }

    private void moverFichaDentroDelTablero(JPanel celdaNueva) {
        if (fichaTablero != null) {
            JPanel celdaAnterior = (JPanel) fichaTablero.getParent();

            if (celdaNueva.getComponentCount() > 0) {
                // Tomar la ficha que ya está en la nueva celda
                FichaClickeableGUI fichaEnNuevaCelda = (FichaClickeableGUI) celdaNueva.getComponent(0);

                celdaNueva.remove(fichaEnNuevaCelda);
                celdaAnterior.remove(fichaTablero);

                celdaNueva.add(fichaTablero);
                celdaAnterior.add(fichaEnNuevaCelda);

            } else {
                // Mover la ficha si la nueva celda está vacía
                celdaAnterior.remove(fichaTablero);
                celdaNueva.add(fichaTablero);
            }
            tableroCore.moverFicha(getCoorCelda(celdaAnterior,true),getCoorCelda(celdaAnterior,false),getCoorCelda(celdaNueva,true),getCoorCelda(celdaNueva,false));

            celdaAnterior.revalidate();
            celdaAnterior.repaint();
            celdaNueva.revalidate();
            celdaNueva.repaint();

            fichaTablero.setBorder(null);
            fichaTablero = null;
        }
    }

    public void colocarFichaEnTablero(JPanel celdaTablero, int cantidadFichaMaximo) {
        //int fichasActualesEnTablero = contarFichasEnTablero();
        int fichasActualesEnTablero = contarFichasEnTablero();

        if (fichasActualesEnTablero >= cantidadFichaMaximo) {
            JOptionPane.showMessageDialog(this,"No puedes colocar más fichas en el tablero");
            return;
        }

        if (celdaTablero.getComponentCount() == 0) {
            Container parent = banca.getFichaDeLaBanca().getParent();
            if (parent != null) {
                parent.remove(banca.getFichaDeLaBanca());
                parent.revalidate();
                parent.repaint();
            }

            int coorX = getCoorCelda(celdaTablero,true);
            int coorY = getCoorCelda(celdaTablero,false);
            banca.getFichaDeLaBanca().animacionArriba();
            celdaTablero.add(banca.getFichaDeLaBanca());
            celdaTablero.revalidate();
            celdaTablero.repaint();
            banca.getFichaDeLaBanca().setBorder(null);
            tableroCore.colocarFicha(coorX,coorY,banca.getFichaDeLaBanca().getFicha());

            if(!fichaEstaEnTablero(banca.getFichaDeLaBanca(),false)){
                //agregarRasgosFicha(banca.getFichaDeLaBanca());
                tableroCore.ordenarContadorRasgos();
                actualizarRasgos();
            }
            fichaTablero = null;
            banca.fichaBancaSetNull();
        }
    }

    public int getCoorCelda(JPanel celda, boolean x) {
        int fila = -1;
        int columna = -1;
        boolean encontrada = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (celdasTablero[i][j] == celda) {
                    fila = i; columna = j;
                    encontrada = true;
                    break;
                }
            }
            if (encontrada) break;
        } if (x) {
            return fila;
        } else {
            return columna;
        }
    }

    public void removerRasgosFicha(FichaClickeableGUI ficha){
        tableroCore.removerContadorRasgos(ficha.getFicha().getRasgo1());
        tableroCore.removerContadorRasgos(ficha.getFicha().getRasgo2());
        tableroCore.removerContadorRasgos(ficha.getFicha().getRasgo3());
        actualizarRasgos();
    }

    public void agregarRasgosFicha(FichaClickeableGUI ficha){
        tableroCore.actualizarContadorRasgos(ficha.getFicha().getRasgo1());
        tableroCore.actualizarContadorRasgos(ficha.getFicha().getRasgo2());
        tableroCore.actualizarContadorRasgos(ficha.getFicha().getRasgo3());
        actualizarRasgos();
    }

    public Map<Rasgo, Integer> getContadorRasgos() {return tableroCore.getContadorRasgos();}

    private int contarFichasEnTablero() {
        int contador = 0;
        for (int fila = 3; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (celdasTablero[fila][columna].getComponentCount() > 0) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public boolean fichaEstaEnTablero(FichaClickeableGUI ficha,boolean vende){
        int contador = 0;
        String nombreFicha = ficha.getFicha().getNombreOriginal();

        for (int fila = 3; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (celdasTablero[fila][columna].getComponentCount() > 0) {
                    FichaClickeableGUI fichaEnCelda = (FichaClickeableGUI) celdasTablero[fila][columna].getComponent(0);
                    if(!vende){
                        if (fichaEnCelda != ficha && fichaEnCelda.getFicha().getNombreOriginal() == nombreFicha) {
                            contador++;
                        }
                    }else{
                        if (fichaEnCelda == ficha){
                            contador++;
                        }
                    }
                }
            }
        }
        return contador >= 1;
    }

    public void sacarFichaTablero(Ficha ficha) {
        for (int fila = 3; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (celdasTablero[fila][columna].getComponentCount() > 0) {
                    FichaClickeableGUI fichaEnCelda = (FichaClickeableGUI) celdasTablero[fila][columna].getComponent(0);

                    //if (fichaEnCelda.getFicha().getNombre().equals(ficha.getNombre())) {
                    if (fichaEnCelda.getFicha() == ficha) {
                        FichaClickeableGUI gui = new FichaClickeableGUI(ficha);
                        if(!fichaEstaEnTablero(gui,true) ){
                           removerRasgosFicha(gui);
                        }
                        actualizarRasgos();
                        celdasTablero[fila][columna].remove(fichaEnCelda);
                        tableroCore.eliminarFicha(fila,columna);
                        celdasTablero[fila][columna].revalidate();
                        celdasTablero[fila][columna].repaint();
                        this.fichaTablero = null;
                        return;
                    }
                }
            }
        }
        //System.out.println("La ficha no se encontró en el tablero.");
    }

    public void limpiarTableroEnemigo() {
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (celdasTablero[fila][columna].getComponentCount() > 0) {
                    FichaClickeableGUI fichaEnemiga = (FichaClickeableGUI) celdasTablero[fila][columna].getComponent(0);

                    celdasTablero[fila][columna].remove(fichaEnemiga);
                }
                celdasTablero[fila][columna].revalidate();
                celdasTablero[fila][columna].repaint();
            }
        }
    }

    public FichaClickeableGUI getFichaEnCelda(int fila, int columna) {
        if (estaCeldaOcupada(fila, columna)) {
            return (FichaClickeableGUI) celdasTablero[fila][columna].getComponent(0);
        }
        return null;
    }

    public void actualizarRasgos() {
        panelRasgos.removeAll();
        panelRasgos.setLayout(new BoxLayout(panelRasgos, BoxLayout.Y_AXIS));
        panelRasgos.setOpaque(false);

        List<Map.Entry<Rasgo, Integer>> listaRasgos = new ArrayList<>(getContadorRasgos().entrySet());
        listaRasgos.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        for (Map.Entry<Rasgo, Integer> entry : listaRasgos) {
            if(!entry.getKey().getNombre().equals(Rasgos.NULL) && entry.getValue()>0){
                Rasgo rasgo = entry.getKey();
                int cantidad = entry.getValue();

                // Crear un panel para cada rasgo con su imagen y cantidad
                JPanel panelRasgo = new JPanel();
                panelRasgo.setOpaque(false);
                panelRasgo.setLayout(new FlowLayout(FlowLayout.LEFT,4,4));
                panelRasgo.setPreferredSize(new Dimension(100, 100));

                ImageIcon imagenRasgo = rasgo.getImagenRasgo();
                // Redimensionar la imagen del rasgo
                Image imgRasgo = imagenRasgo.getImage();
                Image imgRasgoEscalada = imgRasgo.getScaledInstance(80, 30, Image.SCALE_SMOOTH);
                ImageIcon imagenRasgoEscalada = new ImageIcon(imgRasgoEscalada);
                JLabel etiquetaImagen = new JLabel(imagenRasgoEscalada);
                panelRasgo.add(etiquetaImagen);

                JLabel etiquetaTexto = new JLabel(rasgo.getNombre() + ": " + cantidad);
                panelRasgo.add(etiquetaTexto);

                panelRasgos.add(panelRasgo);
            }
        }
        panelRasgos.revalidate();
        panelRasgos.repaint();
    }

    public void habilitarTablero(boolean habilitar) {
        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                celdasTablero[fila][columna].setEnabled(habilitar);
            }
        }
    }

    public JPanel getPanelRasgos(){return this.panelRasgos;}

    public Tablero getTableroCore(){return this.tableroCore;}

}
