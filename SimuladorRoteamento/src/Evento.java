public class Evento implements Comparable<Evento> {

    private TipoEvento tipo;
    private double tempo;
    private Fila origem;
    private Fila destino;

    public Evento (TipoEvento tipo, double tempo, Fila origem, Fila destino) {
        this.tempo = tempo;
        this.tipo = tipo;
        this.origem = origem;
        this.destino = destino;
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

    public Fila getFilaOrigem() {
        return origem;
    }

    public Fila getFilaDestino() {
        return destino;
    }

    @Override
    public int compareTo(Evento other) {
        return Double.compare(this.tempo, other.tempo);
    }

}
