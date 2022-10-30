package DSCoinPackage;
import HelperClasses.*;
public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    int n=coinCount;
    int ntb=coinCount/DSObj.bChain.tr_count;
    int id_curr=100000;
    TransactionBlock brr[]=new TransactionBlock[ntb];
    Transaction t[]=new Transaction[coinCount];
    Members mod = new Members();
    mod.UID = "Moderator";

    int num_mem=DSObj.memberlist.length;
    for(int j=0;j<n;j++)
        t[j]=new Transaction();

    int i=0;
    while(i<coinCount)
    {
        if(t[i]==null)
            System.out.println(i);
     t[i].coinID=String.valueOf(id_curr);
     id_curr++;
     t[i].coinsrc_block=null;
     t[i].Source=mod;
     t[i].Destination=DSObj.memberlist[i%num_mem];
     i++;
    }
    int z=0;
    for(int k=0;k<ntb;k++){
        Transaction arr[]=new Transaction[DSObj.bChain.tr_count];
        for(int l=0;l<DSObj.bChain.tr_count;l++) {
            arr[l] = new Transaction();
        }
        for(int l=0;l<DSObj.bChain.tr_count;l++){
            arr[l]=t[z++];
        }
        TransactionBlock b=new TransactionBlock(arr);
        DSObj.bChain.InsertBlock_Honest(b);
        brr[k]=new TransactionBlock(arr);
        brr[k]=b;
    }
    for(int j=0;j<coinCount;j++){
        DSObj.memberlist[j%num_mem].mycoins.add(new Pair<String, TransactionBlock>(t[j].coinID,brr[j/DSObj.bChain.tr_count]));
    }
    int p=100000 + coinCount-1;
    DSObj.latestCoinID = String.valueOf(p);

  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
      int n = coinCount;
      int ntb = coinCount / DSObj.bChain.tr_count;
      int id_curr = 100000;
      TransactionBlock brr[] = new TransactionBlock[ntb];
      Transaction t[] = new Transaction[coinCount];
      Members mod = new Members();
      mod.UID = "Moderator";

      int num_mem = DSObj.memberlist.length;
      for (int j = 0; j < n; j++)
          t[j] = new Transaction();

      int i = 0;
      while (i < coinCount) {
          if (t[i] == null)
              System.out.println(i);
          t[i].coinID = String.valueOf(id_curr);
          id_curr++;
          t[i].coinsrc_block = null;
          t[i].Source = mod;
          t[i].Destination = DSObj.memberlist[i % num_mem];
          i++;
      }
      int z = 0;
      for (int k = 0; k < ntb; k++) {
          Transaction arr[] = new Transaction[DSObj.bChain.tr_count];
          for (int l = 0; l < DSObj.bChain.tr_count; l++) {
              arr[l] = new Transaction();
          }
          for (int l = 0; l < DSObj.bChain.tr_count; l++) {
              arr[l] = t[z++];
          }
          TransactionBlock b = new TransactionBlock(arr);
          DSObj.bChain.InsertBlock_Malicious(b);
          brr[k] = new TransactionBlock(arr);
          brr[k] = b;
      }
      for (int j = 0; j < coinCount; j++) {
          DSObj.memberlist[j % num_mem].mycoins.add(new Pair<String, TransactionBlock>(t[j].coinID, brr[j / DSObj.bChain.tr_count]));
      }
      int p = 100000 + coinCount - 1;
      DSObj.latestCoinID = String.valueOf(p);
  }
}
