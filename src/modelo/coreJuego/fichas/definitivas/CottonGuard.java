package modelo.coreJuego.fichas.definitivas;

import modelo.coreJuego.fichas.Ficha;

public class CottonGuard extends Definitiva{

    public CottonGuard(){
        super("Aumenta su defensa AP y su defensa fisica en 10%/12%/100% por el resto del combate, se puede acumular");
    }

    @Override
    public void ejecutar(Ficha ficha) {
      switch (ficha.getEstrellas()){
          case 1 : {
              ficha.getEstadisticas().aumentarDefensaApPorcentaje(10);
              ficha.getEstadisticas().aumentarDefensaFisicaTemporal(10);
              break;
          }
          case 2:{
              ficha.getEstadisticas().aumentarDefensaApPorcentaje(12);
              ficha.getEstadisticas().aumentarDefensaFisicaTemporal(12);
              break;
          }
          case 3:{
              ficha.getEstadisticas().aumentarDefensaApPorcentaje(100);
              ficha.getEstadisticas().aumentarDefensaFisicaTemporal(100);
              break;
          }
      }
    }
}
