// Cliente.java
package clientes;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Cliente implements Runnable {

    private String[] tiposRequisicao = {"LEITURA", "ESCRITA"};
    private Random random;

    public Cliente() {
        this.random = new Random();
    }

    @Override
    public void run() {
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

        try {
            int balanceadorPort = (random.nextBoolean()) ? 8080 : 8081; // Escolhe aleatoriamente entre 8080 e 8081
            String balanceadorAddress = "127.0.0.1"; // LocalHost

            System.out.println("Tentando enviar requisição para o balanceador de carga para o tipo: " + tipoRequisicao);
            Socket socket = new Socket(balanceadorAddress, balanceadorPort);
            System.out.println("Conexão com o balanceador de carga bem-sucedida!"); // Mensagem de depuração

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Remover a leitura imediata da resposta

            if (tipoRequisicao.equals("LEITURA")) {
                out.println("LEITURA");
                System.out.println("Requisição de leitura enviada.");
            } else if (tipoRequisicao.equals("ESCRITA")) {
                int num1 = random.nextInt(999999) + 2;
                int num2 = random.nextInt(999999) + 2;

                out.println("ESCRITA " + num1 + " " + num2);
                System.out.println("Requisição de escrita enviada.");
            }

            // Fechar os recursos após o uso
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
