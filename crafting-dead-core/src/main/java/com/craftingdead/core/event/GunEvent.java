/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.craftingdead.core.ammoprovider.IAmmoProvider;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.item.AttachmentItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class GunEvent extends Event {

  private final IGun gun;
  private final ItemStack itemStack;

  public GunEvent(IGun gun, ItemStack itemStack) {
    this.gun = gun;
    this.itemStack = itemStack;
  }

  public IGun getGun() {
    return gun;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public static class Living extends GunEvent {

    private final ILiving<?, ?> living;

    public Living(IGun gun, ItemStack itemStack, ILiving<?, ?> living) {
      super(gun, itemStack);

      this.living = living;
    }

    public ILiving<?, ?> getLiving() {
      return living;
    }
  }

  @Cancelable
  public static class TriggerPressed extends Living {

    public TriggerPressed(IGun gun, ItemStack itemStack, ILiving<?, ?> living) {
      super(gun, itemStack, living);
    }
  }

  public static class Initialize extends GunEvent {

    private final Set<AttachmentItem> attachments = new HashSet<>();
    private IAmmoProvider ammoProvider;

    public Initialize(IGun gun, ItemStack itemStack, IAmmoProvider ammoProvider) {
      super(gun, itemStack);
      this.ammoProvider = ammoProvider;
    }

    public void setAmmoProvider(IAmmoProvider ammoProvider) {
      this.ammoProvider = ammoProvider;
    }

    public IAmmoProvider getAmmoProvider() {
      return this.ammoProvider;
    }

    public void addAttachment(AttachmentItem attachment) {
      this.attachments.add(attachment);
    }

    public Set<AttachmentItem> getAttachments() {
      return Collections.unmodifiableSet(this.attachments);
    }
  }



  @Cancelable
  public static class HitBlock extends Living {

    private final Block block;
    private final BlockPos blockPos;
    private final World world;

    public HitBlock(IGun gun, ItemStack itemStack, Block block, BlockPos blockPos, ILiving<?, ?> living, World world) {
      super(gun, itemStack, living);

      this.block = block;
      this.blockPos = blockPos;
      this.world = world;
    }

    public Block getBlock() {
      return block;
    }

    public BlockPos getBlockPos() {
      return blockPos;
    }

    public World getWorld() {
      return world;
    }
  }

  @Cancelable
  public static class HitEntity extends Living {

    private final Entity target;
    private final Vector3d hitPos;
    private float damage;
    private boolean headshot;

    public HitEntity(
            IGun gun,
            ItemStack itemStack,
            ILiving<?, ?> living,
            Entity target,
            float damage,
            Vector3d hitPos,
            boolean headshot
    ) {
      super(gun, itemStack, living);

      this.target = target;
      this.hitPos = hitPos;
      this.damage = damage;
      this.headshot = headshot;
    }

    public Entity getTarget() {
      return target;
    }

    public Vector3d getHitPos() {
      return hitPos;
    }

    public boolean isHeadshot() {
      return headshot;
    }

    public void setHeadshot(boolean headshot) {
      this.headshot = headshot;
    }

    public float getDamage() {
      return damage;
    }

    public void setDamage(float damage) {
      this.damage = damage;
    }
  }

  public static class ReloadFinish extends GunEvent {

    private ItemStack oldMagazineStack;
    private ItemStack newMagazineStack;

    public ReloadFinish(IGun gun, ItemStack itemStack, ItemStack oldMagazineStack,
        ItemStack newMagazineStack) {
      super(gun, itemStack);
      this.oldMagazineStack = oldMagazineStack;
      this.newMagazineStack = newMagazineStack;
    }

    public ItemStack getOldMagazineStack() {
      return this.oldMagazineStack;
    }

    public void setOldMagazineStack(ItemStack oldMagazineStack) {
      this.oldMagazineStack = oldMagazineStack;
    }

    public ItemStack getNewMagazineStack() {
      return this.newMagazineStack;
    }

    public void setNewMagazineStack(ItemStack newMagazineStack) {
      this.newMagazineStack = newMagazineStack;
    }
  }
}
