<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useGetRestaurants } from '@/lib/api/restaurants/restaurants';
import { computed, ref, unref } from 'vue';
import RestaurantCard from '@/components/restaurants/RestaurantCard.vue';
import { useUser } from '@/composables/useUser';
import RestaurantsMap from '@/components/maps/restaurants/RestaurantsMap.vue';
import { AspectRatio } from '@/components/ui/aspect-ratio';
import type { RestaurantDTO } from '@/lib/api-model';
import { getImageUrl } from '@/lib/assets';
import { formatAddressShort } from '@/lib/googleMaps';

useHead({
  title: 'Restaurants',
});

const pickedRestaurant = ref<RestaurantDTO | null>(null);

const { user } = useUser();
const { data, isPending: areRestaurantsLoading } = useGetRestaurants();
const restaurants = computed(() => unref(data)?.data);
</script>

<template>
  <template v-if="areRestaurantsLoading">
    <p>Loading...</p>
  </template>
  <template v-else-if="restaurants">
    <div class="flex flex-col w-full">
      <h1 class="font-bold text-2xl mb-8 tracking-tight">
        Restaurants {{ user?.address && `in ${user.address.city}` }}
      </h1>

      <RestaurantsMap
        class="mb-10 flex-grow min-h-screen"
        v-if="user?.address"
        @onPinClick="(restaurant) => (pickedRestaurant = restaurant)"
        :center-lat="user.address.latitude"
        :center-lng="user.address.longitude"
        :restaurants="restaurants"
      />

      <RouterLink :to="`/restaurant/${pickedRestaurant.id}`" v-if="pickedRestaurant">
        <div
          class="absolute flex gap-3 bottom-4 mx-3 px-2 py-2 left-2 right-2 rounded bg-white border border-neutral-300"
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

      <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-4" v-if="restaurants.length > 0">
        <RestaurantCard
          v-for="restaurant in restaurants"
          :key="restaurant.id"
          :id="restaurant.id"
          :image-src="restaurant.imageUrl"
          :name="restaurant.name"
          :hours="restaurant.hours"
          :likes="restaurant.likesCount"
          :dislikes="restaurant.dislikesCount"
        />
      </div>
      <div v-else class="flex items-center gap-10 justify-center mt-10">
        <img src="@/assets/images/undraw_no_data_re_kwbl.svg" class="w-20" />
        <div>
          <p class="text-lg tracking-tight mb-2 font-semibold">
            All restaurant menus in your area are empty
          </p>
          <p>Try changing the location in your profile</p>
        </div>
      </div>
    </div>
  </template>
</template>
