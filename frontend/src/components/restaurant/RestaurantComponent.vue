<script setup lang="ts">
import StarItem from '@/components/StarItem.vue';
import MealsList from '@/components/restaurant/MealsList.vue';

import { ThumbsUp } from 'lucide-vue-next';

import { useRoute } from 'vue-router/auto';
import { computed, ref, watchEffect } from 'vue';

import { postUsersDeleteFavourites, postUsersIdFavourites } from '@/lib/api/favourites/favourites';
import { useUserRating } from '@/components/user-rating/useUserRating';
import RequireAuth from '@/components/auth/RequireAuth.vue';
import { getRestaurant } from '@/components/restaurant/restaurant';
import { getMeals } from '@/components/restaurant/restaurant';
import { getImageUrl } from '@/lib/assets';
import InfoPopup from '@/components/restaurant/InfoPopup.vue';
import { useFavourites } from '@/components/favourites/useFavourites';
import type { Uuid } from '@/lib/api-model';

const props = defineProps<{
  userId: Uuid;
}>();

const route = useRoute('/restaurant/[id]');
const id = route.params.id;
const restaurant = getRestaurant(id).restaurant;
const areRestaurantsLoading = getRestaurant(id).areRestaurantsLoading;

const meals = getMeals(id).meals;
const areMealsLoading = getMeals(id).areMealsLoading;

const imgSrc = computed(() => getImageUrl(restaurant.value?.imageUrl));
const imageAltText = `${restaurant.value?.name} restaurant`;

const categories = ref(['Kategoria 1']);

function postFavourite(fav: Boolean) {
  if (fav === true && restaurant.value) {
    postUsersIdFavourites(props.userId, { orderNumber: 0, restaurantId: restaurant.value.id });
  } else if (fav === false && restaurant.value) {
    postUsersDeleteFavourites({ restaurantIds: [restaurant.value.id], userId: props.userId });
  }
}

const { totalRatings, ratingPercentage } = useUserRating(
  restaurant.value?.likesCount,
  restaurant.value?.dislikesCount,
);

const { checkIfFavourite } = useFavourites({
  userId: props.userId,
});

const isFavourite = ref(checkIfFavourite(id));

watchEffect(() => {
  if (checkIfFavourite(id)) {
    isFavourite.value = true;
  } else if (!checkIfFavourite(id)) {
    isFavourite.value = false;
  }
});
</script>

<template>
  <template v-if="areRestaurantsLoading">
    <p>Loading...</p>
  </template>
  <div v-else-if="restaurant && meals">
    <div class="w-full h-40 mb-12">
      <img :src="imgSrc" :alt="imageAltText" class="w-full h-full object-cover" />
    </div>
    <div class="sm:container mx-4">
      <div class="flex flex-row flex-wrap">
        <div class="w-full">
          <div class="flex flex-row mb-4 gap-4 items-center">
            <p class="font-semibold text-3xl">{{ restaurant.name }}</p>
            <RequireAuth>
              <StarItem :isFavourite="isFavourite" @favourite-change="postFavourite" />
            </RequireAuth>

            <div class="flex-grow" />
            <div
              class="flex items-center justify-center min-h-10 min-w-10 rounded-full bg-neutral-200 cursor-pointer"
            >
              <InfoPopup
                :hours="restaurant.hours"
                :address="restaurant.address"
                :phone="restaurant.phoneNumber"
              />
            </div>
          </div>
          <div v-if="totalRatings > 0" class="flex items-center gap-1 text-green-600">
            <ThumbsUp class="h-4 w-4" />
            <p class="text-base">
              {{ ratingPercentage }} ({{ totalRatings }}) users recommends this restaurant
            </p>
          </div>
          <div v-else>
            <p class="text-neutral-500">This restaurant has no ratings yet.</p>
          </div>
        </div>
      </div>
      <MealsList
        :categories="categories"
        :restaurantId="restaurant.id"
        :meals="meals"
        :areMealsLoading="areMealsLoading"
      />
    </div>
  </div>
</template>
