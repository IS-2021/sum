<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useRoute } from 'vue-router/auto';
import { computed } from 'vue';
import { useGetAdminRestaurantsId } from '@/lib/api/admin-restaurants/admin-restaurants';

useHead({
  title: 'Restaurant',
});

const route = useRoute('/admin/restaurants/[id]');
const restaurantId = route.params.id;

const { data } = useGetAdminRestaurantsId(restaurantId);
const restaurant = computed(() => data?.value?.data);
</script>

<template>
  <div v-if="restaurant">
    <h1 class="text-2xl font-semibold tracking-tight mb-10">{{ restaurant.name }}</h1>
  </div>
</template>
