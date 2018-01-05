package servidorjogo;

import java.net.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.util.*;
import static servidorjogo.Heartbeat.TIMEOUT;



class Heartbeat extends Thread {

    
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos
    
     InetAddress serverAddr = null;
        int serverPort = -1;
        DatagramSocket socket = null;
        DatagramPacket packet = null;

        
    public Heartbeat(InetAddress serverAddr, int serverPort) {
        this.serverAddr = serverAddr; 
        this.serverPort = serverPort;  
    }

  @Override
  public void run() {
  
      
      
      while(true) {
      try{
      sleep(1000);
      
      
      
       try{

            
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT*1000);
            
            packet = new DatagramPacket(TIME_REQUEST.getBytes(), TIME_REQUEST.length(), serverAddr,
                    serverPort); 
            socket.send(packet);
            
            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);
            
            System.out.println("Hora indicada pelo servidor: " + new String(packet.getData(), 0, packet.getLength()));
            
            //******************************************************************
            //Exemplo de como retirar os valores da mensagem
            try{
                StringTokenizer tokens = new StringTokenizer(new String(packet.getData(), 0, packet.getData().length)," :");
                        
                int hour = Integer.parseInt(tokens.nextToken().trim());
                int minute = Integer.parseInt(tokens.nextToken().trim());
                int second = Integer.parseInt(tokens.nextToken().trim());
            
                System.out.println("Horas: " + hour + " ; Minutos: " + minute + " ; Segundos: " + second);
            }catch(NumberFormatException e){}
           
            //******************************************************************
            
        }catch(UnknownHostException e){
             System.out.println("Destino desconhecido:\n\t"+e);
        }catch(NumberFormatException e){
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        }catch(SocketTimeoutException e){
            System.out.println("Nao foi recebida qualquer resposta:\n\t"+e);
        }catch(SocketException e){
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }finally{
            if(socket != null){
                socket.close();
            }
        }
      
      
      
  
  } catch (Exception e) {
      
  }}
 
 
}

  }

 class Jogo extends Thread{

    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    InetAddress Player1Addr = null;
    InetAddress Player2Addr = null;
    int Player1Port = -1;
    int Player2Port = -1;
    
    Socket SocketServidorJogo = null;
    

    public Jogo(InetAddress player1Addr, int player1Port, InetAddress player2Addr, int player2Port, Socket socketServidorJogo)  {

        this.Player1Addr = player1Addr;
        this.Player1Port = player1Port;
        this.Player2Addr = player2Addr;
        this.Player2Port = player2Port;
        this.SocketServidorJogo = socketServidorJogo;
    }

    @Override
    public void run() {

        //Lança dois ComunicaJogoCliente com portos e addr e fica à espera de respostas
        
        
    }
}

class ComunicaJogoCliente extends Thread{
    
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    InetAddress PlayerAddr = null;
    int PlayerPort = -1;
    Socket JogoSocket = null;

    public ComunicaJogoCliente(InetAddress playerAddr, int playerPort,Socket jogoSocket){
        this.PlayerAddr = playerAddr;
        this.PlayerPort = playerPort;
        this.JogoSocket = jogoSocket;
    }
    
    @Override
    public void run(){
        Socket socketPlayer= null;
        BufferedReader in= null;
        PrintWriter out = null;
        String response;

        try {
            try {
                socketPlayer= new Socket(this.PlayerAddr, this.PlayerPort);
              

                /*socketPlayer1.setSoTimeout(TIMEOUT*1000);
                     socketPlayer2.setSoTimeout(TIMEOUT*1000); //talvez nao faça sentido*/
                in= new BufferedReader(new InputStreamReader(socketPlayer.getInputStream()));
                out = new PrintWriter(socketPlayer.getOutputStream(), true);

                String mensagem = "TIME";

                while (!mensagem.equalsIgnoreCase("fim")) {

                    out.println(mensagem);
                    out.flush();
                    
                    response = in.readLine();
                
                    if (response == null ) {
                        System.out.println("O servidor nao enviou qualquer respota antes de"
                                + " fechar a ligacao TCP!");
                    } else {

                        System.out.println("Hora indicada pelo servidor para o Player1: " + response);
                    }
                }
                //******************************************************************
            } catch (UnknownHostException e) {
                System.out.println("Destino desconhecido:\n\t" + e);
            } catch (NumberFormatException e) {
                System.out.println("O porto do servidor deve ser um inteiro positivo.");
            } catch (SocketTimeoutException e) {
                System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
            } catch (SocketException e) {
                System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
            } catch (IOException e) {
                System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
            } finally {
                if (socketPlayer != null) {
                    socketPlayer.close();
                }
            }
        } catch (Exception e) {
        }
    }
}
        
        
public class ServidorJogo {

   

    public static void main(String[] args) 
    {
        
        InetAddress serverAddr  = null;
        int serverPort = -1;
    
        try{
            serverAddr = InetAddress.getByName(args[0]);
            serverPort =  Integer.parseInt(args[1]);   
        } catch(UnknownHostException e){
             System.out.println("Destino desconhecido:\n\t"+e);
        }
            
            
        if(args.length != 2){
            System.out.println("Sintaxe: java UdpTimeClient serverAddress serverUdpPort");
            return;
        }

       Heartbeat hb = new Heartbeat(serverAddr, serverPort);
       hb.run();
       
       //Vai à base de dados buscar os dados do cliente
       
       //Inicia Jogo
        
   }
  
}
