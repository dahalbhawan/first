/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leapfrog.SocketApp.handler;

import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author aruun
 */
public class ClientListener extends Thread {

    private Socket client;

    public ClientListener(Socket client) {
        this.client = client;

    }

    @Override
    public void run() {
        try {
            String link = "http://www.imdb.com/chart/top?ref_=nv_mv_250_6";
            String line1 = "";
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line1 = reader1.readLine()) != null) {
                builder.append(line1);
            }
            reader1.close();

            String regex = "<td class=\"titleColumn\">(.*?)<a href=\"(.*?)\"title=\"(.*?)\" >(.*?)</a>"; //<td class=\"ratingColumn imdbRating\"><strong title=\"(.*?)\"></strong></td>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(builder.toString());

            /* 
             socket programming
             */
            PrintStream output = new PrintStream(client.getOutputStream()); //client ko ma display garna getoutputstream
            output.println("Welcome to ChatServer");
            output.println("Please type in your messages:");

            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line = reader.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("imdb")) {
                    output.println("Top 250 Movies: \n ");
                    while (matcher.find()) {
                        output.println(matcher.group(1).trim() + matcher.group(4).trim());
                    }
                }
                else if (line.equalsIgnoreCase("exit")){
                    System.exit(0);
                }
            
            else {
                output.println("Your message: " + line);
                }
                output.flush();
            }

         
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());

    }

}
}
