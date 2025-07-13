public class Main {
    public static void main(String[] args) {
        System.out.println("--- Execucao a partir do arquivo 'entrada.txt' (Debug) ---");
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

        System.out.println("Vertices | Tempos Individuais (ns) e Tempo Medio (ms)");
        System.out.println("---------|----------------------------------------------");

        for (int n : tamanhos) {
            int numArestas = (int)(n * densidadeArestas);

            // Gera o grafo UMA VEZ por tamanho de 'n'
            int[][] grafo = GeradorGrafo.gerarDAG(n, numArestas);

            long tempoTotal = 0;

            System.out.printf("%-8d | ", n);

            // Executa o benchmark 10x NO MESMO GRAFO
            for (int i = 0; i < numExecucoes; i++) {
                // Cria uma nova instância para garantir que o grafo esteja no estado original
                OrdenacaoTopologica ord = new OrdenacaoTopologica(grafo);

                long inicio = System.nanoTime();
                ord.executaSilencioso(); // Apenas a ordenação é medida
                long fim = System.nanoTime();

                long duracaoExecucao = fim - inicio;
                System.out.print(duracaoExecucao + " ");
                tempoTotal += duracaoExecucao;
            }

            double tempoMedioNano = (double) tempoTotal / numExecucoes;
            double tempoMedioMilli = tempoMedioNano / 1_000_000.0;

            System.out.printf("| Media: %.4f ms\n", tempoMedioMilli);
        }
    }
}
