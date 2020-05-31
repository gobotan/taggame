package ga.ganma.taggame.taggame.myException;

public class NoTimeSetException extends Exception{

    private static final long serialVersionUID = 1L;

    public NoTimeSetException(){
        super("ゲーム時間を設定していません！");
    }

}
