import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class BalanceadorDeCarga {

    private List<Socket> servidores;
    private int servidorAtual;

    public BalanceadorDeCarga() {
        servidores = new ArrayList<>();
        servidorAtual = 0;
        
        // Adicione os endereços e portas dos servidores aqui
        servidores.add(conectarServidor("127.0.0.1", 8081));
        servidores.add(conectarServidor("127.0.0.1", 8082));
    }

    public Socket conectarServidor(String serverAddress, int serverPort) {
        try {
            return new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Socket proximoServidor() {
        // Implemente sua política de balanceamento de carga aqui
        int index = servidorAtual;
        servidorAtual = (servidorAtual + 1) % servidores.size();
        return servidores.get(index);
    }

    public void encaminharRequisicao(String request) {
        Socket servidor = proximoServidor();

        try {
            PrintWriter out = new PrintWriter(servidor.getOutputStream(), true);
            out.println(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BalanceadorDeCarga balanceador = new BalanceadorDeCarga();

        try {
            // Inicie o servidor do balanceador de carga
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                // Aceite uma conexão de um cliente
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Leia a requisição do cliente
                String request = in.readLine();

                // Encaminhe a requisição para um servidor
                balanceador.encaminharRequisicao(request);

                // Feche a conexão com o cliente
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
