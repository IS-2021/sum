<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';

import { getRestaurantsId } from '@/lib/api/restaurants/restaurants';

import { formatAddress } from '@/lib/googleMaps';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const restaurant = (await getRestaurantsId(props.restaurantId)).data;
</script>

<template>
  <li>
    <p>Restaurant: {{ restaurant.name }}</p>
  </li>
  <li>
    <p>Address: {{ formatAddress(restaurant.address) }}</p>
  </li>
  <li>
    <p class="mt-8">Contact: {{ restaurant.phoneNumber }}</p>
  </li>
</template>
