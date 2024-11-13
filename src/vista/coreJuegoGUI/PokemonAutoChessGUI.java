package vista.coreJuegoGUI;

import javax.swing.*;

import controlador.Observador;
import modelo.coreJuego.*;
import modelo.music.Sonidos;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class PokemonAutoChessGUI extends JFrame implements Observador {
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
    private JLabel faseRonda;
    private JButton botonRonda;
    private BancaGUI bancaGUI;
    private TableroGUI tableroGUI;
    private Color blancoFondo = new Color(255,255,255,220);
    private Color negroFondo = new Color(0,0,0,200);
    private JugadorGUI jugadorGUI;
    private Juego juego;
    private JLabel labelEsperando;
    private JLabel labelTemporizador;

    public PokemonAutoChessGUI(Jugador jugadorN, ArrayList todosJugadores,Juego juego,Tienda tiendax) {
        setTitle("Pokemon AutoChess");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        juego.agregarObservador(this);

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

        this.tienda = tiendax;
        this.jugador = jugadorN;
        tiendaGUI = new TiendaGUI(tienda,jugador);
        bancaGUI = new BancaGUI();
        tableroGUI = jugador.getTablero();
        bancaGUI.getBancaCore().setTablero(tableroGUI.getTableroCore());
        tableroGUI.getTableroCore().setBanca(bancaGUI.getBancaCore());
        bancaGUI.setTablero(tableroGUI);
        tableroGUI.setBanca(bancaGUI);

        //jugador.setTablero(tableroGUI);

        this.juego = juego;

        jugador.setBanca(bancaGUI);

        jugadorGUI = new JugadorGUI(jugador);

        panelConFondo.add(setupPanelIzquierdo(), BorderLayout.WEST);

        panelConFondo.add(setupPanelCentro(), BorderLayout.CENTER);

        inicializarPorcentajes();

        panelConFondo.add(setupPanelDerecho(todosJugadores), BorderLayout.EAST);

        tiendaGUI.asignarImagenes();

        panelConFondo.add(setupPanelNorte(), BorderLayout.NORTH);

        botonRonda.addActionListener(e -> {
            jugador.setListoParaRonda(true);
            if (juego.todosListosParaNuevaRonda()) {
                juego.subirRonda();
            } else {
                labelEsperando.setEnabled(true);
                labelEsperando.setVisible(true);
                botonRonda.setEnabled(false);
            }
        });

        actualizarLabelNivel();
        tiendaGUI.actualizarLabelMonedas();

        panelConFondo.add(setupPanelInferior(), BorderLayout.SOUTH);

        tableroGUI.getTableroCore().setCantidadMaximaTablero(jugador.getNivel());

        botonSubirNivel.addActionListener(e -> {
            if (jugador.getNivel() == 10) {
                subeXp(4);
                botonSubirNivel.setEnabled(false);
            } else {
                subeXp(4);
            }
        });

        //juego.subirRonda();
        actualizarLabelRonda();
        tiendaGUI.actualizarTiendaGui();
        setContentPane(panelConFondo);
        revalidate();
        repaint();
    }

    public JPanel setupPanelIzquierdo(){
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setOpaque(false);
        panelRasgos = tableroGUI.getPanelRasgos();
        panelRasgos.setOpaque(false);
        musicaDeFondo = new Sonidos();
        musicaDeFondo.reproducirMusica(direccionMusica.get("normal"));

        JSlider sliderVolumen = new JSlider(JSlider.HORIZONTAL, -80, 6, -80);// Rango de -80 dB a 6 dB
        sliderVolumen.setOpaque(false);
        sliderVolumen.addChangeListener(e -> {
            int valor = sliderVolumen.getValue();
            musicaDeFondo.ajustarVolumen((float) valor);
        });

        panelIzquierdo.add(new JScrollPane(panelRasgos));
        panelIzquierdo.add(sliderVolumen);
        return panelIzquierdo;
    }

 public JPanel setupPanelCentro(){
    JPanel panelCentroCompleto = new JPanel();
    panelCentroCompleto.setLayout(new BorderLayout());
    panelCentroCompleto.setOpaque(false);
     panelCentroCompleto.add(tableroGUI, BorderLayout.CENTER);
     panelCentroCompleto.add(bancaGUI, BorderLayout.SOUTH);
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
    JPanel panelNorte = new JPanel(new BorderLayout());
    //panelNorte.setLayout(new FlowLayout(FlowLayout.LEFT));
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
    panelNorte.add(panelInfo,BorderLayout.WEST);

    JPanel panelFase = new JPanel();
    labelTemporizador = new JLabel();
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
    panelFase.add(labelTemporizador);
    labelEsperando = new JLabel("Esperando jugadores...");
    labelEsperando.setEnabled(false);
    labelEsperando.setVisible(false);

    panelNorte.add(panelFase,BorderLayout.CENTER);
    panelNorte.add(labelEsperando,BorderLayout.EAST);

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
    panelBotones.add(jugadorGUI.getLabelXP());
    panelBotones.add(jugadorGUI.getPanelXP());

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
        jugadorGUI.comprarXP(xp);
        tiendaGUI.actualizarLabelMonedas();
        actualizarLabelNivel();
        tableroGUI.getTableroCore().setCantidadMaximaTablero(jugador.getNivel());
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
    public void actualizarRasgos(){}

    @Override
    public void notificarRonda() {
        actualizarLabelRonda();
        tableroGUI.limpiarTableroEnemigo();
        if (juego.getEstadoActual() == EstadosJuego.PELEA){
            tableroGUI.habilitarTablero(false);
        }else if (juego.getEstadoActual() == EstadosJuego.PREPARACION) {
            tableroGUI.habilitarTablero(true);
        }
        if (juego.getEstadoActual()== EstadosJuego.FIN_DE_RONDA){
         subeXp(2);
         tiendaGUI.actualizarTiendaGui();
        }
        labelEsperando.setEnabled(false);
        labelEsperando.setVisible(false);
        juego.cambiarListosNull();
        botonRonda.setEnabled(true);
    }

    @Override
    public void actualizarTiempo(int segundos) {
        labelTemporizador.setText("Tiempo restante: " + segundos + "s");
    }

    @Override
    public void notificarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    @Override
    public void notificarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
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
