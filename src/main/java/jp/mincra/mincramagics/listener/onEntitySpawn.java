package jp.mincra.mincramagics.listener;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.container.MincraEntity;
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

                    NBTEntity nbtEntity = new NBTEntity(entity);
                    nbtEntity.mergeCompound(new NBTContainer(MincraMagics.getMobManager().getMobNBT(mcr_id).toString()));

                    StringBuffer buffer = new StringBuffer("mcr_");
                    buffer.append(mcr_id);
                    nbtEntity.getStringList("Tags").add(buffer.toString());

                    MincraEntity mincraEntity = new MincraEntity();
                    mincraEntity.setMcr_id(mcr_id);
                    mincraEntity.setUuid(entity.getUniqueId());

                    MincraMagics.getMobManager().addMincraEntity(mincraEntity);
                    MincraMagics.getEventNotifier().runCustomEntitySpawn(event.getEntity(), mcr_id);

                    break;
                }
            }
        }
    }
}