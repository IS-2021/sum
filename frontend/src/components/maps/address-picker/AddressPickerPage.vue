<script setup lang="ts">
import { formatAddress } from '@/lib/googleMaps';
import { ref, watch, watchEffect } from 'vue';
import { Button } from '@/components/ui/button';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { LocateFixedIcon, MapPinIcon } from 'lucide-vue-next';
import { useGeolocation } from '@vueuse/core';
import { useAddress } from '@/composables/maps/useAddress';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import type { AddressDTO } from '@/lib/api-model';
import GoogleMaps from '@/components/maps/GoogleMaps.vue';

const coords = ref({
  latitude: 51.7484822,
  longitude: 19.4499251,
});
const {
  isSupported: isGeolocationSupported,
  coords: coordsReading,
  resume: resumeGeolocation,
  pause: pauseGeolocation,
} = useGeolocation({
  immediate: false,
});

const emit = defineEmits<{
  (e: 'save:address', payload: AddressDTO): void;
}>();

const { address, setPlaceId, setCoords } = useAddress();

watchEffect(() => {
  if (!address.value) {
    return;
  }

  coords.value = {
    latitude: address.value.latitude,
    longitude: address.value.longitude,
  };
});

watch(coordsReading, () => {
  if (![coordsReading.value.latitude, coordsReading.value.longitude].every((v) => isFinite(v))) {
    return;
  }

  pauseGeolocation();

  coords.value = {
    latitude: coordsReading.value?.latitude ?? coords.value.latitude,
    longitude: coordsReading.value?.longitude ?? coords.value.longitude,
  };

  setCoords(coords.value);
});

function onSaveClick() {
  if (!address.value) {
    return;
  }

  emit('save:address', address.value);
}
</script>

<template>
  <div class="grid max-h-svh w-full grid-cols-1 md:grid-cols-2">
    <GoogleMaps :latitude="coords.latitude" :longitude="coords.longitude" />

    <div class="grid-rows-5 p-10 md:grid md:h-svh">
      <slot name="page-header">
        <div />
      </slot>

      <div>
        <slot name="content-header" />

        <div>
          <div class="w-full max-w-screen-sm">
            <p class="mb-2 text-neutral-700">Enter your address:</p>
            <AddressAutocompleteInput
              popover-class="md:w-80 lg:w-full lg:max-w-prose"
              @on-place-select="setPlaceId"
            />

            <div v-if="isGeolocationSupported">
              <p class="mb-2 mt-3 text-neutral-700">or use your current location:</p>
              <Button @click="resumeGeolocation" variant="outline">
                <LocateFixedIcon class="mr-2 h-4 w-4" /> Use my current location
              </Button>
            </div>
          </div>

          <div class="mt-8" v-if="address">
            <Alert class="mb-3 bg-secondary/25">
              <MapPinIcon class="h-4 w-4" />
              <AlertTitle>Selected address</AlertTitle>
              <AlertDescription>
                {{ formatAddress(address) }}
              </AlertDescription>
            </Alert>

            <Button @click="onSaveClick"> Save and continue </Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
