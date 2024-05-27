<script setup lang="ts">
import { onMounted, ref, shallowRef } from 'vue';
import { loader } from '@/lib/googleMaps';
import type { RestaurantDTO } from '@/lib/api-model';

const props = defineProps<{
  centerLat: number;
  centerLng: number;
  restaurants: RestaurantDTO[];
}>();

const map = shallowRef<google.maps.Map>();
const mapDiv = ref<HTMLDivElement | null>(null);

onMounted(async () => {
  const parser = new DOMParser();

  if (!mapDiv.value) {
    return;
  }

  const { Map } = await loader.importLibrary('maps');
  map.value = new Map(mapDiv.value, {
    center: {
      lat: props.centerLat,
      lng: props.centerLng,
    },
    zoom: 14,
    mapId: '2ea9a80405b2230f',
    fullscreenControl: false,
    mapTypeControl: false,
    streetViewControl: false,
  });

  const { AdvancedMarkerElement } = await loader.importLibrary('marker');
  const marker = new AdvancedMarkerElement({
    position: {
      lat: props.centerLat,
      lng: props.centerLng,
    },
    map: map.value,
    title: 'You are here',
  });

  for (const restaurant of props.restaurants) {
    new AdvancedMarkerElement({
      position: {
        lat: restaurant.address.latitude,
        lng: restaurant.address.longitude,
      },
      map: map.value,
      title: restaurant.name,
    });
  }
});
</script>

<template>
  <div class="h-96 w-full" ref="mapDiv" />
</template>
