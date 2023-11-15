package Servidores;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorDeAplicacao implements Runnable {

    private int porta;
    private String NOME_ARQUIVO;

    public ServidorDeAplicacao(int porta, String nome) {
        this.porta = porta;
        this.NOME_ARQUIVO = nome;
    }

    public void run() {
        try {
            @SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(porta); // Porta do servidor
            System.out.println("Servidor aguardando conexões na porta " + porta + "...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Conectado: " + clienteSocket);

                BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                String comando = in.readLine();
                System.out.println("Envio do cliente: " + comando);

                new Thread(new ClienteHandler(comando)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClienteHandler implements Runnable {
        private String request;

        public ClienteHandler(String request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                if (request != null) {
                    if (request.equals("LEITURA")) {
                        // Lidar com a requisição de leitura
                        int numLinhas = contarLinhasArquivo();
                        System.out.println("O arquivo possui " + numLinhas + " linhas.");
                    } else if (request.startsWith("ESCRITA")) {
                        // Lidar com a requisição de escrita
                        String[] parts = request.split(" ");
                        if (parts.length == 3) {
                            int num1 = Integer.parseInt(parts[1]);
                            int num2 = Integer.parseInt(parts[2]);
                            verificarMDC(num1, num2);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
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

        private void verificarMDC(int num1, int num2) {
            int mdc = calcularMDC(num1, num2);
            BufferedWriter bw = null;

            try {
                bw = new BufferedWriter(new FileWriter(NOME_ARQUIVO, true));
                bw.write("O MDC entre " + num1 + " e " + num2 + " é " + mdc + ".\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Certifique-se de fechar o BufferedWriter mesmo em caso de exceção
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
