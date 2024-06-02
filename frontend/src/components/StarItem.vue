<script setup lang="ts">
import { Star } from 'lucide-vue-next';
import { Button } from './ui/button';
import { useFavourites } from '@/components/favourites/useFavourites';
import type { Uuid } from '@/lib/api-model';
import { ref, watchEffect } from 'vue';

const props = defineProps<{
  userId: Uuid;
  restaurantId: Uuid;
}>();

const emit = defineEmits<{
  (e: 'favouriteChange', isFavourite: Boolean): void;
}>();

const { checkIfFavourite } = useFavourites({
  userId: props.userId,
});

let isFavourite = ref(false);

watchEffect(() => {
  if (checkIfFavourite(props.restaurantId)) {
    isFavourite.value = true;
  } else if (!checkIfFavourite(props.restaurantId)) {
    isFavourite.value = false;
  }
});

const toggleFavourite = () => {
  isFavourite.value = !isFavourite.value;
  emit('favouriteChange', isFavourite.value);
};
</script>

<template>
  <Button v-if="isFavourite" size="icon" variant="ghost" class="hover:!bg-opacity-40">
    <Star class="w-7 h-7 cursor-pointer fill-primary" @click="toggleFavourite" />
  </Button>
  <Button v-else size="icon" variant="ghost" class="hover:!bg-opacity-40">
    <Star class="w-7 h-7 cursor-pointer" @click="toggleFavourite" />
  </Button>
</template>
