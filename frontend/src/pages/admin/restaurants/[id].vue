<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useRoute } from 'vue-router/auto';
import { computed } from 'vue';
import { useGetAdminRestaurantsId } from '@/lib/api/admin-restaurants/admin-restaurants';
import RestaurantStatus from '@/components/(manage)/common/RestaurantStatus.vue';
import { Button } from '@/components/ui/button';
import ImagePreview from '@/components/(manage)/common/image/ImagePreview.vue';
import { getImageUrl } from '@/lib/assets';
import { Separator } from '@/components/ui/separator';
import SettingsSection from '@/components/(manage)/settings/SettingsSection.vue';
import { formatAddress } from '@/lib/googleMaps';
import { MapPinIcon, PhoneIcon, ThumbsUpIcon } from 'lucide-vue-next';
import OpeningHoursRow from '@/components/restaurants/OpeningHoursRow.vue';
import { useUserRating } from '@/components/user-rating/useUserRating';
import UserRating from '@/components/user-rating/UserRating.vue';
import { useReportsFromRestaurant } from '@/components/(admin)/composables/useReportsFromRestaurant';
import { useReportsAboutRestaurant } from '@/components/(admin)/composables/useReportsAboutRestaurant';
import RestaurantReportsViewer from '@/components/(admin)/reports/RestaurantReportsViewer.vue';
import {
  closeReportAboutRestaurant,
  updateRestaurantStatus,
} from '@/components/(admin)/reports/api';

useHead({
  title: 'Restaurant',
});

const route = useRoute('/admin/restaurants/[id]');
const restaurantId = route.params.id;

const { data: restaurantData, refetch } = useGetAdminRestaurantsId(restaurantId);
const restaurant = computed(() => restaurantData?.value?.data);
const { totalRatings } = useUserRating(
  restaurant.value?.likesCount,
  restaurant.value?.dislikesCount,
);

const { reportsAboutRestaurant } = useReportsAboutRestaurant(restaurantId);
const { reportsFromRestaurant } = useReportsFromRestaurant(restaurantId);

async function handleRestaurantActivate() {
  const res = await updateRestaurantStatus(restaurantId, 'Active');

  if (res.status === 200) {
    await refetch();
  }
}

async function handleRestaurantDeactivate() {
  const res = await updateRestaurantStatus(restaurantId, 'Inactive');

  if (res.status === 200) {
    await refetch();
  }
}

async function handleBanRestaurant(reportId: string) {
  const res = await closeReportAboutRestaurant(reportId, true);

  if (res.status === 200) {
    await refetch();
  }
}

async function handleBanUser(userId: string) {
  await banUser(userId);
}
</script>

<template>
  <div v-if="restaurant">
    <h1 class="mb-10 text-2xl font-semibold tracking-tight">{{ restaurant.name }}</h1>

    <div class="grid-cols-2 gap-10 2xl:grid">
      <SettingsSection class="pb-8">
        <h2 class="text-lg font-semibold tracking-tight">Restaurant profile</h2>
        <Separator class="mb-4 mt-2" />

        <div class="mb-4 mt-4 space-y-3">
          <div class="flex items-center justify-between">
            <RestaurantStatus :status="restaurant.status" />

            <Button
              @click="handleRestaurantActivate"
              v-if="restaurant.status === 'Inactive'"
              class="bg-yellow-500 hover:bg-yellow-500/80"
            >
              Activate
            </Button>
            <Button
              @click="handleRestaurantDeactivate"
              v-if="restaurant.status === 'Active'"
              variant="secondary"
            >
              Deactivate
            </Button>
          </div>

          <p class="flex items-center gap-3">
            <ThumbsUpIcon class="h-4 w-4" />
            <UserRating
              v-if="totalRatings > 0"
              :likes="restaurant.likesCount"
              :dislikes="restaurant.dislikesCount"
            />
            <span v-else>N/A</span>
          </p>
          <p class="flex items-center gap-3">
            <PhoneIcon class="h-4 w-4" />
            {{ restaurant.phoneNumber }}
          </p>
          <p class="flex items-center gap-3">
            <MapPinIcon class="h-4 w-4" />
            {{ formatAddress(restaurant.address) }}
          </p>

          <ImagePreview :src="getImageUrl(restaurant.imageUrl)" />
        </div>

        <h2 class="mt-8 text-lg font-semibold tracking-tight">Opening Hours</h2>
        <Separator class="mb-4 mt-2" />

        <ul>
          <li
            v-for="[day, [openingHours, closingHours]] in Object.entries(restaurant.hours)"
            :key="day"
          >
            <OpeningHoursRow
              :day="day"
              :opening-hours="openingHours"
              :closing-hours="closingHours"
              :is-current-day="false"
            />
          </li>
        </ul>
      </SettingsSection>

      <SettingsSection>
        <h2 class="text-lg font-semibold tracking-tight">Reports</h2>
        <Separator class="mb-4 mt-2" />

        <RestaurantReportsViewer
          @ban-restaurant="handleBanRestaurant"
          :show-closed-reports="true"
          :restaurant-user-reports="reportsFromRestaurant"
          :user-restaurant-reports="reportsAboutRestaurant"
        />
      </SettingsSection>
    </div>
  </div>
</template>
