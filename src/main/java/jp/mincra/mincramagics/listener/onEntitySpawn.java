package jp.mincra.mincramagics.listener;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.List;
import java.util.Random;

public class onEntitySpawn implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        //名前のリストから
        List<String> mcrIdList = MincraMagics.getMobManager().getTypeMCRIDList(entity.getType());

        if (mcrIdList != null) {

            Random random = new Random();
            int hash = random.nextInt(MincraMagics.getMobManager().getTypeSum(entity.getType()));

            for (String mcr_id : mcrIdList) {
                //乱数が0以下ならそのときのmcr_idで実行
                hash = hash - MincraMagics.getMobManager().getEntityJsonMap().get(mcr_id).getInt("chance");

                if (hash < 0) {

                    NBTEntity nbtZombie = new NBTEntity(entity);
                    nbtZombie.mergeCompound(new NBTContainer(MincraMagics.getMobManager().getMobNBT(mcr_id).toString()));

                    nbtZombie.getStringList("Tags").add(mcr_id);

                    MincraMagics.getEventNotifier().runCustomEntitySpawn(event, mcr_id);

                    break;
                }
            }
        }
    }
}
