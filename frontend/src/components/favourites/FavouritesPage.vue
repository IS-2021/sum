<script setup lang="ts">
import type { RestaurantDTO, RestaurantFavouriteInputDTO, UserDTO } from '@/lib/api-model';
import { putUsersIdFavourites, useGetUsersIdFavourites } from '@/lib/api/favourites/favourites';
import { computed, type ComputedRef, ref, unref, watch, watchEffect } from 'vue';
import { ArrowRightIcon, GripVerticalIcon, StarIcon } from 'lucide-vue-next';
import { getImageUrl } from '@/lib/assets';
import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import { VueDraggable } from 'vue-draggable-plus';

const { user } = defineProps<{ user: UserDTO }>();

const { data, isPending } = useGetUsersIdFavourites(unref(user)?.id, {
  query: {
    enabled: !!unref(user)?.id,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
  },
});

const favourites = ref<RestaurantDTO[]>([]);
const hasAnyFavourites = computed(() => favourites.value.length > 0);

const favouritesOrder: ComputedRef<RestaurantFavouriteInputDTO[]> = computed(() =>
  favourites.value.map((favourite, index) => ({
    restaurantId: favourite.id,
    orderNumber: index,
  })),
);

watch(favouritesOrder, (newFavourites) => {
  putUsersIdFavourites(user.id, newFavourites);
});

watchEffect(() => {
  if (data.value && data.value.data.length > 0) {
    favourites.value = data.value.data;
  }
});
</script>

<template>
  <h1 class="font-bold text-2xl mb-8">Your Favourite Restaurants</h1>

  <div v-if="!hasAnyFavourites">
    <p>
      You don't have any favourite restaurants yet. Click on the
      <StarIcon class="inline-block" /> next to the restaurant to make it appear here.
    </p>
  </div>

  <div v-if="favourites">
    <VueDraggable class="space-y-4" v-model="favourites" handle=".handle" :animation="250">
      <li
        v-for="favourite in favourites"
        :key="favourite.id"
        :style="`--image-url: url('${getImageUrl(favourite.imageUrl)}')`"
        :class="cn('rounded bg-cover group list-none', `bg-[image:var(--image-url)]`)"
      >
        <div
          class="flex items-center gap-4 px-5 py-3 transition-all duration-200 bg-opacity-50 hover:bg-opacity-35 bg-neutral-900"
        >
          <GripVerticalIcon class="handle cursor-move" />
          <Button size="icon" variant="ghost" class="hover:!bg-opacity-40">
            <StarIcon class="fill-neutral-50" />
          </Button>

          <RouterLink :to="`/restaurant/${favourite.id}`" class="flex-grow">
            <div class="ml-2">
              <p class="text-lg font-semibold mb-2">{{ favourite.name }}</p>
              <p class="text-neutral-300">Closed</p>
            </div>
          </RouterLink>

          <Button
            as-child
            variant="ghost"
            class="group-hover:opacity-100 opacity-0 transition-all hover:!bg-opacity-40 hidden md:block"
          >
            <RouterLink :to="`/restaurant/${favourite.id}`">
              Open <ArrowRightIcon class="inline-block w-5 h-5 ml-2" />
            </RouterLink>
          </Button>
        </div>
      </li>
    </VueDraggable>
  </div>
</template>
