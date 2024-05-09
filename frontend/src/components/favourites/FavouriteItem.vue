<script setup lang="ts">
import StarItem from '@/components/StarItem.vue';
import { ArrowRightIcon, ClockIcon, GripVerticalIcon } from 'lucide-vue-next';
import { Button } from '@/components/ui/button';
import type { RestaurantDTO } from '@/lib/api-model';
import { openingHoursToString } from '@/components/restaurants/openingHours';

const { favourite, handleDeleteFavourite } = defineProps<{
  favourite: RestaurantDTO;
  handleDeleteFavourite: (isFavourite: Boolean, id: string) => void;
}>();

const isCurrentDay = (day: string) => {
  return day === new Date().toLocaleDateString('en-US', { weekday: 'long' }).toLowerCase();
};

const foundCurrentDayHours = Object.entries(favourite.hours).find(([day]) => isCurrentDay(day));
const currentDayHours = foundCurrentDayHours ? foundCurrentDayHours[1] : [];
</script>

<template>
  <div
    class="flex items-center gap-4 px-5 py-3 transition-all duration-200 bg-opacity-50 hover:bg-opacity-35 bg-neutral-700"
  >
    <GripVerticalIcon class="handle cursor-move text-neutral-50" />
    <StarItem
      :is-favourite="true"
      @props-change="(isFavourite: Boolean) => handleDeleteFavourite(isFavourite, favourite.id)"
      class="text-neutral-50 [&>svg]:fill-neutral-50 hover:text-neutral-50 transition-all"
    />

    <RouterLink :to="`/restaurant/${favourite.id}`" class="flex-grow">
      <div class="ml-2 text-neutral-50">
        <p class="text-lg font-semibold mb-2">{{ favourite.name }}</p>

        <p class="flex items-center gap-2">
          <ClockIcon class="w-5 h-5" />
          <span class="leading-5">{{ openingHoursToString(currentDayHours) }}</span>
        </p>
      </div>
    </RouterLink>

    <Button
      as-child
      variant="ghost"
      class="group-hover:opacity-100 opacity-0 transition-all hover:!bg-opacity-40 hidden md:block text-neutral-50 hover:text-neutral-50"
    >
      <RouterLink :to="`/restaurant/${favourite.id}`">
        Open <ArrowRightIcon class="inline-block w-5 h-5 ml-2" />
      </RouterLink>
    </Button>
  </div>
</template>
