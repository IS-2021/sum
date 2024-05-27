<script setup lang="ts">
import { onMounted, ref, shallowRef } from 'vue';
import { type GoogleMapsMarkerClickEvent, loader } from '@/lib/googleMaps';
import type { RestaurantDTO } from '@/lib/api-model';

const props = defineProps<{
  centerLat: number;
  centerLng: number;
  restaurants: RestaurantDTO[];
}>();

const emit = defineEmits<{
  (e: 'onPinClick', restaurant: RestaurantDTO | null): void;
}>();

const map = shallowRef<google.maps.Map>();
const mapDiv = ref<HTMLDivElement | null>(null);

onMounted(async () => {
  if (!mapDiv.value) {
    return;
  }

  const { Map, InfoWindow } = await loader.importLibrary('maps');
  const { AdvancedMarkerElement, PinElement } = await loader.importLibrary('marker');

  const infoWindow = new InfoWindow();

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

  map.value.addListener('click', () => {
    emit('onPinClick', null);
    infoWindow.close();
  });

  const homePin = new PinElement({
    glyphColor: 'white',
  });
  const homeMarker = new AdvancedMarkerElement({
    position: {
      lat: props.centerLat,
      lng: props.centerLng,
    },
    map: map.value,
    title: 'You are here',
    content: homePin.element,
  });

  for (const restaurant of props.restaurants) {
    const pin = new PinElement({
      scale: 1,
    });

    const restaurantMarker = new AdvancedMarkerElement({
      position: {
        lat: restaurant.address.latitude,
        lng: restaurant.address.longitude,
      },
      map: map.value,
      title: restaurant.name,
      content: pin.element,
      gmpClickable: true,
    });

    restaurantMarker.addListener('click', ({ latLng }: GoogleMapsMarkerClickEvent) => {
      map.value?.panTo(latLng);

      infoWindow.close();
      infoWindow.setContent(restaurant.name);
      infoWindow.open(restaurantMarker.map, restaurantMarker);

      emit('onPinClick', restaurant);
    });
  }
});
</script>

<template>
  <div ref="mapDiv" />
</template>
