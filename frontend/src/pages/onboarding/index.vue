<route lang="yaml">
meta:
  layout: onboarding
</route>

<script setup lang="ts">
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { Button } from '@/components/ui/button';
import { LocateFixedIcon } from 'lucide-vue-next';
import { ref } from 'vue';

const userLocation = ref<GeolocationCoordinates | null>(null);

const getUserLocation = () => {
  navigator.geolocation.getCurrentPosition((position) => {
    userLocation.value = position.coords;
  });
};
</script>

<template>
  <div>
    <h1 class="text-2xl font-bold mb-2">Welcome to FoodGood</h1>

    <p class="mb-8 text-neutral-700 max-w-prose">
      Before you continue, you need to set your location. We’ll use it to show restaurants near you.
      You can always change it latter in the settings.
    </p>

    <div class="max-w-screen-sm w-full">
      <p class="mb-2 text-neutral-700">Enter your address:</p>
      <AddressAutocompleteInput
        popover-class="md:w-80 lg:w-full"
        @on-place-select="(placeId) => console.log(placeId)"
      />

      <p class="mt-3 mb-2 text-neutral-700">or use your current location:</p>
      <Button @click="getUserLocation">
        <LocateFixedIcon class="h-4 w-4 mr-2" /> Use my current location
      </Button>
    </div>
  </div>
</template>
