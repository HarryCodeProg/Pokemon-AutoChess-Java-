package modelo.coreJuego.fichas.definitivas;

import modelo.coreJuego.fichas.Ficha;

public abstract class Definitiva {
    private String descripcion;

    public Definitiva(String descripcion) { this.descripcion = descripcion; }
    public String getDescripcion() { return descripcion; }
    public abstract void ejecutar(Ficha ficha);

}
