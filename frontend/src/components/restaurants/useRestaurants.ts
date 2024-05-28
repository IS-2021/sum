import type { AddressDTO, RestaurantDTO } from '@/lib/api-model';
import { type ComputedRef, type ModelRef, type Ref, ref, watch, watchEffect } from 'vue';
import { useUser } from '@/composables/useUser';
import { getRestaurants } from '@/lib/api/restaurants/restaurants';
import { formatAddress } from '@/lib/googleMaps';

type UseRestaurantsProps = {
  radius: Ref<number> | ComputedRef<number> | ModelRef<number>;
  overrideAddress: Ref<AddressDTO | null>;
};

export function useRestaurants({ radius, overrideAddress }: UseRestaurantsProps) {
  const { user } = useUser();
  const restaurants = ref<RestaurantDTO[]>([]);

  watchEffect(async () => {
    if (!user.value || !user.value.address) {
      return;
    }

    const { address: userAddress } = user.value;
    const address = overrideAddress.value ?? userAddress;

    console.log('address', formatAddress(address));

    const res = await getRestaurants({
      lat: address.latitude,
      lon: address.longitude,
      radius: radius.value,
    });

    if (res.status === 200 && res.data) {
      restaurants.value = res.data;
    }
  });

  return {
    restaurants,
  };
}
