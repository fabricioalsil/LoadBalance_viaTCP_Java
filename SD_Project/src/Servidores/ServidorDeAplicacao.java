package Servidores;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorDeAplicacao implements Runnable {

    private int porta;
    private static final String NOME_ARQUIVO = "arquivo.txt";

    public ServidorDeAplicacao(int porta) {
        this.porta = porta;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor de Aplicação iniciado na porta " + porta);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Crie uma nova thread para lidar com a requisição do cliente
                new Thread(new ClienteHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClienteHandler implements Runnable {
        private Socket clientSocket;

        public ClienteHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String request = in.readLine();

                if (request != null) {
                    if (request.equals("LEITURA")) {
                        // Lidar com a requisição de leitura
                        int numLinhas = contarLinhasArquivo();
                        out.println("O arquivo possui " + numLinhas + " linhas.");
                    } else if (request.startsWith("ESCRITA")) {
                        // Lidar com a requisição de escrita
                        String[] parts = request.split(" ");
                        if (parts.length == 3) {
                            int num1 = Integer.parseInt(parts[1]);
                            int num2 = Integer.parseInt(parts[2]);
                            verificarMDC(num1, num2, out);
                            out.println("O MDC entre " + num1 + " e " + num2 + " é Z."); // Substitua Z pelo valor real
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private int contarLinhasArquivo() {
            try (BufferedReader br = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
                int count = 0;
                while (br.readLine() != null) {
                    count++;
                }
                return count;
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private void verificarMDC(int num1, int num2, PrintWriter out) {
            int mdc = calcularMDC(num1, num2);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOME_ARQUIVO, true))) {
                out.println("O MDC entre " + num1 + " e " + num2 + " é " + mdc + ".");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int calcularMDC(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }
    }
}
