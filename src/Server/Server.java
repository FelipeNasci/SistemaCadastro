package Server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

public class Server extends JFrame {

    public static void main(String args[]) {

        int port = 1333;
        
        ExecutorService pool;
        ServerSocket server;
        Socket client;

        try {

            gui();
            
            File caminho = new File(".");
            
            System.err.println("Caminho = " + caminho.getAbsolutePath());
            
            server = new ServerSocket(port);
            pool = Executors.newCachedThreadPool();
            
            System.err.println("Servidor Ativo");

            while (true) {

                // JOptionPane.showMessageDialog(null, "Hello World");
                client = server.accept();
                pool.execute(new Response(client)); //Encaminha para a pg web

            }

        } catch (Exception error) {
            System.err.println("Erro no Servidor");
        }

    }

    public static void gui() {
        JFrame janela = new JFrame("Plano Liberdade Familiar");
        janela.setVisible(true);
        janela.setSize(400, 400);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
