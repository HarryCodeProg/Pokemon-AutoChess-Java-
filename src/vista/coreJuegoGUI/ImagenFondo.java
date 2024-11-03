package vista.coreJuegoGUI;

import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class ImagenFondo extends StackPane {
    private ImageView imageView;
    private Random random;

    public ImagenFondo(List<String> nombresImagenes) {
        random = new Random();
        cargarImagenAleatoria(nombresImagenes);
        getChildren().add(imageView);
    }

    private void cargarImagenAleatoria(List<String> nombresImagenes) {
        // Seleccionar un nombre de archivo aleatorio de la lista
        String nombreSeleccionado = nombresImagenes.get(random.nextInt(nombresImagenes.size()));
        // Obtener la imagen
        Image imagen = new Image(getClass().getResourceAsStream(nombreSeleccionado));
        imageView = new ImageView(imagen);
        imageView.setFitWidth(getPrefWidth());
        imageView.setFitHeight(getPrefHeight());
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());
    }
}


