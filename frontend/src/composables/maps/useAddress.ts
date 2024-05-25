import { usePlaceId } from '@/composables/maps/usePlaceId';
import { useReverseGeocoding } from '@/composables/maps/useReverseGeocoding';
import { ref, watchEffect } from 'vue';
import type { AddressDTO } from '@/lib/api-model';

export function useAddress() {
  const address = ref<AddressDTO | null>(null);

  const { placeData: placeIdData, setPlaceId } = usePlaceId();
  const { placeData: reverseGeoCodeData, setCoords } = useReverseGeocoding();

  watchEffect(() => {
    if (placeIdData.value) {
      address.value = placeIdData.value;
    }
  });

  watchEffect(() => {
    if (reverseGeoCodeData.value) {
      address.value = reverseGeoCodeData.value;
    }
  });

  return {
    address,
    setPlaceId,
    setCoords,
  };
}
