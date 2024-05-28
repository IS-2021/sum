import type { RestaurantDTO } from '@/lib/api-model';
import { type ComputedRef, type ModelRef, type Ref, ref, watchEffect } from 'vue';
import { useUser } from '@/composables/useUser';
import { getRestaurants } from '@/lib/api/restaurants/restaurants';

type UseRestaurantsProps = {
  radius: Ref<number> | ComputedRef<number> | ModelRef<number>;
};

export function useRestaurants({ radius }: UseRestaurantsProps) {
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
