import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LeitorDat {

    public static List<Fila> carregarFilas(String nomeArquivo) {
        List<Fila> filas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String l;

            while ((l = br.readLine()) != null) {
                l = l.trim();

                if (l.isEmpty() || l.startsWith("#")) {
                    continue;
                }

                String[] partes = l.split("\\|");

                int servidores = Integer.parseInt(partes[1]);
                int capacidade = Integer.parseInt(partes[2]);
                int chegadaMin = Integer.parseInt(partes[3]);
                int chegadaMax = Integer.parseInt(partes[4]);
                int atendimentoMin = Integer.parseInt(partes[5]);
                int atendimentoMax = Integer.parseInt(partes[6]);

                IntervaloTempo chegada = (chegadaMin == 0 && chegadaMax == 0)? null : new IntervaloTempo(chegadaMin, chegadaMax);
                IntervaloTempo atendimento = new IntervaloTempo(atendimentoMin, atendimentoMax);

                Fila fila = new Fila(servidores, capacidade, chegada, atendimento);
                filas.add(fila);
            }

        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return filas;
    }
}
