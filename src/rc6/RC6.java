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
    private ArrayList<int[]> textoDesencriptado;

    /**
     * Construtor que inicia um objeto com texto pré-inserido
     * @param texto texto inicial
     * @param senha senha inicial
     */
    public RC6(String texto, String senha) {
        this.iteracoes = 20;
        this.tamanhoPalavra = 32;
        this.PHI = (1 + Math.sqrt(5))/2;
        this.P = calcularImpar((Math.E -2) * Math.pow(2, this.tamanhoPalavra));
        this.Q = calcularImpar( (PHI - 1) * Math.pow(2, this.tamanhoPalavra));
        this.dividirEmBlocos(texto);
        this.senha = new BlocoChave(senha);
        this.chaveS = gerarChaves();
    }

    /**
     * Construtor que inicia um objeto sem texto pré-inserido
     */
    public RC6() {
        this.iteracoes = 20;
        this.tamanhoPalavra = 32;
        this.PHI = (1 + Math.sqrt(5))/2;
        this.P = calcularImpar((Math.E -2) * Math.pow(2, this.tamanhoPalavra));
        this.Q = calcularImpar( (PHI - 1) * Math.pow(2, this.tamanhoPalavra));
    }

    /**
     * Calcula o menor valor inteiro ímpar maior ou igual ao valor recebido
     * @param value - valor em ponto flutuante
     * @return valor inteiro.
     */
    private int calcularImpar(double value){
        try{
            if(value % 2==0)
                return (int)Math.round(value)+1;
            else
                return (int)Math.round(value);
        } catch (IllegalArgumentException error){
            return 1;
        }
    }

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

    /**
     * Desencripta o bloco de texto fornecido
     * @param bloco bloco de texto a desencriptar
     * @return bloco desencriptado
     */
    private int[] desencriptarBlocos(int[] bloco){
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

    /**
     * Encripta o bloco de texto fornecido
     * @param bloco bloco de texto a encriptar
     * @return bloco encriptado
     */
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

    /**
     * Encripta o texto fornecido
     * @param texto texto a encriptar
     */
    public void encriptar(String texto, String senha){
        dividirEmBlocos(texto);
        this.senha = new BlocoChave(senha);
        this.chaveS = gerarChaves();
        encriptar();
    }

    /**
     * Encripta o texto anteriormente fornecido
     */
    public void encriptar(){
        this.textoEncriptado = new ArrayList<>(this.texto);
        for(int i =0; i< textoEncriptado.size(); i++){
            textoEncriptado.set(i,encriptarBlocos(textoEncriptado.get(i)));
        }
    }

    /**
     * Desencripta o texto fornecido
     */
    public void desencriptar(String texto, String senha){
        dividirEmBlocos(texto);
        this.senha = new BlocoChave(senha);
        this.chaveS = gerarChaves();
        desencriptar(this.texto);
    }

    /**
     * Desencripta o texto fornecido em blocos
     */
    private void desencriptar(ArrayList<int[]> blocosEncriptados){
        this.textoDesencriptado = new ArrayList<>(blocosEncriptados);
        for(int i = 0; i< textoDesencriptado.size(); i++){
            textoDesencriptado.set(i, desencriptarBlocos(textoDesencriptado.get(i)));
        }
    }

    /**
     * Desencripta o texto anteriormente fornecido
     */
    public void desencriptar(){
        this.textoDesencriptado = new ArrayList<>(this.textoEncriptado);
        for(int i = 0; i< textoDesencriptado.size(); i++){
            textoDesencriptado.set(i, desencriptarBlocos(textoDesencriptado.get(i)));
        }
    }

    /**
     * Divide o texto em blocos de 128bits (4 Integer)
     * @param texto texto a dividir em blocos
     */
    private void dividirEmBlocos(String texto){
        this.texto = new ArrayList<>();
        char[] letras = texto.toCharArray();
        int[] letrasAux = new int[letras.length];
        int i = 0;
        for(char carater: letras){
            letrasAux[i++] = carater;
        }
        int index = 0;
        int[] aux = new int[4];
        for(int letra: letrasAux){
            if(index == 4){
                this.texto.add(aux);
                aux = new int[4];
                index = 0;
            }
            aux[index++] = letra;
        }
        if(index!=0)
            this.texto.add(aux);
    }

    /**
     * Retorna o Texto Desencriptado
     * @return String de texto desencriptado
     */
    public String getTextoDesencriptado(){
        String texto = reconverterTexto(this.textoDesencriptado);
        char[] aux = texto.toCharArray();
        while(aux[aux.length-1] == '\u0000'){
            char[] aux2 = new char[aux.length-1];
            System.arraycopy(aux, 0, aux2, 0, aux.length - 1);
            aux = aux2;
        }
        return new String(aux);
    }

    /**
     * Retorna o Texto Encriptado
     * @return String de texto encriptado
     */
    public String getTextoEncriptado(){
        return reconverterTexto(this.textoEncriptado);
    }

    /**
     * Transforma o texto dividido em blocos numa string
     * @param blocos - Blocos de texto a juntar
     * @return String de Texto convertido.
     */
    private String reconverterTexto(ArrayList<int[]> blocos){
        StringBuilder texto = new StringBuilder();
        for(int[] bloco: blocos){
            for(int carater: bloco){
                char aux = (char) carater;
                texto.append(aux);
            }
        }
        return texto.toString();
    }
}
