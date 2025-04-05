import java.util.PriorityQueue;

public class Escalonador {

    private PriorityQueue<Evento> pq;
    private PseudoRandom random;
    private FilaSimples fs;
    
    public Escalonador(FilaSimples fs) {
        pq = new PriorityQueue<>();
        random = new PseudoRandom(2879);
        this.fs = fs;
        agendaChegada(2, new IntervaloTempo(0, 0));
    }

    public Evento proximoEvento() {
        return pq.poll();

    }

    public void agendaChegada(double tempoTotal, IntervaloTempo it) {
        Evento e = new Evento(TipoEvento.CHEGADA, tempoTotal + getRandom(it));
        pq.add(e);
    }

    public void agendaSaida(double tempoTotal, IntervaloTempo it) {
        Evento e = new Evento(TipoEvento.SAIDA, tempoTotal + getRandom(it));
        pq.add(e);
    }

    public double getRandom(IntervaloTempo it) {
        fs.count--;
        return (it.getLimFinal() - it.getLimInicial()) * random.nextRandom() + it.getLimInicial();
    }
}
