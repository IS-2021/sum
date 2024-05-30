import { useUser } from '@/composables/useUser';
import { computed } from 'vue';
import { getRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { useQuery } from '@tanstack/vue-query';

export function getRestaurantUserQueryKey() {
  return ['restaurant', 'this'];
}

export function useRestaurantUser() {
  const {
    user,
    signOut,
    isSignedIn,
    isLoaded: isUserLoaded,
    invalidateCache: invalidateUserCache,
  } = useUser();
  const {
    data,
    isPending: isRestaurantLoading,
    refetch,
  } = useQuery({
    queryKey: getRestaurantUserQueryKey(),
    queryFn: () => getRestaurantsId(user.value?.id ?? ''),
    enabled: isSignedIn.value,
  });

  const isProfileComplete = computed(() => {
    return !!restaurant.value;
  });

  const restaurant = computed(() => data.value?.data);
  const isLoaded = computed(() => isUserLoaded.value && !isRestaurantLoading.value);

  async function invalidateCache() {
    await invalidateUserCache();
    await refetch();
  }

  return {
    user,
    restaurant,
    isSignedIn,
    isLoaded,
    signOut,
    isProfileComplete,
    invalidateCache,
  };
}
