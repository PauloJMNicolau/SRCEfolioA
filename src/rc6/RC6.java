package rc6;

import java.util.ArrayList;

public class RC6 {
    private final double PHI;
    private final int P;
    private final int Q;
    private final int iteracoes;
    private final int tamanhoPalavra;
    private BlocoChave senha;
    private ArrayList<int[]> texto;
    private int[] chaveS;
    private ArrayList<int[]> textoEncriptado;
    private ArrayList<int[]> textoDecriptado;


    public RC6(ArrayList<int[]> texto, String senha) {
        this.iteracoes = 20;
        this.tamanhoPalavra = 32;
        this.PHI = (1 + Math.sqrt(5))/2;
        this.P = calculateOdd((Math.E -2) * Math.pow(2, this.tamanhoPalavra));
        this.Q = calculateOdd( (PHI - 1) * Math.pow(2, this.tamanhoPalavra));
        this.senha = new BlocoChave(senha);
        this.texto = texto;
        this.chaveS = gerarChaves();
    }

    /**
     * Calcula o menor valor inteiro ímpar maior ou igual ao valor recebido
     * @param value - valor em ponto flutuante
     * @return valor inteiro.
     */
    private int calculateOdd(double value){
        try{
            if(value % 2==0)
                return (int)Math.round(value)+1;
            else
                return (int)Math.round(value);
        } catch (IllegalArgumentException error){
            return 1;
        }
    }
/*
    private int rotateLeft(int palavra, int rotacao){
        return (palavra << rotacao) | (palavra >> (32 - rotacao));
    }

    private int rotateRight(int palavra, int rotacao){
        return (palavra >> rotacao) | (palavra << (32 - rotacao));
    }*/

    /**
     * Gerar as chaves de encriptação (Key Schedule)
     * @return Array com as chaves de encriptação
     */
    private int[] gerarChaves(){
        int[] L = this.senha.getChave();
        int iteracoes = (2 * this.iteracoes + 4);
        int[] chaves = new int[iteracoes];
        chaves[0] = this.P;
        this.senha.gerarChave();
        for(int i = 1; i< iteracoes; i++){
            chaves[i] = chaves[i-1] + this.Q;
        }
        int A = 0;
        int B = 0;
        int v = 3 * Math.max(this.tamanhoPalavra, iteracoes);
        for(int iter = 1, i = 0, j = 0; iter< v; iter++, i = ((i+1) % (iteracoes)), j = ((j+1) % this.tamanhoPalavra) ){
            chaves[i] = Integer.rotateLeft(chaves[i] + A +B, 3);
            A = chaves[i];
            L[j] = Integer.rotateLeft(L[j] + A + B, A+B);
            B = L[j];
        }
        return chaves;
    }

    private int[] decriptarBlocos(int[] bloco){
        int[] chaves = this.chaveS;
        int valor = (int) Math.log(this.tamanhoPalavra);
        int A = bloco[0];
        int B = bloco[1];
        int C = bloco[2];
        int D = bloco[3];

        C = C - chaves[2*iteracoes+3];
        A = A - chaves[2*iteracoes+2];

        for(int i = iteracoes; i>=1 ; i--){
            int aux = D;
            D = C;
            C = B;
            B = A;
            A = aux;

            int u = Integer.rotateLeft(D * (2 * D + 1), valor);
            int t = Integer.rotateLeft(B * (2 * B + 1), valor);
            C = (Integer.rotateRight((C - chaves[2 * i + 1]), t) ^ u );
            A = (Integer.rotateRight((A - chaves[2 * i]), u) ^ t );

        }

        D = D - chaves[1];
        B = B - chaves[0];
        return new int[] {A,B,C,D};
    }

    public void encriptar(){
        this.textoEncriptado = getTexto();
        for(int i =0; i< textoEncriptado.size(); i++){
            textoEncriptado.set(i,encriptarBlocos(textoEncriptado.get(i)));
        }
    }

    private int[] encriptarBlocos(int[] bloco){
        int[] chaves = this.chaveS;
        int valor = (int) Math.log(this.tamanhoPalavra);
        int A = bloco[0];
        int B = bloco[1];
        int C = bloco[2];
        int D = bloco[3];

        B = B + chaves[0];
        D = D + chaves[1];

        for(int i = 1; i<= iteracoes; i++){
            int t = Integer.rotateLeft(B * (2 * B + 1), valor);
            int u = Integer.rotateLeft(D * (2 * D + 1), valor);
            A = (Integer.rotateLeft((A ^ t), u) + chaves[2 * i]);
            C = (Integer.rotateLeft((C ^ u), t) + chaves[2 * i + 1]);
            int aux = A;
            A = B;
            B = C;
            C = D;
            D = aux;
        }

        A = A + chaves[2*iteracoes+2];
        C = C + chaves[2*iteracoes+3];
        return new int[] {A,B,C,D};
    }

    public void decriptar(){
        this.textoDecriptado = this.getTextoEncriptado();
        for(int i =0; i< textoDecriptado.size(); i++){
            textoDecriptado.set(i,decriptarBlocos(textoDecriptado.get(i)));
        }
    }

    public ArrayList<int[]> getTexto(){
        return new ArrayList<>(this.texto);
    }

    public ArrayList<int[]> getTextoEncriptado(){
        return new ArrayList<>(this.textoEncriptado);
    }

    public ArrayList<int[]> getTextoDecriptado(){
        return new ArrayList<>(this.textoDecriptado);
    }

}
