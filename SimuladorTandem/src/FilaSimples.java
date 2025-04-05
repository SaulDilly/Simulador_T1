public class FilaSimples {

    private final int SERVIDORES = 1;
    private final int CAPACIDADE_MAXIMA = 5;
    private final IntervaloTempo INTERVALO_CHEGADA = new IntervaloTempo(2, 5);
    private final IntervaloTempo INTERVALO_ATENDIMENTO = new IntervaloTempo(3, 5);

    private Escalonador escalonador;
    private int filaAtual;
    private int perdas;
    private double tempoTotal;
    private double[] tempoEstado;

    public int count = 100000;

    public FilaSimples() {
        escalonador = new Escalonador(this);
        filaAtual = 0;
        tempoTotal = 0.0;
        tempoEstado = new double[CAPACIDADE_MAXIMA + 1];
    }

    public void executarSimulacao() {

        Evento event;

        while (count > 0) {
            event = escalonador.proximoEvento();

            if (event.isChegada()) 
                chegada(event);
            else if (event.isSaida()) 
                saida(event);

        }

        for (int i = 0; i < CAPACIDADE_MAXIMA + 1; i++) {
            System.out.printf("%d: %.4f (%.4f%%)\n", i, tempoEstado[i], tempoEstado[i]/tempoTotal*100);
        }
        System.out.println("Perdas: " + perdas);
        System.out.printf("Tempo total: %.4f", tempoTotal);
    }

    private void chegada(Evento e) {
        tempoEstado[filaAtual] += e.getTempo() - tempoTotal;
        tempoTotal = e.getTempo();

        if (filaAtual < CAPACIDADE_MAXIMA) {
            filaAtual++;
            if (filaAtual <= SERVIDORES) {
                escalonador.agendaSaida(tempoTotal, INTERVALO_ATENDIMENTO);
            }
        } else {
            perdas++;
        }

        escalonador.agendaChegada(tempoTotal, INTERVALO_CHEGADA);

    }

    private void saida(Evento e) {
        tempoEstado[filaAtual] += e.getTempo() - tempoTotal;
        tempoTotal = e.getTempo();

        filaAtual--;
        if (filaAtual >= SERVIDORES)
            escalonador.agendaSaida(tempoTotal, INTERVALO_ATENDIMENTO);

    }
}
