<script setup lang="ts">
import { onMounted, ref, shallowRef, watch } from 'vue';
import { loader } from '@/lib/googleMaps';

interface GMapsComponentProps {
  latitude: number;
  longitude: number;
  zoom?: number;
  panZoom?: number;
}

const props = withDefaults(defineProps<GMapsComponentProps>(), {
  zoom: 10,
  panZoom: 15,
});

const mapDiv = ref<HTMLDivElement | null>(null);
const map = shallowRef<google.maps.Map>();
// const currentPosMarker = shallowRef<google.maps.marker.AdvancedMarkerElement>();

function updateMap() {
  if (map.value) {
    map.value.setZoom(props.panZoom);
    map.value.panTo({ lat: props.latitude, lng: props.longitude });
  }
  // if (currentPosMarker.value) {
  //   currentPosMarker.value.position = { lat: latitude, lng: longitude };
  // }
}

watch(() => [props.latitude, props.longitude, props.zoom], updateMap, { immediate: true });

onMounted(async () => {
  if (!mapDiv.value) {
    return;
  }

  const { Map } = await loader.importLibrary('maps');
  map.value = new Map(mapDiv.value, {
    center: {
      lat: props.latitude,
      lng: props.longitude,
    },
    zoom: props.zoom,
    mapId: '2ea9a80405b2230f',
    fullscreenControl: false,
    mapTypeControl: false,
    streetViewControl: false,
  });
  //
  // const { AdvancedMarkerElement } = await loader.importLibrary('marker');
  // currentPosMarker.value = new AdvancedMarkerElement({
  //   position: {
  //     lat: coords.value.latitude,
  //     lng: coords.value.longitude,
  //   },
  //   map: map.value,
  // });
  //
  // map.value.addListener('click', ({ latLng }: google.maps.MapMouseEvent) => {
  //   if (!latLng) {
  //     return;
  //   }
  //
  //   setCoords({
  //     latitude: latLng.lat(),
  //     longitude: latLng.lng(),
  //   });
  // });
});
</script>

<template>
  <div ref="mapDiv" />
</template>
