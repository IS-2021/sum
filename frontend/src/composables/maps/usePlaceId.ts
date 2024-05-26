import { ref, watchEffect } from 'vue';
import { getGeoPlaces } from '@/lib/api/geo/geo';
import type { AddressDTO } from '@/lib/api-model';

export function usePlaceId() {
  const placeId = ref<string | null>(null);
  const placeData = ref<AddressDTO | null>(null);

  function setPlaceId(id: string) {
    placeId.value = id;
  }

  watchEffect(async () => {
    if (!placeId.value) {
      return;
    }

    const placeRes = await getGeoPlaces({
      placeId: placeId.value,
    });

    if (placeRes.status === 200 && placeRes.data) {
      placeData.value = placeRes.data;
    }
  });

  return {
    placeId,
    placeData,
    setPlaceId,
  };
}
