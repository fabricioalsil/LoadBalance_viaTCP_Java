package clientes;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Cliente {

    private String[] tiposRequisicao = {"LEITURA", "ESCRITA"};
    private String serverAddress;
    private int serverPort;
    private Random random;

    public Cliente(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.random = new Random();
    }

    public void iniciarEnvioRequisicoes() {
        try {
            while (true) {
                enviarRequisicaoAleatoria();
                int tempoDeEspera = random.nextInt(151) + 50; // Tempo aleatório entre 50 e 200 ms
                Thread.sleep(tempoDeEspera);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve the interrupted status
            e.printStackTrace();
        }
    }

    public void enviarRequisicaoAleatoria() {
        String tipoRequisicao = tiposRequisicao[random.nextInt(tiposRequisicao.length)];
        
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
        	System.out.println("Tentando conectar ao servidor para requisição do tipo: " + tipoRequisicao);

            socket = new Socket(serverAddress, serverPort);
            System.out.println("Conexão bem-sucedida!"); // Mensagem de depuração

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (tipoRequisicao.equals("LEITURA")) {
                out.println("LEITURA");
                String response = in.readLine();
                System.out.println("Recebido do servidor: " + response);
            } else if (tipoRequisicao.equals("ESCRITA")) {
                int num1 = random.nextInt(999999) + 2;
                int num2 = random.nextInt(999999) + 2;

                out.println("ESCRITA " + num1 + " " + num2);
                String response = in.readLine();
                System.out.println("Recebido do servidor: " + response);
            }

        } catch (IOException e) {
            System.err.println("Não foi possível conectar ao servidor. Tentando novamente em alguns segundos...");
            try {
                Thread.sleep(5000); // Aguardar 5 segundos antes de tentar reconectar
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                ex.printStackTrace();
            } finally {
                // Fechar os recursos se estiverem abertos
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

}
