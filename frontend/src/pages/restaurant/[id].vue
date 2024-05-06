<route lang="yaml">
meta:
  layout: restaurant
</route>

<script setup lang="ts">
import StarItem from '@/components/StarItem.vue';
import MealsByCategory from '@/components/restaurant/MealsList.vue';
import { ThumbsUp } from 'lucide-vue-next';
import { Info } from 'lucide-vue-next';

import { useRoute } from 'vue-router/auto';
import { unref, computed, ref } from 'vue';

import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { postUsersDeleteFavourites, postUsersIdFavourites } from '@/lib/api/favourites/favourites';
import { useUser } from '@/composables/useUser';

const route = useRoute('/restaurant/[id]');
const id = route.params.id;
const { data, isPending: areRestaurantsLoading } = useGetRestaurantsId(id);
const restaurant = computed(() => unref(data)?.data);

const isFavourite = ref(false);
const categories = ref(['Kategoria 1']);
const { user } = useUser();

function postFavourite(fav: Boolean) {
  if (fav === true && user.value && restaurant.value) {
    postUsersIdFavourites(user.value.id, { orderNumber: 0, restaurantId: restaurant.value.id });
  } else if (fav === false && user.value && restaurant.value) {
    postUsersDeleteFavourites({ restaurantIds: [restaurant.value.id], userId: user.value.id });
  }
}
</script>

<template>
  <template v-if="areRestaurantsLoading">
    <p>Loading...</p>
  </template>
  <div v-else-if="restaurant">
    <div class="w-full h-40 mb-12">
      <img src="@/assets/images/restaurant-image-1.jpg" class="w-full h-full object-cover" />
    </div>
    <div class="container">
      <div class="flex flex-row flex-wrap">
        <div class="w-full">
          <div class="flex flex-row mb-4 gap-4 items-center">
            <p class="font-semibold text-3xl">{{ restaurant.name }}</p>
            <StarItem :isFavourite="isFavourite" @favourite-change="postFavourite" />
            <div class="flex-grow" />
            <div
              class="flex items-center justify-center h-10 w-10 rounded-full bg-neutral-900 cursor-pointer"
            >
              <Info class="h-5 w-5" />
            </div>
          </div>
          <div class="flex flex-row gap-1">
            <ThumbsUp class="text-green-500 h-5 w-5" />
            <p class="text-green-500 text-base">97% users recomends this restaurant</p>
          </div>
        </div>
      </div>
      <MealsByCategory :categories="categories" />
    </div>
  </div>
</template>
