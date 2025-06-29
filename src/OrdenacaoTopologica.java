import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OrdenacaoTopologica {
    private class Elo {
        public int chave;
        public int contador;
        public Elo prox;
        public EloSuc listaSuc;

        public Elo() {
            prox = null;
            contador = 0;
            listaSuc = null;
        }

        public Elo(int chave) {
            this.chave = chave;
            this.contador = 0;
            this.prox = null;
            this.listaSuc = null;
        }
    }

    private class EloSuc {
        public Elo id;
        public EloSuc prox;

        public EloSuc(Elo id, EloSuc prox) {
            this.id = id;
            this.prox = prox;
        }
    }

    private Elo prim;
    private int n;

    public OrdenacaoTopologica() {
        prim = null;
        n = 0;
    }

    public OrdenacaoTopologica(int[][] grafo) {
        this();
        if (grafo == null || grafo.length == 0) return;

        int maxVertice = 0;
        for (int i = 0; i < grafo.length; i++) {
            maxVertice = Math.max(maxVertice, Math.max(grafo[i][0], grafo[i][1]));
        }

        for (int i = 0; i <= maxVertice; i++) {
            procuraOuCriaElo(i);
        }

        for (int i = 0; i < grafo.length; i++) {
            Elo eloX = procuraOuCriaElo(grafo[i][0]);
            Elo eloY = procuraOuCriaElo(grafo[i][1]);

            EloSuc novoSuc = new EloSuc(eloY, eloX.listaSuc);
            eloX.listaSuc = novoSuc;
            eloY.contador++;
        }
    }

    private Elo procuraOuCriaElo(int chave) {
        Elo p = prim;
        Elo ant = null;
        while (p != null) {
            if (p.chave == chave) {
                return p;
            }
            ant = p;
            p = p.prox;
        }

        Elo novoElo = new Elo(chave);
        if (prim == null) {
            prim = novoElo;
        } else {
            ant.prox = novoElo;
        }
        n++;
        return novoElo;
    }

    private int[] parseLinha(String linha) {
        int[] resultado = new int[2];
        int num1 = 0;
        int num2 = 0;
        int i = 0;

        while (i < linha.length() && linha.charAt(i) >= '0' && linha.charAt(i) <= '9') {
            num1 = num1 * 10 + (linha.charAt(i) - '0');
            i++;
        }

        while (i < linha.length() && (linha.charAt(i) == ' ' || linha.charAt(i) == '<')) {
            i++;
        }

        while (i < linha.length() && linha.charAt(i) >= '0' && linha.charAt(i) <= '9') {
            num2 = num2 * 10 + (linha.charAt(i) - '0');
            i++;
        }

        resultado[0] = num1;
        resultado[1] = num2;
        return resultado;
    }

    public void realizaLeitura(String nomeEntrada) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeEntrada))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                int[] chaves = parseLinha(linha);
                int chaveX = chaves[0];
                int chaveY = chaves[1];

                Elo eloX = procuraOuCriaElo(chaveX);
                Elo eloY = procuraOuCriaElo(chaveY);

                EloSuc novoSuc = new EloSuc(eloY, eloX.listaSuc);
                eloX.listaSuc = novoSuc;
                eloY.contador++;
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + nomeEntrada);
            e.printStackTrace();
        }
    }

    private void debug() {
        System.out.println("Debug");
        Elo p = prim;
        while (p != null) {
            System.out.print(p.chave + " predecessores: " + p.contador + ", sucessores: ");
            EloSuc s = p.listaSuc;
            while (s != null) {
                System.out.print(s.id.chave + "->");
                s = s.prox;
            }
            System.out.println("NULL");
            p = p.prox;
        }
        System.out.println();
    }

    public boolean executa() {
        debug();
        return executaLogicaComum(true);
    }

    public boolean executaSilencioso() {
        return executaLogicaComum(false);
    }

    private boolean executaLogicaComum(boolean imprimir) {
        Elo cabecaFila = null;
        Elo outrosNos = null;
        Elo p = this.prim;

        while (p != null) {
            Elo proximo = p.prox;
            if (p.contador == 0) {
                p.prox = cabecaFila;
                cabecaFila = p;
            } else {
                p.prox = outrosNos;
                outrosNos = p;
            }
            p = proximo;
        }

        this.prim = outrosNos;

        Elo fimFila = null;
        if (cabecaFila != null) {
            fimFila = cabecaFila;
            while (fimFila.prox != null) {
                fimFila = fimFila.prox;
            }
        }

        int elementosOrdenados = 0;
        if (imprimir) {
            System.out.println("Ordenacao topologica");
        }

        Elo q = cabecaFila;
        while (q != null) {
            if (imprimir) {
                System.out.print(q.chave + " ");
            }
            elementosOrdenados++;

            EloSuc suc = q.listaSuc;
            while (suc != null) {
                Elo t = suc.id;
                t.contador--;
                if (t.contador == 0) {
                    t.prox = null;
                    if (fimFila == null) {
                        cabecaFila = t;
                        fimFila = t;
                    } else {
                        fimFila.prox = t;
                        fimFila = t;
                    }
                }
                suc = suc.prox;
            }
            q = q.prox;
        }

        if (imprimir) {
            System.out.println("\n");
        }

        return elementosOrdenados == n;
    }
}
