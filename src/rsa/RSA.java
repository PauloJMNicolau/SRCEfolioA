package rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger totientEuler;
    private ArrayList<BigInteger> chavePrivada;
    private ArrayList<BigInteger> chavePublica;
    private ArrayList<BigInteger> textoEmBloco;
    private ArrayList<BigInteger> textoEncriptado;
    private ArrayList<BigInteger> textoDesencriptado;

    /**
     * Cria um objeto apenas com os valores das chaves privada e pública.
     */
    public RSA(){
        this.gerarValoresBase();
        gerarChavePublica();
        gerarChavePrivada();
    }

    /**
     * Cria um objeto com um texto e blocos já definidos.
     * @param texto texto a encriptar / desencriptar
     */
    public RSA(String texto){
        this.gerarValoresBase();
        gerarChavePublica();
        gerarChavePrivada();
        dividirEmBlocos(texto);
    }

    /**
     * Obtém o valor de P
     * @return valor da constante P
     */
    public String getP() {
        return p.toString();
    }

    /**
     * Obtém o valor de Q
     * @return valor da constante Q
     */
    public String getQ() {
        return q.toString();
    }

    /**
     * Obtém o valor de N
     * @return valor da constante N
     */
    private BigInteger getN(){
        return n;
    }

    /**
     * Gera o número primo com comprimento de 4096
     * @param aleatorio Valor aleatório para utilizar como semente
     * @return número primo de grande comprimento
     */
    private BigInteger gerarPrimoGrande(Random aleatorio){
        return BigInteger.probablePrime(4096, aleatorio);
    }

    /**
     * Gera os valores das constantes base do algoritmo
     */
    private void gerarValoresBase(){
        Random aleatorio = new Random();
        this.p = gerarPrimoGrande(aleatorio);
        do {
            this.q = gerarPrimoGrande(aleatorio);
        } while (this.q.equals(this.p));
        this.n = this.p.multiply(this.q);
        this.totientEuler = gerarTotientEuler();
    }

    /**
     * Calcula o valor da função totient de n
     * @return valor da função
     */
    private BigInteger gerarTotientEuler(){
        return (this.p.subtract(BigInteger.ONE)).multiply(this.q.subtract(BigInteger.ONE));
    }

    /**
     * Gera o valor da componente "e" da chave publica
     * @return retorna o valor de "e"
     */
    private BigInteger gerarE(BigInteger valor){
        BigInteger aux = valor;
        if(this.totientEuler.gcd(aux).intValue() > 1) {
            aux = aux.add(new BigInteger("2"));
            return gerarE(aux);
        } else
            return aux;
    }

    /**
     * Gera a chave pública
     */
    private void gerarChavePublica(){
        this.chavePublica = new ArrayList<>();
        this.chavePublica.add(this.getN());
        this.chavePublica.add(gerarE(new BigInteger("3")));
    }

    /**
     * Gera a chave privada
     */
    private void gerarChavePrivada(){
        this.chavePrivada = new ArrayList<>();
        this.chavePrivada.add(this.getN());
        this.chavePrivada.add(this.chavePublica.get(1).modInverse(this.totientEuler));
    }

    /**
     * Divide o texto fornecido em blocos de tamanho menor ao tamanho de n
     * @param texto texto a dividir
     */
    private void dividirEmBlocos(String texto){
        //Converte o texto num array de bytes
        byte[] letras = texto.getBytes();
        int index = 0;
        int indexFim = n.bitLength()/32;
        textoEmBloco = new ArrayList<>();
        //Seleciona uma quantidade determinada de bytes menor que o comprimento de n e converte-os em BigIntegers
        //Adiciona o bloco obtido numa lista de arrays
        while(index < letras.length){
            byte[] aux;
            if(indexFim < letras.length) {
                aux = Arrays.copyOfRange(letras, index, indexFim);
            }else {
                aux = Arrays.copyOfRange(letras, index, letras.length);
            }
            textoEmBloco.add(new BigInteger(aux));
            index = indexFim;
            indexFim += indexFim;
        }
    }

    /**
     * Realiza a desencriptação do bloco de texto
     * @param bloco bloco a desencriptar
     * @return bloco desencriptado
     */
    private BigInteger desencriptarBloco(BigInteger bloco){
        return bloco.modPow(this.chavePrivada.get(1), this.chavePrivada.get(0));
    }

    /**
     * Executa o algoritmo de desencriptação para cada bloco de texto
     */
    public void desencriptar(){
        this.textoDesencriptado = new ArrayList<>(textoEncriptado);
        for(int i=0; i< this.textoDesencriptado.size(); i++){
            this.textoDesencriptado.set(i, desencriptarBloco(this.textoDesencriptado.get(i)));
        }
    }

    /**
     * Realiza a encriptação de um bloco
     * @param bloco bloco a encriptar
     * @return bloco encriptado
     */
    private BigInteger encriptarBloco(BigInteger bloco){
        return bloco.modPow(this.chavePublica.get(1), this.chavePublica.get(0));
    }

    /**
     * Recebe um texto e após dividir em blocos executa o algoritmo de encriptação
     * @param texto texto a encriptar
     */
    public void encriptar(String texto){
        dividirEmBlocos(texto);
        encriptar();
    }

    /**
     * Executa o algoritmo de encriptação para cada bloco
     */
    public void encriptar(){
        this.textoEncriptado = new ArrayList<>(textoEmBloco);
        for(int i=0; i< this.textoEncriptado.size(); i++){
            this.textoEncriptado.set(i, encriptarBloco(this.textoEncriptado.get(i)));
        }
    }

    /**
     * Transforma o texto dividido em blocos numa string
     * @param blocos - Blocos de texto a juntar
     * @return String de Texto convertido.
     */
    private String reconverterTexto(ArrayList<BigInteger> blocos){
        StringBuilder texto = new StringBuilder();
        for(BigInteger bloco: blocos){
            texto.append(new String(bloco.toByteArray()));
        }
        return texto.toString();
    }

    /**
     * Retorna o Texto Desencriptado
     * @return String de texto desencriptado
     */
    public String getTextoDesencriptado(){
        return reconverterTexto(this.textoDesencriptado);
    }

    /**
     * Retorna o Texto Encriptado
     * @return String de texto encriptado
     */
    public String getTextoEncriptado(){
        return reconverterTexto(this.textoEncriptado);
    }

}
