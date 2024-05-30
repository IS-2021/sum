import type { AddressDTO, RestaurantDTO } from '@/lib/api-model';
import { type MaybeRef, type Ref, ref, unref, watchEffect } from 'vue';
import { getRestaurants } from '@/lib/api/restaurants/restaurants';

type UseRestaurantsProps = {
  radius: Ref<number>;
  address: Ref<AddressDTO | null | undefined>;
};

export function useRestaurants({ radius, address }: UseRestaurantsProps) {
  const restaurants = ref<RestaurantDTO[]>([]);

  watchEffect(async () => {
    const addressValue = unref(address);
    if (!addressValue) {
      return;
    }

    const res = await getRestaurants({
      lat: addressValue.latitude,
      lon: addressValue.longitude,
      radius: unref(radius),
    });

    if (res.status === 200 && res.data) {
      restaurants.value = res.data;
    }
  });

  return {
    restaurants,
  };
}
