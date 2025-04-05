package br.com.monitor;

import java.io.OutputStream;
import java.util.Random;

public class ArduinoSimulator implements Runnable {


    private final OutputStream outputStream;

    public ArduinoSimulator(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void run() {
        System.out.println("Iniciando o simulador de arduino");
        try {

            Random random = new Random();

            while (true) {
                int temperatura = random.nextInt(30) + 15; // 15°C a 45°C
                int umidade = random.nextInt(50) + 30; // 30% a 80%
                int luz = random.nextInt(1000); // 0 a 1000 lux

                String dados = temperatura + "," + umidade + "," + luz + "\n";
                outputStream.write(dados.getBytes());
                outputStream.flush();

                System.out.println("Enviado: " + dados);
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}