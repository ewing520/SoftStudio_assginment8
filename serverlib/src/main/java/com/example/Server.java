package com.example;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class Server extends MyWindows implements Runnable
{
    private Thread thread;
    private ServerSocket serverSock;
    private BufferedReader reader;
    private JLabel resultStr;
    //public String strToUI = "";

    public Server()
    {
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        super.setSize(new Dimension(300, 100) );
        super.setResizable(false);
        super.setVisible(true);

        this.resultStr = new JLabel();
        super.add(this.resultStr);

        try {
            // Detect server ip
            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("IP of my server is := " + IP.getHostAddress());
            System.out.println("Waitting for connecting......");

            serverSock = new ServerSocket(9527);

            thread = new Thread(this);
            thread.start();
        }
        catch (java.io.IOException e)
        {
            System.out.println("Error occurred!");
            System.out.println("IOException :" + e.toString());
        }

        }
    @Override
    public void run()
    {
        while(true){
            try {
                Socket clientSock = serverSock.accept();

                System.out.println("Connected!!");

                this.reader = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                while(true){
                    String s = this.reader.readLine();
                    this.resultStr.setText(s);
                }
            }
            catch(Exception e){
                System.out.println("Error: "+e.getMessage());
            }
        }
    }
}
