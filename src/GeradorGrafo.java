import java.util.Random;

public class GeradorGrafo {

    private static boolean arestaExiste(int[][] arestas, int numArestasAdicionadas, int u, int v) {
        for (int i = 0; i < numArestasAdicionadas; i++) {
            if (arestas[i][0] == u && arestas[i][1] == v) {
                return true;
            }
        }
        return false;
    }

    public static int[][] gerarDAG(int numVertices, int numArestas) {
        if (numVertices <= 0 || numArestas < 0) {
            return new int[0][2];
        }

        int[][] arestasArray = new int[numArestas][2];
        Random random = new Random();
        int arestasAdicionadas = 0;

        long maxArestasPossiveis = (long) numVertices * (numVertices - 1) / 2;
        if (numArestas > maxArestasPossiveis) {
            numArestas = (int) maxArestasPossiveis;
        }

        int tentativas = 0;
        int maxTentativas = numArestas * 10;

        while (arestasAdicionadas < numArestas && tentativas < maxTentativas) {
            int u = random.nextInt(numVertices);
            int v = random.nextInt(numVertices);
            tentativas++;

            if (u == v) continue;

            int de = u < v ? u : v;
            int para = u > v ? u : v;

            if (!arestaExiste(arestasArray, arestasAdicionadas, de, para)) {
                arestasArray[arestasAdicionadas][0] = de;
                arestasArray[arestasAdicionadas][1] = para;
                arestasAdicionadas++;
            }
        }

        if (arestasAdicionadas < numArestas) {
            int[][] resultadoReduzido = new int[arestasAdicionadas][2];
            for (int i = 0; i < arestasAdicionadas; i++) {
                resultadoReduzido[i][0] = arestasArray[i][0];
                resultadoReduzido[i][1] = arestasArray[i][1];
            }
            return resultadoReduzido;
        }

        return arestasArray;
    }
}