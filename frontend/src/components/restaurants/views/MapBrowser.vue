<script setup lang="ts">
import { getImageUrl } from '@/lib/assets';
import { formatAddressShort } from '@/lib/googleMaps';
import { ref } from 'vue';
import type { RestaurantDTO } from '@/lib/api-model';
import { useUser } from '@/composables/useUser';
import { AspectRatio } from '@/components/ui/aspect-ratio';
import RestaurantsMap from '@/components/maps/restaurants/RestaurantsMap.vue';

const props = defineProps<{
  restaurants: RestaurantDTO[];
}>();

const { user } = useUser();
const pickedRestaurant = ref<RestaurantDTO | null>(null);
</script>

<template class="overflow-hidden">
  <RestaurantsMap
    id="map"
    class="flex-grow"
    v-if="user?.address"
    @onPinClick="(restaurant) => (pickedRestaurant = restaurant)"
    :center-lat="user.address.latitude"
    :center-lng="user.address.longitude"
    :restaurants="restaurants"
  />

  <RouterLink :to="`/restaurant/${pickedRestaurant.id}`" v-if="pickedRestaurant">
    <div
      class="absolute sm:max-w-[500px] sm:mx-auto sm:bottom-10 shadow flex gap-3 bottom-4 mx-3 px-2 py-2 left-2 right-2 rounded bg-white border border-neutral-300"
    >
      <div class="w-28">
        <AspectRatio :ratio="16 / 9" class="overflow-clip rounded-md">
          <img
            :src="getImageUrl(pickedRestaurant.imageUrl)"
            :alt="pickedRestaurant.name"
            class="object-cover w-full h-full"
          />
        </AspectRatio>
      </div>
      <div>
        <p class="font-bold mb-1">{{ pickedRestaurant.name }}</p>
        <p class="text-sm">{{ formatAddressShort(pickedRestaurant.address) }}</p>
      </div>
    </div>
  </RouterLink>
</template>
