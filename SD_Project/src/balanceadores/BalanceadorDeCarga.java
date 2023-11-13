package balanceadores;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BalanceadorDeCarga implements Runnable {

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

    @Override
    public void run() {
        // Lógica do balanceador vai aqui...
        // Implemente o que é necessário para gerenciar as requisições dos clientes
    }
}
