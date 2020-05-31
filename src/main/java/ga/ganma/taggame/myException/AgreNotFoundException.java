package ga.ganma.taggame.taggame.myException;

public class AgreNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public AgreNotFoundException(){
        super("ゲーム開始しましたが、鬼が一人もいません！");
    }
}
