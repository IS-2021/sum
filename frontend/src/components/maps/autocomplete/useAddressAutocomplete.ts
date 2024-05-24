import { computed, nextTick, ref, watchEffect } from 'vue';
import { v4 } from 'uuid';
import { getGeoAutocomplete } from '@/lib/api/geo/geo';
import type { AutocompleteDTO } from '@/lib/api-model';
import { useDebounceFn } from '@vueuse/core';

interface UseAddressAutocompleteProps {
  minCharsToBeginSearch?: number;
  onAddressSelect: (payload: AutocompleteDTO['placeId']) => void;
}

export function useAddressAutocomplete({
  onAddressSelect,
  minCharsToBeginSearch = 2,
}: UseAddressAutocompleteProps) {
  const canBeginSearch = (query: string) => query.length >= minCharsToBeginSearch;

  const completionQuery = ref<string>('');
  const sessionToken = ref<string>(v4());
  const enableQuery = computed(() => canBeginSearch(completionQuery.value));

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
    if (canBeginSearch(payload)) {
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
    minCharsToBeginSearch,
    canBeginSearch,
    handlePlaceSearch: debouncedHandlePlaceSearch,
    handlePlaceSelect,
  };
}
