<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';

import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';

import { formatAddress } from '@/lib/googleMaps';
import { computed, unref } from 'vue';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const { data } = useGetRestaurantsId(props.restaurantId);
const restaurant = computed(() => unref(data)?.data);
</script>

<template>
  <ul v-if="restaurant" class="space-y-2">
    <li>
      <p>Restaurant: {{ restaurant.name }}</p>
    </li>
    <li>
      <p>Address: {{ formatAddress(restaurant.address) }}</p>
    </li>
    <li>
      <p class="mt-8">Contact: {{ restaurant.phoneNumber }}</p>
    </li>
  </ul>
</template>
