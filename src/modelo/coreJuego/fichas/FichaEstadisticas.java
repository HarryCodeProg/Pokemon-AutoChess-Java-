package modelo.coreJuego.fichas;

public class FichaEstadisticas {
    private int vida;
    private int vidaActual;
    private int danoFisico;
    private int dañoAP;
    private int manaActual = 10;
    private int costeUlti;
    private int defensaFisica;
    private int defensaAP;
    private int defensaApOriginal;
    private int defensaFisicaOriginal;
    private int dañoApOriginal;
    private int danoFisicoOriginal;

    public FichaEstadisticas(int vida,int costeU ,int danoFisico, int dañoAP, int defensaFisica, int defensaAP) {
        this.vida = vida;
        this.vidaActual = vida;
        this.costeUlti = costeU;
        this.danoFisico = danoFisico;
        this.dañoAP = dañoAP;
        this.defensaFisica = defensaFisica;
        this.defensaAP = defensaAP;
        this.defensaApOriginal = defensaAP;
        this.defensaFisicaOriginal = defensaFisica;
        this.dañoApOriginal = dañoAP;
        this.danoFisicoOriginal = danoFisico;
    }

    public void multipicarVida(int valor){vida *= valor;}

    public void multiplicarDanoFisico(int valor) {this.danoFisico *= valor;}

    public void multiplicarDañoAP(int valor) {this.dañoAP *= valor;}

    public int getManaActual(){
        return this.manaActual;
    }

    public int getCosteUlti(){
        return this.costeUlti;
    }

    public int getVidaActual(){
        return this.vidaActual;
    }

    public void setVidaActual(int cantidad){
        this.vidaActual = cantidad;
    }

    public void reducirVida(int cantidad){this.vidaActual -= cantidad;}

    public void setManaActual(int cantidad){this.manaActual = cantidad;}

    public int getVida(){return vida;}

    public int getDanoFisico(){return danoFisico;}

    public int getDañoAP(){return dañoAP;}

    public void aumentarDañoFisico(int sum){this.danoFisico += sum;}

    public int getDañoApOriginal() {return dañoApOriginal;}

    public int getDanoFisicoOriginal() {return danoFisicoOriginal;}

    public int getDefensaApOriginal() {return defensaApOriginal;}

    public int getDefensaFisicaOriginal() {return defensaFisicaOriginal;}

    public int getDefensaFisicaActual() {return defensaFisica;}

    public int getDefensaApActual() {return defensaAP;}

    public void aumentarDañoFisicoPorcentaje(double porcentaje) {
        int aumento = (int) (danoFisico * (porcentaje / 100));
        this.danoFisico += aumento;
    }

    public void aumentarDefensaAp(int sum){this.defensaAP += sum;}

    public void aumentarDefensaApPorcentaje(double porcentaje) {
        int aumento = (int) (defensaAP * (porcentaje / 100));
        this.defensaAP += aumento;
    }

    public void aumentarDañoFisicoTemporal(int sum) {
        this.danoFisicoOriginal = danoFisico;
        this.danoFisico += sum;
    }

    public void aumentarDefensaApTemporal(int sum) {
        this.defensaApOriginal = defensaAP;
        this.defensaAP += sum;
    }

    public void aumentarDefensaFisicaTemporal(int sum) {
        this.defensaFisicaOriginal = defensaFisica;
        this.defensaFisica += sum;
    }

    public void aumentarDañoApTemporal(int sum) {
        this.dañoApOriginal = dañoAP;
        this.dañoAP += sum;
    }

    public void restablecerValoresOriginales() {
        this.defensaAP = defensaApOriginal;
        this.defensaFisica = defensaFisicaOriginal;
        this.dañoAP = dañoApOriginal;
        this.danoFisico = danoFisicoOriginal;
    }

}
