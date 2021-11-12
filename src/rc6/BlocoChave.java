package rc6;

import java.nio.charset.StandardCharsets;

public class BlocoChave {
    private byte[] chave;
    private final int[] L;
    private final int tamanhoChave;
    
    public BlocoChave(String senha){
        this.tamanhoChave = 128;
        gerarBloco(senha);
        this.L = gerarChave();
    }

    /**
     * Gerar um array de bytes com base no tamanho da senha
     * @param senha
     */
    private void gerarBloco(String senha){
        byte[] aux = senha.getBytes(StandardCharsets.UTF_16);
        this.chave = new byte[128];
        for(int index= 0; index < this.chave.length; index++){
            if(index < aux.length)
                this.chave[index] += aux[index];
        }
    }

    /**
     * Obter o array de chave de 128 bytes
     * @return bloco de 128 bits
     */
    public int[] getChave(){
        return this.L;
    }

    public int[] gerarChave(){
        int c = this.tamanhoChave/4;
        int[] L = new int[c];
        for(int i = this.tamanhoChave-1; i>=0; i-- ){
            L[i/4] = Integer.rotateLeft(L[i /4],8) + this.chave[i];
        }
        return L;
    }
}
