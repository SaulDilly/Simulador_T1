import java.util.PriorityQueue;

public class Escalonador {

    private PriorityQueue<Evento> pq;
    private PseudoRandom random;
    private App app;
    
    public Escalonador(App app) {
        pq = new PriorityQueue<>();
        random = new PseudoRandom(2879);
        this.app = app;
        agendaChegada(1.5, new IntervaloTempo(0, 0));
    }

    public Evento proximoEvento() {
        return pq.poll();

    }

    public void agendaChegada(double tempoTotal, IntervaloTempo it) {
        Evento e = new Evento(TipoEvento.CHEGADA, tempoTotal + getRandom(it));
        pq.add(e);
    }
    
    public void agendaPassagem(double tempoTotal, IntervaloTempo it) {
        Evento e = new Evento(TipoEvento.PASSAGEM, tempoTotal + getRandom(it));
        pq.add(e);
    }

    public void agendaSaida(double tempoTotal, IntervaloTempo it) {
        Evento e = new Evento(TipoEvento.SAIDA, tempoTotal + getRandom(it));
        pq.add(e);
    }

    public double getRandom(IntervaloTempo it) {
        app.decCount();
        return (it.getLimFinal() - it.getLimInicial()) * random.nextRandom() + it.getLimInicial();
    }
}
