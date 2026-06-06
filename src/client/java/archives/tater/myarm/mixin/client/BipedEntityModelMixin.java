package archives.tater.myarm.mixin.client;

import archives.tater.myarm.MyArm;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@SuppressWarnings("UnstableApiUsage")
@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {
    @Shadow
    @Final
    public ModelPart leftArm;

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At("TAIL")
    )
    private <T extends LivingEntity> void hideArm(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (MyArm.isDisarmable(livingEntity))
            leftArm.xScale = leftArm.yScale = leftArm.zScale = livingEntity.hasAttached(MyArm.DISARMED) ? 0 : 1;
    }
}
