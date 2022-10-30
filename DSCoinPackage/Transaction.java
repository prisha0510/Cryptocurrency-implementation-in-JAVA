package DSCoinPackage;

public class Transaction {

  public String coinID;
  public Members Source;
  public Members Destination;
  public TransactionBlock coinsrc_block;
  public Transaction next;
  public Transaction prev;

  public void print()
  {
    System.out.println("CoinID = " + coinID);
    if(Source==null)
      System.out.println("Source = null");
    else
      System.out.println("Source = " + Source.UID);
    System.out.println("Destination = " + Destination.UID);
    if(coinsrc_block==null)
      System.out.println("srcblock = null");
    else
      System.out.println("srcblock = " + coinsrc_block.dgst);
  }
}
