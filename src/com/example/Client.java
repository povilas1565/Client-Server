package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

public class Client {
    public static void main(String[] args) throws IOException {
        if (args.length < 5)
            return;
        String IP = args[0];
        int port = Integer.parseInt(args[1]);
        int N = Integer.parseInt(args[2]);
        int M = Integer.parseInt(args[3]);
        int Q = Integer.parseInt(args[4]);
        FileWriter fileWriter = new FileWriter("results.txt");

        // Connect to server
        Socket socket = new Socket();
        socket.setTcpNoDelay(true);
        socket.connect(new InetSocketAddress(IP, port));
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        // Generate random byte array
        Random rand = new Random();
        byte[] byteArray = new byte[N*M + 8];
        rand.nextBytes(byteArray);

        // Perform M iterations and Q sub-iterations
        for (int k = 0; k < M; k++) {
            long totalTime = 0;
            for (int i = 0; i < Q; i++) {
                long startTime = System.nanoTime();
                out.write(byteArray, 0, N*k+8);
                out.flush();
                in.read();
                long endTime = System.nanoTime();
                long timeTaken = endTime - startTime;
                totalTime += timeTaken;
            }
            double averageTime = (double) totalTime / Q;
            fileWriter.write((N*k + 8) + " " + averageTime + "\n");
            System.out.println("Iteration " + k + ": " + (N*k + 8) + " bytes, " + averageTime + " ns");
        }
        fileWriter.close();
        out.close();
        in.close();
        socket.close();
    }
}

