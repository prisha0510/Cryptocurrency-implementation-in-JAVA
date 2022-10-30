package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

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

  public void InsertBlock_Honest (TransactionBlock newBlock) {
	CRF obj=new CRF(64);
    if(this.lastBlock==null){
      String non=find_nonce(start_string,newBlock.trsummary);
      newBlock.nonce=non;
      newBlock.dgst=obj.Fn(start_string+"#"+newBlock.trsummary+"#"+ newBlock.nonce);
      this.lastBlock=newBlock;
      newBlock.previous=null;
      newBlock.nextblock=null;
    }
    else{
      String non=find_nonce(this.lastBlock.dgst,newBlock.trsummary);
      newBlock.nonce=non;
      newBlock.dgst=obj.Fn(lastBlock.dgst+"#"+newBlock.trsummary+"#"+ newBlock.nonce);
      newBlock.previous=lastBlock;
      lastBlock.nextblock=newBlock;
      lastBlock=newBlock;
      newBlock.nextblock=null;
    }
  }
}
