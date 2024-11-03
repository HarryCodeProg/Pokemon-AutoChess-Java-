package modelo.coreJuego.fichas.definitivas;

import modelo.coreJuego.fichas.Ficha;

public class Clone extends Definitiva{

    public Clone(){
        super("Crea una copia aliada de 1/1/6 enemigos aleatorios");
    }

    @Override
    public void ejecutar(Ficha ficha) {
        int cant = 0;
        switch(ficha.getEstrellas()){
            case 1: cant = 1; break;
            case 2: cant = 1; break;
            case 3: cant = 6; break;
        }

        for(int i = 0; i < cant; i++){

        }
    }
}
