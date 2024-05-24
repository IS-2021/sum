import type { Coordinates } from '@/lib/googleMaps';
import { ref, watchEffect } from 'vue';
import { getGeoReverseGeocode } from '@/lib/api/geo/geo';
import type { AddressDTO } from '@/lib/api-model';

export function useReverseGeocoding() {
  const coords = ref<Coordinates | null>(null);
  const placeData = ref<AddressDTO | null>(null);

  function setCoords(c: Coordinates) {
    coords.value = c;
  }

  watchEffect(async () => {
    if (!coords.value) {
      return;
    }

    const res = await getGeoReverseGeocode({
      lat: coords.value.latitude,
      lng: coords.value.longitude,
    });

    if (res.status === 200 && res.data) {
      placeData.value = res.data;
    }
  });

  return {
    placeData,
    coords,
    setCoords,
  };
}
