import {
  postUsersDeleteFavourites,
  putUsersIdFavourites,
  useGetUsersIdFavourites,
} from '@/lib/api/favourites/favourites';
import { computed, type ComputedRef, ref, unref, watch, watchEffect } from 'vue';
import type { RestaurantDTO, RestaurantFavouriteInputDTO } from '@/lib/api-model';

type UseFavouritesProps = {
  userId: string;
};

export function useFavourites({ userId }: UseFavouritesProps) {
  const favourites = ref<RestaurantDTO[]>([]);
  const hasAnyFavourites = computed(() => favourites.value.length > 0);

  const disableDrag = ref(false);
  const isDragDisabled = computed(() => disableDrag.value || isFetching.value);

  const { data, isFetching, refetch } = useGetUsersIdFavourites(unref(userId), {
    query: {
      enabled: !!unref(userId),
      refetchOnWindowFocus: false,
      refetchOnReconnect: false,
    },
  });

  watchEffect(() => {
    if (data.value && data.value.data.length >= 0) {
      favourites.value = data.value.data;
    }
  });

  const checkIfFavourite = (restaurantId: string) => {
    const found = [];
    favourites.value.forEach((element) => {
      if (element.id === restaurantId) {
        found.push(element);
      }
    });
    if (found.length === 0) {
      return false;
    } else {
      return true;
    }
  };

  const handleDeleteFavourite = async (isFavourite: Boolean, restaurantId: string) => {
    if (isFavourite) {
      return;
    }

    disableDrag.value = true;

    const { status } = await postUsersDeleteFavourites({
      userId: userId,
      restaurantIds: [restaurantId],
    });

    if (status === 204) {
      await refetch();
    }

    disableDrag.value = false;
  };

  const favouritesOrder: ComputedRef<RestaurantFavouriteInputDTO[]> = computed(() =>
    favourites.value.map((favourite, index) => ({
      restaurantId: favourite.id,
      orderNumber: index,
    })),
  );

  watch(favouritesOrder, async (newFavourites) => {
    await putUsersIdFavourites(userId, newFavourites);
  });

  return {
    isDragDisabled,
    favourites,
    hasAnyFavourites,
    handleDeleteFavourite,
    checkIfFavourite,
  };
}
