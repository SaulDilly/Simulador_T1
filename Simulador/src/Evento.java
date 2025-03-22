public class Evento implements Comparable<Evento> {

    private TipoEvento tipo;
    private double tempo;

    public Evento (TipoEvento tipo, double tempo) {
        this.tempo = tempo;
        this.tipo = tipo;
    }

    public boolean isChegada() {
        return tipo == TipoEvento.CHEGADA;
    }

    public boolean isSaida() {
        return tipo == TipoEvento.SAIDA;
    }

    public double getTempo() {
        return tempo;
    }

    @Override
    public int compareTo(Evento other) {
        return Double.compare(this.tempo, other.tempo);
    }

}
