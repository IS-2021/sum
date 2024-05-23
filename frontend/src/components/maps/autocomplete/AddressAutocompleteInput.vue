<script setup lang="ts">
import { useDebounceFn } from '@vueuse/core';

import type { AutocompleteDTO } from '@/lib/api-model';
import CitiesCombobox from '@/components/maps/autocomplete/CitiesCombobox.vue';
import { computed, nextTick, ref, watchEffect } from 'vue';
import { getGeoAutocomplete } from '@/lib/api/geo/geo';
import { v4 } from 'uuid';

const emit = defineEmits<{
  (e: 'onPlaceSelect', placeId: string): void;
}>();

const completionQuery = ref<string>('');
const sessionToken = ref<string>(v4());
const enableQuery = computed(() => completionQuery.value.length >= 3);

const cities = ref<AutocompleteDTO[]>([]);

watchEffect(async () => {
  if (enableQuery.value) {
    const res = await getGeoAutocomplete({
      query: completionQuery.value,
      sessionToken: sessionToken.value,
    });

    if (res.status === 200) {
      cities.value = res.data;
    }
  }
});

const handlePlaceSearch = async (payload: string) => {
  if (payload.length < 2) {
    return;
  }

  completionQuery.value = payload;
  await nextTick();
};
const debouncedHandlePlaceSearch = useDebounceFn(handlePlaceSearch, 500);

const handlePlaceSelect = (payload: AutocompleteDTO['placeId']) => {
  sessionToken.value = v4();
  emit('onPlaceSelect', payload);
};
</script>

<template>
  <CitiesCombobox
    :completions="cities"
    @searchChange="debouncedHandlePlaceSearch"
    @onSelect="handlePlaceSelect"
  />
</template>
