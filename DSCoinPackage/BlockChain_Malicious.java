package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    int cond1=0;
    int cond2=0;
    int cond3=0;
    int cond4=1;
    CRF obj=new CRF(64);
    String s=tB.dgst;
    if(s.charAt(0)=='0' && s.charAt(1)=='0' && s.charAt(2)=='0' && s.charAt(3)=='0')
      cond1=1;
    if(tB.previous==null){
      if(s.equals(obj.Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce)))
        cond2=1;
    }
    else{
      if(s.equals(obj.Fn(tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce)))
        cond2=1;
    }
    String trs=tB.Tree.Build(tB.trarray);
    if(tB.trsummary.equals(trs))
      cond3=1;
    int flag=0;
    for(int i=0;i<tB.trarray.length;i++){
      if(!(tB.checkTransaction(tB.trarray[i]))){
        flag=1;
        break;
      }
    }
    if(flag==1)
      cond4=0;
    if(cond1==1 && cond2==1 && cond3==1 && cond4==1)
      return true;
    else
      return false;
  }
  public String find_nonce(String s,String summary){
    String non="1000000000";
    CRF obj=new CRF(64);
    int flag = 0;
    for(int i0=1;i0<10;i0++)
    {
      for(int i1=0;i1<10;i1++)
      {
        for(int i2=0;i2<10;i2++)
        {
          for(int i3=0;i3<10;i3++)
          {
            for(int i4=0;i4<10;i4++)
            {
              for(int i5=0;i5<10;i5++)
              {
                for(int i6=0;i6<10;i6++)
                {
                  for(int i7=0;i7<10;i7++)
                  {
                    for(int i8=0;i8<10;i8++)
                    {
                      for(int i9=0;i9<10;i9++)
                      {
                        non=String.valueOf(i0)+String.valueOf(i1)+String.valueOf(i2)+String.valueOf(i3)+String.valueOf(i4)+String.valueOf(i5)+String.valueOf(i6)+String.valueOf(i7)+String.valueOf(i8)+String.valueOf(i9);
                        if(!(non.equals(1000000000)))
                        {
                          String s1=obj.Fn(s+"#"+summary+"#"+non);
                          if(s1.charAt(0)=='0' && s1.charAt(1)=='0' && s1.charAt(2)=='0' && s1.charAt(3)=='0')
                          {
                            flag = 1;
                            break;
                          }
                        }
                      }
                      if(flag==1)
                        break;
                    }
                    if(flag==1)
                      break;
                  }
                  if(flag==1)
                    break;
                }
                if(flag==1)
                  break;
              }
              if(flag==1)
                break;
            }
            if(flag==1)
              break;
          }
          if(flag==1)
            break;
        }
        if(flag==1)
          break;
      }
      if(flag==1)
        break;
    }
    return non;
  }
  public boolean present_chain(TransactionBlock endblock, TransactionBlock tB){
    int flag=0;
    while(endblock!=null){
      if(endblock==tB){
        flag=1;
        break;
      }
      endblock=endblock.previous;
    }
    if(flag==1)
      return true;
    else
      return false;
  }
  public int return_length(TransactionBlock[] lblist){
    int len = 0;
    while(lblist[len]!=null){
      len++;
    }
    return len;
  }

  public TransactionBlock FindLongestValidChain (){
    int maxlen=0;
    TransactionBlock long_val=lastBlocksList[0];
    int i=0;
    int l=0;
    while(i<return_length(lastBlocksList)){
      TransactionBlock t1=lastBlocksList[i];
      TransactionBlock t2=lastBlocksList[i];
      l=0;
      while(t1!=null){
        if(checkTransactionBlock(t1))
          l++;
        else{
          t2=t1.previous;
          l=0;
        }
        t1=t1.previous;
      }
      if(l>maxlen)
        long_val=t2;
      i++;
    }
    return long_val;
  }
  public void update(TransactionBlock a, TransactionBlock b){
    int i=0;
    int flag=0;
    while(i<lastBlocksList.length){
      if(lastBlocksList[i]==a){
        flag=1;
        break;
      }
      i++;
    }
    if(flag==1)
      lastBlocksList[i]=b;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    CRF obj=new CRF(64);
    TransactionBlock lastblock = this.FindLongestValidChain();
    String s=null;
    if(lastblock==null) {
      s=find_nonce("DSCoin",newBlock.trsummary);
      newBlock.dgst=obj.Fn("DSCoin"+ "#"+ newBlock.trsummary+ "#" +s);
    }

    else {
      s=find_nonce(lastblock.dgst,newBlock.trsummary);
      newBlock.dgst=obj.Fn(lastblock.dgst+ "#"+ newBlock.trsummary+ "#" +s);
      newBlock.previous=lastblock;
      lastblock.nextblock=newBlock;
    }
    newBlock.nonce=s;
    for(int i = 0;i<lastBlocksList.length;i++) {
      if(lastBlocksList[i]==null) {
        update(lastblock, newBlock);
        break;
      }
      else if(present_chain(lastBlocksList[i].previous, lastblock)){
        update(null, newBlock);
        break;
      }
    }
  }
}
