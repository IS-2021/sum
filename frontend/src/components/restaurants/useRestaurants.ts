import type { RestaurantDTO } from '@/lib/api-model';
import { type ComputedRef, type Ref, ref, watchEffect } from 'vue';
import { useUser } from '@/composables/useUser';
import { getRestaurants } from '@/lib/api/restaurants/restaurants';

type UseRestaurantsProps = {
  radiusKm: Ref<number> | ComputedRef<number>;
};

export function useRestaurants({ radiusKm }: UseRestaurantsProps) {
  const { user } = useUser();
  const restaurants = ref<RestaurantDTO[]>([]);

  watchEffect(async () => {
    if (!user.value || !user.value.address) {
      return;
    }

    const { address } = user.value;
    const res = await getRestaurants({
      lat: address.latitude,
      lon: address.longitude,
      radius: radiusKm.value,
    });

    if (res.status === 200 && res.data) {
      restaurants.value = res.data;
    }
  });

  return {
    restaurants,
  };
}
