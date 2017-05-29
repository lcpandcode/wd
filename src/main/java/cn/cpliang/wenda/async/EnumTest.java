package cn.cpliang.wenda.async;

/**
 * Created by lcplcp on 2017/5/15.
 */
public class EnumTest {
    public static void main(String [] arg){
        Enum1 enum1 = Enum1.first;
        //for(enum1:w)


    }
}

enum Enum1{
    first(1,2,3),
    second(1,2,3);

    int i;
    int j;
    int k;
    Enum1(int i,int j,int k){
        this.i = i;
        this.j = j;
        this.k = k;
    }
}
