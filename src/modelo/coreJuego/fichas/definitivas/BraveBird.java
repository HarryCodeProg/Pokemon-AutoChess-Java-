package modelo.coreJuego.fichas.definitivas;

import modelo.coreJuego.fichas.Ficha;

public class BraveBird extends Definitiva{

    public BraveBird(){
        super("Aumenta su daño fisico por 1%/2%/4% de forma permanente");
    }

    @Override
    public void ejecutar(Ficha ficha) {
        double por = 0;
        switch (ficha.getEstrellas()) {
            case 1:{
                por = 1;
                break;
            }
            case 2:{
                por = 2;
                break;
            }
            case 3:{
                por = 4;
                break;
            }
        }
        ficha.getEstadisticas().aumentarDañoFisicoPorcentaje(por);
    }
}
