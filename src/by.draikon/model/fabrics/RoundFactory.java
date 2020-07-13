package by.draikon.model.fabrics;

public enum RoundFactory {
    CEILING{
        protected double roundFunction(double value){
            return Math.ceil(value);
        }//end double roundFunction
    },
    ROUND{
        protected double roundFunction(double value){
            return Math.round(value);
        }//end double roundFunction
    },
    FLOOR{
        final protected double roundFunction(double value){
            return Math.floor(value);
        }//end double roundFunction
    };
    abstract double roundFunction(double value);
    public final int round(double value, int degree){
        int tenPow=pow10(degree);
        return (int)roundFunction(value/tenPow)*tenPow;
    }//end int round(double value, int degree)
    private final static int pow10(int degree){
        int result = 1;
        for (int deg = 0; deg < degree; deg++){
            result *= 10;
        }//end for
        return result;
    }//end int pow10
}//end  enum
