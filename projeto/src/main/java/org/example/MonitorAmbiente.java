package org.example;

import com.fazecast.jSerialComm.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class MonitorAmbiente extends JFrame {

    private final JLabel tempLabel;
    private final JLabel umidLabel;
    private final JLabel luzLabel;

    public MonitorAmbiente() {
        setTitle("Monitor Ambiente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        tempLabel = new JLabel("Temperatura: -- °C", SwingConstants.CENTER);
        umidLabel = new JLabel("Umidade: -- %", SwingConstants.CENTER);
        luzLabel = new JLabel("Luz: -- lux", SwingConstants.CENTER);

        add(tempLabel);
        add(umidLabel);
        add(luzLabel);
    }

    private void atualizarDados(String temperatura, String umidade, String luz) {
        tempLabel.setText("Temperatura: " + temperatura + " °C");
        umidLabel.setText("Umidade: " + umidade + " %");
        luzLabel.setText("Luz: " + luz + " lux");
    }

    public static void main(String[] args) throws IOException {
        MonitorAmbiente tela = new MonitorAmbiente();
        tela.setVisible(true);

        SerialPort serialPort = iniciarPortaSerial(tela);
        if (serialPort == null) return;

        PipedInputStream input = new PipedInputStream(); //simula a saida da porta em que os dados do arduino são lidos

        PipedOutputStream output = new PipedOutputStream(input); //simula a entrada para a porta em que os dados do arduino são lidos

        Thread arudionoSimulator = new Thread(new ArduinoSimulator(output));  //thread que inicia o componente que simula a geração de dados pelo arduino
        arudionoSimulator.start();

        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) { //loop para leitura dos dados e envio para o front
            String data = scanner.nextLine();
            String[] valores = data.split(",");
            tela.atualizarDados(valores[0], valores[1], valores[2]);
        }

        serialPort.closePort();
        scanner.close();
    }

    private static SerialPort iniciarPortaSerial(MonitorAmbiente tela) {
        SerialPort serialPort = SerialPort.getCommPort("COM1");
        serialPort.setBaudRate(9600);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        if (!serialPort.openPort()) {
            JOptionPane.showMessageDialog(tela, "Erro: verifique se a porta selecionada existe ou está disponível", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return serialPort;
    }
}