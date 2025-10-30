package archives.tater.myarm;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

public class ArmItemFeatureRenderer<T extends PlayerEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    private final ItemRenderer itemRenderer;

    public ArmItemFeatureRenderer(FeatureRendererContext<T, M> context, ItemRenderer itemRenderer) {
        super(context);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        var armItem = MyArm.getArm(entity);
        if (armItem.isEmpty()) return;
        matrices.push();
        getContextModel().setArmAngle(Arm.LEFT, matrices);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        if (armItem.isOf(MyArm.ARM)) {
            matrices.translate(-1f / 16, 4f / 16, 2f / 16);
        } else {
            matrices.translate(0, 3f / 16, -1f / 16);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(15));
        }
        itemRenderer.renderItem(entity, armItem, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, true, matrices, vertexConsumers, entity.getWorld(), light, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();
    }
}
