public class Main {
    public static void main(String[] args) {
        System.out.println("--- Execucao a partir do arquivo 'entrada.txt' ---");
        executarDeArquivo();
        System.out.println("--------------------------------------------------\n");

        System.out.println("--- Iniciando Analise de Desempenho ---");
        executarAnaliseDesempenho();
        System.out.println("--- Analise de Desempenho Finalizada ---");
    }

    private static void executarDeArquivo() {
        OrdenacaoTopologica ord = new OrdenacaoTopologica();
        String nomeEntrada = "entrada.txt";
        ord.realizaLeitura(nomeEntrada);

        if (!ord.executa()) {
            System.out.println("O conjunto nao e parcialmente ordenado.");
        } else {
            System.out.println("O conjunto e parcialmente ordenado.");
        }
    }

    private static void executarAnaliseDesempenho() {
        int[] tamanhos = {10, 20, 30, 40, 50, 100, 200, 500, 1000, 5000, 10000, 20000, 30000, 50000, 100000};
        int numExecucoes = 10;
        double densidadeArestas = 1.5;

        System.out.println("Vertices | Tempo Medio (ms) para cada execução");
        System.out.println("---------|------------------");

        for (int n : tamanhos) {
            long tempoTotal = 0;
            int numArestas = (int)(n * densidadeArestas);

            for (int i = 0; i < numExecucoes; i++) {
                int[][] grafo = GeradorGrafo.gerarDAG(n, numArestas);

                OrdenacaoTopologica ord = new OrdenacaoTopologica(grafo);

                long inicio = System.nanoTime();
                ord.executaSilencioso();
                long fim = System.nanoTime();

                tempoTotal += (fim - inicio);
            }

            double tempoMedioNano = (double) tempoTotal / numExecucoes;
            double tempoMedioMilli = tempoMedioNano / 1_000_000.0;

            System.out.printf("%-8d | %.4f\n", n, tempoMedioMilli);
        }
    }
}
