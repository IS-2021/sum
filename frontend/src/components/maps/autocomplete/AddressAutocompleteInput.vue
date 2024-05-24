<script setup lang="ts">
import CitiesCombobox from '@/components/maps/autocomplete/CitiesCombobox.vue';
import { useAddressAutocomplete } from '@/components/maps/autocomplete/useAddressAutocomplete';

const emit = defineEmits<{
  (e: 'onPlaceSelect', placeId: string): void;
}>();

const { cities, handlePlaceSearch, handlePlaceSelect } = useAddressAutocomplete({
  onAddressSelect: (placeId: string) => {
    emit('onPlaceSelect', placeId);
  },
});
</script>

<template>
  <CitiesCombobox
    :completions="cities"
    @searchChange="handlePlaceSearch"
    @onSelect="handlePlaceSelect"
  />
</template>
