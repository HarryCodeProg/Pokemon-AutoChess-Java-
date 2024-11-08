package vista.coreJuegoGUI;

import modelo.coreJuego.fichas.Ficha;
import modelo.coreJuego.fichas.Rasgo;
import modelo.coreJuego.fichas.Rasgos;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class FichaClickeableGUI extends JLabel {
    private Ficha ficha;
    private BufferedImage spriteSheet;
    private BufferedImage[] frames;
    private int currentFrame = 0;
    private Timer timer;
    private boolean animacionActiva = false;
    private JPanel barraVida;
    private JPanel barraMana;
    //private Tablero tablero;
    private BancaGUI banca;

    public FichaClickeableGUI(Ficha ficha) {
        this.ficha = ficha;
        setIcon(ficha.getFichaImagen().getImagen());
        setVerticalTextPosition(JLabel.BOTTOM);
        setHorizontalTextPosition(JLabel.CENTER);

        barraVida = new JPanel();
        barraMana = new JPanel();

        barraVida.setPreferredSize(new Dimension(50, 5));
        barraMana.setPreferredSize(new Dimension(50, 3));

        actualizarBarraVida();
        actualizarBarraMana();

        setLayout(new BorderLayout());

        JPanel panelBarras = new JPanel();
        panelBarras.setLayout(new BoxLayout(panelBarras, BoxLayout.Y_AXIS));
        panelBarras.add(barraVida);
        panelBarras.add(barraMana);

        add(panelBarras,BorderLayout.NORTH);

        addMouseListener(new MouseAdapter() {
           /* @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    iniciarArrastre(e);
                }
            }*/

           /* @Override
            public void mouseReleased(MouseEvent e) {
                if (enArrastre) {
                    finalizarArrastre(e);
                }
            }*/

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    mostrarStatsFicha();
                }else if (SwingUtilities.isLeftMouseButton(e)) {
                    //bTablero.seleccionarFicha(FichaClickeableGUI.this);
                    banca.seleccionarFicha(FichaClickeableGUI.this);
                }
            }
        });
    }

    public void setBanca(BancaGUI banca){
        this.banca = banca;
    }

   /* public void setTab(Tablero tab){
        this.tablero = tab;
    }*/

    public void iniciarAnimacion() {
        if (animacionActiva) {
            detenerAnimacion();
        }
        if(this.ficha.getNombre().equals("Regigigas")){
            animarFicha(ficha.getFichaImagen().getDireccionSprite(),48, 48, 4,0);
        }else{
            animarFicha(ficha.getFichaImagen().getDireccionSprite(),64, 64, 4,0);
        }

        animacionActiva = true;
    }

    public void animacionIzquierda() {
        if (animacionActiva) {
            detenerAnimacion();
        }
        animarFicha(ficha.getFichaImagen().getDireccionSprite(), 64, 64, 4, 1);
        animacionActiva = true;
    }

    public void animacionDerecha() {
        if (animacionActiva) {
            detenerAnimacion();
        }
        animarFicha(ficha.getFichaImagen().getDireccionSprite(), 64, 64, 4, 2);
        animacionActiva = true;
    }

    public void animacionArriba() {
        if (animacionActiva) {
            detenerAnimacion();
        }
        animarFicha(ficha.getFichaImagen().getDireccionSprite(), 64, 64, 4, 3);
        animacionActiva = true;
    }

    public void detenerAnimacion() {
        if (animacionActiva) {
            timer.stop();
            setIcon(ficha.getFichaImagen().getImagen());
            animacionActiva = false;
        }
    }

    public void animarFicha(String rutaSpriteSheet, int frameWidth, int frameHeight, int numFrames, int fila) {
        try {
            spriteSheet = ImageIO.read(getClass().getResource(rutaSpriteSheet));

            // Dividir el sprite sheet en frames individuales de la fila seleccionada
            frames = new BufferedImage[numFrames];
            int spriteSheetWidth = spriteSheet.getWidth();
            int spriteSheetHeight = spriteSheet.getHeight();

            for (int i = 0; i < numFrames; i++) {
                // Verifica que el frame esté dentro de los límites del sprite sheet
                if ((i + 1) * frameWidth <= spriteSheetWidth && (fila + 1) * frameHeight <= spriteSheetHeight) {
                    frames[i] = spriteSheet.getSubimage(i * frameWidth, fila * frameHeight, frameWidth, frameHeight);
                } else {
                    System.out.println("El frame " + i + " está fuera de los límites del sprite sheet.");
                }
            }

            // Configurar la animación para cambiar frames cada 200 ms (0.2 segundos)
            timer = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentFrame = (currentFrame + 1) % numFrames;
                    setIcon(new ImageIcon(frames[currentFrame]));
                }
            });

            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public void actualizarBarraVida() {
        // Obtener el porcentaje de vida
        double porcentajeVida = (double) ficha.getEstadisticas().getVidaActual() / ficha.getEstadisticas().getVida();

        // Ajustar el ancho de la barra de vida según el porcentaje
        int anchoBarraVida = (int) (porcentajeVida * barraVida.getPreferredSize().width);

        barraVida.removeAll();
        barraVida.setLayout(new BorderLayout());
        JPanel vidaActual = new JPanel();
        vidaActual.setBackground(Color.GREEN);
        vidaActual.setPreferredSize(new Dimension(anchoBarraVida, barraVida.getPreferredSize().height));

        JPanel vidaRestante = new JPanel();
        vidaRestante.setBackground(Color.RED);
        vidaRestante.setPreferredSize(new Dimension(barraVida.getPreferredSize().width - anchoBarraVida, barraVida.getPreferredSize().height));

        barraVida.add(vidaActual, BorderLayout.WEST);
        barraVida.add(vidaRestante, BorderLayout.EAST);

        barraVida.revalidate();
        barraVida.repaint();
    }

    public void actualizarBarraMana() {
        // Obtener el porcentaje de maná
        double porcentajeMana = (double) ficha.getEstadisticas().getManaActual() / ficha.getEstadisticas().getCosteUlti();

        // Ajustar el ancho de la barra de maná según el porcentaje
        int anchoBarraMana = (int) (porcentajeMana * barraMana.getPreferredSize().width);

        barraMana.removeAll();
        barraMana.setLayout(new BorderLayout());

        // Panel para la parte de maná actual (llena)
        JPanel manaActual = new JPanel();
        manaActual.setBackground(Color.BLUE);
        manaActual.setPreferredSize(new Dimension(anchoBarraMana, barraMana.getPreferredSize().height));

        // Añadir solo la barra de maná llena
        barraMana.add(manaActual, BorderLayout.WEST);

        barraMana.revalidate();
        barraMana.repaint();
    }

    public Ficha getFicha() {
        return this.ficha;
    }

    private JPanel crearPanelConNombre(ImageIcon imagen, String nombre) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel imagenLabel = new JLabel(imagen);
        JLabel nombreLabel = new JLabel(nombre);
        nombreLabel.setHorizontalAlignment(JLabel.CENTER);
        /*nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setBackground(Color.BLACK);*/
        nombreLabel.setOpaque(true);
        panel.add(imagenLabel);
        panel.add(nombreLabel);
        return panel;
    }

    private void mostrarStatsFicha() {
        JPanel panelStats = new JPanel(new BorderLayout());
        //panelStats.setLayout(new BoxLayout(panelStats, BoxLayout.Y_AXIS));
        panelStats.setBackground(ficha.getColor());

        JPanel evoluciones = new JPanel();
        evoluciones.setLayout(new BoxLayout(evoluciones, BoxLayout.X_AXIS));
        JPanel panelFicha = crearPanelConNombre(ficha.getFichaImagen().getImagen(), ficha.getNombreOriginal());
        JPanel panelE1 = crearPanelConNombre(ficha.getFichaImagen().crearImagen(ficha.getFichaImagen().getDireccionMiniEvol1()), ficha.getNombreEvolucion1());
        JPanel panelE2 = crearPanelConNombre(ficha.getFichaImagen().crearImagen(ficha.getFichaImagen().getDireccionMiniEvol2()), ficha.getNombreEvolucion2());
        evoluciones.add(panelFicha);
        evoluciones.add(panelE1);
        evoluciones.add(panelE2);
        panelStats.add(evoluciones,BorderLayout.NORTH);

        JPanel panelRasgos = new JPanel();
        panelRasgos.setLayout(new BoxLayout(panelRasgos, BoxLayout.Y_AXIS));
        panelRasgos.setBackground(ficha.getColor());
        panelRasgos.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Rasgo rasgo : ficha.getRasgos()) {
            if(!rasgo.getNombre().equals(Rasgos.NULL)) {
                ImageIcon imagenRasgo = rasgo.getImagenRasgo();

                Image imgRasgo = imagenRasgo.getImage();
                Image imgRasgoEscalada = imgRasgo.getScaledInstance(60, 30, java.awt.Image.SCALE_SMOOTH);
                ImageIcon imagenRasgoEscalada = new ImageIcon(imgRasgoEscalada);

                JLabel rasgoLabel = new JLabel(imagenRasgoEscalada);
                panelRasgos.add(rasgoLabel);
            }
        }
        panelStats.add(panelRasgos,BorderLayout.CENTER);

        String stats = String.format(
                "Actual: %s\nEstrellas: %d\nAlcance: %d\nVida: %d\nDaño Físico: %d\nDaño AP: %d\nDefensa Fisica:%d\nDefensa AP:%d\nCoste: %d",
                ficha.getNombre(), ficha.getEstrellas(),ficha.getMovimiento().getAlcance(), ficha.getEstadisticas().getVida(), ficha.getEstadisticas().getDanoFisico(), ficha.getEstadisticas().getDañoAP(),ficha.getEstadisticas().getDefensaFisicaActual(),ficha.getEstadisticas().getDefensaApActual() ,ficha.getCoste()
        );

        JLabel statsLabel = new JLabel("<html>" + stats.replace("\n", "<br>") + "</html>"); // Convertir el texto a HTML para saltos de línea
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelStats.add(statsLabel,BorderLayout.SOUTH);

        // Mostrar el panel en un JOptionPane
        JOptionPane.showMessageDialog(this,  panelStats, "Estadísticas de la Ficha", JOptionPane.INFORMATION_MESSAGE);
        setBackground(new Color(0, 0, 0, 60));
    }

}