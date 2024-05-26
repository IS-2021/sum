<script setup lang="ts">
import { formatAddress, loader } from '@/lib/googleMaps';
import { onMounted, ref, shallowRef, watch, watchEffect } from 'vue';
import { Button } from '@/components/ui/button';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { LocateFixedIcon, MapPinIcon } from 'lucide-vue-next';
import { useGeolocation } from '@vueuse/core';
import { useAddress } from '@/composables/maps/useAddress';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import type { AddressDTO } from '@/lib/api-model';

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

const map = shallowRef<google.maps.Map>();
const currentPosMarker = shallowRef<google.maps.marker.AdvancedMarkerElement>();
const mapDiv = ref<HTMLDivElement | null>(null);
const { address, setPlaceId, setCoords } = useAddress();

function updateMap({ latitude, longitude }: { latitude: number; longitude: number }) {
  if (map.value) {
    map.value.setZoom(15);
    map.value.panTo({ lat: latitude, lng: longitude });
  }
  if (currentPosMarker.value) {
    currentPosMarker.value.position = { lat: latitude, lng: longitude };
  }
}

watchEffect(() => {
  if (!address.value) {
    return;
  }

  coords.value = {
    latitude: address.value.latitude,
    longitude: address.value.longitude,
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

  map.value.addListener('click', ({ latLng }: google.maps.MapMouseEvent) => {
    if (!latLng) {
      return;
    }

    setCoords({
      latitude: latLng.lat(),
      longitude: latLng.lng(),
    });
  });
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
  updateMap(coords.value);
});

function onSaveClick() {
  if (!address.value) {
    return;
  }

  emit('save:address', address.value);
}
</script>

<template>
  <div class="w-full grid grid-cols-1 md:grid-cols-2 max-h-svh">
    <div class="hidden md:block h-svh border-r border-neutral-300" ref="mapDiv" />

    <div class="p-10 md:grid md:h-svh grid-rows-5">
      <slot name="page-header">
        <div />
      </slot>

      <div>
        <slot name="content-header" />

        <div>
          <div class="max-w-screen-sm w-full">
            <p class="mb-2 text-neutral-700">Enter your address:</p>
            <AddressAutocompleteInput
              popover-class="md:w-80 lg:w-full lg:max-w-prose"
              @on-place-select="setPlaceId"
            />

            <div v-if="isGeolocationSupported">
              <p class="mt-3 mb-2 text-neutral-700">or use your current location:</p>
              <Button @click="resumeGeolocation" variant="outline">
                <LocateFixedIcon class="h-4 w-4 mr-2" /> Use my current location
              </Button>
            </div>
          </div>

          <div class="mt-8" v-if="address">
            <Alert class="bg-secondary/25 mb-3">
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