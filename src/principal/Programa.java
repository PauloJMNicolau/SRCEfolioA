package principal;

import rc6.RC6;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Programa {

    private String senha;
    private String textoCifradoSimetrico;
    private String textoCifradoAssimetrico;
    private String textoDecifradoSimetrico;
    private String textoDecifradoAssimetrico;
    private String texto;
    private int tamanho;
    private ArrayList<int[]> textoEmBloco;

    /**
     * Construtor da clase principal
     */
    public Programa(){
        texto = "";
        textoDecifradoSimetrico = "";
        textoDecifradoAssimetrico = "";
        textoCifradoAssimetrico = "";
        textoCifradoSimetrico = "";
        senha = "";
        textoEmBloco = new ArrayList<>();
    }

    /**
     * Imprimir o cabeçalho do programa na consola
     */
    private void imprimirCabecalho(){
        for(int i = 0; i < 72; i++)
            System.out.print('*');
        System.out.println("\n* Programa desenvolvido por Paulo Nicolau - 1800465 (UAB - Student ID) *");
        System.out.println("* E-folio A - Algoritmos de Encriptação Simétricos e Assimétricos      *");
        for(int i = 0; i < 72; i++)
            System.out.print('*');
        System.out.println();
    }

    /**
     * Getter da String de texto armazenada
     * @return string de texto armazenada
     */
    private String getTexto(){
        return this.texto;
    }

    /**
     * Atualiza a String de texto a utilizar
     * @param texto - texto a utilizar
     */
    private void setTexto(String texto){
        this.texto = texto;
        this.tamanho = texto.length();
    }

    /**
     * Getter da String de senha armazenada
     * @return string de senha armazenada
     */
    private String getSenha(){
        return this.senha;
    }

    /**
     * Atualiza a String de senha a utilizar
     * @param senha - senha a utilizar
     */
    private void setSenha(String senha){
        this.senha = senha;
    }

    /**
     * Getter do texto encriptado pelo algoritmo RC6     * @return string de texto encriptado por RC6     */
    private String getTextoCifradoSimetrico(){
        return this.textoCifradoSimetrico;
    }

    /**
     * Getter do texto decriptado pelo algoritmo RC6     * @return string de texto decriptado pelo RC6     */
    private String getTextoDecifradoSimetrico(){
        return this.textoDecifradoSimetrico;
    }

    /**
     * Getter do texto encriptado pelo algoritmo ...
     * @return string de texto encriptado pelo ...
     */
    private String getTextoCifradoAssimetrico(){
        return this.textoCifradoAssimetrico;
    }

    /**
     * Getter do texto decriptado pelo algoritmo ...
     * @return string de texto decriptado pelo
     */
    private String getTextoDecifradoAssimetrico(){
        return this.textoDecifradoAssimetrico;
    }

    /**
     * Verifica se o texto original é igual ao texto encriptado pelo algoritmo RC6
     * @return
     */
    private boolean compararTextoComTwoFishDecript(){
        return this.texto.equals(this.textoDecifradoSimetrico);
    }

    /**
     * Mostra o resultado da execução do algoritmo RC6
     */
    private void mostraResultadoSimetrico(){
        if(compararTextoComTwoFishDecript())
            System.out.println("Texto decriptado é idêntico ao texto original");
        else
            System.out.println("Texto decriptado não é identico ao texto original");
        System.out.println("Texto Original: " + this.juntarBlocos(this.textoEmBloco));
        System.out.println("Texto Encirptado pelo RC6: " + this.getTextoCifradoSimetrico());
        System.out.println("Texto Decriptado pelo RC6: " + this.getTextoDecifradoSimetrico());
    }


    private boolean compararTextoComDecript(){
        String aux = juntarBlocos(this.textoEmBloco);
        return aux.equals(this.textoDecifradoAssimetrico);
    }

    private void mostraResultadoAssimetrico(){
        if(compararTextoComDecript())
            System.out.println("Texto decriptado é idêntico ao texto original");
        else
            System.out.println("Texto decriptado não é identico ao texto original");
        System.out.println("Texto Original: " + this.getTexto());
        System.out.println("Texto Encirptado pelo ...: " + this.getTextoCifradoAssimetrico());
        System.out.println("Texto Decriptado pelo ...: " + this.getTextoDecifradoAssimetrico());
    }

    /**
     * Função que permite selecionar tipo de algoritmo
     * @return -1 Inválido | 0 - 2
     */
    private Opcao pedirAlgoritmo(){
        String op;
        Scanner leitor = new Scanner(System.in);
        do{
            System.out.println("Escolha o tipo de Algoritmo. (Para sair faça apenas enter) [ Simetrico | Assimetrico ]");
            op = leitor.nextLine().toLowerCase();
            if(!op.equals("simetrico") && !op.equals("assimetrico") && !op.equals(""))
                System.out.println("Opção Inválida! Tente novamente.");
        }while (!op.equals("simetrico") && !op.equals("assimetrico") && !op.equals(""));
        leitor.close();
        return switch (op) {
            case "simetrico" -> Opcao.RC6;
            case "assimetrico" -> Opcao.NTRUENCRYPT;
            default -> Opcao.SAIR;
        };
    }

    /**
     * Pedir o texto a encriptar / Decriptar
     */
    private void pedirTexto(){
        Scanner leitor = new Scanner(System.in);
        System.out.print("Introduza o texto: ");
        setTexto(leitor.nextLine());
        leitor.close();
    }

    /**
     * Pedir a senha a utilizar como chave de encriptar / decriptar
     */
    private void pedirSenha(){
        Scanner leitor = new Scanner(System.in);
        System.out.print("Introduza a senha: ");
        setSenha(leitor.nextLine());
        leitor.close();
    }

    private void executeRC6(){
        pedirTexto();
        pedirSenha();
        dividirEmBlocos(getTexto());
        RC6 algoritmo = new RC6(this.textoEmBloco, getSenha());
        algoritmo.encriptar();
        this.textoCifradoSimetrico = juntarBlocosEncriptados(algoritmo.getTextoEncriptado());
        algoritmo.decriptar();
        this.textoDecifradoSimetrico = juntarBlocos(algoritmo.getTextoDecriptado());

        mostraResultadoSimetrico();
        apagarMemoria();
    }

    private void executeNTRUEncrypt(){
        pedirTexto();
        pedirSenha();

        mostraResultadoAssimetrico();
        apagarMemoria();
    }

    /**
     * Repoem os valores de origem na memoria
     */
    private void apagarMemoria(){
        texto = "";
        textoDecifradoSimetrico = "";
        textoDecifradoAssimetrico = "";
        textoCifradoAssimetrico = "";
        textoCifradoSimetrico = "";
        senha = "";
        textoEmBloco = new ArrayList<>();
    }

    public void execute(){
        imprimirCabecalho();
        Opcao op;
        do{
            op = pedirAlgoritmo();
            switch (op){
                case RC6:
                    executeRC6();
                    break;
                case NTRUENCRYPT:
                    executeNTRUEncrypt();
                    break;
                case SAIR:
                    break;
            }
        } while (!Objects.equals(op, Opcao.SAIR));
    }

    /**
     * Divide o texto em blocos de texto (128 bits - 4 Integer)
     * @param texto
     */
    private void dividirEmBlocos(String texto){
        char[] letras = texto.toCharArray();
        int[] letrasAux = new int[letras.length];
        int i=0;
        for(char letra: letras){
            letrasAux[i++] = (letra);
        }
        int index = 0;
        int[] aux = new int[4];
        for(int letra: letrasAux){
            if(index == 4){
                this.textoEmBloco.add(aux);
                aux = new int[4];
                index = 0;
            }
            aux[index++] = letra;
        }
        if(index!=0)
            this.textoEmBloco.add(aux);
    }

    private String juntarBlocosEncriptados (ArrayList<int[]> blocos){
        StringBuilder texto = new StringBuilder();
        for(int[] bloco: blocos){
            for(int carater: bloco){
                char aux = (char) carater;
                texto.append(aux);
            }
        }
        return texto.toString();
    }

    private String juntarBlocos (ArrayList<int[]> blocos){
        StringBuilder texto = new StringBuilder();
        for(int[] bloco: blocos){
            for(int carater: bloco){
                char aux = (char) carater;
                texto.append(aux);
            }
        }
        String textoFinal = texto.toString();
        char[] aux = textoFinal.toCharArray();
        while(aux[aux.length-1] == '\u0000'){
            char[] aux2 = new char[aux.length-2];
            System.arraycopy(aux, 0, aux2, 0, aux.length - 2);
            aux = aux2;
        }
        textoFinal = new String(aux);
        return textoFinal;
    }
}
