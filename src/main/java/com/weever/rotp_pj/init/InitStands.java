package com.weever.rotp_pj.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityBlock;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.StandInstance;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.weever.rotp_pj.RotpPJAddon;
import com.weever.rotp_pj.action.stand.*;
import com.weever.rotp_pj.entity.PJEntity;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpPJAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpPJAddon.MOD_ID);
    
 // ======================================== Pearl Jam ========================================
    public static final RegistryObject<StandEntityAction> INJECT_FOOD = ACTIONS.register("pj_inject",
         () -> new InjectFood(new StandEntityAction.Builder()
                 .partsRequired(StandInstance.StandPart.ARMS)
                 .cooldown(3)
                 .staminaCost(300)
                 .holdToFire(10, false)
                 .shout(InitSounds.PJ_INJECT)
         ));

    public static final RegistryObject<StandEntityAction> INJECT_POISON_FOOD = ACTIONS.register("pj_poison_inject",
            () -> new InjectPoisonFood(new StandEntityAction.Builder()
                    .partsRequired(StandInstance.StandPart.ARMS)
                    .cooldown(6)
                    .staminaCost(500)
                    .holdToFire(20, false)
                    .shout(InitSounds.PJ_INJECT)
                    .shiftVariationOf(INJECT_FOOD)
            ));

    public static final RegistryObject<StandEntityAction> UNJECT_FOOD = ACTIONS.register("pj_unject",
            () -> new UnjectFood(new StandEntityAction.Builder()
                    .partsRequired(StandInstance.StandPart.ARMS)
                    .cooldown(3)
                    .staminaCost(300)
                    .holdToFire(10, false)
                    .shout(InitSounds.PJ_UNJECT)
            ));

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<PJEntity>> STAND_PJ =
            new EntityStandRegistryObject<>("pearl_jam",
                    STANDS, 
                    () -> new EntityStandType.Builder<>()
                    .color(0xfaa84b)
                    .storyPartName(ModStandsInit.PART_4_NAME)
                    .leftClickHotbar(
                            INJECT_FOOD.get()
                    )
                    .rightClickHotbar(
                            UNJECT_FOOD.get()
                    )
                    .defaultStats(StandStats.class, new StandStats.Builder()
                            .power(2)
                            .speed(9)
                            .range(11, 13)
                            .durability(14)
                            .precision(2)
                            .build()
                    )
                    .disableManualControl().disableStandLeap()
                    .build(),
                    InitEntities.ENTITIES,
                    () -> new StandEntityType<>(PJEntity::new, 0.65F, 1.8F)
                            .summonSound(InitSounds.PJ_SUMMON)
                            .unsummonSound(InitSounds.PJ_UNSUMMON))
            .withDefaultStandAttributes();
    
}
