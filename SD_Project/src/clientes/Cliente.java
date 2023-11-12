package clientes;
import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Endereço do servidor
        int serverPort = 8080; // Porta do servidor
        Random random = new Random();

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                // Escolha aleatoriamente entre requisição de leitura e escrita
                boolean isReadRequest = random.nextBoolean();
                String requestType = isReadRequest ? "Leitura" : "Escrita";

                if (isReadRequest) {
                    // Requisição de leitura
                    out.println("LEITURA");
                } else {
                    // Requisição de escrita com dois números aleatórios
                    int num1 = 2 + random.nextInt(999999); // Números entre 2 e 1.000.000
                    int num2 = 2 + random.nextInt(999999);
                    out.println("ESCRITA " + num1 + " " + num2);
                }

                // Aguarde a resposta do servidor
                String response = in.readLine();
                System.out.println("Recebido do servidor: " + response);

                // Durma por um tempo aleatório entre 50 e 200 ms
                int sleepTime = 50 + random.nextInt(151);
                Thread.sleep(sleepTime);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
