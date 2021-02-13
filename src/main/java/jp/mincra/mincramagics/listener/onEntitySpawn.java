package jp.mincra.mincramagics.listener;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTList;
import jp.mincra.mincramagics.MincraMagics;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.json.JSONObject;

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

//                    NBTList<String> tagsList = nbtEntity.getStringList("Tags");
//
//                    JSONObject statusObject = MincraMagics.getMobManager().getMobStatus(mcr_id);
//                    statusObject.put("id", mcr_id);
//                    String status = statusObject.toString();
//
//                    if (status != null) {
//                        tagsList.add(status);
//                    }

                    MincraMagics.getEventNotifier().runCustomEntitySpawn(event.getEntity(), mcr_id);

                    break;
                }
            }
        }
    }
}