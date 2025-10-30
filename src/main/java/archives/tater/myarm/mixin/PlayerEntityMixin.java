package archives.tater.myarm.mixin;

import archives.tater.myarm.MyArm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    public abstract Arm getMainArm();

    @Shadow
    public abstract @Nullable ItemEntity dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void dropOffhand(CallbackInfo ci) {
        if (!MyArm.getArm((PlayerEntity) (Object) this).isEmpty()) return;
        var hand = getMainArm() == Arm.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND;
        var stack = getStackInHand(hand);
        if (stack.isEmpty()) return;

        setStackInHand(hand, ItemStack.EMPTY);
        dropItem(stack, true, true);
    }
}
