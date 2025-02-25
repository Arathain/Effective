package ladysnake.effective.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sammy.ortus.systems.rendering.particle.ParticleTextureSheets;
import ladysnake.effective.client.EffectiveConfig;
import ladysnake.effective.client.particle.types.ColoredParticleType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class AllayTwinkleParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	public AllayTwinkleParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
		super(world, x, y, z, 0, 0, 0);

		this.spriteProvider = spriteProvider;
		this.setSpriteForAge(spriteProvider);

		this.maxAge = 15;

//        this.scale = 0.06f;
	}

	public ParticleTextureSheet getType() {
		return EffectiveConfig.additiveAllayTwinkle ? ParticleTextureSheets.ADDITIVE : ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.markDead();
		}

		this.velocityX = 0f;
		this.velocityY = 0.05f;
		this.velocityZ = 0f;

		this.move(velocityX, velocityY, velocityZ);
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		this.setSpriteForAge(spriteProvider);

		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternion quaternion2;
		if (this.angle == 0.0F) {
			quaternion2 = camera.getRotation();
		} else {
			quaternion2 = new Quaternion(camera.getRotation());
			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion2.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
		}

		Vec3f Vec3f = new Vec3f(-1.0F, -1.0F, 0.0F);
		Vec3f.rotate(quaternion2);
		Vec3f[] Vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
		float j = this.getSize(tickDelta);

		for (int k = 0; k < 4; ++k) {
			Vec3f Vec3f2 = Vec3fs[k];
			Vec3f2.rotate(quaternion2);
			Vec3f2.scale(j);
			Vec3f2.add(f, g, h);
		}

		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();

		int l = 15728880;

		vertexConsumer.vertex(Vec3fs[0].getX(), Vec3fs[0].getY(), Vec3fs[0].getZ()).uv(maxU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vec3fs[1].getX(), Vec3fs[1].getY(), Vec3fs[1].getZ()).uv(maxU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vec3fs[2].getX(), Vec3fs[2].getY(), Vec3fs[2].getZ()).uv(minU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vec3fs[3].getX(), Vec3fs[3].getY(), Vec3fs[3].getZ()).uv(minU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
	}

	@Environment(EnvType.CLIENT)
	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			AllayTwinkleParticle instance = new AllayTwinkleParticle(world, x, y, z, spriteProvider);
			if (parameters instanceof ColoredParticleType allayParticleParameters && allayParticleParameters.initialData != null) {
				instance.colorRed = (float) (allayParticleParameters.initialData.color >> 16 & 0xFF) / 255.0f;
				instance.colorGreen = (float) (allayParticleParameters.initialData.color >> 8 & 0xFF) / 255.0f;
				instance.colorBlue = (float) (allayParticleParameters.initialData.color & 0xFF) / 255.0f;
			}
			return instance;
		}
	}
}
