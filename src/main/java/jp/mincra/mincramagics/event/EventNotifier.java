package jp.mincra.mincramagics.event;

import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventNotifier {

    private Map<Class<? extends Event>, Set<EventMethod>> eventMap = new ConcurrentHashMap<>();

    public void registerEvents(Listener listener) {
        Set<Method> methodSet;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methodSet = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            Collections.addAll(methodSet, publicMethods);
            Collections.addAll(methodSet, privateMethods);

        } catch (NoClassDefFoundError e) {
            ChatUtil.sendConsoleMessage("Failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return;
        }

        for (final Method method : methodSet) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null)
                continue;
            if (method.isBridge() || method.isSynthetic())
                continue;
            if (method.getParameterCount() != 1)
                continue;

            Class<?>[] parameter = method.getParameterTypes();
            Class<? extends Event> eventClass = parameter[0].asSubclass(Event.class);
            EventMethod eventMethod = new EventMethod(method, listener);

            if (!eventMap.containsKey(eventClass)) {
                Set<EventMethod> eventMethodSet = new HashSet<>();
                eventMap.put(eventClass, eventMethodSet);
            }
            eventMap.get(eventClass).add(eventMethod);

        }

    }

    public void fireEvent(Event event) {
        Set<EventMethod> eventMethodSet = eventMap.get(event.getClass());

        for (EventMethod eventMethod : eventMethodSet) {
            try {
                eventMethod.getMethod().invoke(eventMethod.getListener(), event);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


//    private List<PlayerUseMagicRodEvent> playerUseMagicRod = new ArrayList<>();
//    private List<PlayerUseMagicRodToEntityEvent> playerUseMagicRodToEntity = new ArrayList<>();
//    private List<PlayerUseMaterialEvent> playerUseMaterial = new ArrayList<>();
//    private List<CustomEntitySpawnEvent> customEntitySpawn = new ArrayList<>();
//
//    /**
//     * プレイヤーが魔法杖を使ったときのイベント
//     * @param player 使用したプレイヤー
//     * @param mcr_id 使用した杖のmcr_id
//     */
//    public void runPlayerUseMagicRod(Player player, String mcr_id) {
//        if (this.playerUseMagicRod != null) {
//            for (PlayerUseMagicRodEvent playerUseMagicRodEvent : playerUseMagicRod) {
//                playerUseMagicRodEvent.onPlayerUseMagicRod(player, mcr_id);
//            }
//        }
//    }
//
//    /**
//     * プレイヤーがエンティティに魔法杖を使ったときのイベント
//     * @param player 使用したプレイヤー
//     * @param target 実行した対象
//     * @param mcr_id 使用した杖のmcr_id
//     */
//    public void runPlayerUseMagicRodToEntity(Player player, Entity target, String mcr_id) {
//        if (this.playerUseMagicRodToEntity != null) {
//            for (PlayerUseMagicRodToEntityEvent playerUseMagicRodToEntityEvent : playerUseMagicRodToEntity) {
//                playerUseMagicRodToEntityEvent.onPlayerUseMagicRodToEntity(player, target, mcr_id);
//            }
//        }
//    }
//
//    public void runPlayerUseMaterial(Player player, String mcr_id) {
//        if (this.playerUseMaterial != null) {
//            for (PlayerUseMaterialEvent event : playerUseMaterial) {
//                event.onPlayerUseMaterial(player, mcr_id);
//            }
//        }
//    }
//
//    /**
//     * カスタムエンティティがスポーンしたときに実行
//     * @param entity イベント
//     * @param mcr_id スポーンさせるエンティティ名
//     */
//    public void runCustomEntitySpawn(Entity entity, String mcr_id) {
//        if (this.customEntitySpawn != null) {
//            for (CustomEntitySpawnEvent customEntitySpawnEvent : customEntitySpawn) {
//                customEntitySpawnEvent.onCustomEntitySpawn(entity, mcr_id);
//            }
//        }
//    }
//
///*
//    /**
//     * リスナーを追加する。
//     * @param listener リスナーを実装したクラス
//     *
//    public void registerEvents(MincraListener listener) {
//        if (listener instanceof PlayerUseMagicRodEvent) {
//            playerUseMagicRod.add((PlayerUseMagicRodEvent) listener);
//        }
//        if (listener instanceof PlayerUseMagicRodToEntityEvent) {
//            playerUseMagicRodToEntity.add((PlayerUseMagicRodToEntityEvent) listener);
//        }
//        if (listener instanceof PlayerUseMaterialEvent) {
//            playerUseMaterial.add((PlayerUseMaterialEvent) listener);
//        }
//        if (listener instanceof CustomEntitySpawnEvent) {
//            customEntitySpawn.add((CustomEntitySpawnEvent) listener);
//        }
//    }
//
// */
}
