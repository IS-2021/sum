import { useUser } from '@/composables/useUser';
import { computed } from 'vue';
import { getRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { useQuery } from '@tanstack/vue-query';

export function getRestaurantUserQueryKey() {
  return ['restaurant', 'this'];
}

export function useRestaurantUser() {
  const { user, signOut, isSignedIn, isLoaded: isUserLoaded } = useUser();
  const { data, isPending: isRestaurantLoading } = useQuery({
    queryKey: getRestaurantUserQueryKey(),
    queryFn: () => getRestaurantsId(user.value?.id ?? ''),
  });

  const isProfileComplete = computed(() => {
    return !!restaurant.value;
  });

  const restaurant = computed(() => data.value?.data);
  const isLoaded = computed(() => isUserLoaded.value && !isRestaurantLoading.value);

  return {
    user,
    restaurant,
    isSignedIn,
    isLoaded,
    signOut,
    isProfileComplete,
  };
}
