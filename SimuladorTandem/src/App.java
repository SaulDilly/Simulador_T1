public class App {

    private Escalonador escalonador;
    private Fila fila1;
    private Fila fila2;

    private double tempoTotal;

    public int count = 100000;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.fila1 = new Fila(2, 3, new IntervaloTempo(1, 4), new IntervaloTempo(3, 4));
        app.fila2 = new Fila(1, 5, null, new IntervaloTempo(2, 3));
        app.escalonador = new Escalonador(app);
        app.executarSimulacao();
    }

    private void executarSimulacao() {
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

        System.out.println("Tempos fila 1:");
        fila1.printTempos();
        System.out.println("Tempos fila 2:");
        fila2.printTempos();

        System.out.println("Perdas fila 1:");
        System.out.println(fila1.getPerda());
        System.out.println("Perdas fila 2:");
        System.out.println(fila2.getPerda());

        System.out.printf("Tempo total: %.4f", tempoTotal);
    }


    private void chegada(Evento e) {
        fila1.acumulaTempo(e);
        fila2.acumulaTempo(e);

        if (fila1.getStatus() < fila1.getCapacidade()) {
            fila1.in();
            if (fila1.getStatus() <= fila1.getServidores()) {
                escalonador.agendaPassagem(tempoTotal, fila1.getAtendimento());
            }
        } else {
            fila1.loss();
        }

        escalonador.agendaChegada(tempoTotal, fila1.getChegada());

    }

    private void passagem(Evento e) {
        fila1.acumulaTempo(e);
        fila2.acumulaTempo(e);

        fila1.out();
        if (fila1.getStatus() >= fila1.getServidores())
            escalonador.agendaPassagem(tempoTotal, fila1.getAtendimento());

        if (fila2.getStatus() < fila2.getCapacidade()) {
            fila2.in();
            if (fila2.getStatus() <= fila2.getServidores()) {
                escalonador.agendaSaida(tempoTotal, fila2.getAtendimento());
            }
        }
    }

    private void saida(Evento e) {
        fila1.acumulaTempo(e);
        fila2.acumulaTempo(e);

        fila2.out();
        if (fila2.getStatus() >= fila2.getServidores())
            escalonador.agendaSaida(tempoTotal, fila2.getAtendimento());

    }

    public void decCount() {
        count--;
    }

}
