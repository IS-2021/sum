<route lang="yaml">
meta:
  layout: empty
</route>

<script setup lang="ts">
import Logo from '@/components/Logo.vue';
import { loader } from '@/lib/googleMaps';
import { onMounted, ref, shallowRef, watchEffect } from 'vue';
import { Button } from '@/components/ui/button';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { LocateFixedIcon } from 'lucide-vue-next';
import { useGeolocation } from '@vueuse/core';
import { usePlaceId } from '@/composables/usePlaceId';

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

const map = shallowRef<google.maps.Map>();
const currentPosMarker = shallowRef<google.maps.marker.AdvancedMarkerElement>();
const mapDiv = ref<HTMLDivElement | null>(null);
const { placeData, setPlaceId } = usePlaceId();

function updateMap({ latitude, longitude }: { latitude: number; longitude: number }) {
  if (map.value) {
    map.value.setCenter({ lat: latitude, lng: longitude });
    map.value.setZoom(15);
  }
  if (currentPosMarker.value) {
    currentPosMarker.value.position = { lat: latitude, lng: longitude };
  }
}

watchEffect(() => {
  if (!placeData.value) {
    return;
  }

  coords.value = {
    latitude: placeData.value.latitude,
    longitude: placeData.value.longitude,
  };
  updateMap(coords.value);
});

onMounted(async () => {
  if (!mapDiv.value) {
    return;
  }

  const { Map } = await loader.importLibrary('maps');
  map.value = new Map(mapDiv.value, {
    center: {
      lat: coords.value.latitude,
      lng: coords.value.longitude,
    },
    zoom: 10,
    mapId: '2ea9a80405b2230f',
    fullscreenControl: false,
    mapTypeControl: false,
    streetViewControl: false,
  });

  const { AdvancedMarkerElement } = await loader.importLibrary('marker');
  currentPosMarker.value = new AdvancedMarkerElement({
    position: {
      lat: coords.value.latitude,
      lng: coords.value.longitude,
    },
    map: map.value,
  });
});

watchEffect(() => {
  if (![coordsReading.value.latitude, coordsReading.value.longitude].every((v) => isFinite(v))) {
    return;
  }

  pauseGeolocation();

  coords.value = {
    latitude: coordsReading.value?.latitude ?? coords.value.latitude,
    longitude: coordsReading.value?.longitude ?? coords.value.longitude,
  };

  updateMap(coords.value);
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
            @on-place-select="setPlaceId"
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
