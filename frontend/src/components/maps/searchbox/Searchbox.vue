<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { placesApi } from '@/lib/googleMaps';

const placeModel = defineModel('');
const placeRef = ref<HTMLInputElement>(null);
const autocomplete = ref<google.maps.places.Autocomplete>();

function onPlaceChanged() {
  const place = autocomplete.value?.getPlace();

  if (!place) {
    return;
  }

  console.log(place);
  console.log(place.geometry?.location);

  for (const component of place.address_components || []) {
    const componentType = component.types[0];

    console.log(componentType, component);
  }
}

onMounted(() => {
  autocomplete.value = new placesApi.Autocomplete(placeRef.value, {
    componentRestrictions: {
      country: ['pl'],
    },
    fields: ['address_components', 'adr_address', 'geometry.location'],
    types: ['address'],
  });
  placeRef.value.focus();

  autocomplete.value.addListener('place_changed', onPlaceChanged);
});
</script>

<template>
  <input
    v-model="placeModel"
    ref="placeRef"
    id="pac-input"
    class="controls"
    type="text"
    placeholder="Search Box"
  />
  <p>{{ placeModel }}</p>
</template>
