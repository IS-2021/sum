<script setup lang="ts">
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import ReportCard from '@/components/(manage)/dashboard/ReportCard.vue';
import type { ReportDTO, Uuid } from '@/lib/api-model';

interface RestaurantReportsViewerProps {
  /**
   * Reports made by users about the restaurant.
   */
  reportsAboutRestaurant: ReportDTO[];
  /**
   * Reports made by the restaurant about users.
   */
  reportsAboutUser: ReportDTO[];
  showClosedReports: boolean;
}

withDefaults(defineProps<RestaurantReportsViewerProps>(), {
  showClosedReports: true,
});

const emits = defineEmits<{
  (e: 'banRestaurant', reportId: Uuid): void;
  (e: 'banUser', reportId: Uuid): void;
  (e: 'closeReport', reportId: Uuid, reportAbout: 'restaurant' | 'user'): void;
}>();

function banRestaurantHandler(reportId: Uuid) {
  emits('banRestaurant', reportId);
}
function banUserHandler(reportId: Uuid) {
  emits('banUser', reportId);
}
function closeReportHandler(reportId: Uuid, reportAbout: 'restaurant' | 'user') {
  emits('closeReport', reportId, reportAbout);
}
</script>

<template>
  <Tabs default-value="userRestaurant">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="userRestaurant"> By users </TabsTrigger>
      <TabsTrigger value="restaurantUsers"> By restaurant </TabsTrigger>
    </TabsList>
    <TabsContent value="userRestaurant">
      <ul v-if="reportsAboutRestaurant.length > 0" class="space-y-2">
        <li v-for="report in reportsAboutRestaurant" :key="report.id">
          <ReportCard
            @ban-user="banUserHandler"
            @ban-restaurant="banRestaurantHandler"
            @close-report="(reportId) => closeReportHandler(reportId, 'restaurant')"
            :report="report"
            :showActionButtons="report.isOpen"
            :actions="['banRestaurant', 'closeReport', 'viewUser']"
          />
        </li>
      </ul>
      <p v-else class="mt-4">No one reported this restaurant.</p>
    </TabsContent>
    <TabsContent value="restaurantUsers">
      <ul v-if="reportsAboutUser.length > 0">
        <li v-for="report in reportsAboutUser" :key="report.id">
          <ReportCard
            @ban-user="banUserHandler"
            @ban-restaurant="banRestaurantHandler"
            @close-report="(reportId) => closeReportHandler(reportId, 'user')"
            :report="report"
            :showActionButtons="report.isOpen"
            :actions="['banUser', 'closeReport', 'viewUser']"
          />
        </li>
      </ul>
      <p v-else class="mt-4">This restaurant hasn't reported anyone.</p>
    </TabsContent>
  </Tabs>
</template>
