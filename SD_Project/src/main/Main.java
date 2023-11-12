package main;

import Servidores.ServidorDeAplicacao;
import balanceadores.BalanceadorDeCarga;
import clientes.Cliente;

public class Main {

    public static void main(String[] args) {
        // Inicialize os componentes principais
        BalanceadorDeCarga balanceador = new BalanceadorDeCarga();
        ServidorDeAplicacao servidor1 = new ServidorDeAplicacao(8081); // Porta 8081
        ServidorDeAplicacao servidor2 = new ServidorDeAplicacao(8082); // Porta 8082

        // Inicie os servidores de aplicação em threads separadas
        Thread servidor1Thread = new Thread(servidor1);
        Thread servidor2Thread = new Thread(servidor2);
        servidor1Thread.start();
        servidor2Thread.start();

        // Inicie um cliente para testes
        Cliente cliente = new Cliente("127.0.0.1", 8081); // Conecta-se ao balanceador de carga

        try {
            while (true) {
                // Simule requisições de leitura e escrita do cliente
                cliente.enviarRequisicaoAleatoria();
                Thread.sleep(500); // Aguarde meio segundo entre as requisições (simulação)
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
