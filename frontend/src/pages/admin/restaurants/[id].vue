<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useRoute } from 'vue-router/auto';
import { computed } from 'vue';
import {
  putAdminRestaurantsId,
  useGetAdminRestaurantsId,
} from '@/lib/api/admin-restaurants/admin-restaurants';
import RestaurantStatus from '@/components/(manage)/common/RestaurantStatus.vue';
import { Button } from '@/components/ui/button';

useHead({
  title: 'Restaurant',
});

const route = useRoute('/admin/restaurants/[id]');
const restaurantId = route.params.id;

const { data, refetch } = useGetAdminRestaurantsId(restaurantId);
const restaurant = computed(() => data?.value?.data);

async function handleRestaurantActivate() {
  const res = await putAdminRestaurantsId(restaurantId);

  if (res.status === 200) {
    await refetch();
  }
}
</script>

<template>
  <div v-if="restaurant" class="max-w-screen-md">
    <h1 class="mb-10 text-2xl font-semibold tracking-tight">{{ restaurant.name }}</h1>

    <div
      v-if="restaurant.status === 'Inactive'"
      class="flex items-center justify-between rounded border border-yellow-500 bg-yellow-50 px-4 py-2"
    >
      <RestaurantStatus :status="restaurant.status" />

      <Button @click="handleRestaurantActivate" class="bg-yellow-500 hover:bg-yellow-500/80">
        Activate
      </Button>
    </div>
  </div>
</template>
