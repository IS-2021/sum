<script setup lang="ts">
import type { UserDTO } from '@/lib/api-model';
import { useGetUsersIdFavourites } from '@/lib/api/favourites/favourites';
import { computed, unref } from 'vue';
import { GripVerticalIcon, StarIcon, ArrowRightIcon } from 'lucide-vue-next';
import { getImageUrl } from '@/lib/assets';
import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';

const { user } = defineProps<{ user: UserDTO }>();

const { data, isPending } = useGetUsersIdFavourites(unref(user)?.id, {
  query: {
    enabled: !!unref(user)?.id,
  },
});
const favourites = computed(() => data.value?.data);
const hasAnyFavourites = computed(() => (favourites.value ? favourites.value.length > 0 : false));
</script>

<template>
  <h1 class="font-bold text-2xl mb-8">Your Favourite Restaurants</h1>

  <div v-if="!hasAnyFavourites">
    <p>
      You don't have any favourite restaurants yet. Click on the
      <StarIcon class="inline-block" /> next to the restaurant to make it appear here.
    </p>
  </div>

  <div>
    <ul class="space-y-4">
      <li
        v-for="favourite in favourites"
        :key="favourite.id"
        :style="`--image-url: url('${getImageUrl(favourite.imageUrl)}')`"
        :class="cn('rounded bg-cover group', `bg-[image:var(--image-url)]`)"
      >
        <div
          class="flex items-center gap-4 px-5 py-3 transition-all duration-200 bg-opacity-50 hover:bg-opacity-35 bg-neutral-900"
        >
          <GripVerticalIcon class="cursor-move" />
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
    </ul>
  </div>
</template>
