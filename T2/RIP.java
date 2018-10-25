/*
  Lucca La Fonte Albuquerque Carvalho - 726563
  Vitor Mesquita Fogaça - 726597
*/

import java.io.*;
import java.net.*;
import java.util.*;

class RIP implements Runnable{
  static private No no;
  static private int pid;
  static private int[] distVecLocal;
  static private boolean ended;

  static private int basePort;
  private Socket connectionSocket;

  static private  boolean TESTE=true;

  public RIP(Socket connectionSocket){
    this.connectionSocket = connectionSocket;
  }

  public static void main(String argv[]) throws IOException{
    pid = Integer.parseInt(argv[0]);
    basePort = 7000;
    no = new No(pid);
    distVecLocal = new int[4];
    for(int i=0; i<4; i++)
      distVecLocal[i] = no.getVec()[i];
    ended = false;
    new Thread(enviaMensagem).start();
    new Thread(recebeMensagem).start();
  }

  private static Runnable enviaMensagem = new Runnable() {
    public void run(){
      try{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
          String option = reader.readLine();
          if(option.equals("M")){
            no.setVec(distVecLocal);
            no.imprimeVec();
          }
          else if(option.equals("E")){
            if(ended)
              System.out.println("O vetor não será enviado pois não ocorreram mudanças na ultima rodada.");
            else{
              ended = true;
              Socket clientSocket;
              StringBuilder message = new StringBuilder();
              int[] auxVec = no.getVec();
              message.append(Integer.toString(pid)+'\n'+Integer.toString(auxVec[0])+'\n'+Integer.toString(auxVec[1])+'\n'+
                Integer.toString(auxVec[2])+'\n'+Integer.toString(auxVec[3]));
              for(int i=0; i<4; i++){
                if(basePort+pid!=basePort+i && no.isNodeConnected(i)){
                  clientSocket = new Socket("200.18.100.109", basePort+i);
                  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                  outToServer.writeBytes(message.toString());
                  clientSocket.close();
                }
              }
            }
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
  };

  private static Runnable recebeMensagem = new Runnable() {
    public void run(){
      try{
        ServerSocket welcomeSocket = new ServerSocket(basePort+pid);

        while(true){
          Socket connectionSocket = welcomeSocket.accept();

          RIP aux = new RIP(connectionSocket);

          new Thread(aux).start();
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
  };

  public void run(){
    try{
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

      int rcvPid = Integer.parseInt(inFromClient.readLine());
      int[] rcvDistVec = new int[4];
      for(int i=0; i<4; i++){
        rcvDistVec[i] = Integer.parseInt(inFromClient.readLine());
      }

      newDistVec(rcvPid, rcvDistVec);

    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
    public static synchronized void newDistVec(int rcvPid, int[] rcvDistVec){
      for(int i=0; i<4; i++){
        int minimo = Math.min(no.getVec()[rcvPid]+rcvDistVec[i], distVecLocal[i]);
        if(minimo<distVecLocal[i]){
          System.out.println("Caminho para o nó "+ i +" alterado:");
          System.out.println("Antes: Next Hop->"+no.getNextHop(i)+" || Custo->"+distVecLocal[i]);
          System.out.println("Agora: Next Hop->"+ rcvPid +" || Custo->"+minimo);
          no.setNextHop(rcvPid, i);
          ended = false;
          distVecLocal[i] = minimo;
        }
      }
    }
}
