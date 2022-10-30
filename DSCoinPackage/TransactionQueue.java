package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
	if(firstTransaction==null){
      this.firstTransaction=transaction;
      this.lastTransaction=transaction;
      numTransactions=1;
    }
    else{
      Transaction t=new Transaction();
      t=transaction;
      t.prev=this.lastTransaction;
      this.lastTransaction.next=t;
      this.lastTransaction=t;
      numTransactions=numTransactions+1;
    }

  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    Transaction t=new Transaction();
    if(firstTransaction==null)
    {
      throw new EmptyQueueException();
    }
    else if(numTransactions==1){
      t=this.firstTransaction;
      this.lastTransaction=null;
      this.firstTransaction=null;
      numTransactions=0;
    }
    else{

      t=this.firstTransaction;
      firstTransaction=firstTransaction.next;
      numTransactions-=1;
    }
    return t;
  }

  public int size() {
    return numTransactions;
  }
}
