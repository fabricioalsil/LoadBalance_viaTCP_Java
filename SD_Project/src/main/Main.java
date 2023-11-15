package main;

import Servidores.ServidorDeAplicacao;
import balanceadores.BalanceadorDeCarga;
import clientes.Cliente;

public class Main {

    public static void main(String[] args) {
    	
    	//Criar servidor com porta 8090
    	ServidorDeAplicacao servidor8090 = new ServidorDeAplicacao(8090, "arquivo.txt");
    	
    	//Criar servidor com porta 8091
    	ServidorDeAplicacao servidor8091 = new ServidorDeAplicacao(8091, "arquivo2.txt");
    	
    	//Criar e iniciar threads para os servidores
    	Thread servidor8090Thread = new Thread(servidor8090);
    	Thread servidor8091Thread = new Thread(servidor8091);
    	
    	servidor8090Thread.start();
    	servidor8091Thread.start();
    	
        // Criar balanceador de carga com porta 8080
        BalanceadorDeCarga balanceador8080 = new BalanceadorDeCarga(8080);

        // Criar balanceador de carga com porta 8081
        BalanceadorDeCarga balanceador8081 = new BalanceadorDeCarga(8081);

        // Criar e iniciar threads para os balanceadores
        Thread balanceador8080Thread = new Thread(balanceador8080);
        Thread balanceador8081Thread = new Thread(balanceador8081);

        balanceador8080Thread.start();
        balanceador8081Thread.start();

        // Criar e iniciar threads para os clientes
        Thread clienteThread1 = new Thread(new Cliente());
        Thread clienteThread2 = new Thread(new Cliente());

        clienteThread1.start();
        clienteThread2.start();
    }
}



