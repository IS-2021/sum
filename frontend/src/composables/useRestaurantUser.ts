import { useUser } from '@/composables/useUser';
import { computed, ref, watchEffect } from 'vue';
import type { RestaurantDTO } from '@/lib/api-model';
import { getRestaurantsId } from '@/lib/api/restaurants/restaurants';

export function useRestaurantUser() {
  const { user, signOut, isSignedIn, isLoaded } = useUser();
  const restaurant = ref<RestaurantDTO | null>(null);

  const isProfileComplete = computed(() => {
    return !!restaurant.value;
  });

  watchEffect(async () => {
    if (!isLoaded.value || !isSignedIn.value || !user.value) {
      return;
    }

    const res = await getRestaurantsId(user.value.id);

    if (res.status === 200) {
      restaurant.value = res.data;
    }
  });

  return {
    user,
    restaurant,
    isSignedIn,
    isLoaded,
    signOut,
    isProfileComplete,
  };
}
