<script setup lang="ts">
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import ReportCard from '@/components/(manage)/dashboard/ReportCard.vue';
import type { ReportDTO, Uuid } from '@/lib/api-model';

interface RestaurantReportsViewerProps {
  /**
   * Reports made by users about the restaurant.
   */
  userRestaurantReports: ReportDTO[];
  /**
   * Reports made by the restaurant about users.
   */
  restaurantUserReports: ReportDTO[];
}

withDefaults(defineProps<RestaurantReportsViewerProps>(), {
  defaultTab: 'userRestaurant',
});

const emits = defineEmits<{
  (e: 'banRestaurant', reportId: Uuid): void;
  (e: 'banUser', reportId: Uuid): void;
}>();

const banRestaurantHandler = (reportId: Uuid) => {
  emits('banRestaurant', reportId);
};
function banUserHandler(reportId: Uuid) {
  emits('banUser', reportId);
}
</script>

<template>
  <Tabs default-value="userRestaurant">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="userRestaurant"> By users </TabsTrigger>
      <TabsTrigger value="restaurantUsers"> By restaurant </TabsTrigger>
    </TabsList>
    <TabsContent value="userRestaurant">
      <ul v-if="userRestaurantReports.length > 0">
        <li v-for="report in userRestaurantReports" :key="report.id">
          <ReportCard
            @ban-user="banUserHandler"
            @ban-restaurant="banRestaurantHandler"
            :report="report"
            :showActionButtons="report.isOpen"
            :actions="['banRestaurant', 'viewUser']"
          />
        </li>
      </ul>
      <p v-else class="mt-4">No one reported this restaurant.</p>
    </TabsContent>
    <TabsContent value="restaurantUsers">
      <ul v-if="restaurantUserReports.length > 0">
        <li v-for="report in restaurantUserReports" :key="report.id">
          <ReportCard
            @ban-user="banUserHandler"
            @ban-restaurant="banRestaurantHandler"
            :report="report"
            :showActionButtons="report.isOpen"
            :actions="['banUser', 'viewUser']"
          />
        </li>
      </ul>
      <p v-else class="mt-4">This restaurant hasn't reported anyone.</p>
    </TabsContent>
  </Tabs>
</template>
