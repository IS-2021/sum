import { useAddress } from '@/composables/maps/useAddress';
import { ref, watch, watchEffect } from 'vue';
import { useGeolocation } from '@vueuse/core';

export function useAddressSettings() {
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

  function handleMapsClick({ latLng }: google.maps.MapMouseEvent) {
    if (!latLng) {
      return;
    }

    coords.value = {
      latitude: latLng.lat(),
      longitude: latLng.lng(),
    };
    setCoords(coords.value);
  }

  return {
    address,
    isGeolocationSupported,
    coords,
    handleMapsClick,
    setPlaceId,
    setCoords,
    resumeGeolocation,
    pauseGeolocation,
  };
}
