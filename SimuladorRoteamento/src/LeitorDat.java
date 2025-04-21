import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;

public class LeitorDat {

    private ObjetoLeitura objL;
    private Map<Fila, List<Map.Entry<Double, Fila>>> topologia;
    private List<Fila> filas;
    private Map<Integer, Double> chegadas;
    private int filaLeitura;

    private int servidores;
    private int capacidade;
    private int chegadaMinima;
    private int chegadaMaxima;
    private int servicoMinimo;
    private int servicoMaximo;

    private int origem;
    private int destino;
    private double probabilidade;

    public LeitorDat() {
        filaLeitura = 0;
        filas = new ArrayList<>();
        topologia = new HashMap<>();
        chegadas = new HashMap<>();
        objL = ObjetoLeitura.IDLE;
        initControleFilas();
        initControleTopologia();
    }

    public void carregarFilas(String nomeArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String l;

            while ((l = br.readLine()) != null) {
                l = l.trim();

                if (l.isEmpty() || l.startsWith("#")) {
                    continue;
                }

                if (l.startsWith("*chegadas")) {
                    objL = ObjetoLeitura.CHEGADAS;
                    continue;
                } else if (l.startsWith("*filas")) {
                    objL = ObjetoLeitura.FILAS;
                    continue;
                } else if (l.startsWith("*topologia")) {
                    objL = ObjetoLeitura.TOPOLOGIA;
                    continue;
                }

                if (objL == ObjetoLeitura.CHEGADAS) chegadas(l);
                else if (objL == ObjetoLeitura.FILAS) filas(l);
                else if (objL == ObjetoLeitura.TOPOLOGIA) topologia(l);
            }

            validateTopologia();

        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<Integer, Double> getChegadas() {
        return chegadas;
    }

    public List<Fila> getFilas() {
        return filas;
    }

    public Map<Fila, List<Map.Entry<Double, Fila>>> getTopologia() {
        return topologia;
    }

    private void chegadas(String l) {
        String[] partes = l.split(":");
        int index = Integer.parseInt(partes[0].trim());
        double tempo = Double.parseDouble(partes[1].trim());
        chegadas.put(index, tempo);
    }

    private void filas(String l) {
        String[] partes = l.split(":");

        if (l.contains("servidores"))
            servidores = Integer.parseInt(partes[1].trim());
        else if (l.contains("capacidade"))
            capacidade = Integer.parseInt(partes[1].trim());
        else if (l.contains("chegadaMinima"))
            chegadaMinima = Integer.parseInt(partes[1].trim());
        else if (l.contains("chegadaMaxima"))
            chegadaMaxima = Integer.parseInt(partes[1].trim());
        else if (l.contains("servicoMinimo"))
            servicoMinimo = Integer.parseInt(partes[1].trim());
        else if (l.contains("servicoMaximo")) {
            servicoMaximo = Integer.parseInt(partes[1].trim());
            IntervaloTempo itCh = new IntervaloTempo(chegadaMinima, chegadaMaxima);
            IntervaloTempo itSrv = new IntervaloTempo(servicoMinimo, servicoMaximo);
            if (capacidade == 0) capacidade = 20;
            Fila fila = new Fila(servidores, capacidade, itCh, itSrv);
            filas.add(filaLeitura - 1, fila);
            System.out.printf("Initialized queue %d: servidores=%d, capacidade=%d, chegada=[%d, %d], servico=[%d, %d]\n",
                filas.size(), servidores, capacidade, chegadaMinima, chegadaMaxima, servicoMinimo, servicoMaximo);
        } else {
            initControleFilas();
            filaLeitura = Integer.parseInt(partes[0].trim());
        }
    }

    private void topologia(String l) {
        String[] partes = l.split(":");

        if (l.startsWith("-")) initControleTopologia();

        if (l.contains("origem"))
            origem = Integer.parseInt(partes[1].trim());
        else if (l.contains("destino"))
            destino = Integer.parseInt(partes[1].trim());
        else if (l.contains("probabilidade")) {
            probabilidade = Double.parseDouble(partes[1].trim());

            Fila fOrigem = filas.get(origem - 1);
            Fila fDestino = filas.get(destino - 1);

            List<Map.Entry<Double, Fila>> probList = topologia.computeIfAbsent(fOrigem, k -> new ArrayList<>());
            probList.add(new AbstractMap.SimpleEntry<>(probabilidade, fDestino));

            System.out.printf("Added topology: origem=%d, destino=%d, probabilidade=%.2f\n",
                origem, destino, probabilidade);
        }
    }

    private void validateTopologia() {
        for (Map.Entry<Fila, List<Map.Entry<Double, Fila>>> entry : topologia.entrySet()) {
            Fila origem = entry.getKey();
            List<Map.Entry<Double, Fila>> probList = entry.getValue();

            double sum = probList.stream().mapToDouble(Map.Entry::getKey).sum();
            if (Math.abs(sum - 1.0) > 1e-6) {
                System.err.printf("Warning: Probabilities for queue %d do not sum to 1 (sum=%.4f)\n",
                    filas.indexOf(origem) + 1, sum);
            }
        }
    }

    private void initControleFilas() {
        servidores = 0;
        capacidade = 0;
        chegadaMinima = 0;
        chegadaMaxima = 0;
        servicoMinimo = 0;
        servicoMaximo = 0;
    }

    private void initControleTopologia() {
        origem = -1;
        destino = -1;
        probabilidade = 0.0;
    }

    private enum ObjetoLeitura {
        IDLE, CHEGADAS, FILAS, TOPOLOGIA;
    }
}