package ga.ganma.taggame.taggame.command.subcommands;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.command.CommandAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Taghelp implements CommandAPI {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        Player p = (Player) sender;
        if(p.isOp()){
            p.sendMessage(Taggame.prefix + "/tag admin set [mcid]");
            p.sendMessage(Taggame.prefix + "指定のプレイヤーを運営に追加します。");
            p.sendMessage(Taggame.prefix + "/tag admin remove [mcid]");
            p.sendMessage(Taggame.prefix + "指定のプレイヤーを運営から逃げへ変更します。");
            p.sendMessage(Taggame.prefix + "/tag end");
            p.sendMessage(Taggame.prefix + "ゲームを強制終了します。");
            p.sendMessage(Taggame.prefix + "/tag help");
            p.sendMessage(Taggame.prefix + "このヘルプを表示します。");
            p.sendMessage(Taggame.prefix + "/tag ogre wp");
            p.sendMessage(Taggame.prefix + "エメラルドブロックの上に乗っているプレイヤーを鬼にします。");
            p.sendMessage(Taggame.prefix + "エメラルドブロックに乗っているプレイヤーはランダムで決められます。");
            p.sendMessage(Taggame.prefix + "/tag ogre set [mcid]");
            p.sendMessage(Taggame.prefix + "指定のプレイヤーを鬼にします。");
            p.sendMessage(Taggame.prefix + "/tag ogre remove [mcid]");
            p.sendMessage(Taggame.prefix + "指定のプレイヤーを鬼から逃げへ変更します。");
            p.sendMessage(Taggame.prefix + "/tag runaway set [mcid]");
            p.sendMessage(Taggame.prefix + "指定のプレイヤーを逃げに指定します。鯖に入った時点で通常逃走者になっています。");
            p.sendMessage(Taggame.prefix + "/tag start");
            p.sendMessage(Taggame.prefix + "鬼ごっこをスタートします。鬼、逃げ、時間を決めていることを確認してください。");
            p.sendMessage(Taggame.prefix + "/tag time set [秒数]");
            p.sendMessage(Taggame.prefix + "鬼ごっこの時間を指定します。");
            p.sendMessage(Taggame.prefix + "/tag setspawn");
            p.sendMessage(Taggame.prefix + "スポーン地点を登録します。");
            p.sendMessage(Taggame.prefix + "/tag setlobby");
            p.sendMessage(Taggame.prefix + "ロビー地点を登録します。");
        }
        else {
            p.sendMessage(Taggame.prefix + "/tag help");
            p.sendMessage(Taggame.prefix + "このヘルプを表示します。");
        }
        return false;
    }
}
