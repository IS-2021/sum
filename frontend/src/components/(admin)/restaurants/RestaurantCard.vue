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
      :class="
        cn(
          'group flex gap-3 rounded border border-neutral-300 bg-white px-3 py-3 text-left transition-shadow hover:shadow',
          {
            'border-yellow-500 bg-yellow-50': restaurant.status === 'Inactive',
            'border-red-500 bg-red-50': restaurant.status === 'Banned',
          },
        )
      "
    >
      <div class="w-28 flex-shrink-0 sm:w-40">
        <AspectRatio
          :ratio="16 / 9"
          :class="
            cn(
              'overflow-clip rounded-md brightness-90 transition-all group-hover:brightness-100',
              restaurant.status !== 'Active' && 'grayscale',
            )
          "
        >
          <img
            :src="getImageUrl(restaurant.imageUrl)"
            :alt="restaurant.name"
            class="h-full w-full object-cover"
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
            cn('text-sm text-neutral-500', {
              'text-yellow-500': restaurant.status === 'Inactive',
            })
          "
        />
      </div>
    </div>
  </RouterLink>
</template>
