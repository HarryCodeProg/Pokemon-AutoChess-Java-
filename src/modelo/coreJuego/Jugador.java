package modelo.coreJuego;

import java.io.Serializable;

public class Jugador implements Serializable {
    private int monedas = 9000;
    private int nivelJugador = 1;
    private int interes;
    private String nombre;
    private int vida;
    private int xpActual;
    private boolean listoParaRonda;

    public Jugador(String nombreS) {
        this.nombre = nombreS;
        this.vida = 100;
        this.listoParaRonda = false;
    }

    public int getNivel() {
        return this.nivelJugador;
    }

    public void subirNivel() {
        this.nivelJugador++;
    }

    public int getMonedas() {
        return this.monedas;
    }

    public void actualizarMonedasRonda() {
        this.monedas += 5;
        this.interes = Math.min(this.monedas / 10, 5);
        this.monedas += interes;
    }

    public void restarMonedas(int cantidad) {
        this.monedas = Math.max(0, this.monedas - cantidad);
    }

    public void sumarMonedas(int cantidad) {
        this.monedas += cantidad;
    }

    public int getVida() {
        return this.vida;
    }

    public void sumarVida(int cantidad) {
        this.vida += cantidad;
    }

    public void restarVida(int cantidad) {
        this.vida -= cantidad;
    }

    public void sumarXp(int xp){
        this.xpActual += xp;
    }

    public int getXpActual(){
        return this.xpActual;
    }

    public void restarXp(int xpParaRestar) {
        // Si la experiencia actual es mayor o igual a la experiencia para restar, simplemente restamos
        if (xpActual >= xpParaRestar) {
            xpActual -= xpParaRestar;
        } else {
            xpActual = 0;
        }
    }

    public boolean estaListoParaRonda() {
        return listoParaRonda;
    }

    public void setListoParaRonda(boolean listoParaRonda) {
        this.listoParaRonda = listoParaRonda;
    }

    public String getNombre() {
        return this.nombre;
    }
}
