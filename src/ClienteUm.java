import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteUm {

    public static void main(String[] args) {
        System.out.println("*!BEM VINDO AO JOGO!*");

        System.out.println("No Jokempo, cada jogador escolhe entre Pedra (0), Papel (1) ou Tesoura (2). " +
                "\nPedra vence Tesoura, Tesoura vence Papel e Papel vence Pedra." +
                "\nOs jogadores digitam o número à sua escolha e o vencedor é determinado " +
                "com base nessas regras.");

        System.out.println("\nDigite (1) para JOGADOR VS CPU | (2) para JOGADOR VS JOGADOR | (3) para FECHAR O JOGO:");
        Scanner scanner = new Scanner(System.in);
        int modoJogo = scanner.nextInt();

        if(modoJogo == 1){
            iniciarJogoVsCPU();
        }
        else if(modoJogo == 2){
            JogadorVsJogador();
        }
        else if(modoJogo == 3){
            System.exit(0);
        }
    }

    public static void JogadorVsJogador() {
        System.out.println("\nDigite o endereco IP do servidor: ");
        Scanner scanner1 = new Scanner(System.in);
        String enderecoServidor = scanner1.nextLine();

        System.out.println("Digite a porta do servidor: ");
        int portaServidor = scanner1.nextInt();

        scanner1.nextLine(); // Consumir a nova linha

        System.out.println("Digite o nome do Jogador: ");
        String nomeJogador = scanner1.nextLine();

        try {
            Socket jogadorSocket = new Socket(enderecoServidor, portaServidor);
            Thread jogadorThread = new Thread(new Jogador(jogadorSocket, nomeJogador));
            jogadorThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Jogador implements Runnable {
        private Socket socket;
        private String jogadorNome;

        Jogador(Socket socket, String jogadorNome) {
            this.socket = socket;
            this.jogadorNome = jogadorNome;
        }

        @Override
        public void run() {
            try (
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
                    Scanner entradaUsuario = new Scanner(System.in);
            ) {
                while (true) {
                    System.out.println("\n" + jogadorNome + ", digite: (0) - Pedra | (1) - Papel | (2) - Tesoura | (Para sair do jogo digite - (3))");
                    int escolha = entradaUsuario.nextInt();

                    saida.println(jogadorNome + ":" + escolha);

                    String resultado = entrada.readLine();
                    System.out.println(resultado);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void iniciarJogoVsCPU() {

        System.out.println("\nEscolha: (0) - Pedra | (1) - Papel | (2) - Tesoura | (Para sair do jogo digite - (3))");
        Scanner scanner = new Scanner(System.in);
        int escolha = scanner.nextInt();

        int escolhaCPU = (int) (Math.random() * 3);

        obterResultadoCPU(escolha, escolhaCPU);

        if(escolha == 3){
            main(new String[]{});
        }
        else{
            if(escolhaCPU == 0){
                System.out.println("\nO bot escolheu pedra!");
            } else if (escolhaCPU == 1) {
                System.out.println("\nO bot escolheu papel!");
            }
            else if (escolhaCPU == 2) {
                System.out.println("\nO bot escolheu tesoura!");
            }

            iniciarJogoVsCPU();
        }
    }

    private static int obterResultadoCPU(int escolha, int escolhaCPU) {
        if ((escolha <= 2 && escolhaCPU <= 2) && (escolha == escolhaCPU))
        {
            System.out.println("\nEMPATE");
            return 0;
        }
        else if ((escolha == 0 && escolhaCPU == 2) || (escolha == 1 && escolhaCPU == 0) || (escolha == 2 && escolhaCPU == 1)) {
            System.out.println("\nO jogador VENCEU!");
            return 1;
        }
        else if((escolhaCPU == 0 && escolha == 2) || (escolhaCPU == 1 && escolha == 0) || (escolhaCPU == 2 && escolha == 1)){
            System.out.println("\nO bot VENCEU!");
            return 2;
        }
        else if(escolha == 3){
            System.out.println("\nJogo resetado\n");
            return 3; // Reinicia o jogo
        }
        else {
            System.out.println("\nFACA UMA JOGADA VALIDA!");
            return 4;
        }
    }
}
