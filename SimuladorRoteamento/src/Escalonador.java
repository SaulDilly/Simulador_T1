import java.util.PriorityQueue;

public class Escalonador {

    private PriorityQueue<Evento> pq;
    private PseudoRandom random;
    
    public Escalonador(PseudoRandom random) {
        pq = new PriorityQueue<>();
        this.random = random;        
    }

    public Evento proximoEvento() {
        return pq.poll();

    }

    public void agendaChegada(double tempoTotal, IntervaloTempo it, Fila f) {
        Evento e = new Evento(TipoEvento.CHEGADA, tempoTotal + getRandom(it), f);
        pq.add(e);
    }
    
    public void agendaPassagem(double tempoTotal, IntervaloTempo it, Fila f) {
        Evento e = new Evento(TipoEvento.PASSAGEM, tempoTotal + getRandom(it), f);
        pq.add(e);
    }

    public void agendaSaida(double tempoTotal, IntervaloTempo it, Fila f) {
        Evento e = new Evento(TipoEvento.SAIDA, tempoTotal + getRandom(it), f);
        pq.add(e);
    }

    public double getRandom(IntervaloTempo it) {
        return (it.getLimFinal() - it.getLimInicial()) * random.nextRandom() + it.getLimInicial();
    }
}
