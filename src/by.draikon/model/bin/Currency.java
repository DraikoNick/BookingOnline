package by.draikon.model.bin;

import by.draikon.model.fabrics.RoundFactory;
import java.util.Scanner;

public class Currency implements Comparable<Currency> {
    private final int value;

    public Currency(){
        this(0);
    }
    public Currency(int value){
        this.value=value;
    }

    public Currency(int rub, int coins){
        this(rub*100+coins);
    }
    public Currency(Currency byn){
        this(byn.value);
    }
    public Currency(Scanner sc){
        value=sc.nextInt();
    }

    public int getRubs(){
        return value-value%100;
    }
    public int getCoins(){
        return value%100;
    }

    public Currency add(Currency byn){        //plus
        Currency result=new Currency(value+byn.value);
        return result;
    }
    public Currency sub(Currency byn){        //minus
        Currency result=new Currency(value-byn.value);
        return result;
    }
    public Currency mul(int multiplier){ //multiply
        Currency result=new Currency(value*multiplier);
        return result;
    }
    public Currency mul(double multiplier, RoundFactory roundMethod, int degree){ //multiply and round result
        Currency result=new Currency(roundMethod.round(value*multiplier,degree));
        return result;
    }
    public Currency round(RoundFactory roundMethod, int degree){
        Currency result=new Currency(roundMethod.round(value,degree));
        return result;
    }

    public String toString(){   // returns a string representation of a financial entity in the format d+.dd
        String result=(value/100)+"."+(value%100/10)+(value%100%10);
        return result;
    }
    @Override
    public int compareTo(Currency that){
        return this.value-that.value;
    }
    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }
        if(obj==null){
            return false;
        }
        if(this.getClass() != obj.getClass()){
            return false;
        }
        Currency that=(Currency)obj;
        return value==that.value;
    }
}
