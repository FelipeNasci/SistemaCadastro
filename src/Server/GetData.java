package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import javax.swing.JOptionPane;

/*

CLASSE PARA RESPOSTA DO MÉTODO POST

*/

public class GetData {

    public GetData(Socket client, String fileName, String protocol) {

        defineFile(fileName);           //  Inicia o arquivo
        this.protocol = protocol;       //  Necessario para o cabecalho
        this.client = client;
        this.filename = fileName;
    }

    private Socket client;
    private File file;
    private byte[] content;

    private String status;
    private String address;
    private String protocol;

    private String filename;

    public void data() {
        try {

            DataInputStream in = new DataInputStream(client.getInputStream());
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(in));


            while (true) {
                System.out.println(in.readByte());
            }

        } catch (Exception e) {
            System.out.println("Erro na funcao Data");
        }

    }

    public void response() {

        try {

            OutputStream out = client.getOutputStream();

            //  Ler todo o arquivo e serializar
            content = Files.readAllBytes(file.toPath());

            //  Escreve para o cliente o Header e o body
            String str = headerResponse();  //cabecalho

            out.write(str.getBytes());
            out.write(content);             //envia o arquivo

            out.flush();
            out.close();

        } catch (IOException err) {
            System.err.println("Erro em response");
        }
    }

    public String headerResponse() {

        //  Autorizacao indica o final do cabecalho
        String authorization = "\r\n";

        return this.protocol + " " + this.status + "\r\n"
                + "Location: http://localhost:1333/\r\n"
                + "Server: Server/1.0\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + String.valueOf(content.length) + "\r\n"
                + authorization;

    }

    private void defineFile(String fileName) {

        
        this.address = "src/view/" + fileName;          //  Define o endereco do arquivo
        this.file = new File(address);                  //  Procura o arquivo
        this.status = "200 OK";                         //  Arquivo encontrado | tudo ok

        if (!file.exists()) {                           //  Se o arquivo nao existe
            this.address = "src/" + "Site/erro.html";   //  Localiza pg de erro 
            this.file = new File(address);              //  Identifica o arquivo de erro
            this.status = "404 Not Found";              //  Bad request
            JOptionPane.showMessageDialog(null, fileName + "NÃO EXISTE");
        }
    }
}
