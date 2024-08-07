package it.hurts.weever.rotp_pj;

import com.github.standobyte.jojo.init.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = RotpPJAddon.MOD_ID)
public class GameplayUtil {
    private static int Duration = 250;
    private static final Map<PlayerEntity, Boolean> passiveUsers = new HashMap<>();
    public static Map<PlayerEntity, Boolean> getPassiveUsers() {
        return passiveUsers;
    }
    private static final Map<PlayerEntity, Boolean> eatUsers = new HashMap<>();
    private static final Set<PlayerEntity> handUsers = new HashSet<>();
    public static Set<PlayerEntity> getMainHandOrNot() {
        return handUsers;
    }
    private static final Set<PlayerEntity> secretFood = new HashSet<>();
    public static Set<PlayerEntity> getSecretFoodOrNot() {
        return secretFood;
    }

//    @SubscribeEvent
//    public static void onCraftEvent(PlayerEvent.ItemCraftedEvent event) {
//        PlayerEntity player = event.getPlayer();
//        if (passiveUsers.getOrDefault(player, false)) {
//            ItemStack itemStack = event.getCrafting();
//            if (itemStack.isEdible()) {
//                itemStack.getOrCreateTag().putBoolean("injected", true);
//                itemStack.getOrCreateTag().putBoolean("crafted", true);
//            }
//        }
//    } // TODO: Rewrite/Fix

//    @SubscribeEvent
//    public static void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
//        Entity entity = event.getEntity();
//        System.out.println(entity.getName().getString());
//        if (entity instanceof ItemEntity) {
//            System.out.println("AAAAA");
//            if (!entity.getTags().isEmpty()) {
//                System.out.println(entity.getTags());
//                if (entity.getTags().contains("injected")) {
//                    entity.level.addParticle(ModParticles.HAMON_SPARK_YELLOW.get(),
//                            entity.getX(), entity.getY() + 0.5, entity.getZ(),
//                            0, 0, 0);
//                } else if (entity.getTags().contains("injected")) {
//                    entity.level.addParticle(ModParticles.HAMON_SPARK_BLUE.get(),
//                            entity.getX(), entity.getY() + 0.5, entity.getZ(),
//                            0, 0, 0);
//                }
//            }
//        }
//    } // TODO: understand how to add particle to item entities what have "injected" or "poisoned" in tags


    @SubscribeEvent
    public static void onEatingEvent(LivingEntityUseItemEvent.Tick event) {
        LivingEntity entity = event.getEntityLiving();
        ItemStack item = event.getItem();
        if (item.isEdible() && item.hasTag()) {
            CompoundNBT tags = item.getTag();
            Vector3d pos = entity.position();
            if (tags.getBoolean("injected")) {
                entity.level.addParticle(ModParticles.HAMON_AURA_RED.get(), pos.x, pos.y + 1.8, pos.z, 0, 0, 0);
            } else if (tags.getBoolean("poisoned")) {
                entity.level.addParticle(ModParticles.HAMON_AURA_BLUE.get(), pos.x, pos.y + 1.8, pos.z, 0, 0, 0);
            }
        }
    }

    @SubscribeEvent
    public static void onEatEvent(LivingEntityUseItemEvent.Finish event) {
        ItemStack item = event.getItem();
        LivingEntity entity = event.getEntityLiving();
        Random random = new Random();
        boolean randomBoolean = random.nextBoolean();
        if (item.isEdible() && item.hasTag()) {
            CompoundNBT tags = item.getTag();
            if (eatUsers.get(entity) == null) {
                eatUsers.put((PlayerEntity) entity, true);
            } else {
                eatUsers.remove(entity);
                return;
            }
            if (tags.getBoolean("injected")) {
                addBuffs(entity, randomBoolean);
            } else if (tags.getBoolean("poisoned")) {
                addDeBuffs(entity, randomBoolean);
            }
        }
    }

    private static void addBuffs(LivingEntity entity, boolean isPowerful) {
        int amplifier = isPowerful ? 3 : 0;
        int randomEffectIndex = new Random().nextInt(11);

        switch (randomEffectIndex) {
            case 0:
                entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Duration, amplifier, false, false, true));
                break;
            case 1:
                entity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, Duration, amplifier, false, false, true));
                break;
            case 2:
                entity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Duration, amplifier, false, false, true));
                break;
            case 3:
                entity.addEffect(new EffectInstance(Effects.JUMP, Duration, amplifier, false, false, true));
                break;
            case 4:
                entity.addEffect(new EffectInstance(Effects.NIGHT_VISION, Duration, amplifier, false, false, true));
                break;
            case 5:
                entity.addEffect(new EffectInstance(Effects.HEALTH_BOOST, Duration, amplifier-2, false, false, true));
                break;
            case 6:
                entity.addEffect(new EffectInstance(Effects.REGENERATION, Duration, amplifier, false, false, true));
                break;
            case 7:
                entity.addEffect(new EffectInstance(Effects.SLOW_FALLING, Duration, amplifier, false, false, true));
                break;
            case 8:
                entity.addEffect(new EffectInstance(Effects.DIG_SPEED, Duration, amplifier, false, false, true));
                break;
            case 9:
                entity.addEffect(new EffectInstance(Effects.DOLPHINS_GRACE, Duration, amplifier, false, false, true));
                break;
            case 10:
                entity.addEffect(new EffectInstance(Effects.WATER_BREATHING, Duration, amplifier, false, false, true));
                break;
        }
    }

    private static void addDeBuffs(LivingEntity entity, boolean isPowerful) {
        int amplifier = isPowerful ? 3 : 0;
        Random random = new Random();
        int randomDebuffIndex = random.nextInt(9);

        switch (randomDebuffIndex) {
            case 0:
                entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, Duration, amplifier, false, false, true));
                break;
            case 1:
                entity.addEffect(new EffectInstance(Effects.BLINDNESS, Duration, amplifier, false, false, true));
                break;
            case 2:
                entity.addEffect(new EffectInstance(Effects.WEAKNESS, Duration, amplifier, false, false, true));
                break;
            case 3:
                entity.addEffect(new EffectInstance(Effects.HUNGER, Duration, amplifier, false, false, true));
                break;
            case 4:
                entity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, Duration, amplifier, false, false, true));
                break;
            case 5:
                entity.addEffect(new EffectInstance(Effects.DIG_SPEED, Duration, amplifier, false, false, true));
                break;
            case 6:
                entity.addEffect(new EffectInstance(Effects.POISON, Duration, amplifier, false, false, true));
                break;
            case 7:
                entity.addEffect(new EffectInstance(Effects.UNLUCK, Duration, amplifier, false, false, true));
                break;
            case 8:
                entity.addEffect(new EffectInstance(Effects.WITHER, Duration, amplifier, false, false, true));
                break;
        }
    }

    public static ItemStack getFoodItem(@NotNull LivingEntity user) {
        ItemStack foodItemStack = ItemStack.EMPTY;
        ItemStack handItem;
        if (handUsers.contains(user)) {
            handItem = user.getMainHandItem();
        } else {
            handItem = user.getOffhandItem();
        }
        Item item = handItem.getItem();
        if (item.isEdible() || item.getUseAnimation(handItem) == UseAction.EAT) {
            foodItemStack = handItem;
        }
        return foodItemStack;
    }
}
