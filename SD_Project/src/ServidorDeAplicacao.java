import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ServidorDeAplicacao {

    public static void main(String[] args) {
        int serverPort = 8081; // Porta do servidor de aplicação
        ExecutorService threadPool = Executors.newFixedThreadPool(10); // Número de threads no pool

        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Servidor de Aplicação iniciado na porta " + serverPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Crie uma nova thread para lidar com a requisição do cliente
                threadPool.execute(new ClienteHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClienteHandler implements Runnable {

    private Socket clientSocket;

    public ClienteHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = in.readLine();

            if (request != null) {
                if (request.equals("LEITURA")) {
                    // Lidar com a requisição de leitura
                    int numLinhas = contarLinhasArquivo(); // Implemente a lógica de contagem de linhas
                    out.println("O arquivo possui " + numLinhas + " linhas.");
                } else if (request.startsWith("ESCRITA")) {
                    // Lidar com a requisição de escrita
                    String[] parts = request.split(" ");
                    if (parts.length == 3) {
                        int num1 = Integer.parseInt(parts[1]);
                        int num2 = Integer.parseInt(parts[2]);
                        verificarMDC(num1, num2); // Implemente a lógica de verificação do MDC
                        out.println("O MDC entre " + num1 + " e " + num2 + " é Z."); // Substitua Z pelo valor real
                    }
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int contarLinhasArquivo() {
        // Implemente a lógica para contar as linhas do arquivo local
        return 0; // Substitua pelo valor real
    }

    private void verificarMDC(int num1, int num2) {
        // Implemente a lógica para verificar o MDC entre os números
        // Atualize o arquivo local com o resultado
    }
}
