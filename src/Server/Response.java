package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Response implements Runnable {

    public Response(Socket client) {
        this.client = client;
    }

    Socket client;
    DataInputStream in;

    private String method;
    private String fileName;
    private String protocol;
    private String header;

    @Override
    public void run() {
        try {

            in = new DataInputStream(client.getInputStream());
            headerClient();

            switch (method) {
                case "GET":
                    new Page(client, fileName, protocol).response();
                    break;
                case "POST":

                    new GetData(client, fileName, protocol).data();
                    break;

                default:
                    break;
            }

        } catch (IOException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void headerClient() {

        try {
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(in));

            String str[] = buffReader.readLine().split(" ");    //  Quebra primeira linha
            String control;                                     //  Variavel para extrair cada linha da requisicao
            this.method = str[0];
            this.fileName = str[1];
            this.protocol = str[2];

            //  Armazena o cabecalho da requisicao HTTP e recupera a primeira linha
            this.header = method + " " + fileName + " " + protocol + "\r\n";

            do {
                control = buffReader.readLine();
                this.header += control + "\r\n";

            } while (!control.isEmpty());

            if (this.fileName.equals("/")) {
                this.fileName = "/cadastro.html";
            }

            // System.out.println(header);

        } catch (Exception error) {
            System.out.println("Erro em headerClient()");
        }

    }

}
