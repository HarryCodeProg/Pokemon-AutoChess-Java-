package vista.coreJuegoGUI;

import javax.swing.*;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modelo.coreJuego.fichas.Rasgo;
import modelo.music.Sonidos;
import modelo.coreJuego.fichas.Ficha;
import modelo.coreJuego.fichas.Rasgos;
import modelo.coreJuego.Tienda;
import modelo.coreJuego.Jugador;
import modelo.coreJuego.Juego;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

public class PokemonAutoChessGUI extends JFrame {

    private JPanel panelFichas;
    private JLabel labelNivel;
    private JLabel labelMonedas;
    private JLabel labelJugador;
    private JPanel panelRasgos;
    private JPanel panelJugadores;
    private JLabel labelMensaje;
    private Tienda tienda;
    private TiendaGUI tiendaGUI;
    private Jugador jugador;
    private JButton botonSubirNivel;
    private Map<Integer, String> porcentajesPorNivel;
    private Map<String, String> direccionMusica;
    private Sonidos musicaDeFondo;
    private List<String> rutasImagenes;
    private Juego juego;
    private JLabel faseRonda;
    private JButton botonRonda;
    private Banca banca;
    private Tablero tablero;
    private Color blancoFondo = new Color(255,255,255,220);
    private Color negroFondo = new Color(0,0,0,200);

    public PokemonAutoChessGUI(Jugador jugadorN, ArrayList todosJugadores) {
        setTitle("Pokemon AutoChess");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarMusica();
        cargarRutasFondo();
        Random random = new Random();
        String dir = rutasImagenes.get(random.nextInt(rutasImagenes.size()));
        Image imagenFondo = new ImageIcon(getClass().getResource(dir)).getImage();

        JPanel panelConFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelConFondo.setLayout(new BorderLayout());

        tienda = new Tienda();
        tienda.setJugador(jugadorN);
        tiendaGUI = new TiendaGUI(tienda);
        this.jugador = jugadorN;
        banca = new Banca();
        tablero = new Tablero();
        banca.setTablero(tablero);
        tablero.setBanca(banca);
        tienda.setBancaYTablero(banca,tablero);

        //this.juego = new Juego(jugador, tienda, bancaYTablero);
        this.juego = new Juego(jugador, tienda, banca,tablero);

        panelConFondo.add(setupPanelIzquierdo(), BorderLayout.WEST);

        panelConFondo.add(setupPanelCentro(), BorderLayout.CENTER);

        inicializarPorcentajes();

        panelConFondo.add(setupPanelDerecho(todosJugadores), BorderLayout.EAST);

        tiendaGUI.asignarImagenes();

        panelConFondo.add(setupPanelNorte(), BorderLayout.NORTH);

        botonRonda.addActionListener(e -> {
            //bancaYTablero.limpiarTableroEnemigo();
            tablero.limpiarTableroEnemigo();
            juego.subirRonda();
            if(juego.getFase()>2 && juego.getRonda() == 5){
                musicaDeFondo.detenerMusica();
                cambiarMusica(direccionMusica.get("boss"));
            }
            subeXp(2);
            tiendaGUI.actualizarTiendaGui();
            actualizarLabelRonda();
        });

        actualizarLabelNivel();
        tiendaGUI.actualizarLabelMonedas();


        panelConFondo.add(setupPanelInferior(), BorderLayout.SOUTH);

        //bancaYTablero.setCantidadMaximaTablero(jugador.getNivel());
        tablero.setCantidadMaximaTablero(jugador.getNivel());

        botonSubirNivel.addActionListener(e -> {
            if (jugador.getNivel() == 10) {
                subeXp(4);
                botonSubirNivel.setEnabled(false);
            } else {
                subeXp(4);
            }
        });

        juego.subirRonda();
        actualizarLabelRonda();
        tiendaGUI.actualizarTiendaGui(); // Inicializa la tienda al arrancar
        //cambiarOpaque(this);
        setContentPane(panelConFondo);
        revalidate();
        repaint();
    }

    public JPanel setupPanelIzquierdo(){
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setOpaque(false);
        //panelRasgos = bancaYTablero.getPanelRasgos();
        panelRasgos = tablero.getPanelRasgos();
        panelRasgos.setOpaque(false);
        musicaDeFondo = new Sonidos();
        musicaDeFondo.reproducirMusica(direccionMusica.get("normal"));

        JSlider sliderVolumen = new JSlider(JSlider.HORIZONTAL, -80, 6, -30);// Rango de -80 dB a 6 dB, valor inicial
        sliderVolumen.setOpaque(false);
        sliderVolumen.addChangeListener(e -> {
            int valor = sliderVolumen.getValue();
            musicaDeFondo.ajustarVolumen((float) valor); // Ajustar el volumen según el valor del slider
        });

        panelIzquierdo.add(new JScrollPane(panelRasgos));
        panelIzquierdo.add(sliderVolumen);
        return panelIzquierdo;
    }

 public JPanel setupPanelCentro(){
    JPanel panelCentroCompleto = new JPanel();
    panelCentroCompleto.setLayout(new BorderLayout());
    panelCentroCompleto.setOpaque(false);
    //panelCentroCompleto.add(this.bancaYTablero, BorderLayout.CENTER);
     panelCentroCompleto.add(tablero, BorderLayout.CENTER);
     panelCentroCompleto.add(banca, BorderLayout.SOUTH);
    return panelCentroCompleto;
}

 public JPanel setupPanelDerecho(ArrayList todosJugadores){
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);
        JPanel venta = tiendaGUI.getVenta();
        panelJugadores = new JPanel();
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setOpaque(false);
        actualizarJugadoresPorVida(todosJugadores);
        panelDerecho.add(panelJugadores,BorderLayout.NORTH);
        panelDerecho.add(venta,BorderLayout.SOUTH);
        return panelDerecho;
 }

 public JPanel setupPanelNorte(){
    JPanel panelNorte = new JPanel();
    panelNorte.setLayout(new FlowLayout(FlowLayout.LEFT));
    panelNorte.setOpaque(false);
    labelNivel = new JLabel();
    labelNivel.setHorizontalAlignment(SwingConstants.CENTER);
    labelNivel.setForeground(blancoFondo);
    labelNivel.setBackground(negroFondo);
    labelNivel.setOpaque(true);
    //labelNivel.setOpaque(false);
    labelMonedas = tiendaGUI.getLabelMonedas();
    labelMonedas.setHorizontalAlignment(SwingConstants.CENTER);
    labelMonedas.setForeground(blancoFondo);
    labelMonedas.setBackground(negroFondo);
    labelMonedas.setOpaque(true);
    //labelMonedas.setOpaque(false);

    JPanel panelInfo = new JPanel();
    panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
    panelInfo.setOpaque(false);
    panelInfo.add(labelNivel);
    panelInfo.add(Box.createRigidArea(new Dimension(10, 10)));
    panelInfo.add(labelMonedas);
    panelNorte.add(panelInfo);

    JPanel panelFase = new JPanel();
    faseRonda = new JLabel();
    faseRonda.setForeground(blancoFondo);
    faseRonda.setBackground(negroFondo);
    faseRonda.setOpaque(true);
    botonRonda = new JButton("Siguiente Ronda");
    panelFase.setLayout(new BoxLayout(panelFase, BoxLayout.Y_AXIS));
    panelFase.setOpaque(false);
    panelFase.add(faseRonda);
    panelFase.add(Box.createRigidArea(new Dimension(10, 10)));
    panelFase.add(botonRonda);
    panelFase.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelNorte.add(panelFase);

    actualizarLabelRonda();
    return panelNorte;
}

public JPanel setupPanelInferior(){
    JPanel panelInferior = new JPanel();
    panelInferior.setLayout(new BorderLayout());
    panelInferior.setOpaque(false);

    JPanel panelBotones = new JPanel();
    panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
    panelBotones.setOpaque(false);
    panelBotones.add(juego.getLabelXP());
    panelBotones.add(juego.getPanelXP());

    botonSubirNivel = new JButton("Comprar XP | 4");
    panelBotones.add(botonSubirNivel, BorderLayout.WEST);

    JButton botonActualizar = new JButton("Actualizar | 2");
    panelBotones.add(botonActualizar, BorderLayout.WEST);
    panelInferior.add(panelBotones, BorderLayout.WEST);

    panelFichas = tiendaGUI.getPanelFichas();
    panelFichas.setLayout(new GridLayout(1, 5, 2, 2));
    panelInferior.add(panelFichas, BorderLayout.CENTER);
    botonActualizar.addActionListener(e -> {
        if (jugador.getMonedas() >= 2) {
            botonActualizar.setEnabled(true);
            tiendaGUI.actualizarTiendaGui();
            jugador.restarMonedas(2);
            tiendaGUI.actualizarLabelMonedas();
        } else {
            labelMensaje.setText("Monedas insuficientes");
        }
    });
    return panelInferior;
}

    void cargarRutasFondo(){
        rutasImagenes = Arrays.asList(
                "/modelo/images/maps/1 (1).png",
                "/modelo/images/maps/1 (2).png",
                "/modelo/images/maps/1 (3).png",
                "/modelo/images/maps/1 (4).png",
                "/modelo/images/maps/1 (5).png",
                "/modelo/images/maps/1 (6).png",
                "/modelo/images/maps/1 (7).png",
                "/modelo/images/maps/1 (8).png",
                "/modelo/images/maps/1 (9).png",
                "/modelo/images/maps/1 (10).png",
                "/modelo/images/maps/1 (11).png",
                "/modelo/images/maps/1 (12).png",
                "/modelo/images/maps/1 (13).png",
                "/modelo/images/maps/1 (14).png",
                "/modelo/images/maps/1 (15).png",
                "/modelo/images/maps/1 (16).png",
                "/modelo/images/maps/1 (17).png",
                "/modelo/images/maps/1 (18).png",
                "/modelo/images/maps/1 (19).png",
                "/modelo/images/maps/1 (20).png"
        );
    }

    public void inicializarMusica(){
        direccionMusica = new HashMap<>();
        direccionMusica.put("crab","C:\\Users\\Harry\\IdeaProjects\\PokemonAutoChess\\src\\modelo\\music\\crab.wav");
        direccionMusica.put("boss","C:\\Users\\Harry\\IdeaProjects\\PokemonAutoChess\\src\\modelo\\music\\lost.wav");
        direccionMusica.put("normal","C:\\Users\\Harry\\IdeaProjects\\PokemonAutoChess\\src\\modelo\\music\\kuze.wav");
    }

    void inicializarPorcentajes() {
        porcentajesPorNivel = new HashMap<>();
        porcentajesPorNivel.put(1, "(1):100%");
        porcentajesPorNivel.put(2, "(1):100%");
        porcentajesPorNivel.put(3, "(1):75%, (2):25%");
        porcentajesPorNivel.put(4, "(1):55%, (2):30%, (3)15%");
        porcentajesPorNivel.put(5, "(1):45%, (2):33%, (3)20%, (4):2%");
        porcentajesPorNivel.put(6, "(1):30%, (2):40%, (3)25%, (4):5%");
        porcentajesPorNivel.put(7, "(1):19%, (2):30%, (3)35%,(4):10%,(5):1%");
        porcentajesPorNivel.put(8, "(1):18%, (2):25%, (3)36%,(4):18%,(5):3%");
        porcentajesPorNivel.put(9, "(1):10%, (2):20%, (3)25%,(4):35%,(5):10%");
        porcentajesPorNivel.put(10, "(1):5%, (2):10%, (3)20%,(4):40%,(5):25%");
    }

    void actualizarLabelNivel() {
        String porcentaje = porcentajesPorNivel.getOrDefault(jugador.getNivel(), "N/A");
        labelNivel.setText(String.format("Nivel: %d | Porcentajes: %s", jugador.getNivel(), porcentaje));
        labelNivel.revalidate();
        labelNivel.repaint();
    }

    public void cambiarMusica(String dir){
        musicaDeFondo.reproducirMusica(dir);
    }

    private void cambiarColorDeFondo(Component componente, Color color) {
        if (componente instanceof JPanel || componente instanceof JLabel) {
            componente.setBackground(color);
        }

        // Si el componente es un contenedor (como JPanel), recorrer sus hijos
        if (componente instanceof Container) {
            for (Component child : ((Container) componente).getComponents()) {
                cambiarColorDeFondo(child, color);
            }
        }
    }

    public void cambiarOpaque(Container container) {
        if (container instanceof JComponent) {
            ((JComponent) container).setOpaque(false);
        }

        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                cambiarOpaque((Container) component);
            }
        }
    }

    public void addListenerBotonRonda(ActionListener listener) {
        botonRonda.addActionListener(listener);
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void actualizarEstadoDeRonda(Jugador jugador) {
        if (jugador.estaListoParaRonda()) {
            faseRonda.setText(jugador.getNombre() + " está listo para la siguiente ronda.");
        } else {
            faseRonda.setText(jugador.getNombre() + " no está listo aún.");
        }
    }

    public void actualizarJugadoresPorVida(List<Jugador> jugadores) {
        panelJugadores.removeAll();

        jugadores.sort(Comparator.comparingInt(Jugador::getVida).reversed());

        for (Jugador jugador : jugadores) {
            JLabel labelJugador = new JLabel(jugador.getNombre() + " - Vida: " + jugador.getVida());
            labelJugador.setForeground(blancoFondo);
            labelJugador.setBackground(negroFondo);
            labelJugador.setOpaque(true);
            panelJugadores.add(labelJugador);
        }

        panelJugadores.revalidate();
        panelJugadores.repaint();
    }

    public void subeXp(int xp){
        juego.comprarXP(xp);
        tiendaGUI.actualizarLabelMonedas();
        actualizarLabelNivel();
        //bancaYTablero.setCantidadMaximaTablero(jugador.getNivel());
        tablero.setCantidadMaximaTablero(jugador.getNivel());
    }

    /*void actualizarLabelJugadores() {
        this.labelJugador.setText(String.format("Nombre:%s Vida:%d",jugador.getNombre(),jugador.getVida()));
        labelJugador.revalidate();
        labelJugador.repaint();
    }*/

    public void actualizarLabelRonda(){
        this.faseRonda.setText(String.format("Fase:%d|Ronda:%d",juego.getFase(),juego.getRonda()));
        faseRonda.revalidate();
        faseRonda.repaint();
    }


    @Override
    public void dispose() {
        super.dispose();
        musicaDeFondo.detenerMusica();
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PokemonAutoChessGUI().setVisible(true);
            }
        });
    }*/
}
