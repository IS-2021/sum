<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { postUsersDeleteFavourites, postUsersIdFavourites } from '@/lib/api/favourites/favourites';
import { useGetUsersMe } from '@/lib/api/users/users';
import { Star } from 'lucide-vue-next';
import { ref, unref, computed } from 'vue';

const props = defineProps<{
  isFavourite: Boolean;
  restaurantId: Uuid;
}>();

const emit = defineEmits<{
  (e: 'favouriteChange', isFavourite: Boolean): void;
}>();

const isFavourite = ref(props.isFavourite);

const toggleFavourite = () => {
  const { data } = useGetUsersMe();
  const user = computed(() => unref(data)?.data);
  isFavourite.value = !isFavourite.value;
  emit('favouriteChange', isFavourite.value);
  if (isFavourite.value === true && user.value) {
    postUsersIdFavourites(user.value.id, { restaurantId: props.restaurantId });
  } else if (isFavourite.value === false && user.value) {
    postUsersDeleteFavourites({ restaurantIds: [props.restaurantId], userId: user.value.id });
  }
};
</script>

<template>
  <Star v-if="isFavourite" fill="white" class="w-7 h-7 cursor-pointer" @click="toggleFavourite" />
  <Star v-else class="w-7 h-7 cursor-pointer" @click="toggleFavourite" />
</template>
