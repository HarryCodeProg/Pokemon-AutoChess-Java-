package modelo.coreJuego;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelo.coreJuego.fichas.*;
import modelo.coreJuego.Jugador;
import vista.coreJuegoGUI.Banca;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import vista.coreJuegoGUI.FichaClickeableGUI;
import vista.coreJuegoGUI.Tablero;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Tienda {

    private List<Ficha> todasLasFichas;
    private Random random = new Random();
    private Ficha krabby;
    private Ficha rattata;
    private Ficha regigigas;
    private Jugador jugador;
    private Banca banca;
    private Tablero tablero;

    public Tienda() {
        todasLasFichas = new ArrayList<>();
        Rasgo breaker = new Rasgo(Rasgos.BREAKER);
        Rasgo bulky = new Rasgo(Rasgos.BULKY);
        Rasgo dark = new Rasgo(Rasgos.DARK);
        Rasgo disruptor = new Rasgo(Rasgos.DISRUPTOR);
        Rasgo dragon = new Rasgo(Rasgos.DRAGON);
        Rasgo electric = new Rasgo(Rasgos.ELECTRIC);
        Rasgo fairy = new Rasgo(Rasgos.FAIRY);
        Rasgo fire = new Rasgo(Rasgos.FIRE);
        Rasgo flying = new Rasgo(Rasgos.FLYING);
        Rasgo ghost = new Rasgo(Rasgos.GHOST);
        Rasgo grass = new Rasgo(Rasgos.GRASS);
        Rasgo ground = new Rasgo(Rasgos.GROUND);
        Rasgo hazard = new Rasgo(Rasgos.HAZARD);
        Rasgo ice = new Rasgo(Rasgos.ICE);
        Rasgo normal = new Rasgo(Rasgos.NORMAL);
        Rasgo fight = new Rasgo(Rasgos.NULL);
        Rasgo pivot = new Rasgo(Rasgos.PIVOT);
        Rasgo poison = new Rasgo(Rasgos.POISON);
        Rasgo psychic = new Rasgo(Rasgos.PSYCHIC);
        Rasgo revenger = new Rasgo(Rasgos.REVENGER);
        Rasgo rock = new Rasgo(Rasgos.ROCK);
        Rasgo steel = new Rasgo(Rasgos.STEEL);
        Rasgo support = new Rasgo(Rasgos.SUPPORT);
        Rasgo sweeper = new Rasgo(Rasgos.SWEEPER);
        Rasgo utility = new Rasgo(Rasgos.UTILITY);
        Rasgo wall = new Rasgo(Rasgos.WALL);
        Rasgo water = new Rasgo(Rasgos.WATER);
        Rasgo bug = new Rasgo(Rasgos.BUG);

        FichaMovimiento movDaño = new FichaMovimiento(3);
        FichaMovimiento movTanque = new FichaMovimiento(1);

        for (int i = 0; i<= 21;i++){
            todasLasFichas.add(new Ficha("Abra", psychic, sweeper, fight, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/1-a abra.png","/modelo/images/pokemon/1-a abra.png"));
            todasLasFichas.add(new Ficha("Bulbasaur", grass, poison, wall, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/2-a bulbasaur.png","/modelo/images/pokemon/2-a bulbasaur.png"));
            todasLasFichas.add(new Ficha("Beldum", steel, psychic, bulky, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/3-a beldum.png","/modelo/images/pokemon/3-a beldum.png"));
            todasLasFichas.add(new Ficha("Happiny", normal, wall, fight, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/4-a happiny.png","/modelo/images/pokemon/4-a happiny.png"));
            todasLasFichas.add(new Ficha("Exeggcute", grass, psychic, breaker, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/5-a exeggcute.png","/modelo/images/pokemon/5-a exeggcute.png"));
            todasLasFichas.add(new Ficha("Gible", dragon, dark, bulky, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/6-a gible.png","/modelo/images/pokemon/6-a gible.png"));
            todasLasFichas.add(new Ficha("Pichu", electric, revenger, fight, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/7-a pichu.png","/modelo/images/pokemon/7-a pichu.png"));
            todasLasFichas.add(new Ficha("Zubat", poison, flying, disruptor, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/8-a zubat.png","/modelo/images/pokemon/8-a zubat.png"));
            todasLasFichas.add(new Ficha("Gastly", ghost, poison, utility, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/9-a gastly.png","/modelo/images/pokemon/9-a gastly.png"));
            todasLasFichas.add(new Ficha("Larvitar", rock,dark,bulky, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/10-a larvitar.png","/modelo/images/pokemon/10-a larvitar.png"));
        }

        for (int i = 0; i<= 19;i++){
            todasLasFichas.add(new Ficha("Weedle", bug, poison, sweeper, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/11-a weedle.png","/modelo/images/pokemon/11-a weedle.png"));
            todasLasFichas.add(new Ficha("Scyther", bug, steel, pivot, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/12-a scyther.png","/modelo/images/pokemon/12-a scyther.png"));
            todasLasFichas.add(new Ficha("Magikarp", water, flying, sweeper, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/13-a magikarp.png","/modelo/images/pokemon/13-a magikarp.png"));
            todasLasFichas.add(new Ficha("Porygon", normal, breaker, fight, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/14-a porygon.png","/modelo/images/pokemon/14-a porygon.png"));
            todasLasFichas.add(new Ficha("Litwick", fire, ghost, sweeper, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/15-a litwick.png","/modelo/images/pokemon/15-a litwick.png"));
            todasLasFichas.add(new Ficha("Honedge", steel, ghost, sweeper, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/16-a honedge.png","/modelo/images/pokemon/16-a honedge.png"));
            todasLasFichas.add(new Ficha("Dreepy", dragon, ghost, revenger, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/17-a dreepy.png","/modelo/images/pokemon/17-a dreepy.png"));
            todasLasFichas.add(new Ficha("Darkrai", dark, disruptor, fight, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/18- darkrai.png","/modelo/images/pokemon/18- darkrai.png"));
            todasLasFichas.add(new Ficha("Lapras", water, ice, bulky, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/19- lapras.png","/modelo/images/pokemon/19- lapras.png"));
            todasLasFichas.add(new Ficha("Mudkip", water, ground, pivot, 2,1,400,50,100,100,20,20,"/modelo/images/pokemon/mini/20-a mudkip.png","/modelo/images/pokemon/20-a mudkip.png"));
        }

        for (int i = 0; i<= 16;i++){
            todasLasFichas.add(new Ficha("Landorus",ground,flying,bulky,3,1,500,50,150,150,25,25,"/modelo/images/pokemon/mini/21- landorus.png","/modelo/images/pokemon/21- landorus.png"));
            todasLasFichas.add(new Ficha("Larvesta",fire,bug,sweeper,3,1,500,50,150,150,25,25,"/modelo/images/pokemon/mini/22-a larvesta.png","/modelo/images/pokemon/22-a larvesta.png"));
            todasLasFichas.add(new Ficha("Fletchling",fire,flying,revenger,1,3,500,50,150,150,25,25,"/modelo/images/pokemon/mini/23-a fletchling.png","/modelo/images/pokemon/23-a fletchling.png"));
            todasLasFichas.add(new Ficha("Geodude", rock,ground,wall,3,1,500,50,150,150,25,25,"/modelo/images/pokemon/mini/23-a fletchling.png","/modelo/images/pokemon/23-a fletchling.png"));
            todasLasFichas.add(new Ficha("Heatran",fire,steel,breaker,3,1,500,50,150,150,25,25,"/modelo/images/pokemon/mini/25- heatran.png","/modelo/images/pokemon/25- heatran.png"));
            todasLasFichas.add(new Ficha("Litten",fire,dark,bulky,3,1,500,50,150,150,25,25,"/modelo/images/pokemon/mini/26-a litten.png","/modelo/images/pokemon/26-a litten.png"));
            todasLasFichas.add(new Ficha("Seedot",grass,dark,disruptor,3,1,500,50,150,150,25,25,"/modelo/images/pokemon/mini/27-a seedot.png","/modelo/images/pokemon/27-a seedot.png"));
            todasLasFichas.add(new Ficha("Magnemite",electric,steel,support,3,1,500,50,150,150,25,25,"/modelo/images/pokemon/mini/28-a magnemite.png","/modelo/images/pokemon/28-a magnemite.png"));
        }

        for (int i = 0; i<= 9;i++){
            //todasLasFichas.add(new Ficha("Rattata",Rasgos.NULL,Rasgos.NULL,Rasgos.NULL,4,600,200,200,"/images/pokemon/mini/30- rattata.png","/images/pokemon/30- rattata.png"));
            todasLasFichas.add(new Ficha("Grubbin",electric,bug,breaker,4,1,600,60,200,200,30,30,"/modelo/images/pokemon/mini/33-a grubbin.png","/modelo/images/pokemon/33-a grubbin.png"));
            todasLasFichas.add(new Ficha("Sneasel",ice,dark, revenger,4,1,600,60,200,200,30,30,"/modelo/images/pokemon/mini/34-a sneasel.png","/modelo/images/pokemon/34-a sneasel.png"));
            todasLasFichas.add(new Ficha("Rotomw",water,electric,utility,4,1,600,60,200,200,30,30,"/modelo/images/pokemon/mini/35- rotomw.png","/modelo/images/pokemon/35- rotomw.png"));
            todasLasFichas.add(new Ficha("Turtonator",fire,dragon,wall,4,1,600,60,200,200,30,30,"/modelo/images/pokemon/mini/36- turtonator.png","/modelo/images/pokemon/36- turtonator.png"));
            todasLasFichas.add(new Ficha("Wooloo",normal,wall,fight,4,1,600,60,200,200,30,30,"/modelo/images/pokemon/mini/37-a wooloo.png","/modelo/images/pokemon/37-a wooloo.png"));
            todasLasFichas.add(new Ficha("Zorua",dark,rock,pivot,4,1,600,60,200,200,30,30,"/modelo/images/pokemon/mini/40-a zorua.png","/modelo/images/pokemon/40-a zorua.png"));
        }

        for (int i = 0; i<= 8;i++){
            todasLasFichas.add(new Ficha("Mewtwo",psychic,utility,fight,5,1,1000,30,200,200,35,35,"/modelo/images/pokemon/mini/29- mewtwo.png","/modelo/images/pokemon/29- mewtwo.png"));
            todasLasFichas.add(new Ficha("Nihilego",rock,poison,wall,5,1,1000,30,200,200,35,35,"/modelo/images/pokemon/mini/31- nihilego.png","/modelo/images/pokemon/31- nihilego.png"));
            //todasLasFichas.add(new Ficha("Regigigas",Rasgos.NULL,Rasgos.NULL,Rasgos.NULL,5,1000,200,200,"/images/pokemon/mini/32- regigigas.png","/images/pokemon/32- regigigas.png"));
            todasLasFichas.add(new Ficha("Stonjourner",rock,support,fight,5,1,1000,30,200,200,35,35,"/modelo/images/pokemon/mini/38- stonjourner.png","/modelo/images/pokemon/38- stonjourner.png"));
        }
        krabby = new Ficha("Krabby", fight, fight, fight, 1,1,300,40,100,100,15,15,"/modelo/images/pve/Normal.png","/modelo/images/pve/Swing.png");
        rattata = new Ficha("Rattata",fight,fight,fight, 1,1,300,40,100,100,15,15,"/modelo/images/pokemon/mini/30- rattata.png","/modelo/images/pokemon/30- rattata.png");
        regigigas = new Ficha("Regigigas",fight,fight,fight,5,1,1000,30,200,200,35,35,"/modelo/images/pokemon/mini/32- regigigas.png","/modelo/images/pve/regigigas.png");

    }

    public List<Ficha> getTodasLasFichas(){return this.todasLasFichas;}

    public Jugador getJugador(){return this.jugador;}

    public Banca getBanca() {return banca;}

    public Tablero getTablero() {return tablero;}

    public List<Ficha> actualizarTienda(int nivel) {
        List<Ficha> fichasEnTienda = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int costeSeleccionado = seleccionarCoste(nivel);
            List<Ficha> fichasFiltradas = new ArrayList<>();
            for (Ficha ficha : todasLasFichas) {
                if (ficha.getCoste() == costeSeleccionado) {
                    fichasFiltradas.add(ficha);
                }
            }
            if (!fichasFiltradas.isEmpty()) {
                Ficha fichaSeleccionada = fichasFiltradas.get(random.nextInt(fichasFiltradas.size()));
                fichasEnTienda.add(fichaSeleccionada);
            }
        }
        return fichasEnTienda;
    }

    public List<Ficha> generarFichas(int nivel) {
        List<Ficha> fichasEnTienda = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int costeSeleccionado = seleccionarCoste(nivel);
            List<Ficha> fichasFiltradas = new ArrayList<>();

            for (Ficha ficha : todasLasFichas) {
                if (ficha.getCoste() == costeSeleccionado) {
                    fichasFiltradas.add(ficha);
                }
            }

            if (!fichasFiltradas.isEmpty()) {
                Ficha fichaSeleccionada = fichasFiltradas.get(random.nextInt(fichasFiltradas.size()));
                fichasEnTienda.add(fichaSeleccionada);
            }
        }

        return fichasEnTienda;
    }

    public void sacarFichaDelPool(Ficha ficha){
        for (Ficha fichaIch : todasLasFichas){
            if(ficha.getNombre() == fichaIch.getNombre()){
                todasLasFichas.remove(ficha);
                break;
            }
        }
    }

    public void agregarFichaAlPool(Ficha ficha,int cantidad){
       for (int i = 0;i<cantidad;i++){
           //todasLasFichas.add(new Ficha("Abra", psychic, sweeper, fight, 1,"/modelo/images/pokemon/mini/1-a abra.png","/modelo/images/pokemon/1-a abra.png"));
           todasLasFichas.add(new Ficha(ficha.getNombreOriginal(),ficha.getRasgo1(),ficha.getRasgo2(),ficha.getRasgo3(), ficha.getCosteAnterior(),ficha.getMovimiento().getAlcance(),ficha.getEstadisticas().getVida(),ficha.getEstadisticas().getCosteUlti(),ficha.getEstadisticas().getDanoFisicoOriginal(),ficha.getEstadisticas().getDañoApOriginal(),ficha.getEstadisticas().getDefensaFisicaOriginal(),ficha.getEstadisticas().getDefensaApOriginal(),ficha.getFichaImagen().getDireccionMini(),ficha.getFichaImagen().getSpriteOriginal() ));
       }

    }

    public Ficha getKrabby(){return this.krabby;}

    public Ficha getRattata(){return this.rattata;}

    public Ficha getRegigigas() {return regigigas;}

    public void setJugador(Jugador jugador){this.jugador = jugador;}

    public void setBancaYTablero(Banca ba,Tablero tab){
        this.banca = ba;
        this.tablero = tab;
    }

    private int seleccionarCoste(int nivel) {
        int aleatorio = random.nextInt(100);
        switch (nivel) {
            case 1:
                return 1;

            case 2:
                return 1;

            case 3:
                if (aleatorio < 75) return 1;
                else return 2;

            case 4:
                if (aleatorio < 55) return 1;
                else if (aleatorio < 85) return 2;
                else return 3;

            case 5:
                if (aleatorio < 45) return 1;
                else if (aleatorio < 78) return 2;
                else if(aleatorio < 98) return 3;
                else return 4;

            case 6:
                if (aleatorio < 30) return 1;
                else if (aleatorio < 70) return 2;
                else if(aleatorio < 95) return 3;
                else return 4;

            case 7:
                if (aleatorio < 19) return 1;
                else if (aleatorio < 49) return 2;
                else if(aleatorio < 84) return 3;
                else if(aleatorio < 94) return 4;
                else return 5;

            case 8:
                if (aleatorio < 18) return 1;
                else if (aleatorio < 43) return 2;
                else if(aleatorio < 79) return 3;
                else if(aleatorio < 97) return 4;
                else return 5;

            case 9:
                if (aleatorio < 10) return 1;
                else if (aleatorio < 30) return 2;
                else if(aleatorio < 55) return 3;
                else if(aleatorio < 90) return 4;
                else return 5;

            case 10:
                if (aleatorio < 5) return 1;
                else if (aleatorio < 15) return 2;
                else if(aleatorio < 35) return 3;
                else if(aleatorio < 75) return 4;
                else return 5;

        }

        return 1;
    }

}
