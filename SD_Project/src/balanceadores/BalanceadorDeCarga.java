package balanceadores;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BalanceadorDeCarga implements Runnable {

    private List<Socket> servidores;
    private int servidorAtual;
    private int portaDoBalanceador;

    public BalanceadorDeCarga(int portaDoBalanceador) {
        this.portaDoBalanceador = portaDoBalanceador;
        servidores = new ArrayList<>();
        servidorAtual = 0;

        // Adicione os endereços e portas dos servidores aqui
        try {
            servidores.add(conectarServidor("127.0.0.1", 8090));
            servidores.add(conectarServidor("127.0.0.1", 8091));
        } catch (IOException e) {
            e.printStackTrace(); // Ou outro tratamento apropriado
        }
    }


    public Socket conectarServidor(String serverAddress, int serverPort) throws IOException {
        try {
            return new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            System.err.println("Falha ao conectar ao servidor " + serverAddress + ":" + serverPort);
            throw e; // Lança a exceção para indicar o problema ao chamador
        }
    }


    public Socket proximoServidor() {
        int index = servidorAtual;
        servidorAtual = (servidorAtual + 1) % servidores.size();
        return servidores.get(index);
    }

    public void encaminharRequisicao(Socket clienteSocket, String request) {
        Socket servidor = proximoServidor();
        PrintWriter outServidor = null;

        try {
            outServidor = new PrintWriter(servidor.getOutputStream(), true);

            outServidor.println(request);
            System.out.println("Requisição encaminhada para o servidor.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Fechar o PrintWriter apenas após enviar a requisição
                if (outServidor != null) {
                    outServidor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    // Fechar o socket do cliente após encaminhar a requisição
                    if (clienteSocket != null && !clienteSocket.isClosed()) {
                        clienteSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            @SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(portaDoBalanceador); // Porta do balanceador
            System.out.println("Balanceador de carga aguardando conexões na porta " + portaDoBalanceador + "...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket);

                BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                String request = in.readLine();
                System.out.println("Recebido do cliente: " + request);

                encaminharRequisicao(clienteSocket, request);

                // Fechar os recursos após o uso
                in.close();
                clienteSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
