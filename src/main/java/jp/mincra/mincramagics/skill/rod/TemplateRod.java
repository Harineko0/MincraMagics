package jp.mincra.mincramagics.skill.rod;

import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.event.player.PlayerUseMagicRodEvent;
import org.bukkit.entity.Player;

public class TemplateRod implements PlayerUseMagicRodEvent {

    @Override
    public void onPlayerUseMagicRod(Player player, String mcr_id) {
        if (mcr_id.contains("rod_exp")) {

            switch (Integer.parseInt(mcr_id.substring(mcr_id.length() - 1))) {
                case 1:
                    TemplateOne(player, mcr_id);
                    break;
                    /*
                case 2:
                    TemplateTwo(player, mcr_id);
                    break;

                case 3:
                    TemplateThree(player, mcr_id);
                    */
            }
        }
    }

    public void TemplateOne(Player player, String mcr_id) {

            //メイン

            //装飾

    }
}
