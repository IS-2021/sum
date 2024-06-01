<script setup lang="ts">
import { getImageUrl } from '@/lib/assets';
import { formatAddress } from '@/lib/googleMaps';
import type { RestaurantDTO } from '@/lib/api-model';
import { AspectRatio } from '@/components/ui/aspect-ratio';
import RestaurantStatus from '@/components/(manage)/common/RestaurantStatus.vue';
import { cn } from '@/lib/utils';

const props = defineProps<{
  restaurant: RestaurantDTO;
}>();
</script>

<template>
  <RouterLink :to="`/admin/restaurants/${restaurant.id}`">
    <div
      class="hover:shadow group transition-shadow flex gap-3 bg-white rounded text-left border border-neutral-300 px-3 py-3"
    >
      <div class="w-28 sm:w-40 flex-shrink-0">
        <AspectRatio
          :ratio="16 / 9"
          :class="
            cn(
              'overflow-clip rounded-md brightness-90 group-hover:brightness-100 transition-all',
              restaurant.status !== 'Active' && 'grayscale',
            )
          "
        >
          <img
            :src="getImageUrl(restaurant.imageUrl)"
            :alt="restaurant.name"
            class="object-cover w-full h-full"
          />
        </AspectRatio>
      </div>
      <div class="flex flex-col justify-between">
        <div class="space-y-1">
          <p class="font-bold">{{ restaurant.name }}</p>
          <p class="text-sm">{{ formatAddress(restaurant.address) }}</p>
        </div>
        <RestaurantStatus
          :status="restaurant.status"
          :class="
            cn('text-neutral-500 text-sm', {
              'text-yellow-500': restaurant.status === 'Inactive',
            })
          "
        />
      </div>
    </div>
  </RouterLink>
</template>
