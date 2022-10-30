package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public TransactionBlock nextblock;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    trarray = new Transaction[t.length];
    for(int i=0;i<t.length;i++){
      this.trarray[i]=t[i];
    }
    this.previous=null;
    this.nextblock=null;
    Tree = new MerkleTree();
    trsummary=this.Tree.Build(trarray);
    this.dgst=null;
    this.nonce=null;
  }

   public boolean check(Transaction t){
    int flag=0;
    for(int i=0;i<t.coinsrc_block.trarray.length;i++){
      if(t.coinsrc_block.trarray[i].coinID.equals(t.coinID) && t.coinsrc_block.trarray[i].Destination.UID.equals(t.Source.UID)){
        flag=1;
        break;
      }
    }
    if(flag==0)
      return false;
    else
      return true;
  }

  public boolean checkTransaction (Transaction t) {
      int cond1=1;
    int flag = 0;
    int count = 0;
      if(t.coinsrc_block==null)
        return true;

    for (int i = 0; i < t.coinsrc_block.trarray.length; i++)
      if (t.coinsrc_block.trarray[i].coinID.equals(t.coinID) && t.coinsrc_block.trarray[i].Destination.UID.equals(t.Source.UID))
        flag++;

    for(int i = 0;i<this.trarray.length;i++)
      if(this.trarray[i].coinID.equals(t.coinID))
        count++;

    if(this.previous!=null) {
      TransactionBlock curr=this.previous;
      while(curr!=t.coinsrc_block && curr!=null){
        for(int i=0;i<curr.trarray.length;i++) {
          if (curr.trarray[i].coinID.equals(t.coinID)) {
            cond1 = 0;
            break;
          }
        }
        curr=curr.previous;
      }
    }

      if(cond1==1 && count==1 && flag ==1)
        return true;
      else
        return false;
  }
}
