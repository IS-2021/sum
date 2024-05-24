<script setup lang="ts">
import Logo from '@/components/Logo.vue';
import { loader } from '@/lib/googleMaps';
import { onMounted, ref, watchEffect } from 'vue';
import { Button } from '@/components/ui/button';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { LocateFixedIcon } from 'lucide-vue-next';
import { useGeolocation } from '@vueuse/core';

const coords = ref({
  latitude: 51.7484822,
  longitude: 19.4499251,
});
const {
  isSupported: isGeolocationSupported,
  coords: coordsReading,
  resume: resumeGeolocation,
} = useGeolocation({
  immediate: false,
});

const mapDiv = ref<HTMLDivElement | null>(null);
onMounted(async () => {
  const { Map } = await loader.importLibrary('maps');
  const map = new Map(mapDiv.value, {
    center: {
      lat: coords.value.latitude,
      lng: coords.value.longitude,
    },
    zoom: 10,
  });
});

watchEffect(() => {
  if (isFinite(coordsReading.value.latitude) && isFinite(coordsReading.value.longitude)) {
    coords.value = {
      latitude: coordsReading.value?.latitude ?? coords.value.latitude,
      longitude: coordsReading.value?.longitude ?? coords.value.longitude,
    };
  }
});
</script>

<template>
  <div class="w-full grid grid-cols-1 md:grid-cols-2 max-h-svh">
    <div class="hidden md:block h-svh" ref="mapDiv" />

    <div class="p-10 self-center">
      <RouterLink to="/">
        <Logo class="mb-8" />
      </RouterLink>

      <div>
        <h1 class="text-2xl font-bold mb-2">Welcome to FoodGood</h1>

        <p class="mb-8 text-neutral-700 max-w-prose">
          Before you continue, you need to set your location. We’ll use it to show restaurants near
          you. You can always change it latter in the settings.
        </p>

        <div class="max-w-screen-sm w-full">
          <p class="mb-2 text-neutral-700">Enter your address:</p>
          <AddressAutocompleteInput
            popover-class="md:w-80 lg:w-full"
            @on-place-select="(placeId) => console.log(placeId)"
          />

          <div v-if="isGeolocationSupported">
            <p class="mt-3 mb-2 text-neutral-700">or use your current location:</p>
            <Button @click="resumeGeolocation">
              <LocateFixedIcon class="h-4 w-4 mr-2" /> Use my current location
            </Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
