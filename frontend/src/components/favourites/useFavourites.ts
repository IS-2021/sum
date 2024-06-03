import {
  postUsersDeleteFavourites,
  postUsersIdFavourites,
  putUsersIdFavourites,
  useGetUsersIdFavourites,
} from '@/lib/api/favourites/favourites';
import { computed, type ComputedRef, ref, unref, watch, watchEffect } from 'vue';
import type { RestaurantDTO, RestaurantFavouriteInputDTO, Uuid } from '@/lib/api-model';

type UseFavouritesProps = {
  userId: string;
  restaurantId?: Uuid;
};

export function useFavourites({ userId, restaurantId }: UseFavouritesProps) {
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

  const IsFavourite = computed(() => {
    if (!restaurantId) {
      return false;
    }
    let found = [];
    found = favourites.value.filter((e) => e.id === restaurantId);
    if (found.length === 1) {
      return true;
    }
    return false;
  });

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

  const addFavourite = async (restaurantId: Uuid) => {
    const res = await postUsersIdFavourites(userId, {
      orderNumber: 0,
      restaurantId: restaurantId,
    });

    if (res.status === 200) {
      await refetch();
    }
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
    IsFavourite,
    addFavourite,
  };
}
