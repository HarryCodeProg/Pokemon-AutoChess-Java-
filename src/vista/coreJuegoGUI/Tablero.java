package vista.coreJuegoGUI;

import controlador.Observador;
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

public class Tablero extends JPanel{
    private JPanel[][] celdasTablero;
    private FichaClickeableGUI fichaTablero = null;
    private int cantidadMaximaTablero;
    private Map<Rasgo, Integer> contadorRasgos = new HashMap<>();
    private ArrayList<Observador> observadores;
    private JPanel panelRasgos;
    private Banca banca;

    public Tablero(){
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

                final int filaFinal = fila; // Necesario para uso en clase anónima
                final int columnaFinal = columna;

                celda.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (filaFinal < 3) {
                            System.out.println("fila incorrecta");
                        } else {
                            if (banca.getFichaDeLaBanca() != null) {
                                colocarFichaEnTablero(celda, cantidadMaximaTablero);
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

    public void setBanca(Banca ba){
        this.banca = ba;
    }

    public void añadirAlTableroPorCoor(int x, int y, Ficha ficha){
        FichaClickeableGUI fichagui = new FichaClickeableGUI(ficha);
        fichagui.setBanca(banca);
        this.celdasTablero[x][y].add(fichagui);
        fichagui.iniciarAnimacion();
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

            // Actualizar las celdas para reflejar los cambios
            celdaAnterior.revalidate();
            celdaAnterior.repaint();
            celdaNueva.revalidate();
            celdaNueva.repaint();

            fichaTablero.setBorder(null);
            fichaTablero = null;
        }
    }

    public void colocarFichaEnTablero(JPanel celdaTablero, int cantidadFichaMaximo) {
        int fichasActualesEnTablero = contarFichasEnTablero();

        // Verificar si ya se alcanzó el límite de fichas permitidas en el tablero
        if (fichasActualesEnTablero >= cantidadFichaMaximo) {
            System.out.println("No puedes colocar más fichas en el tablero");
            JOptionPane.showMessageDialog(this,"No puedes colocar más fichas en el tablero");
            return;
        }

        // Si la celda está vacía, colocar la ficha desde la banca
        if (celdaTablero.getComponentCount() == 0) {
            Container parent = banca.getFichaDeLaBanca().getParent();
            if (parent != null) {
                parent.remove(banca.getFichaDeLaBanca());
                parent.revalidate();
                parent.repaint();
            }

            banca.getFichaDeLaBanca().animacionArriba();
            celdaTablero.add(banca.getFichaDeLaBanca());
            celdaTablero.revalidate();
            celdaTablero.repaint();
            banca.getFichaDeLaBanca().setBorder(null);

            if(!fichaEstaEnTablero(banca.getFichaDeLaBanca())){
                agregarRasgosFicha(banca.getFichaDeLaBanca());
                ordenarContadorRasgos();
            }

            fichaTablero = null;
            banca.fichaBancaSetNull();
        }
    }

    public void removerRasgosFicha(FichaClickeableGUI ficha){
        removerContadorRasgos(ficha.getFicha().getRasgo1());
        removerContadorRasgos(ficha.getFicha().getRasgo2());
        removerContadorRasgos(ficha.getFicha().getRasgo3());
        actualizarRasgos();
    }

    public void agregarRasgosFicha(FichaClickeableGUI ficha){
        actualizarContadorRasgos(ficha.getFicha().getRasgo1());
        actualizarContadorRasgos(ficha.getFicha().getRasgo2());
        actualizarContadorRasgos(ficha.getFicha().getRasgo3());
        actualizarRasgos();
    }

    public void removerContadorRasgos(Rasgo rasgo) {
        if (rasgo != null) {
            contadorRasgos.put(rasgo, contadorRasgos.getOrDefault(rasgo, 0) - 1);
        }
    }

    private void actualizarContadorRasgos(Rasgo rasgo) {
        if (rasgo != null) {
            contadorRasgos.put(rasgo, contadorRasgos.getOrDefault(rasgo, 0) + 1);
        }
    }

    private void ordenarContadorRasgos() {
        contadorRasgos = contadorRasgos.entrySet().stream()
                .sorted(Map.Entry.<Rasgo, Integer>comparingByValue().reversed())
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    public Map<Rasgo, Integer> getContadorRasgos() {
        return contadorRasgos;
    }

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

    public boolean fichaEstaEnTablero(FichaClickeableGUI ficha){
        int contador = 0;
        for (int fila = 3; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (celdasTablero[fila][columna].getComponentCount() > 0) {
                    FichaClickeableGUI fichaEnCelda = (FichaClickeableGUI) celdasTablero[fila][columna].getComponent(0);

                    if (fichaEnCelda.getFicha().getNombreOriginal().equals(ficha.getFicha().getNombreOriginal()) || fichaEnCelda.getFicha().getNombre().equals(ficha.getFicha().getNombreOriginal())) {
                        contador++;
                    }
                }
            }
        }
        return contador >= 2;
    }

    public void setCantidadMaximaTablero(int cantidad){
        this.cantidadMaximaTablero = cantidad;
    }

    public void sacarFichaTablero(Ficha ficha) {
        for (int fila = 3; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (celdasTablero[fila][columna].getComponentCount() > 0) {
                    FichaClickeableGUI fichaEnCelda = (FichaClickeableGUI) celdasTablero[fila][columna].getComponent(0);

                    //if (fichaEnCelda.getFicha().getNombre().equals(ficha.getNombre())) {
                    if (fichaEnCelda.getFicha() == ficha) {
                        FichaClickeableGUI gui = new FichaClickeableGUI(ficha);
                        if(!fichaEstaEnTablero(gui) ){
                           removerRasgosFicha(gui);
                        }
                        actualizarRasgos();
                        celdasTablero[fila][columna].remove(fichaEnCelda);
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
                Image imgRasgoEscalada = imgRasgo.getScaledInstance(80, 30, java.awt.Image.SCALE_SMOOTH);
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

    public JPanel getPanelRasgos(){
        return this.panelRasgos;
    }
}
