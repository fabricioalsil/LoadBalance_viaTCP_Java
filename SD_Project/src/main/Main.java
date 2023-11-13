package main;

import Servidores.ServidorDeAplicacao;
import balanceadores.BalanceadorDeCarga;
import clientes.Cliente;

public class Main {

	public static void main(String[] args) {
	    // Inicialize os componentes principais
	    ServidorDeAplicacao servidor1 = new ServidorDeAplicacao(8081); // Porta 8081
	    ServidorDeAplicacao servidor2 = new ServidorDeAplicacao(8082); // Porta 8082

	    // Inicie os servidores de aplicação em threads separadas
	    Thread servidor1Thread = new Thread(servidor1);
	    Thread servidor2Thread = new Thread(servidor2);
	    servidor1Thread.start();
	    servidor2Thread.start();

	    // Aguarde alguns segundos para garantir que os servidores estejam inicializados
	    try {
	        Thread.sleep(5000); // Aguarde 5 segundos
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	        e.printStackTrace();
	    }

	    // Inicie o balanceador e o cliente em threads separadas
	    BalanceadorDeCarga balanceador = new BalanceadorDeCarga();
	    Cliente cliente = new Cliente("127.0.0.1", 8081); // Conecta-se ao balanceador de carga

	    Thread balanceadorThread = new Thread(balanceador);
	    balanceadorThread.start();

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
