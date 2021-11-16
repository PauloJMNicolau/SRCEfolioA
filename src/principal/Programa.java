package principal;

import rc6.RC6;
import rsa.RSA;

import java.util.Objects;
import java.util.Scanner;

public class Programa {
    private String senha;
    private String texto;

    /**
     * Construtor da clase principal
     */
    public Programa(){
        this.texto = "";
        this.senha = "";
    }

    /**
     * Imprimir o cabeçalho do programa na consola
     */
    private void imprimirCabecalho(){
        for(int i = 0; i < 72; i++)
            System.out.print('*');
        System.out.println("\n* Programa desenvolvido por Paulo Nicolau — 1800465 (UAB — Student ID) *");
        System.out.println("* E-folio A — Algoritmos de Encriptação Simétricos e Assimétricos      *");
        for(int i = 0; i < 72; i++)
            System.out.print('*');
        System.out.println();
    }

    /**
     * Getter da String de texto original
     * @return string de texto original
     */
    private String getTexto(){
        return this.texto;
    }

    /**
     * Atualiza a String de texto original
     * @param texto - texto original
     */
    private void setTexto(String texto){
        this.texto = texto;
    }

    /**
     * Getter da String de senha
     * @return string de senha
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
     * Verifica se o texto original é igual ao texto encriptado pelo algoritmo
     * @return resultado booleano da comparação
     */
    private boolean compararTexto(String texto, String desencriptacao){
        return texto.equals(desencriptacao);
    }

    /**
     * Mostra o resultado da execução do algoritmo
     */
    private void mostraResultado(String textoOriginal, String encriptacao, String desencriptacao, String algoritmo){
        if(compararTexto(textoOriginal, desencriptacao))
            System.out.println("\nSUCESSO: Texto desencriptado é idêntico ao texto original\n");
        else
            System.out.println("\nINSUCESSO: Texto desencriptado não é idêntico ao texto original\n");
        System.out.println("Texto Original: " + textoOriginal);
        System.out.println("Texto Encriptado pelo " + algoritmo + ": " + encriptacao);
        System.out.println("Texto Desencriptado pelo " + algoritmo + ": " + desencriptacao);
    }

    /**
     * Função que permite selecionar tipo de algoritmo
     * @return RC6 | RSA | SAIR
     */
    private Opcao pedirAlgoritmo(){
        String op;
        Scanner leitor = new Scanner(System.in);
        do{
            System.out.println("Escolha o Algoritmo. (Para sair faça apenas enter) [Simétrico | Assimétrico | Demonstração]");
            op = leitor.nextLine().toLowerCase();
            if(!op.equals("simétrico") && !op.equals("assimétrico") && !op.equals("")  && !op.equals("demonstração"))
                System.out.println("Opção Inválida! Tente novamente.");
        }while (!op.equals("simétrico") && !op.equals("assimétrico") && !op.equals("") && !op.equals("demonstração"));
        return switch (op) {
            case "simétrico" -> Opcao.RC6;
            case "assimétrico" -> Opcao.RSA;
            case "demonstração" -> Opcao.DEMONSTRACAO;
            default -> Opcao.SAIR;
        };
    }

    /**
     * Pedir o texto a encriptar / Desencriptar
     */
    private void pedirTexto(){
        Scanner leitor = new Scanner(System.in);
        System.out.print("Introduza o texto: ");
        setTexto(leitor.nextLine());
    }

    /**
     * Pedir a senha a utilizar como chave de encriptar / desencriptar
     */
    private void pedirSenha(){
        Scanner leitor = new Scanner(System.in);
        System.out.print("Introduza a senha: ");
        setSenha(leitor.nextLine());
    }

    /**
     * Executa Algoritmo de encriptação RC6
     */
    private void executeRC6(){
        pedirTexto();
        pedirSenha();
        RC6 algoritmo = new RC6();
        algoritmo.encriptar(this.getTexto(), this.getSenha());
        algoritmo.desencriptar();
        mostraResultado(this.getTexto(), algoritmo.getTextoEncriptado(), algoritmo.getTextoDesencriptado(), "RC6");
        apagarMemoria();
    }

    /**
     * Executa algoritmo de encriptação RSA
     */
    private void executeRSA(){
        pedirTexto();
        RSA algoritmo = new RSA();
        algoritmo.encriptar(this.getTexto());
        algoritmo.desencriptar();
        mostraResultado(this.getTexto(), algoritmo.getTextoEncriptado(), algoritmo.getTextoDesencriptado(), "RSA");
        apagarMemoria();
    }

    /**
     * Executa uma demonstração do programa com texto pré-definido
     */
    private void executeDemonstracao(){
        String textoExemplo = "Texto de Exemplo para demonstração do processo de encriptação e desencriptação de texto " +
                "pelos algoritmos RC6 (Simétrico) e RSA (Assimétrico)";
        String senhaExemplo = "jklahrqweredkrjqw4uo564jltrguoy59t340eklioeruotawer5805rlçwrtirgrtp+ri05405irfljt9045";
        System.out.println("\n*** Algoritmo RC6 ***");
        RC6 algoritmo = new RC6(textoExemplo, senhaExemplo);
        algoritmo.encriptar();
        algoritmo.desencriptar();
        System.out.println("Texto utilizado: " + textoExemplo);
        System.out.println("Senha utilizada: " + senhaExemplo);
        mostraResultado(textoExemplo, algoritmo.getTextoEncriptado(), algoritmo.getTextoDesencriptado(), "RC6");
        apagarMemoria();
        System.out.println("\n*** Algoritmo RSA ***");
        RSA algoritmo2 = new RSA(textoExemplo);
        algoritmo2.encriptar();
        algoritmo2.desencriptar();
        System.out.println("Chave P: " + algoritmo2.getP() + "\nChave Q: "+ algoritmo2.getQ());
        mostraResultado(textoExemplo, algoritmo2.getTextoEncriptado(), algoritmo2.getTextoDesencriptado(), "RSA");
        apagarMemoria();
    }

    /**
     * Repoem os valores de origem na memoria
     */
    private void apagarMemoria(){
        this.texto = "";
        this.senha = "";
    }

    /**
     * Executa algoritmos de encriptação
     */
    public void execute(){
        imprimirCabecalho();
        Opcao op;
        do{
            op = pedirAlgoritmo();
            switch (op){
                case RC6:
                    executeRC6();
                    break;
                case RSA:
                    executeRSA();
                    break;
                case DEMONSTRACAO:
                    executeDemonstracao();
                    break;
                case SAIR:
                    break;
            }
        } while (!Objects.equals(op, Opcao.SAIR));
    }

}
