package vista.coreJuegoGUI;

import modelo.coreJuego.Tienda;
import modelo.coreJuego.fichas.Ficha;
import modelo.coreJuego.fichas.Rasgo;
import modelo.coreJuego.fichas.Rasgos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiendaGUI {
    private List<String> direccionesImagenesRasgos = new ArrayList<>();
    private Tienda tienda;
    private Map<Rasgos, String> imagenesRasgos = new HashMap<>();
    private JPanel panelFichas = new JPanel();
    private Label labelMensaje = new Label();
    private JLabel labelMonedas = new JLabel();
    private JPanel venta;

    public TiendaGUI(Tienda t){
        this.tienda = t;
        asignarImagenesRasgos();
        inicializarVenta();
    }

    public Map<Rasgos, String> getImagenesRasgos(){
        return this.imagenesRasgos;
    }

    public void asignarImagenes() {
        try {
            // Leer el archivo JSON como String
            String contenido = new String(Files.readAllBytes(Paths.get("C:\\Users\\Harry\\IdeaProjects\\PokemonAutoChess\\src\\modelo\\images\\package.json")));

            // Convertir el contenido a un objeto JSONObject
            JSONObject jsonFichas = new JSONObject(contenido);

            // Asignar imágenes a las fichas
            for (Ficha ficha : this.tienda.getTodasLasFichas()) {
                // Verificar si el nombre de la ficha existe en el JSON
                if (jsonFichas.has(ficha.getNombre())) {
                    JSONArray imagenes = jsonFichas.getJSONArray(ficha.getNombre());
                    ficha.setCosteAnterior();

                    // Obtener las imágenes y asignarlas, asegurando que existan suficientes elementos en el array
                    if (imagenes.length() > 0) {
                        ficha.getFichaImagen().setNombreImagen(imagenes.getString(0));
                    }
                    if (imagenes.length() > 1) {
                        ficha.getFichaImagen().setDireccionEvolucion1(imagenes.getString(1));
                    }
                    if (imagenes.length() > 2) {
                        ficha.getFichaImagen().setDireccionEvolucion2(imagenes.getString(2));
                    }
                    if (imagenes.length() > 3) {
                        ficha.setNombreEvolucion1(imagenes.getString(3));
                    }
                    if(imagenes.length() > 4) {
                        ficha.setNombreEvolucion2(imagenes.getString(4));
                    }
                    if (imagenes.length() > 5){
                        ficha.getFichaImagen().setDireccionMiniEvol1(imagenes.getString(5));
                    }
                    if (imagenes.length() > 6){
                        ficha.getFichaImagen().setDireccionMiniEvol2(imagenes.getString(6));
                    }
                } else {
                    // Si la ficha no tiene imágenes en el JSON, dejar los campos vacíos
                    ficha.getFichaImagen().setNombreImagen("");
                    ficha.getFichaImagen().setDireccionEvolucion1("");
                    ficha.getFichaImagen().setDireccionEvolucion2("");
                }
            }
        } catch (IOException e) {
            // Manejo de error al leer el archivo
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            // Manejo de error en la estructura del JSON
            System.out.println("Error al procesar el JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void inicializarVenta(){
        venta = new JPanel();
        ImageIcon im = new ImageIcon();
        im.setImage(new ImageIcon("src/modelo/images/papelera.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        JLabel ms = new JLabel("vender ficha");
        ms.setForeground(Color.BLACK);
        JLabel img = new JLabel(im);
        venta.add(img);
        venta.add(ms);
        venta.setBackground(Color.RED);
        venta.setOpaque(true);
        venta.setPreferredSize(new Dimension(20, 100));
        venta.setVisible(true);
        venta.setEnabled(true);
        venta.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                    if (tienda.getBanca().getFichaDeLaBanca()!= null){
                        tienda.getBanca().eliminarFichaSeleccionada(tienda.getBanca().getFichaDeLaBanca());
                        tienda.agregarFichaAlPool(tienda.getBanca().getFichaDeLaBanca().getFicha(),tienda.getBanca().getFichaDeLaBanca().getFicha().getEstrellas()/3);
                    }else if(tienda.getTablero().getFichaTablero()!= null){
                        tienda.getTablero().sacarFichaTablero(tienda.getTablero().getFichaTablero().getFicha());
                        tienda.getBanca().eliminarFichaSeleccionada(tienda.getTablero().getFichaTablero());
                        tienda.agregarFichaAlPool(tienda.getTablero().getFichaTablero().getFicha(),tienda.getTablero().getFichaTablero().getFicha().getEstrellas()/3);

                    }else{
                        System.out.println("ninguna ficha seleccionada");
                    }

            }
        });
    }

    public void cambiarVisibilidadVenta(boolean v){
        venta.setVisible(v);
        venta.setEnabled(v);
    }

    public JPanel getVenta(){return this.venta;}

    public void cargarDireccionesImagenesRasgos(){
        // Ruta de la carpeta que contiene las imágenes
        String rutaCarpeta = "C:\\Users\\Harry\\IdeaProjects\\PokemonAutoChess\\src\\modelo\\images\\types";

        // Crear un objeto File para la carpeta
        File carpeta = new File(rutaCarpeta);

        // Verificar si es un directorio
        if (carpeta.isDirectory()) {
            // Obtener los archivos en la carpeta
            File[] archivos = carpeta.listFiles();

            if (archivos != null) {
                for (File archivo : archivos) {
                    // Verificar si el archivo es un .png
                    if (archivo.isFile() && archivo.getName().endsWith(".png")) {
                        direccionesImagenesRasgos.add(archivo.getName());
                    }
                }
            }
        } else {
            System.out.println("La ruta especificada no es un directorio.");
        }
    }

    public void asignarImagenesRasgos() {
        cargarDireccionesImagenesRasgos();
        int i = 0;
        // Iterar sobre los rasgos
        for (Rasgos rasgo : Rasgos.values()) {
            // Solo asignar si hay una imagen disponible
            if (i < direccionesImagenesRasgos.size()) {
                imagenesRasgos.put(rasgo, direccionesImagenesRasgos.get(i));
                i++;
            } else {
                break; // Salir del bucle si ya no hay más imágenes
            }
        }
    }

    public void actualizarTiendaGui() {
        panelFichas.removeAll(); // Limpiar las fichas anteriores
        //List<Ficha> fichas = this.tienda.generarFichas(this.tienda.getJugador().getNivel());
        List<Ficha> fichas = tienda.actualizarTienda(tienda.getJugador().getNivel());
        for (Ficha ficha : fichas) {
            JPanel panelFicha = new JPanel(new BorderLayout());

            JPanel panelRasgos = new JPanel();
            panelRasgos.setLayout(new BoxLayout(panelRasgos, BoxLayout.Y_AXIS));

            for (Rasgo rasgo : ficha.getRasgos()) {
                if(!rasgo.getNombre().equals(Rasgos.NULL)){
                    ImageIcon imagenRasgo = rasgo.getImagenRasgo();

                    // Escalar la imagen a un tamaño más pequeño
                    //Image imagenEscalada = imagenRasgo.getImage().getScaledInstance(60, 40, Image.SCALE_SMOOTH);
                    Image imagenEscalada = imagenRasgo.getImage().getScaledInstance(50, 30, Image.SCALE_SMOOTH); // Imágenes más pequeñas
                    JLabel rasgoLabel = new JLabel(new ImageIcon(imagenEscalada));
                    panelRasgos.add(rasgoLabel);
                }
            }
            JLabel coste = new JLabel("coste: "+ficha.getCoste());
            coste.setAlignmentX(Component.LEFT_ALIGNMENT);  // Alinear el coste a la izquierda, igual que las imágenes
            panelRasgos.add(Box.createVerticalGlue());  // Añadir espacio flexible antes del coste
            panelRasgos.add(Box.createRigidArea(new Dimension(10, 0))); // Ajustar espaciado horizontal si es necesario
            panelRasgos.add(coste);
            panelRasgos.add(Box.createVerticalStrut(5));
            //panelRasgos.add(coste,BorderLayout.SOUTH);

            // Crear un JLabel con la imagen y el nombre de la ficha
            JLabel fichaLabel = new JLabel(ficha.getNombre(), ficha.getFichaImagen().getImagen(), JLabel.CENTER);
            fichaLabel.setVerticalTextPosition(JLabel.BOTTOM);
            fichaLabel.setHorizontalTextPosition(JLabel.CENTER);

            fichaLabel.setFont(new Font("Arial", Font.PLAIN, 10));

            fichaLabel.setBackground(ficha.getColor());
            fichaLabel.setOpaque(true);

            // Añadir el JLabel al JPanel que representa la ficha
            panelFicha.add(panelRasgos, BorderLayout.WEST);
            panelFicha.add(fichaLabel, BorderLayout.CENTER);

            // Añadir MouseListener al JLabel de la ficha
            fichaLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (tienda.getJugador().getMonedas() >= ficha.getCoste()) {
                        tienda.sacarFichaDelPool(ficha);
                        tienda.getJugador().restarMonedas(ficha.getCoste()); // Restar las monedas del jugador
                        actualizarLabelMonedas(); // Actualizar la visualización de monedas
                        //banca.agregarFicha(ficha); // Mover la ficha a la banca
                        FichaClickeableGUI fichaClickeableGUI = new FichaClickeableGUI(ficha);
                        //fichaClickeableGUI.setTablero(bancaTab);
                        fichaClickeableGUI.setBanca(tienda.getBanca());
                        //bancaTab.agregarFichaALaBanca(fichaClickeableGUI);
                        tienda.getBanca().agregarFichaALaBanca(fichaClickeableGUI);

                        // Eliminar la ficha de la tienda
                        panelFicha.removeAll(); // Vaciar el panel donde estaba la ficha
                        panelFicha.revalidate();
                        panelFicha.repaint();
                    } else {
                        labelMensaje.setText("Monedas insuficientes"); // Mostrar mensaje de error
                    }
                }
            });

            // Añadir el panel que contiene la ficha al panel de la tienda
            panelFichas.add(panelFicha);
        }

        panelFichas.revalidate(); // Revalidar para actualizar la vista
        panelFichas.repaint(); // Repintar para asegurarse de que los cambios se reflejen
    }

    public void actualizarLabelMonedas() {
        labelMonedas.setText(String.format("Monedas: %d", tienda.getJugador().getMonedas()));
        labelMonedas.revalidate();
        labelMonedas.repaint();
    }

    public JLabel getLabelMonedas() {
        return this.labelMonedas;
    }

    public JPanel getPanelFichas(){return this.panelFichas;}

}
