import { usePlaceId } from '@/composables/usePlaceId';
import { useReverseGeocoding } from '@/composables/useReverseGeocoding';
import { ref, watchEffect } from 'vue';
import type { AddressDTO } from '@/lib/api-model';

export function useUnifiedPlaceData() {
  const placeData = ref<AddressDTO | null>(null);

  const { placeData: placeIdData, setPlaceId } = usePlaceId();
  const { placeData: reverseGeoCodeData, setCoords } = useReverseGeocoding();

  watchEffect(() => {
    if (placeIdData.value) {
      placeData.value = placeIdData.value;
    }
  });

  watchEffect(() => {
    if (reverseGeoCodeData.value) {
      placeData.value = reverseGeoCodeData.value;
    }
  });

  return {
    placeData,
    setPlaceId,
    setCoords,
  };
}
