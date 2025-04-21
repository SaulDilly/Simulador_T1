public class Fila {

    private int servidores;
    private int capacidade;
    private IntervaloTempo intervaloChegada;
    private IntervaloTempo intervaloAtendimento;

    private int status;
    private int perdas;
    private double tempoTotal;
    private double[] tempoEstado;


    public Fila(int servidores, int capacidade, IntervaloTempo ic, IntervaloTempo ia) {
        this.servidores = servidores;
        this.capacidade = capacidade;
        this.intervaloChegada = ic;
        this.intervaloAtendimento = ia;
        status = 0;
        tempoTotal = 0.0;
        tempoEstado = new double[capacidade + 1];
    }

    public int getStatus() {
        return status;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getServidores() {
        return servidores;
    }
    
    public IntervaloTempo getChegada() {
        return intervaloChegada;
    }

    public IntervaloTempo getAtendimento() {
        return intervaloAtendimento;
    }
    
    public int getPerda() {
        return perdas;
    }

    public void in() {
        status++;    
    }
    
    public void out() {
        status--;    
    }

    public void loss() {
        perdas++;    
    }

    public void acumulaTempo(Evento e) {
        tempoEstado[status] += e.getTempo() - tempoTotal;
        tempoTotal = e.getTempo();
    }

    public void printTempos() {

        for (int i = 0; i < capacidade + 1; i++) {
            if (tempoEstado[i] > 0)
                System.out.printf("%d: %.4f (%.4f%%)\n", i, tempoEstado[i], tempoEstado[i]/tempoTotal*100);
        }
    }
}
