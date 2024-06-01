<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useRoute } from 'vue-router/auto';
import { computed } from 'vue';
import {
  putAdminRestaurantsId,
  useGetAdminRestaurantsId,
} from '@/lib/api/admin-restaurants/admin-restaurants';
import RestaurantStatus from '@/components/(manage)/common/RestaurantStatus.vue';
import { Button } from '@/components/ui/button';
import ImagePreview from '@/components/(manage)/common/image/ImagePreview.vue';
import { getImageUrl } from '@/lib/assets';
import { Separator } from '@/components/ui/separator';
import SettingsSection from '@/components/(manage)/settings/SettingsSection.vue';
import { formatAddress } from '@/lib/googleMaps';
import { BanIcon, MapPinIcon, PhoneIcon, ThumbsDownIcon, ThumbsUpIcon } from 'lucide-vue-next';
import OpeningHoursRow from '@/components/restaurants/OpeningHoursRow.vue';
import { putRestaurantsDeactivateId } from '@/lib/api/restaurants/restaurants';
import { useUserRating } from '@/components/user-rating/useUserRating';
import UserRating from '@/components/user-rating/UserRating.vue';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { useUserRestaurantReports } from '@/components/(admin)/composables/useUserRestaurantReports';
import ReportCard from '@/components/(manage)/dashboard/ReportCard.vue';
import { useRestaurantUserReports } from '@/components/(admin)/composables/useRestaurantUserReports';

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

const { userRestaurantReports } = useUserRestaurantReports(restaurantId);
const { restaurantUserReports } = useRestaurantUserReports(restaurantId);

async function handleRestaurantActivate() {
  const res = await putAdminRestaurantsId(restaurantId);

  if (res.status === 200) {
    await refetch();
  }
}

async function handleRestaurantDeactivate() {
  const res = await putRestaurantsDeactivateId(restaurantId);

  if (res.status === 200) {
    await refetch();
  }
}
</script>

<template>
  <div v-if="restaurant" class="max-w-screen-md">
    <h1 class="mb-10 text-2xl font-semibold tracking-tight">{{ restaurant.name }}</h1>

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

      <Tabs default-value="restaurant">
        <TabsList class="grid w-full grid-cols-2">
          <TabsTrigger value="restaurant"> By users </TabsTrigger>
          <TabsTrigger value="users"> By restaurant </TabsTrigger>
        </TabsList>
        <TabsContent value="restaurant">
          <ul v-if="userRestaurantReports.length > 0">
            <li v-for="report in userRestaurantReports" :key="report.id">
              <article class="rounded border border-neutral-300 bg-white px-6 py-4">
                <div class="mb-4 flex items-center gap-6">
                  <ThumbsDownIcon class="flex-shrink-0 text-red-500" />
                  <p class="max-w-prose flex-shrink">{{ report.cause }}</p>
                </div>

                <Separator class="mb-4 mt-2" />

                <div class="flex gap-2">
                  <Button variant="outline-destructive">
                    <BanIcon class="mr-2 inline-block h-4 w-4" />Ban restaurant
                  </Button>
                  <Button variant="secondary" as-child>
                    <RouterLink :to="`/admin/users/${report.userId}`"> View user </RouterLink>
                  </Button>
                </div>
              </article>
            </li>
          </ul>
          <p v-else class="mt-4">No one reported this restaurant.</p>
        </TabsContent>
        <TabsContent value="users">
          <ul v-if="restaurantUserReports.length > 0">
            <li v-for="report in restaurantUserReports" :key="report.id">
              <ReportCard :cause="report.cause" :reported-at="report.timestamp" />
            </li>
          </ul>
          <p v-else class="mt-4">This restaurant hasn't reported anyone.</p>
        </TabsContent>
      </Tabs>
    </SettingsSection>
  </div>
</template>
