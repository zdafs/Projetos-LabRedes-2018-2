import java.io.*;

class No {
  private int[] distVec;
  private int[] nextHop;
  private boolean[] isConnected;

  public No(int pid){
    distVec = null;
    isConnected = null;
    nextHop = null;

    if(pid==0){
      int[] distVec = {0, 1, 3, 7};
      int [] nextHop = {0, 1, 2, 3};
      boolean[] isConnected = {true, true, true, true};
      this.distVec = distVec;
      this.isConnected = isConnected;
      this.nextHop = nextHop;
    }
    if(pid==1){
      int[] distVec = {1, 0, 1, 999};
      int [] nextHop = {0, 1, 2, -1};
      boolean[] isConnected = {true, true, true, false};
      this.distVec = distVec;
      this.isConnected = isConnected;
      this.nextHop = nextHop;
    }
    if(pid==2){
      int[] distVec = {3, 1, 0, 2};
      int[] nextHop = {0, 1, 2, 3};
      boolean[] isConnected = {true, true, true, true};
      this.distVec = distVec;
      this.isConnected = isConnected;
      this.nextHop = nextHop;
    }
    if(pid==3){
      int[] distVec = {7, 999, 2, 0};
      int[] nextHop = {0, -1, 2, 3};
      boolean[] isConnected = {true, false, true, true};
      this.distVec = distVec;
      this.isConnected = isConnected;
      this.nextHop = nextHop;
    }
  }

  public int[] getVec(){
    return distVec;
  }

  public void setVec(int[] v){
    for(int i=0; i<4; i++){
      distVec[i] = v[i];
    }
  }

  public boolean isNodeConnected(int pid){
    return isConnected[pid];
  }

  public void imprimeVec(){
    System.out.print("[");
    for(int i=0; i<4; i++){
      System.out.print(" "+distVec[i]);
    }
    System.out.println(" ]");
  }

  public void setNextHop(int pid, int i){
    nextHop[i] = pid;
  }

  public int getNextHop(int i){
    return nextHop[i];
  }
}
