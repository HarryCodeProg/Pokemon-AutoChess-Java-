package modelo.coreJuego.fichas;

import javax.swing.*;
import java.awt.*;

public class FichaImagen {
    private String nombreImagen;
    private String direccionMini;
    private String direccionMiniEvol1;
    private String direccionMiniEvol2;
    private ImageIcon imagen;
    private String direccionSprite;
    private String spriteOriginal;
    private String direccionEvolucion1;
    private String direccionEvolucion2;


    public FichaImagen(String dirM,String direccionSprite) {
       this.direccionMini = dirM;
       this.direccionSprite = direccionSprite;
       this.spriteOriginal = direccionSprite;
    }

    public void setNombreImagen(String nombre) {this.nombreImagen = nombre;}

    public void setDireccionEvolucion1(String nombreEvolucion) {this.direccionEvolucion1 = nombreEvolucion;}

    public void cambiarEvol1(){this.direccionSprite = direccionEvolucion1;}

    public void cambiarEvol2(){this.direccionSprite = direccionEvolucion2;}

    public void cambiarImagen(String dir){
        this.imagen = crearImagen(dir);
    }

    public ImageIcon crearImagen(String direccion){
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(direccion));
        Image imagenOriginal = iconoOriginal.getImage();
        Image imFin = imagenOriginal.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(imFin);
    }

    public String getDireccionEvolucion1() {return direccionEvolucion1;}

    public void setDireccionEvolucion2(String nombreEvolucion) {
        this.direccionEvolucion2 = nombreEvolucion;
    }

    public String getDireccionEvolucion2() {return direccionEvolucion2;}

    public void setDireccionMiniEvol1(String dir) {this.direccionMiniEvol1 = dir;}

    public void setDireccionMiniEvol2(String dir) {this.direccionMiniEvol2 = dir;}

    public String getDireccionMiniEvol1() {return this.direccionMiniEvol1;}

    public String getDireccionMiniEvol2() {return this.direccionMiniEvol2;}

    public ImageIcon getImagen() {return imagen;}

    public String getDireccionMini() {return direccionMini;}

    public String getDireccionSprite(){return this.direccionSprite;}

    public String getSpriteOriginal(){return this.spriteOriginal;}
}
