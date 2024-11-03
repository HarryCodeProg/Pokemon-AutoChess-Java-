package modelo.music;

import java.io.File;
import javax.sound.sampled.*;

public class Sonidos {
    private Clip clip;
    private FloatControl volumenControl;

    public void reproducirMusica(String rutaArchivo) {
        try {
            // Abrir el archivo de audio
            File archivoMusica = new File(rutaArchivo);
            if (archivoMusica.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(archivoMusica);
                clip = AudioSystem.getClip();
                clip.open(audioInput);

                // Obtener control de volumen
                volumenControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumenControl.setValue(-30.0f);

                // Reproducir el audio en bucle
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } else {
                System.out.println("El archivo de audio no fue encontrado.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void detenerMusica() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void ajustarVolumen(float valor) {
        if (volumenControl != null) {
            // El rango de FloatControl va de -80.0 dB (silencio) a 6.0 dB (volumen máximo)
            volumenControl.setValue(valor);
        }
    }
}
