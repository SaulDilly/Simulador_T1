import java.util.List;
import java.util.Map;

public class App {

    private Escalonador escalonador;
    PseudoRandom random;

    private LeitorDat leitor;
    private List<Fila> filas;
    private Map<Fila, List<Map.Entry<Double, Fila>>> topologia;

    private double tempoTotal;

    public int count = 50000;

    public static void main(String[] args) throws Exception {
        App app = new App();

        app.leitor = new LeitorDat();
        app.leitor.carregarFilas("SimuladorRoteamento/filas.dat");

        app.random = new PseudoRandom(2879, app);
        app.escalonador = new Escalonador(app.random);
        app.executarSimulacao();
    }

    private void executarSimulacao() {

        filas = leitor.getFilas();
        topologia = leitor.getTopologia();
        
        for (Map.Entry<Integer, Double> entry : leitor.getChegadas().entrySet()) {
            Integer filaIndex = entry.getKey();
            Double chegada = entry.getValue();
            escalonador.agendaChegada(chegada, filas.get(filaIndex - 1).getChegada(), null, filas.get(filaIndex - 1));
        }

        Evento event;

        while (count > 0) {
            event = escalonador.proximoEvento();
            tempoTotal = event.getTempo();

            if (event.isChegada())
                chegada(event);
            else if (event.isSaida())
                saida(event);
            else if (event.isPassagem())
                passagem(event);
        }

        int i = 1;
        for (Fila f : filas) {
            System.out.println("Tempos fila " + i + ":");
            f.printTempos();
            i++;
        }

        i = 1;
        for (Fila f : filas) {
            if (f.getPerda() > 0) 
                System.out.println("Perdas fila " + i + ":" + f.getPerda());
            i++;
        }

        System.out.printf("Tempo total: %.4f", tempoTotal);
    }

    private void chegada(Evento e) {
        acumulaTempo(e);
        Fila destino = e.getFilaDestino();

        if (destino.getStatus() < destino.getCapacidade()) {
            destino.in();
            if (destino.getStatus() <= destino.getServidores()) {
                agendaProximoEvento(destino);
            }
        } else {
            destino.loss();
        }

        escalonador.agendaChegada(tempoTotal, destino.getChegada(), null, destino);
    }

    private void passagem(Evento e) {
        acumulaTempo(e);

        Fila origem = e.getFilaOrigem();
        Fila destino = e.getFilaDestino();

        origem.out();
        if (origem.getStatus() >= origem.getServidores()) {
            agendaProximoEvento(origem);
        }

        if (destino.getStatus() < destino.getCapacidade()) {
            destino.in();
            if (destino.getStatus() <= destino.getServidores()) {
                escalonador.agendaSaida(tempoTotal, destino.getAtendimento(), destino, null);
            }
        } else {
            destino.loss();
        }
    }

    private void saida(Evento e) {
        acumulaTempo(e);

        Fila origem = e.getFilaOrigem();

        origem.out();

        if (origem.getStatus() >= origem.getServidores()) {
            agendaProximoEvento(origem);
        }
    }

    private void agendaProximoEvento(Fila origem) {
        Fila destino = getDestino(origem);

        if (destino != null) {
            escalonador.agendaPassagem(tempoTotal, origem.getAtendimento(), origem, destino);
        } else {
            escalonador.agendaSaida(tempoTotal, origem.getAtendimento(), origem, null);
        }
    }

    private Fila getDestino(Fila origem) {
        if (!topologia.containsKey(origem)) {
            return null;
        }

        double r = random.nextRandom();
        double sum = 0.0;
        List<Map.Entry<Double, Fila>> probList = topologia.get(origem);

        for (Map.Entry<Double, Fila> entry : probList) {
            sum += entry.getKey();
            if (r < sum) {
                return entry.getValue();
            }
        }

        return null;
    }

    public void decCount() {
        count--;
    }

    private void acumulaTempo(Evento e) {
        filas.forEach(f -> f.acumulaTempo(e));
    }
}