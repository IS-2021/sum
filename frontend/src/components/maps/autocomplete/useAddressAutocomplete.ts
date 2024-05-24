import { computed, nextTick, ref, watchEffect } from 'vue';
import { v4 } from 'uuid';
import { getGeoAutocomplete } from '@/lib/api/geo/geo';
import type { AutocompleteDTO } from '@/lib/api-model';
import { useDebounceFn } from '@vueuse/core';

interface UseAddressAutocompleteProps {
  onAddressSelect: (payload: AutocompleteDTO['placeId']) => void;
}

export function useAddressAutocomplete({ onAddressSelect }: UseAddressAutocompleteProps) {
  const completionQuery = ref<string>('');
  const sessionToken = ref<string>(v4());
  const enableQuery = computed(() => completionQuery.value.length >= 3);

  const cities = ref<AutocompleteDTO[]>([]);

  watchEffect(async () => {
    if (enableQuery.value) {
      const res = await getGeoAutocomplete({
        query: completionQuery.value,
        sessionToken: sessionToken.value,
      });

      if (res.status === 200) {
        cities.value = res.data;
      }
    }
  });

  const handlePlaceSearch = async (payload: string) => {
    if (payload.length < 2) {
      return;
    }

    completionQuery.value = payload;
    await nextTick();
  };
  const debouncedHandlePlaceSearch = useDebounceFn(handlePlaceSearch, 500);

  const handlePlaceSelect = (payload: AutocompleteDTO['placeId']) => {
    completionQuery.value = '';
    sessionToken.value = v4();
    onAddressSelect(payload);
  };

  return {
    completionQuery,
    cities,
    handlePlaceSearch: debouncedHandlePlaceSearch,
    handlePlaceSelect,
  };
}
