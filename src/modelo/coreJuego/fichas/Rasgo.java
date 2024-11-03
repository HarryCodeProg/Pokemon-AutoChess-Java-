package modelo.coreJuego.fichas;

import javax.swing.*;

public class Rasgo {
    private Rasgos nombre;
    private int nivel;
    private  int minFichasBronce;
    private  int minFichasPlata;
    private  int minFichasOro;
    private  String descripcion;
    private ImageIcon imagenRasgo;

   public Rasgo(Rasgos nombre) {
    this.nombre = nombre;
    setImagenRasgo();
   }

  public Rasgos getNombre() {
    return nombre;
  }

  public void setImagenRasgo(){
      imagenRasgo = new ImageIcon(getClass().getResource("/modelo/images/types/" +nombre+".png"));
  }

  public ImageIcon getImagenRasgo() {
    return imagenRasgo;
  }

  public int getNivel() {
    return nivel;
  }

  public int getMinFichasBronce() {
        return minFichasBronce;
   }

    public int getMinFichasPlata() {
        return minFichasPlata;
    }

    public int getMinFichasOro() {
        return minFichasOro;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
