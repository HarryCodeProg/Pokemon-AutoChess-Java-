package controlador;

public interface Observador {
    void actualizarRasgos();
    void agregarObservador(Observador o);
    void eliminarObservador(Observador o);
    void notificarObservadores();
    void notificar();
    void notificarMensaje(String mensaje);
    void notificarError(String mensaje);
}

