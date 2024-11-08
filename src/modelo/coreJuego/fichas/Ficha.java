package modelo.coreJuego.fichas;

import java.awt.*;
import javax.swing.ImageIcon;

public class Ficha {
    private String nombre;
    private String nombreOriginal;
    private Rasgo rasgo1;
    private Rasgo rasgo2;
    private Rasgo rasgo3;
    private int coste;
    private int costeAnterior;
    private int estrellas;
    private FichaEstadisticas estadisticas;
    private FichaMovimiento movimiento;
    private FichaImagen imagen;
    private String nombreEvolucion1;
    private String nombreEvolucion2;
    private boolean fichaEnemiga = false;


    public Ficha(String nombre, Rasgo rasgo1, Rasgo rasgo2, Rasgo rasgo3, int coste,int mov,int vida,int costeU ,int danoFisico, int dañoAP, int defensaFisica, int defensaAP,String rutaImagen,String rutaSprite) {
        this.nombre = nombre;
        this.rasgo1 = rasgo1;
        this.rasgo2 = rasgo2;
        this.rasgo3 = rasgo3;
        this.coste = coste;
        this.estrellas = 1;
        imagen = new FichaImagen(rutaImagen,rutaSprite);
        estadisticas = new FichaEstadisticas(vida,costeU,danoFisico,dañoAP,defensaFisica,defensaAP);
        this.nombreOriginal = nombre;
        this.movimiento = new FichaMovimiento(mov);

        imagen.cambiarImagen(rutaImagen);
    }

    public void subirEstrellas() {
        if (estrellas < 3) {
            if(estrellas == 1){
                imagen.cambiarEvol1();
            }
            if(estrellas == 2){
                imagen.cambiarEvol2();
            }
            estrellas++;
            this.estadisticas.multipicarVida(2);
            this.estadisticas.multiplicarDanoFisico(2);
            this.estadisticas.multiplicarDañoAP(2);
            coste *= 3;
        }
    }

    public void setNombreEvolucion1(String nombreE){this.nombreEvolucion1 = nombreE;}

    public void setNombreEvolucion2(String nombreE){this.nombreEvolucion2 = nombreE;}

    public void cambiarNombre(){
        if (this.estrellas == 2){
            this.nombre = nombreEvolucion1;
        }
        if (this.estrellas == 3){
            this.nombre = nombreEvolucion2;
        }
    }

    public void setFichaMovimiento(FichaMovimiento fm){this.movimiento = fm;}

    public void setFichaEstadistica(FichaEstadisticas fe){this.estadisticas = fe;}

    public String getNombreOriginal(){return this.nombreOriginal;}

    public Color getColor(){
        switch(this.costeAnterior){
            case 1:return new Color(128, 128, 128, 60);
            case 2:return new Color(20, 156, 66, 60);
            case 3:return new Color(22, 66, 203, 60);
            case 4:return new Color(191, 8, 8, 60);
            case 5:return new Color(216, 244, 15, 60);
            default:
                return Color.BLACK;
        }
    }

    public void setCosteAnterior(){this.costeAnterior = coste;}

    public int getCosteAnterior(){return costeAnterior;}

    public void recibirDaño(int cantidad) {
        estadisticas.reducirVida(cantidad);
        if (estadisticas.getVidaActual() < 0) estadisticas.setVidaActual(0);
    }

    public FichaEstadisticas getEstadisticas(){return this.estadisticas;}

    public FichaMovimiento getMovimiento(){return this.movimiento;}

    public boolean estaViva() {return estadisticas.getVida() > 0;}

    public void atacar(Ficha enemigo) {
        enemigo.getEstadisticas().reducirVida(this.estadisticas.getDanoFisico());
        if (this.estadisticas.getManaActual() == this.estadisticas.getCosteUlti()){
            this.estadisticas.setManaActual(0);

        }else{
            this.estadisticas.setManaActual(5);
        }

        if (!enemigo.estaViva()) {
            /*Ficha nuevoEnemigo = buscarEnemigoCercano(tablero, this.fila, this.columna);
            if (nuevoEnemigo != null) {
                atacar(nuevoEnemigo);
            }*/
        }
    }

    public String getNombre() { return nombre; }

    public Rasgo getRasgo1() { return rasgo1; }

    public Rasgo getRasgo2() { return rasgo2; }

    public Rasgo getRasgo3() { return rasgo3; }

    public int getCoste() { return coste; }

    public int getEstrellas() {return estrellas;}

    public Rasgo[] getRasgos() {return new Rasgo[]{rasgo1, rasgo2, rasgo3};}

    public void esFichaEnemiga(){this.fichaEnemiga = true;}

    public boolean getFichaEnemiga() {return fichaEnemiga;}

    public FichaImagen getFichaImagen(){return this.imagen;}

    public String getNombreEvolucion1() {return nombreEvolucion1;}

    public String getNombreEvolucion2() {return nombreEvolucion2;}

    @Override
    public String toString() {
        if(rasgo3.getNombre() == Rasgos.NULL){
            return String.format("Nombre: %s\nRasgo1: %s\nRasgo2: %s\nCoste: %d\n",
                    nombre, rasgo1, rasgo2, coste);
        }else{
            return String.format("Nombre: %s\nRasgo1: %s\nRasgo2: %s\nRasgo3: %s\nCoste: %d\n",
                    nombre, rasgo1, rasgo2, rasgo3, coste);
        }

    }

}

