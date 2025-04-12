public class Evento implements Comparable<Evento> {

    private TipoEvento tipo;
    private double tempo;
    private Fila fila;

    public Evento (TipoEvento tipo, double tempo, Fila fila) {
        this.tempo = tempo;
        this.tipo = tipo;
        this.fila = fila;
    }

    public boolean isChegada() {
        return tipo == TipoEvento.CHEGADA;
    }

    public boolean isSaida() {
        return tipo == TipoEvento.SAIDA;
    }
    
    public boolean isPassagem() {
        return tipo == TipoEvento.PASSAGEM;
    }

    public double getTempo() {
        return tempo;
    }

    public Fila getFila() {
        return fila;
    }

    @Override
    public int compareTo(Evento other) {
        return Double.compare(this.tempo, other.tempo);
    }

}
