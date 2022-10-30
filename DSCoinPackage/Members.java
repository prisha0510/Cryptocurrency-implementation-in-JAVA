package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;
import HelperClasses.*;
public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;
  public int count = 0;

  public TreeNode findnode(TreeNode node, String s){
         if(node.left==null && node.right==null && node.val.equals(s))
             return node;
         else if(node.left==null && node.right!=null)
             return findnode(node.right, s);
         else if(node.left!=null && node.right==null)
             return findnode(node.left, s);
         else if(node.right!=null && node.left!=null){
             TreeNode node1 = findnode(node.right, s);
             TreeNode node2 = findnode(node.left, s);
             if(node1!=null)
                 return node1;
             else if(node2!=null)
                 return node2;
             else
                 return null;
         }
         else
             return null;
     }
     public ArrayList<Pair<String, String>> path(Transaction node,TreeNode rnode, MerkleTree m) {
         ArrayList<Pair<String, String>> a = new ArrayList<Pair<String, String>>();
         TreeNode t = findnode(rnode, m.get_str(node));
         while(t.parent!=null) {
             a.add(new Pair<String, String>(t.parent.left.val, t.parent.right.val));
             t = t.parent;
         }
         a.add(new Pair<String, String>(rnode.val, null));
         return a;
     }


  public void coinsort(List<Pair<String, TransactionBlock>> mc){
      for(int i=0;i<mc.size();i++){
          for(int j=i+1;j<mc.size();j++){
              Pair<String, TransactionBlock> temp=null;
              Pair<String, TransactionBlock> p1=mc.get(i);
              Pair<String, TransactionBlock> p2=mc.get(j);
              String s1=p1.first;
              String s2=p2.first;
              if(s1.compareTo(s2)>0){
                  temp=mc.get(i);
                  mc.add(i,mc.get(j));
                  mc.add(j,temp);
              }
          }
      }
  }

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj){
    coinsort(mycoins);
    Pair<String, TransactionBlock> p=this.mycoins.get(0);
    mycoins.remove(0);
    Transaction tobj=new Transaction();
    tobj.coinID=p.get_first();
    tobj.coinsrc_block=p.get_second();
    tobj.Source=this;
    int i=0;
    while(!(DSobj.memberlist[i].UID.equals(destUID))) {
        i++;
    }
        tobj.Destination=DSobj.memberlist[i];
        Transaction[] a=new Transaction[in_process_trans.length +1];
        a[in_process_trans.length]=new Transaction();
        a[in_process_trans.length]=tobj;
        in_process_trans=a;
        DSobj.pendingTransactions.AddTransactions(tobj);
  }

     public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
         coinsort(mycoins);
         Pair<String, TransactionBlock> p=this.mycoins.get(0);
         mycoins.remove(0);
         Transaction tobj=new Transaction();
         tobj.coinID=p.get_first();
         tobj.coinsrc_block=p.get_second();
         tobj.Source=this;
         int i=0;
         while(!(DSobj.memberlist[i].UID.equals(destUID))) {
             i++;
         }
         tobj.Destination=DSobj.memberlist[i];
         int y=0;
         while(in_process_trans[y]!=null){
             y++;
         }
         in_process_trans[y]=tobj;
         DSobj.pendingTransactions.AddTransactions(tobj);
     }

     public boolean check_uniqueCoinID(Transaction[] arr, Transaction t,int nt){
         int flag=1;
         for(int i=0;i<arr.length;i++){
             if(t.coinID.equals(arr[i].coinID)) {
                 flag=0;
                 break;
             }
         }
         if(flag==1)
             return true;
         else
             return false;
     }

     public Transaction remove_repeated (DSCoin_Honest DSObj, Transaction[] tr, Transaction t,int nt)throws EmptyQueueException{
         try{
             if(!check_uniqueCoinID(tr,t,nt)){
                 return remove_repeated(DSObj, tr ,DSObj.pendingTransactions.RemoveTransaction(), nt);
             }
             else{
                 TransactionBlock tB = t.coinsrc_block;
                 if(!find_in_tb(t, tB)) {
                     return remove_repeated(DSObj, tr ,DSObj.pendingTransactions.RemoveTransaction(), nt);
                 }
                 else {
                     int i = 0;
                     for (TransactionBlock curr = tB.nextblock; curr != null; curr = curr.nextblock)
                         if (find_in_tb(t, curr))
                             return remove_repeated(DSObj, tr ,DSObj.pendingTransactions.RemoveTransaction(), nt);
                     return t;
                 }

             }
         }
         catch(DSCoinPackage.EmptyQueueException e) {
             return null;
         }
     }

  public boolean find_in_tb(Transaction t, TransactionBlock Tb){
        int flag=0;
        for(int i=0;i<Tb.trarray.length;i++) {
            if(Tb.trarray[i].coinID.equals(t.coinID) && Tb.trarray[i].Destination.UID.equals(t.Source.UID)) {
                flag=1;
                break;
            }
        }
        if(flag==1) {
            return true;
        }
        else {
            return false;
        }
  }

  public int num(Transaction[] tr){
      int n=0;
      while(tr[n]!=null){
          if(n==tr.length-1)
              break;
          else
              n++;
      }
      return n;
  }
  public boolean check_in(Transaction[] tr, Transaction t) {
      int flag = 0;
      for (int i = 0; i < tr.length; i++){
          if (tr[i] == t) {
              flag = 1;
              break;
          }
        }
      if(flag==1)
          return true;
      else
          return false;
     }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    List <Pair<String,String>> l1= new ArrayList<Pair<String,String>>();
    List <Pair<String,String>> l2= new ArrayList<Pair<String,String>>();

    TransactionBlock tB = DSObj.bChain.lastBlock;
      while(!check_in(tB.trarray, tobj)) {
          if(tB.previous==null)
              throw new MissingTransactionException();
          else
              tB = tB.previous;
      }
      TreeNode rnode=tB.Tree.rootnode;
      l1=path(tobj,rnode,tB.Tree);


      Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(tobj.coinID, tB);
      //System.out.println("Finalise");
      //System.out.println(tB.dgst);
      //tobj.print();
      /*for(int i = 0;i<tB.trarray.length;i++)
          tB.trarray[i].print();*/
      List<Pair<String, TransactionBlock>> l3 = new ArrayList<Pair<String, TransactionBlock>>();
      int z=0;
      int x=Integer.parseInt(p.get_first());
      for(z= 0; Integer.parseInt(tobj.Destination.mycoins.get(z).get_first())<x;z++) {
          l3.add(tobj.Destination.mycoins.get(z));
      }
      l3.add(p);
      for(; z< tobj.Destination.mycoins.size();z++){
          l3.add(tobj.Destination.mycoins.get(z));
      }
      tobj.Destination.mycoins = l3;
      List<Pair<String, String>> dgst_list = new ArrayList<Pair<String, String>>();
      String prev = "DSCoin";
      if(tB.previous!=null)
          prev = tB.previous.dgst;
      l2.add(new Pair<String, String>(prev, null));
      l2.add(new Pair<String, String>(tB.dgst, prev + "#" + tB.trsummary + "#" + tB.nonce));
      for(tB = tB.nextblock;tB!=null;tB = tB.nextblock)
          l2.add(new Pair<String, String>(tB.dgst, tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce));

      Pair<List<Pair<String, String>>, List<Pair<String, String>>> lf= new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(l1,l2);
      int y=0;
      while(in_process_trans[y]!=null){
          y++;
      }
      Transaction[] a=new Transaction[in_process_trans.length];
      for(int u=0;u<y;u++){
          a[u]=in_process_trans[u];
      }
      in_process_trans=a;
      return lf;
  }

   public TransactionQueue validity_check(TransactionQueue p,Transaction t){
         TransactionQueue q = new TransactionQueue();
         try{
             for(;p.size()>0;) {
                 Transaction temp = p.RemoveTransaction();
                 if(!temp.coinID.equals(t.coinID))
                     q.AddTransactions(temp);
             }
         }
         catch(DSCoinPackage.EmptyQueueException e) {
             return null;
         }
         return q;
     }

  public void MineCoin(DSCoin_Honest DSObj) throws EmptyQueueException {
    int n=DSObj.bChain.tr_count-1;
    Transaction[] arr = new Transaction[n+1];
    for(int i = 0;i<arr.length;i++){
        arr[i] = new Transaction();
    }
      if(DSObj.pendingTransactions.numTransactions==0)
          throw new EmptyQueueException();

      for (int i = 0; i < DSObj.bChain.tr_count - 1; i++) {
              Transaction t = DSObj.pendingTransactions.RemoveTransaction();
              //t.print();
              DSObj.pendingTransactions=validity_check(DSObj.pendingTransactions,t);
              t = remove_repeated(DSObj,arr, t, i);
              //t.print();
              arr[i] = t;
          }

    Transaction miner_reward=new Transaction();
    int num=Integer.parseInt(DSObj.latestCoinID);
    num++;
    DSObj.latestCoinID=String.valueOf(num);
    miner_reward.coinID=DSObj.latestCoinID;
    miner_reward.Source=null;
    miner_reward.coinsrc_block=null;
    miner_reward.Destination=this;
    arr[n]=miner_reward;
    TransactionBlock tB=new TransactionBlock(arr);
    DSObj.bChain.InsertBlock_Honest(tB);
    Pair<String, TransactionBlock> p2=new Pair<String, TransactionBlock>(DSObj.latestCoinID,tB);
    mycoins.add(p2);
  }

     public Transaction remove_repeated2 (DSCoin_Malicious DSObj, Transaction[] tr, Transaction t,int nt)throws EmptyQueueException {
         if (DSObj.pendingTransactions.numTransactions == 0)
             throw new EmptyQueueException();
         else {
             TransactionBlock lb=DSObj.bChain.FindLongestValidChain();
             int flag = 0;
             for (TransactionBlock curr=lb; curr!=null; curr=curr.previous) {
                 if (curr==t.coinsrc_block)
                     flag=1;
             }
             if (flag==1) {
                 if (!check_uniqueCoinID(tr,t,nt))
                     return remove_repeated2(DSObj,tr,DSObj.pendingTransactions.RemoveTransaction(),nt);
                 else {
                     TransactionBlock tB=t.coinsrc_block;
                     if (!find_in_tb(t,tB))
                         return remove_repeated2(DSObj,tr,DSObj.pendingTransactions.RemoveTransaction(),nt);
                     else {
                         for (TransactionBlock current=lb; current!=tB; current=current.previous)
                             if (find_in_tb(t,current))
                                 return remove_repeated2(DSObj,tr,DSObj.pendingTransactions.RemoveTransaction(),nt);
                         return t;
                     }
                 }
             } else
                 return remove_repeated2(DSObj,tr,DSObj.pendingTransactions.RemoveTransaction(),nt);

         }
     }

  public void MineCoin(DSCoin_Malicious DSObj) throws EmptyQueueException {
      int n=DSObj.bChain.tr_count-1;
      int cond1=0;
      int cond2=1;
      int nt=0;
      if(DSObj.pendingTransactions.numTransactions==0)
          throw new EmptyQueueException();
      Transaction[] arr=new Transaction[n+1];
      for(int i = 0;i<arr.length;i++)
          arr[i] = new Transaction();
      for (int i = 0; i <n; i++) {
          Transaction t = DSObj.pendingTransactions.RemoveTransaction();
          t = remove_repeated2(DSObj, arr, t, i);
          arr[i] = t;
      }

      Transaction miner_reward=new Transaction();
      int num=Integer.parseInt(DSObj.latestCoinID);
      num++;
      DSObj.latestCoinID=String.valueOf(num);
      miner_reward.coinID=DSObj.latestCoinID;
      miner_reward.Source=null;
      miner_reward.coinsrc_block=null;
      miner_reward.Destination=this;
      arr[DSObj.bChain.tr_count-1] = miner_reward;
      TransactionBlock tB=new TransactionBlock(arr);
      DSObj.bChain.InsertBlock_Malicious(tB);
      Pair<String, TransactionBlock> p2=new Pair<String, TransactionBlock>(DSObj.latestCoinID,tB);
      mycoins.add(p2);
  }  
}
