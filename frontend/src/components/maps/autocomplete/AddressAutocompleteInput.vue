<script setup lang="ts">
import { useDebounceFn } from '@vueuse/core';

import type { AutocompleteDTO } from '@/lib/api-model';
import CitiesCombobox from '@/components/maps/autocomplete/CitiesCombobox.vue';
import { computed, ref } from 'vue';

const cities = ref<AutocompleteDTO[]>([
  { placeId: 'foo', description: 'Warszawa' },
  { placeId: 'bar', description: 'Wrocław' },
]);

function generateCities(prefix: string): AutocompleteDTO[] {
  return [
    { placeId: 'foo', description: `${prefix}Warszawa` },
    { placeId: 'bar', description: `${prefix}Wrocław` },
  ];
}

const handleUpdate = (payload: string) => {
  const newCities = generateCities(payload);
  console.log('updating', newCities);
  cities.value = newCities;
};
const debouncedHandleUpdate = useDebounceFn(handleUpdate, 500);
</script>

<template>
  <CitiesCombobox :completions="cities" @searchChange="debouncedHandleUpdate" />
</template>
